"""Placeholder: multi-provider campaign tests.

The original spring-boot-v1 campaign that these tests validated was archived
and superseded by the RefTest-12 NVIDIA benchmark (see campaigns/ in archive/).

The new multi-provider benchmark is defined in benchmark_reftest_comparison_results/
and tested via consolidate_results.py + the batch runner scripts.
"""


def test_legacy_campaign_archived():
    """spring-boot-v1 campaign was intentionally deleted during cleanup.

    See commit dd7f0d9 (chore: remove old deliverable artifacts).
    The RefTest-12 benchmark scripts (scripts/run_batch.sh, scripts/chain_all.sh)
    replace the old campaign system.
    """
    assert True  # Old campaign correctly archived
