"""Unit tests for PIT mutation testing report parser."""

from __future__ import annotations

import pytest

from postproc.pit_parser import parse_pit_report


SAMPLE_MUTATIONS_XML = """<?xml version="1.0" encoding="UTF-8"?>
<mutations>
  <mutation detected="true">
    <mutatedClass>com.example.Service</mutatedClass>
    <mutatedMethod>doSomething</mutatedMethod>
    <mutator>org.pitest.mutationtest.engine.gregor.mutators.VoidMethodCallMutator</mutator>
  </mutation>
  <mutation detected="false">
    <mutatedClass>com.example.Service</mutatedClass>
    <mutatedMethod>doOther</mutatedMethod>
    <mutator>org.pitest.mutationtest.engine.gregor.mutators.VoidMethodCallMutator</mutator>
  </mutation>
  <mutation detected="true">
    <mutatedClass>com.example.Service</mutatedClass>
    <mutatedMethod>doThird</mutatedMethod>
    <mutator>org.pitest.mutationtest.engine.gregor.mutators.ReturnValsMutator</mutator>
  </mutation>
</mutations>
"""

EMPTY_MUTATIONS_XML = """<?xml version="1.0" encoding="UTF-8"?>
<mutations>
</mutations>
"""

MALFORMED_XML = "not xml <broken>"


class TestParsePitReport:
    def test_parse_valid_mutations_xml(self, tmp_path):
        pit_dir = tmp_path / "pit-reports" / "20240101"
        pit_dir.mkdir(parents=True)
        (pit_dir / "mutations.xml").write_text(SAMPLE_MUTATIONS_XML, encoding="utf-8")

        result = parse_pit_report(str(tmp_path / "pit-reports"))

        assert result is not None
        assert result["total_mutations"] == 3
        assert result["killed_mutations"] == 2
        assert result["mutation_score_pct"] == pytest.approx(66.6667, rel=1e-3)

    def test_parse_empty_mutations_xml(self, tmp_path):
        pit_dir = tmp_path / "pit-reports"
        pit_dir.mkdir(parents=True)
        (pit_dir / "mutations.xml").write_text(EMPTY_MUTATIONS_XML, encoding="utf-8")

        result = parse_pit_report(str(pit_dir))

        assert result is not None
        assert result["total_mutations"] == 0
        assert result["killed_mutations"] == 0
        assert result["mutation_score_pct"] is None

    def test_missing_file_returns_none(self, tmp_path):
        non_existent = tmp_path / "no-pit-reports"
        result = parse_pit_report(str(non_existent))
        assert result is None

    def test_malformed_xml_returns_none(self, tmp_path):
        pit_dir = tmp_path / "pit-reports"
        pit_dir.mkdir(parents=True)
        (pit_dir / "mutations.xml").write_text(MALFORMED_XML, encoding="utf-8")

        result = parse_pit_report(str(pit_dir))
        assert result is None
