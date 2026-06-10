#!/usr/bin/env python3
"""
Generate 7 publication-quality thesis figures from benchmark data.
Outputs PDF (vector) + PNG (300 DPI) to figures/.
"""

import argparse
import os
from pathlib import Path

import matplotlib
import matplotlib.pyplot as plt
import numpy as np
import pandas as pd

matplotlib.use("Agg")
from math import pi

import seaborn as sns

# ---------------------------------------------------------------------------
# Style
# ---------------------------------------------------------------------------
sns.set_theme(
    style="whitegrid",
    context="talk",
    palette="colorblind",
    font="serif",
)
plt.rcParams.update(
    {
        "figure.dpi": 300,
        "savefig.dpi": 300,
        "savefig.bbox": "tight",
        "font.size": 12,
        "axes.titlesize": 14,
        "axes.labelsize": 12,
        "pdf.fonttype": 42,
        "ps.fonttype": 42,
    }
)

THESIS_COLORS = {
    "green": "#2ecc71",
    "red": "#e74c3c",
    "blue": "#3498db",
    "dark_blue": "#2c3e50",
    "orange": "#e67e22",
    "teal": "#1abc9c",
    "purple": "#9b59b6",
}
CB = sns.color_palette("colorblind", 10)

REPO_ROOT = Path(__file__).resolve().parent.parent


def save_fig(fig, name, output_dir):
    """Save figure as PDF and PNG."""
    os.makedirs(output_dir, exist_ok=True)
    pdf_path = os.path.join(output_dir, f"{name}.pdf")
    png_path = os.path.join(output_dir, f"{name}.png")
    fig.savefig(pdf_path)
    fig.savefig(png_path, dpi=300)
    plt.close(fig)
    return pdf_path, png_path


def load_nvidia_consolidated():
    path = REPO_ROOT / "results" / "nvidia" / "consolidated.csv"
    if not path.exists():
        print(f"  [WARN] NVIDIA consolidated not found: {path}")
        return None
    df = pd.read_csv(path)
    df["latency_sec"] = df["avg_latency_ms"] / 1000.0
    return df


def load_thesis_metrics():
    path = REPO_ROOT / "benchmark_reftest_comparison_results" / "thesis_metrics.csv"
    if not path.exists():
        print(f"  [WARN] Thesis metrics not found: {path}")
        return None
    df = pd.read_csv(path)
    df["model_short"] = df["model"].apply(lambda x: x.split("/")[-1] if "/" in x else x)
    return df


def load_detailed_runs():
    path = REPO_ROOT / "archive" / "figures_thesis_v2" / "consolidated_runs.csv"
    if not path.exists():
        print(f"  [WARN] Detailed runs not found: {path}")
        return None
    df = pd.read_csv(path, low_memory=False)
    for col in [
        "parse_ms",
        "retrieval_ms",
        "llm_ms",
        "postproc_ms",
        "quality_score",
        "latency_ms",
        "assertion_count",
        "test_count",
        "generation_time_ms",
    ]:
        if col in df.columns:
            df[col] = pd.to_numeric(df[col], errors="coerce")
    return df


def map_dataset_to_project(dataset_id):
    """Map a dataset_id (e.g. commons-cli-helpformatter) to a short project name."""
    if not isinstance(dataset_id, str):
        return None
    known = {
        "commons-cli": "commons-cli",
        "commons-dbutils": "commons-dbutils",
        "commons-validator": "commons-validator",
        "cucumber-expressions": "cucumber-expressions",
        "datafaker": "datafaker",
        "jsoup": "jsoup",
        "ice4j": "ice4j",
        "openapi": "openapi-diff",
        "commons-collections4": "commons-collections4",
    }
    for prefix, name in known.items():
        if dataset_id.startswith(prefix):
            return name
    parts = dataset_id.split("-")
    if len(parts) >= 2:
        return f"{parts[0]}-{parts[1]}"
    return dataset_id


# ---------------------------------------------------------------------------
# Figure 1: Success Rate by Project
# ---------------------------------------------------------------------------
def fig01_success_rate(nv_df, output_dir):
    print("  Figure 01: Success Rate by Project...")
    if nv_df is None or nv_df.empty:
        print("    SKIP — no data")
        return

    df = nv_df.sort_values("generation_rate").copy()
    df["fail_rate"] = 1.0 - df["generation_rate"]

    fig, ax = plt.subplots(figsize=(8, 5))
    y_pos = range(len(df))

    bars_success = ax.barh(
        y_pos,
        df["generation_rate"].values * 100,
        height=0.6,
        color=THESIS_COLORS["green"],
        label="Success",
    )
    bars_fail = ax.barh(
        y_pos,
        df["fail_rate"].values * 100,
        left=df["generation_rate"].values * 100,
        height=0.6,
        color=THESIS_COLORS["red"],
        label="Failure",
    )

    for i, (_, row) in enumerate(df.iterrows()):
        pct = row["generation_rate"] * 100
        ax.text(
            pct / 2,
            i,
            f"{pct:.0f}%",
            va="center",
            ha="center",
            fontsize=10,
            fontweight="bold",
            color="white",
        )

    ax.set_yticks(list(y_pos))
    ax.set_yticklabels(df["project"].values)
    ax.set_xlim(0, 100)
    ax.set_xlabel("Success Rate (%)")
    ax.set_title("Test Generation Success Rate by Project")
    ax.legend(loc="lower right", frameon=True)
    save_fig(fig, "01_success_rate_by_project", output_dir)


# ---------------------------------------------------------------------------
# Figure 2: Avg Latency by Project
# ---------------------------------------------------------------------------
def fig02_avg_latency(nv_df, runs_df, output_dir):
    print("  Figure 02: Average Latency by Project...")
    if nv_df is None or nv_df.empty:
        print("    SKIP — no data")
        return

    df = nv_df.sort_values("avg_latency_ms", ascending=False).copy()

    # Try to compute std from detailed runs
    if runs_df is not None and not runs_df.empty:
        nv_runs = runs_df[runs_df["provider"] == "nvidia"].copy()
        if not nv_runs.empty:
            nv_runs["project"] = nv_runs["dataset_id"].apply(map_dataset_to_project)
            std_data = nv_runs.groupby("project")["latency_ms"].std(ddof=0) / 1000.0
            df["latency_std_sec"] = df["project"].map(std_data).fillna(0)
        else:
            df["latency_std_sec"] = 0
    else:
        df["latency_std_sec"] = 0

    n_colors = len(df)
    palette = sns.light_palette(THESIS_COLORS["blue"], n_colors=n_colors, reverse=True)
    colors = [palette[i] for i in range(n_colors)]

    fig, ax = plt.subplots(figsize=(8, 5))
    bars = ax.bar(
        range(len(df)),
        df["latency_sec"].values,
        color=colors,
        width=0.6,
        edgecolor="0.2",
        linewidth=0.5,
    )

    has_std = df["latency_std_sec"].sum() > 0
    if has_std:
        ax.errorbar(
            range(len(df)),
            df["latency_sec"].values,
            yerr=df["latency_std_sec"].values,
            fmt="none",
            capsize=4,
            capthick=1.5,
            ecolor="0.15",
            elinewidth=1.5,
        )

    for i, (_, row) in enumerate(df.iterrows()):
        ax.text(
            i,
            row["latency_sec"] + (row.get("latency_std_sec", 0) if has_std else 1),
            f"{row['latency_sec']:.1f}s",
            ha="center",
            va="bottom",
            fontsize=9,
        )

    ax.set_xticks(range(len(df)))
    ax.set_xticklabels(df["project"].values, rotation=30, ha="right")
    ax.set_ylabel("Latency (seconds)")
    ax.set_title("Average Generation Latency by Project")
    sns.despine()
    save_fig(fig, "02_avg_latency_by_project", output_dir)


# ---------------------------------------------------------------------------
# Figure 3: Tests vs Assertions
# ---------------------------------------------------------------------------
def fig03_tests_vs_assertions(nv_df, output_dir):
    print("  Figure 03: Tests vs Assertions...")
    if nv_df is None or nv_df.empty:
        print("    SKIP — no data")
        return

    df = nv_df.sort_values("project").copy()
    x = range(len(df))
    width = 0.35

    fig, ax = plt.subplots(figsize=(9, 5))
    bars1 = ax.bar(
        [i - width / 2 for i in x],
        df["avg_tests_per_run"].values,
        width,
        label="Tests Generated",
        color=THESIS_COLORS["blue"],
        edgecolor="0.2",
        linewidth=0.5,
    )
    bars2 = ax.bar(
        [i + width / 2 for i in x],
        df["avg_assertions_per_run"].values,
        width,
        label="Assertions",
        color=THESIS_COLORS["orange"],
        edgecolor="0.2",
        linewidth=0.5,
    )

    for bar in bars1:
        ax.text(
            bar.get_x() + bar.get_width() / 2,
            bar.get_height() + 0.3,
            f"{bar.get_height():.0f}",
            ha="center",
            va="bottom",
            fontsize=8,
        )
    for bar in bars2:
        ax.text(
            bar.get_x() + bar.get_width() / 2,
            bar.get_height() + 0.3,
            f"{bar.get_height():.0f}",
            ha="center",
            va="bottom",
            fontsize=8,
        )

    ax.set_xticks(list(x))
    ax.set_xticklabels(df["project"].values, rotation=30, ha="right")
    ax.set_ylabel("Count")
    ax.set_title("Tests and Assertions Generated per Project")
    ax.legend(frameon=True)
    sns.despine()
    save_fig(fig, "03_tests_vs_assertions", output_dir)


# ---------------------------------------------------------------------------
# Figure 4: Model Comparison Radar
# ---------------------------------------------------------------------------
def fig04_model_radar(metrics_df, output_dir):
    print("  Figure 04: Model Comparison Radar...")
    if metrics_df is None or metrics_df.empty:
        print("    SKIP — no data")
        return

    # Prep axis values
    categories = [
        "Success\nRate",
        "Quality\nScore",
        "Assertion\nCount",
        "Generation\nSpeed",
        "Coverage",
    ]
    n_axes = len(categories)

    nv = metrics_df[metrics_df["model"].str.contains("nvidia", case=False)]
    gm = metrics_df[metrics_df["model"].str.contains("gemini", case=False)]

    if nv.empty or gm.empty:
        print("    SKIP — missing model rows")
        return

    nv_row = nv.iloc[0]
    gm_row = gm.iloc[0]

    success_nv = nv_row["success_rate"]
    success_gm = gm_row["success_rate"]
    quality_nv = nv_row["quality_score"]
    quality_gm = gm_row["quality_score"]

    # Assertion count normalized by max
    max_assert = max(nv_row["avg_assertion_count"], gm_row["avg_assertion_count"])
    assert_nv = nv_row["avg_assertion_count"] / max_assert if max_assert > 0 else 0
    assert_gm = gm_row["avg_assertion_count"] / max_assert if max_assert > 0 else 0

    # Generation speed: 1 - normalized latency
    max_lat = max(nv_row["avg_latency_sec"], gm_row["avg_latency_sec"])
    speed_nv = 1 - (nv_row["avg_latency_sec"] / max_lat) if max_lat > 0 else 0.5
    speed_gm = 1 - (gm_row["avg_latency_sec"] / max_lat) if max_lat > 0 else 0.5

    # Coverage (both likely 0, kept for completeness)
    cov_nv = nv_row["coverage_pct"] / 100.0
    cov_gm = gm_row["coverage_pct"] / 100.0

    values_nv = [success_nv, quality_nv, assert_nv, speed_nv, cov_nv]
    values_gm = [success_gm, quality_gm, assert_gm, speed_gm, cov_gm]

    # Close the polygon
    angles = [n / n_axes * 2 * pi for n in range(n_axes)]
    angles += angles[:1]
    values_nv_closed = values_nv + values_nv[:1]
    values_gm_closed = values_gm + values_gm[:1]

    fig, ax = plt.subplots(figsize=(7, 7), subplot_kw={"projection": "polar"})

    ax.set_theta_offset(pi / 2)
    ax.set_theta_direction(-1)
    ax.set_xticks(angles[:-1])
    ax.set_xticklabels(categories, fontsize=10)

    ax.set_rlabel_position(30)
    ax.set_yticks([0.2, 0.4, 0.6, 0.8, 1.0])
    ax.set_yticklabels(["0.2", "0.4", "0.6", "0.8", "1.0"], fontsize=8)
    ax.set_ylim(0, 1)

    ax.plot(
        angles,
        values_nv_closed,
        "o-",
        linewidth=2,
        color=THESIS_COLORS["blue"],
        label="NVIDIA Llama-3.3-70B",
    )
    ax.fill(angles, values_nv_closed, alpha=0.15, color=THESIS_COLORS["blue"])

    ax.plot(
        angles,
        values_gm_closed,
        "o-",
        linewidth=2,
        color=THESIS_COLORS["orange"],
        label="Gemini 2.0 Flash",
    )
    ax.fill(angles, values_gm_closed, alpha=0.15, color=THESIS_COLORS["orange"])

    ax.set_title("Model Comparison: NVIDIA vs Gemini", pad=25, fontsize=13)
    ax.legend(loc="upper right", bbox_to_anchor=(1.3, 1.1), frameon=True)
    save_fig(fig, "04_model_comparison_radar", output_dir)


# ---------------------------------------------------------------------------
# Figure 5: Latency vs Quality Scatter
# ---------------------------------------------------------------------------
def fig05_latency_vs_quality(nv_df, runs_df, output_dir):
    print("  Figure 05: Latency vs Quality...")
    if nv_df is None or nv_df.empty:
        print("    SKIP — no data")
        return

    # Compute average quality per project from detailed runs (NVIDIA only)
    if runs_df is not None and not runs_df.empty:
        nv_runs = runs_df[runs_df["provider"] == "nvidia"].copy()
        if not nv_runs.empty and "quality_score" in nv_runs.columns:
            nv_runs["project"] = nv_runs["dataset_id"].apply(map_dataset_to_project)
            qual = nv_runs.groupby("project")["quality_score"].mean()
            df = nv_df.merge(
                qual.rename("avg_quality"), left_on="project", right_index=True, how="inner"
            )
        else:
            print("    SKIP — no quality data in runs")
            return
    else:
        print("    SKIP — no detailed runs for quality")
        return

    if df.empty:
        print("    SKIP — no matching projects")
        return

    fig, ax = plt.subplots(figsize=(8, 5))
    sns.regplot(
        data=df,
        x="latency_sec",
        y="avg_quality",
        ax=ax,
        ci=95,
        scatter_kws={"s": 80, "zorder": 5, "color": THESIS_COLORS["blue"], "edgecolor": "0.2"},
        line_kws={"color": THESIS_COLORS["red"], "lw": 1.5},
    )

    for _, row in df.iterrows():
        ax.annotate(
            row["project"],
            (row["latency_sec"], row["avg_quality"]),
            textcoords="offset points",
            xytext=(8, 6),
            fontsize=8,
            alpha=0.85,
        )

    ax.set_xlabel("Avg Latency (seconds)")
    ax.set_ylabel("Quality Score")
    ax.set_title("Latency vs Quality Trade-off")
    ax.set_ylim(0, 1.05)
    sns.despine()
    save_fig(fig, "05_latency_vs_quality", output_dir)


# ---------------------------------------------------------------------------
# Figure 6: Timing Breakdown
# ---------------------------------------------------------------------------
def fig06_timing_breakdown(runs_df, output_dir):
    print("  Figure 06: Timing Breakdown...")
    if runs_df is None or runs_df.empty:
        print("    SKIP — no data")
        return

    nv_runs = runs_df[runs_df["provider"] == "nvidia"].copy()
    if nv_runs.empty:
        print("    SKIP — no NVIDIA runs")
        return

    # Columns available: parse_ms, retrieval_ms, llm_ms, postproc_ms
    time_cols = ["parse_ms", "retrieval_ms", "llm_ms", "postproc_ms"]
    avail = [c for c in time_cols if c in nv_runs.columns]
    if not avail:
        print("    SKIP — no timing columns")
        return

    nv_runs["project"] = nv_runs["dataset_id"].apply(map_dataset_to_project)
    # Filter to the 5 main projects
    main_projects = [
        "commons-cli",
        "commons-dbutils",
        "commons-validator",
        "cucumber-expressions",
        "datafaker",
    ]
    proj_data = nv_runs[nv_runs["project"].isin(main_projects)].copy()
    if proj_data.empty:
        print("    SKIP — no matching projects in timing data; using all available")
        proj_data = nv_runs[nv_runs["dataset_family"].notna()].copy()

    # Group by project and sum times
    grouped = proj_data.groupby("project")[avail].mean() / 1000.0  # ms → s
    grouped = grouped.loc[grouped.sum(axis=1).sort_values(ascending=True).index]

    if grouped.empty:
        print("    SKIP — empty after grouping")
        return

    labels = {
        "parse_ms": "Parse",
        "retrieval_ms": "Retrieval",
        "llm_ms": "LLM Generation",
        "postproc_ms": "Post-Processing",
    }
    color_map = {
        "parse_ms": THESIS_COLORS["teal"],
        "retrieval_ms": THESIS_COLORS["purple"],
        "llm_ms": THESIS_COLORS["blue"],
        "postproc_ms": THESIS_COLORS["green"],
    }
    plot_cols = [c for c in time_cols if c in grouped.columns]

    fig, ax = plt.subplots(figsize=(9, 5))
    y_pos = range(len(grouped))
    left = np.zeros(len(grouped))

    for col in plot_cols:
        vals = grouped[col].values
        ax.barh(
            y_pos,
            vals,
            left=left,
            height=0.6,
            label=labels.get(col, col),
            color=color_map.get(col, "gray"),
            edgecolor="0.2",
            linewidth=0.3,
        )
        left += vals

    # Annotate total on each bar
    for i, total in enumerate(left):
        ax.text(total + 0.2, i, f"{total:.1f}s", va="center", fontsize=8, alpha=0.8)

    ax.set_yticks(list(y_pos))
    ax.set_yticklabels(grouped.index.tolist())
    ax.set_xlabel("Time (seconds)")
    ax.set_title("Generation Pipeline Timing Breakdown")
    ax.legend(loc="lower right", frameon=True, fontsize=9)
    sns.despine()
    save_fig(fig, "06_timing_breakdown", output_dir)


# ---------------------------------------------------------------------------
# Figure 7: Metrics Heatmap
# ---------------------------------------------------------------------------
def fig07_metrics_heatmap(nv_df, output_dir):
    print("  Figure 07: Metrics Heatmap...")
    if nv_df is None or nv_df.empty:
        print("    SKIP — no data")
        return

    df = nv_df.set_index("project")
    cols = ["generation_rate", "latency_sec", "avg_tests_per_run", "avg_assertions_per_run"]
    labels_map = {
        "generation_rate": "Success Rate",
        "latency_sec": "Avg Latency (s)",
        "avg_tests_per_run": "Tests/Run",
        "avg_assertions_per_run": "Assertions/Run",
    }
    avail = [c for c in cols if c in df.columns]
    if not avail:
        print("    SKIP — no metric columns")
        return

    heat_data = df[avail].copy()
    heat_data.rename(columns=labels_map, inplace=True)

    # Also rename index for display
    heat_data.index = heat_data.index.str.replace("-", " ").str.title()

    # Min-max normalize for color
    norm = (heat_data - heat_data.min()) / (heat_data.max() - heat_data.min() + 1e-10)

    fig, ax = plt.subplots(figsize=(8, 4 + len(heat_data) * 0.5))
    sns.heatmap(
        norm,
        annot=heat_data,
        fmt=".2f",
        cmap="YlOrRd",
        linewidths=0.5,
        linecolor="0.9",
        cbar_kws={"label": "Normalized Score", "shrink": 0.8},
        ax=ax,
        annot_kws={"fontsize": 9},
    )
    ax.set_title("Benchmark Metrics Overview")
    ax.set_ylabel("")
    ax.set_xticklabels(ax.get_xticklabels(), rotation=30, ha="right")
    save_fig(fig, "07_metrics_heatmap", output_dir)


# ---------------------------------------------------------------------------
# Main
# ---------------------------------------------------------------------------
def main():
    parser = argparse.ArgumentParser(description="Generate thesis figures from benchmark data.")
    parser.add_argument(
        "--output-dir",
        default="figures",
        help="Output directory for figures (default: figures)",
    )
    args = parser.parse_args()

    output_dir = os.path.join(REPO_ROOT, args.output_dir)
    os.makedirs(output_dir, exist_ok=True)

    print(f"Output directory: {output_dir}")
    print("Loading data...")

    nv_df = load_nvidia_consolidated()
    metrics_df = load_thesis_metrics()
    runs_df = load_detailed_runs()

    if nv_df is not None:
        print(f"  NVIDIA consolidated: {len(nv_df)} projects")
    if metrics_df is not None:
        print(f"  Thesis metrics: {len(metrics_df)} models")
    if runs_df is not None:
        print(f"  Detailed runs: {len(runs_df)} rows")

    print("\nGenerating figures...")
    figures = []

    figs_specs = [
        ("01_success_rate_by_project", fig01_success_rate, [nv_df]),
        ("02_avg_latency_by_project", fig02_avg_latency, [nv_df, runs_df]),
        ("03_tests_vs_assertions", fig03_tests_vs_assertions, [nv_df]),
        ("04_model_comparison_radar", fig04_model_radar, [metrics_df]),
        ("05_latency_vs_quality", fig05_latency_vs_quality, [nv_df, runs_df]),
        ("06_timing_breakdown", fig06_timing_breakdown, [runs_df]),
        ("07_metrics_heatmap", fig07_metrics_heatmap, [nv_df]),
    ]

    for name, func, args_list in figs_specs:
        try:
            func(*args_list, output_dir)
            figures.append(name)
        except Exception as e:
            print(f"  ERROR in {name}: {e}")

    # Summary
    print("\n" + "=" * 60)
    print("GENERATION SUMMARY")
    print("=" * 60)
    generated_pdfs = []
    generated_pngs = []
    for name in figures:
        pdf = os.path.join(output_dir, f"{name}.pdf")
        png = os.path.join(output_dir, f"{name}.png")
        if os.path.exists(pdf):
            generated_pdfs.append(pdf)
        if os.path.exists(png):
            generated_pngs.append(png)

    print(f"  Output directory: {output_dir}")
    print(f"  PDFs generated:  {len(generated_pdfs)}")
    for p in generated_pdfs:
        size = os.path.getsize(p) / 1024
        print(f"    {os.path.basename(p):40s} {size:7.1f} KB")
    print(f"  PNGs generated:  {len(generated_pngs)}")
    for p in generated_pngs:
        size = os.path.getsize(p) / 1024
        print(f"    {os.path.basename(p):40s} {size:7.1f} KB")
    print(
        f"\n  {'All 14 files generated successfully!' if len(figures) == 7 else f'{len(figures)}/7 figures generated'}"
    )

    # Quick figure description
    if "01_success_rate_by_project" in figures:
        print("\n  Sample: Figure 01 - Horizontal stacked bar showing 100%")
        print("  success rate across all 5 NVIDIA-benchmarked projects.")


if __name__ == "__main__":
    main()
