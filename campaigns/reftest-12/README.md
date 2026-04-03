# RefTest 12-Project Dataset

Reproduction of the RefTest benchmark: 12 open-source Java projects used to
evaluate automated unit-test generation approaches (RAG vs RL).

## Projects

| # | Project | Source | Tag/Version |
|---|---------|--------|-------------|
| 1 | commons-cli | apache/commons-cli | rel/commons-cli-1.5.0 |
| 2 | commons-collections4 | apache/commons-collections | rel/commons-collections4-4.5.0-M1 |
| 3 | datafaker | datafaker-net/datafaker | 1.9.0 |
| 4 | binance-connector | binance/binance-connector-java | v2.0.0 |
| 5 | jsoup | jhy/jsoup | latest (main) |
| 6 | openapi-diff | OpenAPITools/openapi-diff | latest (master) |
| 7 | cucumber-expressions | cucumber/cucumber-expressions | latest (main) |
| 8 | rtree | davidmoten/rtree | latest (master) |
| 9 | ice4j | jitsi/ice4j | latest (master) |
| 10 | morel | hydromatic/morel | latest (main) |
| 11 | commons-dbutils | apache/commons-dbutils | latest (master) |
| 12 | commons-validator | apache/commons-validator | latest (master) |

## Filtering Criteria

Not every method in every class is included. The dataset applies the RefTest
eligibility filter (`filter_reftest_methods` from `core/parser.py`):

- **Exclude private methods** -- only public, protected, and package-private
  methods are eligible.
- **Exclude trivial methods** -- methods with 1 or fewer effective lines of
  code (after stripping comments, blanks, and bare braces) are excluded.
- **Exclude inner-class methods** -- methods declared inside inner or
  anonymous classes are excluded.

Each qualifying method produces one manifest entry (one focal method = one
unit-test generation task).

## Directory Layout

```
campaigns/reftest-12/
  manifest.json          -- dataset entries for benchmark execution
  README.md              -- this file

datasets/reftest-12/     -- cloned repositories (created by gather script)
  commons-cli/
  commons-collections4/
  ...
```

## Regenerating the Manifest

```bash
# From the project root:

# Full run (clone + scan)
python scripts/gather_dataset.py

# Skip cloning if repos already exist
python scripts/gather_dataset.py --skip-clone

# Override directories
python scripts/gather_dataset.py --repos-dir /tmp/repos --output-dir ./my-campaign
```

The script prints progress for each repository and a summary at the end
showing the total number of projects and focal methods discovered.
