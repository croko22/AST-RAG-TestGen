#!/usr/bin/env python3
"""
Genera gráficos comparativos a partir de todos los resultados del benchmark.

Recorre recursivamente el repo buscando result.json / results.json,
consolida los runs en un DataFrame y produce figuras thesis-ready.

Uso:
    python scripts/generate_thesis_plots.py
    python scripts/generate_thesis_plots.py --output ./figures_nuevas
"""

from __future__ import annotations

import argparse
import json
import re
from collections import Counter
from pathlib import Path
from typing import Any

import matplotlib

matplotlib.use("Agg")
import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
import seaborn as sns

# Tesis styling: fuentes grandes, grids limpios, colores profesionales
sns.set_theme(style="whitegrid", context="paper", font_scale=1.2)
matplotlib.rcParams["figure.dpi"] = 150
matplotlib.rcParams["savefig.dpi"] = 300
matplotlib.rcParams["figure.figsize"] = (10, 6)
matplotlib.rcParams["axes.labelsize"] = 12
matplotlib.rcParams["axes.titlesize"] = 14
matplotlib.rcParams["legend.fontsize"] = 10

COLORS = sns.color_palette("tab10")
SUCCESS_COLOR = "#2ecc71"
FAIL_COLOR = "#e74c3c"
PARTIAL_COLOR = "#f39c12"


def discover_result_files(root: Path) -> list[Path]:
    """Busca todos los result.json / results.json debajo de root."""
    files: list[Path] = []
    for p in root.rglob("result.json"):
        files.append(p)
    for p in root.rglob("results.json"):
        files.append(p)
    # Deduplicar por path absoluto
    seen: set[Path] = set()
    uniq: list[Path] = []
    for p in files:
        rp = p.resolve()
        if rp not in seen:
            seen.add(rp)
            uniq.append(p)
    return sorted(uniq)


def _coerce_bool(val: Any) -> bool | None:
    if val is None:
        return None
    if isinstance(val, bool):
        return val
    return bool(val)


def _coerce_float(val: Any) -> float | None:
    if val is None:
        return None
    try:
        return float(val)
    except (ValueError, TypeError):
        return None


def _coerce_int(val: Any) -> int | None:
    if val is None:
        return None
    try:
        return int(val)
    except (ValueError, TypeError):
        return None


def extract_runs(path: Path) -> list[dict[str, Any]]:
    """Extrae runs de un archivo JSON de resultados."""
    try:
        data = json.loads(path.read_text(encoding="utf-8"))
    except (json.JSONDecodeError, OSError):
        return []

    if isinstance(data, list):
        return list(data)
    if isinstance(data, dict):
        return data.get("runs", [data])  # a veces el archivo es un solo run dict
    return []


def derive_campaign(relpath: Path) -> str:
    """Deriva un nombre de campaña legible desde la ruta relativa."""
    parts = list(relpath.parts)
    # Heurística: buscar 'campaigns/NAME' o 'results/NAME'
    for i, part in enumerate(parts):
        if part == "campaigns" and i + 1 < len(parts):
            return f"campaign_{parts[i + 1]}"
        if part == "results" and i + 1 < len(parts):
            # Verificar si es un directorio de campaña suelta (no run_)
            next_part = parts[i + 1]
            if not next_part.startswith("run_"):
                return f"results_{next_part}"
    # Fallback: usar directorio padre del archivo
    return relpath.parent.name if relpath.parent.name else "unknown"


def load_all_runs(root: Path) -> pd.DataFrame:
    """Carga y normaliza todos los runs en un DataFrame."""
    files = discover_result_files(root)
    records: list[dict[str, Any]] = []

    for fp in files:
        relpath = fp.relative_to(root)
        campaign = derive_campaign(relpath)
        runs = extract_runs(fp)
        for run in runs:
            if not isinstance(run, dict):
                continue
            m = run.get("metrics", {})
            cfg = run.get("config_snapshot", {})
            rec: dict[str, Any] = {
                "campaign": campaign,
                "source_file": str(relpath),
                "run_id": run.get("run_id", ""),
                "status": run.get("status", "unknown"),
                "dataset_id": run.get("dataset_id", "unknown"),
                "provider": run.get("provider", "unknown"),
                "model": run.get("model", "unknown"),
                "trial": _coerce_int(run.get("trial")),
                "latency_ms": _coerce_float(run.get("latency_ms")),
                "compile_pass": _coerce_bool(m.get("compile_pass")),
                "test_pass": _coerce_bool(m.get("test_pass")),
                "coverage_pct": _coerce_float(m.get("coverage_pct")),
                "failure_type": m.get("failure_type"),
                "failure_message": m.get("failure_message"),
                # Métricas nuevas (solo en runs recientes)
                "quality_score": _coerce_float(m.get("quality_score")),
                "assertion_count": _coerce_int(m.get("assertion_count")),
                "test_count": _coerce_int(m.get("test_count")),
                "trivial_flag": _coerce_bool(m.get("trivial_flag")),
                "generation_time_ms": _coerce_float(m.get("generation_time_ms")),
                # Timings por fase
                "parse_ms": _coerce_float(m.get("timings", {}).get("parse_ms")),
                "retrieval_ms": _coerce_float(m.get("timings", {}).get("retrieval_ms")),
                "prompt_ms": _coerce_float(m.get("timings", {}).get("prompt_ms")),
                "llm_ms": _coerce_float(m.get("timings", {}).get("llm_ms")),
                "postproc_ms": _coerce_float(m.get("timings", {}).get("postproc_ms")),
                # Config
                "max_dependencies": _coerce_int(cfg.get("max_dependencies")),
                "timeout_seconds": _coerce_int(cfg.get("timeout_seconds")),
            }
            records.append(rec)

    df = pd.DataFrame.from_records(records)
    # Normalizar modelos
    df["model_short"] = (
        df["model"]
        .str.replace(r"^meta/", "", regex=True)
        .str.replace(r"^nvidia/", "", regex=True)
        .str.replace(r"^google/", "", regex=True)
    )
    # Familia de dataset
    df["dataset_family"] = df["dataset_id"].str.split("-").str[0]
    # Éxito compuesto
    df["full_success"] = (df["compile_pass"] == True) & (df["test_pass"] == True)
    df["compile_only"] = (df["compile_pass"] == True) & (df["test_pass"] != True)
    df["failed"] = df["compile_pass"] != True
    return df


# ── Helpers de plotting ────────────────────────────────────────────────────


def _save(fig: plt.Figure, outdir: Path, name: str) -> None:
    outdir.mkdir(parents=True, exist_ok=True)
    fig.savefig(outdir / f"{name}.png", bbox_inches="tight", facecolor="white")
    fig.savefig(outdir / f"{name}.pdf", bbox_inches="tight", facecolor="white")
    plt.close(fig)


def _bar_labels(ax: plt.Axes) -> None:
    for bar in ax.patches:
        height = bar.get_height()
        if not np.isnan(height) and height > 0:
            ax.text(
                bar.get_x() + bar.get_width() / 2,
                height,
                f"{height:.0f}%" if height <= 100 else f"{height:.1f}",
                ha="center",
                va="bottom",
                fontsize=9,
            )


def _print_summary(df: pd.DataFrame) -> None:
    print("\n" + "=" * 60)
    print("RESUMEN DE DATOS CONSOLIDADOS")
    print("=" * 60)
    print(f"Total runs: {len(df)}")
    print(f"Campañas: {df['campaign'].nunique()} ({', '.join(df['campaign'].unique())})")
    print(f"Modelos: {df['model_short'].nunique()} ({', '.join(df['model_short'].unique())})")
    print(f"Datasets: {df['dataset_id'].nunique()}")
    print(f"Runs con quality_score: {df['quality_score'].notna().sum()}")
    print(f"Runs con timings: {df['llm_ms'].notna().sum()}")
    print(f"Tasa de compilación global: {df['compile_pass'].mean():.1%}")
    print(f"Tasa de test_pass global: {df.loc[df['compile_pass'] == True, 'test_pass'].mean():.1%}")
    print("\nPor campaña:")
    for camp, g in df.groupby("campaign"):
        ok = g["compile_pass"].sum()
        total = len(g)
        q_avail = g["quality_score"].notna().sum()
        print(f"  {camp:30s}: {ok}/{total} compilan ({ok / total:.0%}) | quality: {q_avail} runs")
    print("=" * 60 + "\n")


# ── Gráficos ───────────────────────────────────────────────────────────────


def plot_compile_rate_by_campaign(df: pd.DataFrame, outdir: Path) -> None:
    """Gráfico 1: Tasa de compilación por campaña."""
    camp_stats = df.groupby("campaign").agg(
        total=("run_id", "count"),
        compiled=("compile_pass", lambda x: x.sum()),
    )
    camp_stats["rate"] = camp_stats["compiled"] / camp_stats["total"] * 100
    camp_stats = camp_stats.sort_values("rate", ascending=True)

    fig, ax = plt.subplots(figsize=(10, max(4, len(camp_stats) * 0.5)))
    bars = ax.barh(camp_stats.index, camp_stats["rate"], color=COLORS[0])
    ax.set_xlim(0, 105)
    ax.set_xlabel("Tasa de compilación (%)")
    ax.set_title("Tasa de compilación por campaña")
    for i, (idx, row) in enumerate(camp_stats.iterrows()):
        ax.text(row["rate"] + 1, i, f"{row['compiled']}/{row['total']}", va="center", fontsize=9)
    plt.tight_layout()
    _save(fig, outdir, "01_compile_rate_by_campaign")


def plot_status_distribution(df: pd.DataFrame, outdir: Path) -> None:
    """Gráfico 2: Distribución de estados por campaña."""
    status_counts = df.groupby(["campaign", "status"]).size().unstack(fill_value=0)
    status_counts = status_counts.sort_values(by=status_counts.columns.tolist(), ascending=True)

    fig, ax = plt.subplots(figsize=(10, max(4, len(status_counts) * 0.5)))
    status_counts.plot(
        kind="barh", stacked=True, ax=ax, color=[SUCCESS_COLOR, FAIL_COLOR, PARTIAL_COLOR]
    )
    ax.set_xlabel("Cantidad de runs")
    ax.set_title("Distribución de estados por campaña")
    ax.legend(title="Estado", loc="lower right")
    plt.tight_layout()
    _save(fig, outdir, "02_status_distribution")


def plot_latency_by_campaign(df: pd.DataFrame, outdir: Path) -> None:
    """Gráfico 3: Latencia (s) por campaña."""
    df_plot = df[df["latency_ms"].notna() & (df["latency_ms"] > 0)].copy()
    if len(df_plot) < 2:
        return
    df_plot["latency_sec"] = df_plot["latency_ms"] / 1000.0
    # Ordenar campañas por mediana
    order = df_plot.groupby("campaign")["latency_sec"].median().sort_values().index.tolist()

    fig, ax = plt.subplots(figsize=(12, max(5, len(order) * 0.6)))
    sns.boxplot(data=df_plot, y="campaign", x="latency_sec", order=order, palette="pastel", ax=ax)
    ax.set_xlabel("Latencia (s)")
    ax.set_ylabel("")
    ax.set_title("Distribución de latencia por campaña")
    plt.tight_layout()
    _save(fig, outdir, "03_latency_by_campaign")


def plot_success_rate_by_model(df: pd.DataFrame, outdir: Path) -> None:
    """Gráfico 4: Tasa de éxito por modelo."""
    model_stats = df.groupby("model_short").agg(
        total=("run_id", "count"),
        compiled=("compile_pass", lambda x: x.sum()),
        tested=("test_pass", lambda x: x.sum()),
    )
    model_stats["compile_rate"] = model_stats["compiled"] / model_stats["total"] * 100
    model_stats["test_rate"] = model_stats["tested"] / model_stats["total"] * 100
    model_stats = model_stats.sort_values("compile_rate", ascending=True)

    fig, ax = plt.subplots(figsize=(10, max(4, len(model_stats) * 0.5)))
    x = np.arange(len(model_stats))
    width = 0.35
    bars1 = ax.barh(
        x - width / 2, model_stats["compile_rate"], width, label="Compila", color=SUCCESS_COLOR
    )
    bars2 = ax.barh(
        x + width / 2, model_stats["test_rate"], width, label="Test pass", color=COLORS[1]
    )
    ax.set_yticks(x)
    ax.set_yticklabels(model_stats.index)
    ax.set_xlabel("Tasa de éxito (%)")
    ax.set_title("Tasa de compilación y test pass por modelo")
    ax.legend()
    ax.set_xlim(0, 105)
    # Etiquetas
    for bar in bars1:
        ax.text(
            bar.get_width() + 1,
            bar.get_y() + bar.get_height() / 2,
            f"{bar.get_width():.0f}%",
            va="center",
            fontsize=8,
        )
    plt.tight_layout()
    _save(fig, outdir, "04_success_rate_by_model")


def plot_quality_score(df: pd.DataFrame, outdir: Path) -> None:
    """Gráfico 5: Quality score por dataset (solo runs con quality_score)."""
    df_q = df[df["quality_score"].notna()].copy()
    if len(df_q) < 2:
        print("[SKIP] plot_quality_score: menos de 2 runs con quality_score")
        return
    # Agregar columnas para color
    df_q["resultado"] = df_q.apply(
        lambda r: (
            "compila+test"
            if r["compile_pass"] and r["test_pass"]
            else "compila"
            if r["compile_pass"]
            else "falló"
        ),
        axis=1,
    )
    order = df_q.groupby("dataset_id")["quality_score"].mean().sort_values(ascending=True).index

    fig, ax = plt.subplots(figsize=(12, max(5, len(order) * 0.6)))
    sns.barplot(
        data=df_q,
        y="dataset_id",
        x="quality_score",
        order=order,
        hue="resultado",
        palette={"compila+test": SUCCESS_COLOR, "compila": PARTIAL_COLOR, "falló": FAIL_COLOR},
        ax=ax,
    )
    ax.set_xlabel("Quality Score")
    ax.set_ylabel("")
    ax.set_title("Quality score por dataset (con estado de compilación)")
    ax.set_xlim(0, 1.05)
    ax.legend(title="Resultado", loc="lower right")
    plt.tight_layout()
    _save(fig, outdir, "05_quality_score_by_dataset")


def plot_assertions_per_test(df: pd.DataFrame, outdir: Path) -> None:
    """Gráfico 6: Assertions / test por dataset."""
    df_a = df[
        (df["assertion_count"].notna()) & (df["test_count"].notna()) & (df["test_count"] > 0)
    ].copy()
    if len(df_a) < 2:
        print("[SKIP] plot_assertions_per_test: menos de 2 runs con assertions")
        return
    df_a["assertions_per_test"] = df_a["assertion_count"] / df_a["test_count"]
    order = (
        df_a.groupby("dataset_id")["assertions_per_test"].mean().sort_values(ascending=True).index
    )

    fig, ax = plt.subplots(figsize=(12, max(5, len(order) * 0.6)))
    sns.barplot(
        data=df_a,
        y="dataset_id",
        x="assertions_per_test",
        order=order,
        hue="compile_pass",
        palette={True: SUCCESS_COLOR, False: FAIL_COLOR, None: "#95a5a6"},
        ax=ax,
    )
    ax.set_xlabel("Assertions / test")
    ax.set_ylabel("")
    ax.set_title("Assertions por test generado")
    ax.legend(title="Compila", loc="lower right")
    plt.tight_layout()
    _save(fig, outdir, "06_assertions_per_test")


def plot_timing_breakdown(df: pd.DataFrame, outdir: Path) -> None:
    """Gráfico 7: Desglose de tiempos por fase (solo runs con timings)."""
    timing_cols = ["parse_ms", "retrieval_ms", "prompt_ms", "llm_ms", "postproc_ms"]
    df_t = df[df["llm_ms"].notna()].copy()
    if len(df_t) < 2:
        print("[SKIP] plot_timing_breakdown: menos de 2 runs con timings")
        return
    # Convertir a segundos
    for col in timing_cols:
        df_t[col] = df_t[col] / 1000.0
    df_t = df_t.sort_values("generation_time_ms", ascending=True)
    labels = df_t["dataset_id"].values

    fig, ax = plt.subplots(figsize=(14, max(6, len(labels) * 0.5)))
    bottom = np.zeros(len(labels))
    phase_labels = {
        "parse_ms": "Parse AST",
        "retrieval_ms": "Retrieval",
        "prompt_ms": "Prompt build",
        "llm_ms": "LLM inference",
        "postproc_ms": "Post-proc",
    }
    for i, col in enumerate(timing_cols):
        vals = df_t[col].fillna(0).values
        ax.barh(labels, vals, left=bottom, label=phase_labels[col], color=COLORS[i % len(COLORS)])
        bottom += vals
    ax.set_xlabel("Tiempo (s)")
    ax.set_title("Desglose de tiempos del pipeline por dataset")
    ax.legend(loc="lower right", title="Fase")
    plt.tight_layout()
    _save(fig, outdir, "07_timing_breakdown")


def plot_llm_dominance(df: pd.DataFrame, outdir: Path) -> None:
    """Gráfico 8: % del tiempo total que representa LLM inference."""
    df_t = df[
        df["llm_ms"].notna() & df["generation_time_ms"].notna() & (df["generation_time_ms"] > 0)
    ].copy()
    if len(df_t) < 2:
        print("[SKIP] plot_llm_dominance: menos de 2 runs con timings completos")
        return
    df_t["llm_pct"] = df_t["llm_ms"] / df_t["generation_time_ms"] * 100
    order = df_t.groupby("dataset_id")["llm_pct"].mean().sort_values(ascending=True).index

    fig, ax = plt.subplots(figsize=(12, max(5, len(order) * 0.6)))
    sns.barplot(
        data=df_t,
        y="dataset_id",
        x="llm_pct",
        order=order,
        hue="compile_pass",
        palette={True: SUCCESS_COLOR, False: FAIL_COLOR, None: "#95a5a6"},
        ax=ax,
    )
    ax.set_xlabel("% del tiempo total consumido por LLM")
    ax.set_ylabel("")
    ax.set_title("Dominancia del tiempo de inferencia LLM")
    ax.set_xlim(0, 105)
    ax.legend(title="Compila", loc="lower right")
    plt.tight_layout()
    _save(fig, outdir, "08_llm_dominance")


def plot_latency_vs_quality(df: pd.DataFrame, outdir: Path) -> None:
    """Gráfico 9: Scatter latencia vs quality score."""
    df_sc = df[df["latency_ms"].notna() & df["quality_score"].notna()].copy()
    if len(df_sc) < 3:
        print("[SKIP] plot_latency_vs_quality: menos de 3 runs con ambas métricas")
        return
    df_sc["latency_sec"] = df_sc["latency_ms"] / 1000.0
    fig, ax = plt.subplots(figsize=(10, 6))
    scatter = sns.scatterplot(
        data=df_sc,
        x="latency_sec",
        y="quality_score",
        hue="model_short",
        style="compile_pass",
        s=150,
        ax=ax,
        palette="tab10",
    )
    ax.set_xlabel("Latencia (s)")
    ax.set_ylabel("Quality Score")
    ax.set_title("Latencia vs Quality Score")
    ax.set_ylim(-0.05, 1.05)
    ax.legend(loc="best", fontsize=8)
    plt.tight_layout()
    _save(fig, outdir, "09_latency_vs_quality")


def plot_compile_rate_by_family(df: pd.DataFrame, outdir: Path) -> None:
    """Gráfico 10: Tasa de compilación por familia de dataset."""
    fam_stats = df.groupby("dataset_family").agg(
        total=("run_id", "count"),
        compiled=("compile_pass", lambda x: x.sum()),
    )
    fam_stats["rate"] = fam_stats["compiled"] / fam_stats["total"] * 100
    fam_stats = fam_stats.sort_values("rate", ascending=True)
    if len(fam_stats) < 2:
        return

    fig, ax = plt.subplots(figsize=(10, max(4, len(fam_stats) * 0.5)))
    ax.barh(fam_stats.index, fam_stats["rate"], color=COLORS[2])
    ax.set_xlim(0, 105)
    ax.set_xlabel("Tasa de compilación (%)")
    ax.set_title("Tasa de compilación por familia de proyecto")
    for i, (idx, row) in enumerate(fam_stats.iterrows()):
        ax.text(row["rate"] + 1, i, f"{row['compiled']}/{row['total']}", va="center", fontsize=9)
    plt.tight_layout()
    _save(fig, outdir, "10_compile_rate_by_family")


def plot_success_rate_by_campaign_model(df: pd.DataFrame, outdir: Path) -> None:
    """Gráfico 11: Heatmap de tasa de compilación (campaña x modelo)."""
    pivot = (
        df.groupby(["campaign", "model_short"])
        .agg(
            total=("run_id", "count"),
            compiled=("compile_pass", lambda x: x.sum()),
        )
        .reset_index()
    )
    pivot["rate"] = pivot["compiled"] / pivot["total"] * 100
    pivot = pivot.pivot(index="campaign", columns="model_short", values="rate")

    if pivot.empty or pivot.shape[0] < 2 or pivot.shape[1] < 2:
        print("[SKIP] plot_success_rate_by_campaign_model: matriz insuficiente")
        return

    fig, ax = plt.subplots(figsize=(max(8, pivot.shape[1] * 1.5), max(5, pivot.shape[0] * 0.6)))
    sns.heatmap(
        pivot,
        annot=True,
        fmt=".0f",
        cmap="RdYlGn",
        vmin=0,
        vmax=100,
        linewidths=0.5,
        ax=ax,
        cbar_kws={"label": "Tasa de compilación (%)"},
    )
    ax.set_title("Tasa de compilación: campaña vs modelo")
    plt.tight_layout()
    _save(fig, outdir, "11_compile_heatmap")


# ── Main ───────────────────────────────────────────────────────────────────


def main() -> None:
    parser = argparse.ArgumentParser(description="Genera gráficos de resultados del benchmark.")
    parser.add_argument(
        "--output",
        type=Path,
        default=Path("figures_thesis"),
        help="Directorio de salida para las figuras",
    )
    parser.add_argument(
        "--root",
        type=Path,
        default=Path(__file__).resolve().parent.parent,
        help="Raíz del repo para buscar resultados",
    )
    args = parser.parse_args()

    outdir = args.output
    root = args.root

    print(f"🔍 Buscando resultados en: {root}")
    files = discover_result_files(root)
    print(f"   Encontrados {len(files)} archivo(s) de resultados")

    df = load_all_runs(root)
    print(f"   Total runs cargados: {len(df)}")

    if df.empty:
        print("✖ No se encontraron runs. Abortando.")
        return

    _print_summary(df)

    # Generar figuras
    print("📊 Generando gráficos...")
    plot_compile_rate_by_campaign(df, outdir)
    plot_status_distribution(df, outdir)
    plot_latency_by_campaign(df, outdir)
    plot_success_rate_by_model(df, outdir)
    plot_quality_score(df, outdir)
    plot_assertions_per_test(df, outdir)
    plot_timing_breakdown(df, outdir)
    plot_llm_dominance(df, outdir)
    plot_latency_vs_quality(df, outdir)
    plot_compile_rate_by_family(df, outdir)
    plot_success_rate_by_campaign_model(df, outdir)

    print(f"\n✅ Gráficos guardados en: {outdir.resolve()}")
    print("   Archivos PNG + PDF generados para cada figura.")

    # Guardar CSV consolidado para referencia
    csv_path = outdir / "consolidated_runs.csv"
    df.to_csv(csv_path, index=False)
    print(f"   CSV consolidado: {csv_path}")


if __name__ == "__main__":
    main()
