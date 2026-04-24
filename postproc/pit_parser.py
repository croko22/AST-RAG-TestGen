"""PIT mutation testing report parser."""

from __future__ import annotations

import glob
import xml.etree.ElementTree as ET
from pathlib import Path


def parse_pit_report(pit_report_path: str) -> dict | None:
    """Parse PIT mutations.xml → {mutation_score_pct, killed_mutations, total_mutations}.

    Args:
        pit_report_path: Base directory where PIT reports are stored.
            mutations.xml will be searched recursively under this path.

    Returns:
        dict with mutation_score_pct, killed_mutations, total_mutations,
        or None if report not found or unparsable.
    """
    path = Path(pit_report_path)

    # Find mutations.xml (may be under timestamp subdir, use glob)
    pattern = str(path / "**" / "mutations.xml")
    matches = glob.glob(pattern, recursive=True)

    if not matches:
        return None

    xml_file = Path(matches[0])

    try:
        root = ET.fromstring(xml_file.read_text(encoding="utf-8"))
    except (ET.ParseError, OSError, UnicodeDecodeError):
        return None

    total_mutations = 0
    killed_mutations = 0

    for mutation in root.iter("mutation"):
        total_mutations += 1
        detected = mutation.attrib.get("detected", "false").lower()
        if detected == "true":
            killed_mutations += 1

    if total_mutations == 0:
        return {
            "mutation_score_pct": None,
            "killed_mutations": 0,
            "total_mutations": 0,
        }

    mutation_score_pct = (killed_mutations / total_mutations) * 100.0

    return {
        "mutation_score_pct": mutation_score_pct,
        "killed_mutations": killed_mutations,
        "total_mutations": total_mutations,
    }
