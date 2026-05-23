#!/usr/bin/env python3
"""
Genera gráficos thesis-ready SOLO de la campaña spring-boot-v1.
Separa llama-3.1-405b vs Claude. Enfocado en resultados reales.

Uso:
    python scripts/generate_springboot_plots.py
"""

from __future__ import annotations

import json
from pathlib import Path
from typing import Any

import matplotlib
matplotlib.use("Agg")
import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
import seaborn as sns

sns.set_theme(style="whitegrid", context="paper", font_scale=1.3)
matplotlib.rcParams["figure.dpi"] = 150
matplotlib.rcParams["savefig.dpi"] = 300
matplotlib.rcParams["figure.figsize"] = (10, 6)
matplotlib.rcParams["axes.labelsize"] = 13
matplotlib.rcParams["axes.titlesize"] = 15
matplotlib.rcParams["legend.fontsize"] = 11

LLAMA_COLOR = "#2ecc71"
CLAUDE_COLOR = "#e74c3c"
FAIL_COLOR = "#e74c3c"
WARN_COLOR = "#f39c12"

ROOT = Path(__file__).resolve().parent.parent
CAMPAIGN_DIR = ROOT / "campaigns" / "spring-boot-v1"
OUTDIR = ROOT / "figures_springboot"


def load_springboot_runs() -> pd.DataFrame:
    """Carga todos los runs de spring-boot-v1."""
    records: list[dict[str, Any]] = []

    # Buscar todos los result.json / results.json
    files = list(CAMPAIGN_DIR.rglob("result.json")) + list(CAMPAIGN_DIR.rglob("results.json"))
    seen_files: set[Path] = set()

    for fp in files:
        rp = fp.resolve()
        if rp in seen_files:
            continue
        seen_files.add(rp)

        try:
            data = json.loads(fp.read_text(encoding="utf-8"))
        except (json.JSONDecodeError, OSError):
            continue

        runs: list[dict] = []
        if isinstance(data, list):
            runs = data
        elif isinstance(data, dict):
            runs = data.get("runs", [data])

        for run in runs:
            if not isinstance(run, dict):
                continue
            m = run.get("metrics", {})
            cfg = run.get("config_snapshot", {})

            # Determinar tipo de dataset
            ds = run.get("dataset_id", "unknown")
            family = ds.split("-")[0] if "-" in ds else ds
            # Servicios vs entidades/controladores
            is_service = "service" in ds.lower()
            complexity = "servicio" if is_service else {
                "gsapp": "simple", "jwt": "medio", "petclinic": "complejo"
            }.get(family, family)

            records.append({
                "run_id": run.get("run_id", ""),
                "dataset_id": ds,
                "family": family,
                "is_service": is_service,
                "complexity": complexity,
                "model": run.get("model", "unknown"),
                "model_short": run.get("model", "").replace("meta/", "").replace("anthropic/", ""),
                "provider": run.get("provider", "unknown"),
                "status": run.get("status", "unknown"),
                "compile_pass": m.get("compile_pass", False),
                "test_pass": m.get("test_pass"),
                "latency_ms": run.get("latency_ms", 0),
                "latency_sec": run.get("latency_ms", 0) / 1000.0,
                "failure_type": m.get("failure_type"),
                "java_file": cfg.get("java_file", ""),
            })

    return pd.DataFrame.from_records(records)


def _save(fig: plt.Figure, name: str) -> None:
    OUTDIR.mkdir(parents=True, exist_ok=True)
    fig.savefig(OUTDIR / f"{name}.png", bbox_inches="tight", facecolor="white")
    fig.savefig(OUTDIR / f"{name}.pdf", bbox_inches="tight", facecolor="white")
    plt.close(fig)


# ── Gráficos ──────────────────────────────────────────────────────────────

def plot_compile_by_model(df: pd.DataFrame) -> None:
    """Fig 1: Tasa de compilación por modelo."""
    stats = df.groupby("model_short").agg(
        total=("run_id", "count"),
        compiled=("compile_pass", "sum"),
    )
    stats["rate"] = stats["compiled"] / stats["total"] * 100
    stats = stats.sort_values("rate", ascending=True)

    fig, ax = plt.subplots(figsize=(10, 4))
    colors = [LLAMA_COLOR if "llama" in idx.lower() else CLAUDE_COLOR for idx in stats.index]
    bars = ax.barh(stats.index, stats["rate"], color=colors, edgecolor="black", linewidth=0.5)
    ax.set_xlim(0, 105)
    ax.set_xlabel("Tasa de compilación (%)")
    ax.set_title("Tasa de compilación por modelo de lenguaje — Spring Boot v1")
    for bar, (idx, row) in zip(bars, stats.iterrows()):
        ax.text(row["rate"] + 2, bar.get_y() + bar.get_height()/2,
                f"{int(row['compiled'])}/{int(row['total'])}",
                va="center", fontsize=11, fontweight="bold")
    # Leyenda manual
    from matplotlib.patches import Patch
    legend_elements = [
        Patch(facecolor=LLAMA_COLOR, label="Llama 3.1 405B"),
        Patch(facecolor=CLAUDE_COLOR, label="Claude 3.5 Sonnet"),
    ]
    ax.legend(handles=legend_elements, loc="lower right")
    plt.tight_layout()
    _save(fig, "01_compile_rate_by_model")


def plot_compile_by_family_model(df: pd.DataFrame) -> None:
    """Fig 2: Compilación por familia y modelo."""
    pivot = df.groupby(["family", "model_short"]).agg(
        total=("run_id", "count"),
        compiled=("compile_pass", "sum"),
    ).reset_index()
    pivot["rate"] = pivot["compiled"] / pivot["total"] * 100

    # Ordenar familias por complejidad
    order = ["gsapp", "jwt", "petclinic"]
    pivot["family"] = pd.Categorical(pivot["family"], categories=order, ordered=True)
    pivot = pivot.sort_values(["family", "model_short"])

    fig, ax = plt.subplots(figsize=(12, 5))
    x = np.arange(len(order))
    width = 0.35

    llama = pivot[pivot["model_short"].str.contains("llama")].set_index("family")["rate"].reindex(order, fill_value=0)
    claude = pivot[pivot["model_short"].str.contains("claude")].set_index("family")["rate"].reindex(order, fill_value=0)

    bars1 = ax.bar(x - width/2, llama.values, width, label="Llama 3.1 405B", color=LLAMA_COLOR, edgecolor="black", linewidth=0.5)
    bars2 = ax.bar(x + width/2, claude.values, width, label="Claude 3.5 Sonnet", color=CLAUDE_COLOR, edgecolor="black", linewidth=0.5)

    ax.set_xticks(x)
    ax.set_xticklabels(["gsapp (simple)", "jwt-auth (medio)", "petclinic (complejo)"])
    ax.set_ylabel("Tasa de compilación (%)")
    ax.set_title("Tasa de compilación por familia de proyecto y modelo")
    ax.set_ylim(0, 105)
    ax.legend()

    # Etiquetas
    for bar in bars1:
        if bar.get_height() > 0:
            ax.text(bar.get_x() + bar.get_width()/2, bar.get_height() + 2,
                    f"{bar.get_height():.0f}%", ha="center", fontsize=10, fontweight="bold")
    for bar in bars2:
        if bar.get_height() > 0:
            ax.text(bar.get_x() + bar.get_width()/2, bar.get_height() + 2,
                    f"{bar.get_height():.0f}%", ha="center", fontsize=10, fontweight="bold")

    plt.tight_layout()
    _save(fig, "02_compile_by_family_model")


def plot_latency_by_family(df: pd.DataFrame) -> None:
    """Fig 3: Latencia por familia (solo llama-405b compilados)."""
    df_l = df[(df["model_short"].str.contains("llama")) & (df["compile_pass"] == True)].copy()
    if len(df_l) < 3:
        print("[SKIP] plot_latency_by_family: pocos datos")
        return

    order = ["gsapp", "jwt", "petclinic"]
    df_l["family"] = pd.Categorical(df_l["family"], categories=order, ordered=True)

    fig, ax = plt.subplots(figsize=(10, 5))
    sns.boxplot(data=df_l, x="family", y="latency_sec", order=order, palette="pastel", width=0.5, ax=ax)
    # Agregar strip plot para ver cada punto
    sns.stripplot(data=df_l, x="family", y="latency_sec", order=order, color="black", size=8, jitter=True, ax=ax)

    ax.set_xlabel("Familia de proyecto")
    ax.set_ylabel("Latencia (s)")
    ax.set_title("Latencia de inferencia por complejidad del proyecto — Llama 3.1 405B")
    ax.set_xticklabels(["gsapp\n(simple)", "jwt-auth\n(medio)", "petclinic\n(complejo)"])

    # Anotar valores
    for i, fam in enumerate(order):
        vals = df_l[df_l["family"] == fam]["latency_sec"]
        if len(vals) > 0:
            ax.annotate(f"mediana: {vals.median():.1f}s\n(n={len(vals)})",
                        xy=(i, vals.max()), xytext=(10, 5), textcoords="offset points",
                        fontsize=9, ha="left", bbox=dict(boxstyle="round,pad=0.3", facecolor="yellow", alpha=0.3))

    plt.tight_layout()
    _save(fig, "03_latency_by_family")


def plot_latency_by_dataset(df: pd.DataFrame) -> None:
    """Fig 4: Latencia por dataset (solo llama-405b compilados)."""
    df_l = df[(df["model_short"].str.contains("llama")) & (df["compile_pass"] == True)].copy()
    if len(df_l) < 3:
        print("[SKIP] plot_latency_by_dataset: pocos datos")
        return

    # Ordenar por latencia
    order = df_l.groupby("dataset_id")["latency_sec"].median().sort_values(ascending=True).index.tolist()

    fig, ax = plt.subplots(figsize=(12, 6))
    colors = [LLAMA_COLOR] * len(order)
    bars = ax.barh(order, [df_l[df_l["dataset_id"] == d]["latency_sec"].values[0] for d in order], color=colors, edgecolor="black", linewidth=0.5)
    ax.set_xlabel("Latencia (s)")
    ax.set_title("Latencia de inferencia por archivo fuente — Llama 3.1 405B (compilados)")

    # Agregar líneas de separación por familia
    families = [d.split("-")[0] for d in order]
    current_fam = families[0]
    for i, fam in enumerate(families[1:], 1):
        if fam != current_fam:
            ax.axhline(y=i - 0.5, color="gray", linestyle="--", alpha=0.5)
            current_fam = fam

    # Etiquetas de familia
    ax2 = ax.twinx()
    ax2.set_ylim(ax.get_ylim())
    ax2.set_yticks(range(len(order)))
    ax2.set_yticklabels([d.split("-")[0] for d in order], fontsize=9, color="gray")
    ax2.tick_params(axis="y", labelcolor="gray")

    plt.tight_layout()
    _save(fig, "04_latency_by_dataset")


def plot_entity_vs_service(df: pd.DataFrame) -> None:
    """Fig 5: Entidades vs Servicios (solo llama-405b)."""
    df_l = df[df["model_short"].str.contains("llama")].copy()
    df_l["tipo"] = df_l["is_service"].apply(lambda x: "Servicio" if x else "Entidad/Controlador")

    stats = df_l.groupby("tipo").agg(
        total=("run_id", "count"),
        compiled=("compile_pass", "sum"),
    )
    stats["rate"] = stats["compiled"] / stats["total"] * 100

    fig, ax = plt.subplots(figsize=(8, 5))
    colors = [LLAMA_COLOR, WARN_COLOR]
    bars = ax.bar(stats.index, stats["rate"], color=colors, edgecolor="black", linewidth=0.5, width=0.5)
    ax.set_ylabel("Tasa de compilación (%)")
    ax.set_title("Entidades/Controladores vs Servicios — Llama 3.1 405B")
    ax.set_ylim(0, 105)

    for bar, (idx, row) in zip(bars, stats.iterrows()):
        ax.text(bar.get_x() + bar.get_width()/2, bar.get_height() + 3,
                f"{int(row['compiled'])}/{int(row['total'])}\n({row['rate']:.0f}%)",
                ha="center", fontsize=12, fontweight="bold")

    plt.tight_layout()
    _save(fig, "05_entity_vs_service")


def plot_status_heatmap(df: pd.DataFrame) -> None:
    """Fig 6: Heatmap de resultado por dataset y modelo."""
    pivot = df.pivot_table(
        index="dataset_id",
        columns="model_short",
        values="compile_pass",
        aggfunc="first",
    )
    # Rellenar NaN con -1 para que no interfiera con el colormap
    pivot = pivot.fillna(-1).astype(float)

    fig, ax = plt.subplots(figsize=(8, max(6, len(pivot) * 0.5)))
    # Crear colormap con 3 colores: gris para NaN, rojo para fallo, verde para éxito
    cmap = matplotlib.colors.ListedColormap(["#95a5a6", FAIL_COLOR, LLAMA_COLOR])
    bounds = [-1.5, -0.5, 0.5, 1.5]
    norm = matplotlib.colors.BoundaryNorm(bounds, cmap.N)

    sns.heatmap(pivot, cmap=cmap, norm=norm, cbar=False, linewidths=0.5, linecolor="white",
                annot=True, fmt=".0f", ax=ax,
                annot_kws={"color": "white", "fontweight": "bold"})
    ax.set_title("Compilación exitosa por dataset y modelo")
    ax.set_xlabel("Modelo")
    ax.set_ylabel("Dataset")

    # Custom legend
    from matplotlib.patches import Patch
    legend_elements = [
        Patch(facecolor=LLAMA_COLOR, label="Compila (1)"),
        Patch(facecolor=FAIL_COLOR, label="Falló (0)"),
        Patch(facecolor="#95a5a6", label="No evaluado"),
    ]
    ax.legend(handles=legend_elements, loc="upper left", bbox_to_anchor=(1.05, 1))

    plt.tight_layout()
    _save(fig, "06_status_heatmap")


def plot_claude_errors(df: pd.DataFrame) -> None:
    """Fig 7: Qué le pasó a Claude."""
    df_c = df[df["model_short"].str.contains("claude")].copy()
    if len(df_c) == 0:
        print("[SKIP] plot_claude_errors: no hay runs de Claude")
        return

    status_counts = df_c["status"].value_counts()
    failure_counts = df_c["failure_type"].fillna("sin_error").value_counts()

    fig, axes = plt.subplots(1, 2, figsize=(14, 4))

    # Status
    axes[0].bar(status_counts.index, status_counts.values, color=CLAUDE_COLOR, edgecolor="black", linewidth=0.5)
    axes[0].set_title("Estado de ejecución — Claude 3.5 Sonnet")
    axes[0].set_ylabel("Cantidad de runs")
    for bar in axes[0].patches:
        axes[0].text(bar.get_x() + bar.get_width()/2, bar.get_height() + 0.3,
                     f"{int(bar.get_height())}", ha="center", fontsize=11, fontweight="bold")

    # Failure type
    axes[1].barh(failure_counts.index, failure_counts.values, color=WARN_COLOR, edgecolor="black", linewidth=0.5)
    axes[1].set_title("Tipo de fallo — Claude 3.5 Sonnet")
    axes[1].set_xlabel("Cantidad de runs")
    for bar in axes[1].patches:
        axes[1].text(bar.get_width() + 0.3, bar.get_y() + bar.get_height()/2,
                     f"{int(bar.get_width())}", va="center", fontsize=11, fontweight="bold")

    plt.tight_layout()
    _save(fig, "07_claude_errors")


def print_summary(df: pd.DataFrame) -> None:
    print("\n" + "=" * 60)
    print("SPRING-BOOT-V1: RESUMEN DE DATOS LIMPIOS")
    print("=" * 60)
    print(f"Total runs: {len(df)}")
    print(f"Modelos: {df['model_short'].unique().tolist()}")
    print(f"Datasets: {df['dataset_id'].nunique()}")
    print(f"Familias: {df['family'].unique().tolist()}")
    print()

    for model, g in df.groupby("model_short"):
        ok = g["compile_pass"].sum()
        total = len(g)
        print(f"  {model:30s}: {ok}/{total} compilan ({ok/total:.0%})")
        # Por familia
        for fam, fg in g.groupby("family"):
            fok = fg["compile_pass"].sum()
            ftot = len(fg)
            print(f"      {fam:15s}: {fok}/{ftot}")
    print("=" * 60 + "\n")


def main() -> None:
    print("🔍 Cargando spring-boot-v1...")
    df = load_springboot_runs()

    if df.empty:
        print("✖ No se encontraron runs. Abortando.")
        return

    print_summary(df)

    # Borrar figuras viejas
    if OUTDIR.exists():
        for f in OUTDIR.glob("*.png"):
            f.unlink()
        for f in OUTDIR.glob("*.pdf"):
            f.unlink()
        print(f"🗑️  Figuras viejas borradas de {OUTDIR}")

    print("📊 Generando gráficos spring-boot...")
    plot_compile_by_model(df)
    plot_compile_by_family_model(df)
    plot_latency_by_family(df)
    plot_latency_by_dataset(df)
    plot_entity_vs_service(df)
    plot_status_heatmap(df)
    plot_claude_errors(df)

    print(f"\n✅ Gráficos guardados en: {OUTDIR.resolve()}")

    # CSV
    csv_path = OUTDIR / "springboot_runs.csv"
    df.to_csv(csv_path, index=False)
    print(f"   CSV: {csv_path}")


if __name__ == "__main__":
    main()
