# Dataset Survey: Benchmarks for Java Unit Test Generation

This document catalogs the datasets and benchmarks used in the RefTest paper and
related work on LLM-based Java unit test generation. Research conducted April 2026.

---

## 1. RefTest Dataset (12 projects, 1515 methods)

**Paper**: "Reference-Based Retrieval-Augmented Unit Test Generation"
- Authors: Zhe Zhang, Xingyu Liu, Yuanzhang Lin, Xiang Gao, Hailong Sun, Yuan Yuan
- Venue: ACM TOSEM 2025
- DOI: [10.1145/3765758](https://doi.org/10.1145/3765758)
- Preprint (as "APT"): [arXiv:2410.13542](https://arxiv.org/abs/2410.13542)
- No public replication package found on GitHub or Zenodo

### Dataset Composition

The RefTest paper states: "We utilize 4 moderately complex projects with >10,000 lines of code
from the datasets provided by HITS and ChatUniTest. We additionally crawl 8 repositories from GitHub,
bringing the total number of projects to 12."

Selection criteria (from the paper):
- At least 150 stars on GitHub
- Updated within the last month (active maintenance)
- Encompasses utilities, parsers (HTML/expression), and network protocols

### 4 Projects from HITS + ChatUniTest Datasets

| Project | GitHub | Domain |
|---------|--------|--------|
| Commons-CLI | https://github.com/apache/commons-cli | Command-line interface parsing |
| Commons-Collections | https://github.com/apache/commons-collections | Data structure utilities |
| Datafaker | https://github.com/datafaker-net/datafaker | Data generation |
| (4th project likely Commons-Codec, Gson, or JDom2 -- see HITS dataset below) | | |

Note: The exact 4 projects from HITS/ChatUniTest are not individually listed in the paper.
Based on overlap analysis between HITS (10 projects), ChatUniTest (4 projects), and the
RefTest Table 4 (12 projects), the shared projects are likely Commons-CLI, Commons-Collections,
and Datafaker. The 4th could be any of Commons-Codec, Gson, JDom2, or Commons-CSV.

### 8 Additional Projects Crawled from GitHub

| Project | GitHub | Domain |
|---------|--------|--------|
| binance-connector-java | https://github.com/binance/binance-connector-java | Crypto exchange API |
| morel | https://github.com/hydromatic/morel | Functional query language |
| cucumber-expressions | https://github.com/cucumber/cucumber-expressions | Expression parsing |
| openapi-diff | https://github.com/OpenAPITools/openapi-diff | API diff tool |
| commons-dbutils | https://github.com/apache/commons-dbutils | Database utilities |
| commons-validator | https://github.com/apache/commons-validator | Validation utilities |
| jsoup | https://github.com/jhy/jsoup | HTML parsing |
| rtree | https://github.com/davidmoten/rtree | Spatial indexing |
| ice4j | https://github.com/jitsi/ice4j | Network protocols (ICE) |

Note: There are 9 projects listed above because the exact 4 from HITS/ChatUniTest is uncertain.
The actual total is 12; the overlap needs the published paper to confirm.

### Per-Project Results (from Table 4, Iterative Strategy Ablation)

| Project | Compilation & Run Error | Assertion Error | Successful Exec | Full Coverage |
|---------|------------------------|-----------------|-----------------|---------------|
| binance-connector-java | 12.3% | 34.1% | 53.6% | 53.3% |
| commons-collections | 17.9% | 4.8% | 77.2% | 73.8% |
| commons-cli | 23.1% | 9.6% | 67.3% | 57.7% |
| morel | 32.7% | 9.6% | 57.7% | 50.0% |
| datafaker | 69.5% | 8.4% | 22.1% | 21.4% |
| cucumber-expressions | 37.5% | 0.0% | 62.5% | 54.2% |
| openapi-diff | 58.5% | 4.6% | 36.9% | 35.4% |
| commons-dbutils | 0.0% | 0.0% | 100.0% | 91.7% |
| commons-validator | 20.0% | 14.7% | 65.3% | 53.3% |
| jsoup | 11.3% | 12.5% | 76.2% | 60.5% |
| rtree | 57.5% | 2.5% | 40.0% | 32.5% |
| ice4j | 34.5% | 5.6% | 59.9% | 50.0% |

---

## 2. HITS Dataset (10 projects, 120 complex methods)

**Paper**: "HITS: High-coverage LLM-based Unit Test Generation via Method Slicing"
- Authors: Zejun Wang, Kaibo Liu, Ge Li, Zhi Jin (Peking University)
- Venue: ASE 2024
- DOI: [10.1145/3691620.3695501](https://doi.org/10.1145/3691620.3695501)
- Preprint: [arXiv:2408.11324](https://arxiv.org/abs/2408.11324)
- No dedicated GitHub repository found for the HITS tool itself
- HITS is integrated into ChatUniTest as `phaseType=HITS`

### Dataset Composition

HITS builds a dataset of "complex focal methods" collected from projects used by existing
state-of-the-art approaches. Projects are split into two groups: those created before
the GPT-3.5 training cutoff (Learned=Y) and those created after (Learned=N).

Selection criteria:
1. The project or its domain should be used in EvoSuite or ChatUniTest
2. The project should contain 1 to 30 complex methods
3. When selecting projects in the domain adapted by ChatUniTest, prioritize projects with high stars on GitHub

### Project List (from Table 2)

| Project | Abbr. | Domain | Version | In Training Data? | # Methods Under Test | Avg. Lines | Avg. Perplexity |
|---------|-------|--------|---------|--------------------|---------------------|------------|-----------------|
| Commons-CLI | CLI | Command-line Interface | 1.5.0 | Yes | 6 | 32.83 | 12.00 |
| Commons-CSV | CSV | Data processing | 1.10.0 | Yes | 6 | 47.67 | 15.67 |
| Gson | GSO | Serialization | 2.10.1 | Yes | 21 | 51.14 | 20.29 |
| Commons-codec | COD | Encoding | 3a6873e | Yes | 19 | 73.00 | 21.58 |
| Commons-collections4 | COL | Utility | 4.5.0-M1 | Yes | 14 | 28.36 | 14.00 |
| JDom2 | JDO | Text Processing (XML) | 2.0.6 | Yes | 21 | 36.71 | 14.19 |
| Datafaker | DAT | Data Generation | 1.9.0 | No | 8 | 28.13 | 11.62 |
| Event-ruler | RUL | Event Engine | 1.4.0 | No | 16 | 75.94 | 24.62 |
| windward | WIN | Microservices | 1.5.1-SNAPSHOT | No | 2 | 34.00 | 13.00 |
| batch-processing-gateway | BPG | Cloud Computing | 1.1 | No | 7 | 48.57 | 13.71 |

**Total**: 120 complex methods across 10 projects

### GitHub Repositories

| Project | GitHub URL |
|---------|-----------|
| Commons-CLI | https://github.com/apache/commons-cli |
| Commons-CSV | https://github.com/apache/commons-csv |
| Gson | https://github.com/google/gson |
| Commons-codec | https://github.com/apache/commons-codec |
| Commons-collections4 | https://github.com/apache/commons-collections |
| JDom2 | https://github.com/hunterhacker/jdom |
| Datafaker | https://github.com/datafaker-net/datafaker |
| Event-ruler | https://github.com/aws/event-ruler |
| windward | (microservices project -- possibly private or renamed) |
| batch-processing-gateway | (cloud computing project -- possibly private or renamed) |

### How to Use

Since HITS is integrated into ChatUniTest:
```bash
mvn chatunitest:method -DselectMethod=ClassName#methodName -DphaseType=HITS
```

---

## 3. ChatUniTest Dataset (4 projects, 264 sampled methods from 835 total)

**Paper**: "ChatUniTest: A Framework for LLM-Based Test Generation"
- Authors: Yinghao Chen, Zehao Hu, Chen Zhi, Junxiao Han, Shuiguang Deng, Jianwei Yin (Zhejiang University)
- Venue: FSE 2024 Demo track
- DOI: [10.1145/3663529.3663801](https://doi.org/10.1145/3663529.3663801)
- Preprint: [arXiv:2305.04764](https://arxiv.org/abs/2305.04764)
- Repository: https://github.com/ZJU-ACES-ISE/ChatUniTest

### Dataset Composition

ChatUniTest selected 4 Java projects for its effectiveness evaluation. The first two are
"widely used in related work" and likely in GPT-3.5 training data. The latter two are
"unseen popular projects" (100+ stars, created after GPT-3.5 training cutoff).

Random sampling with 95% confidence level and 5% margin of error yielded 264 methods
from 835 total focal methods.

### Project List (from Table 1)

| Project | Version | In Training Data? | ChatUniTest Line Coverage | TestSpark | EvoSuite |
|---------|---------|--------------------|--------------------------|-----------|----------|
| Commons-CLI | 1.5.0 | No (seen) | 70.9% | 78.4% | 91.8% |
| Commons-CSV | 1.10.0 | No (seen) | 73.3% | - (token limit) | 28.3% |
| Ecommerce-microservice | 695a6d4 | Yes (unseen) | 26.7% | 36.7% | - (JDK issue) |
| Binance-connector | 2.0.0 | Yes (unseen) | 49.2% | 29.7% | 20.8% |

### GitHub Repositories

| Project | GitHub URL |
|---------|-----------|
| Commons-CLI | https://github.com/apache/commons-cli |
| Commons-CSV | https://github.com/apache/commons-csv |
| Ecommerce-microservice | https://github.com/eugenp/tutorials (ecommerce module, commit 695a6d4) |
| Binance-connector | https://github.com/binance/binance-connector-java |

### How to Use

```bash
# Install ChatUniTest Maven plugin
mvn chatunitest:class -DselectClass=ClassName
mvn chatunitest:project  # For entire project
```

---

## 4. ChatTester Dataset (185 projects, 1748 data pairs, 1000 benchmark)

**Paper**: "No More Manual Tests? Evaluating and Improving ChatGPT for Unit Test Generation"
- Authors: Zhiqiang Yuan, Mingwei Liu, Shiji Ding, Kaixin Wang, Yixuan Chen, Xin Peng, Yiling Lou (Fudan University)
- Venue: FSE 2024
- DOI: [10.1145/3660783](https://doi.org/10.1145/3660783)
- Preprint: [arXiv:2305.04207](https://arxiv.org/abs/2305.04207)
- Repository: https://github.com/jstzwj/ChatTester

### Dataset Construction

1. Start from 4,685 Java projects in **CodeSearchNet** benchmark
2. Filter criteria:
   - Continuously maintained (updated after Jan 1, 2023)
   - At least 100 GitHub stars
   - Built with Maven, successfully compilable locally
3. Result: **185 Java projects** (502 source files, 118 test files, 244,876 LOC)
4. Extract 1,748 focal method + test method data pairs
5. Sample 1,000 data pairs as final benchmark for empirical study

### Project-Level Evaluation (RQ7)

For project-level evaluation, 3 representative projects were selected:

| Project | GitHub | LOC | Domain |
|---------|--------|-----|--------|
| zappos-json | https://github.com/jhy/jsoup (related) / search GitHub for "zappos-json" | 3,552 | Serialization |
| tabula-java | https://github.com/tabulapdf/tabula-java | 5,586 | File processing |
| jInstagram | https://github.com/sachin-handiekar/jInstagram | 6,303 | Instagram API wrapper |

### How to Access

The benchmark data is referenced at "our website" (ChatTESTER, 2023) in the paper.
The GitHub repo (https://github.com/jstzwj/ChatTester) contains the tool but the `data/`
directory only has a `.gitignore`, suggesting the benchmark data must be obtained separately.

---

## 5. Other Public Benchmarks for Java Unit Test Generation

### 5.1 Defects4J

- **GitHub**: https://github.com/rjust/defects4j (949 stars)
- **Description**: A database of real faults and experimental infrastructure for controlled SE experiments
- **Contains**: 835+ bugs from 17+ open-source Java projects
- **Projects include**: Commons-Lang, Commons-Math, Commons-Closure, JFreeChart, Mockito, Jackson-Core, Jackson-Databind, Jsoup, JxPath, Chart, Time, Gson, etc.
- **Usage**: Primarily for fault localization and program repair research, but the test cases make it useful for unit test generation evaluation
- **Citation**: Just et al., "Defects4J: A Database of Existing Faults to Enable Controlled Testing Studies for Java Programs", ISSTA 2014

### 5.2 SF110 Corpus

- **Source**: EvoSuite project, hosted on SourceForge
- **Description**: 110 Java projects from SourceForge, used for EvoSuite evaluation
- **GitHub helper**: https://github.com/bstee615/SF110-scripts (scripts to run EvoSuite on SF110)
- **Usage**: Standard benchmark for search-based test generation (EvoSuite)
- **Citation**: Fraser and Arcuri, "EvoSuite: Automatic Test Suite Generation for Object-Oriented Software", FSE 2011

### 5.3 CodeSearchNet

- **Source**: GitHub et al., 2019
- **Description**: 4,685 Java projects with code-search annotations
- **Used by**: ChatTester paper as starting point for their benchmark construction
- **Not specifically for testing**, but serves as a project pool

### 5.4 Methods2Test

- **Description**: A dataset mapping Java methods to their corresponding test cases
- **Used for**: Training and evaluating machine learning models for test generation
- **Related to**: ATHENATEST and similar learning-based approaches

### 5.5 EvoSuite Benchmark (standard)

The standard EvoSuite benchmark includes projects from:
- SF110 (110 projects from SourceForge)
- Defects4J projects
- Various Apache Commons libraries

### 5.6 TestBench

- **Paper**: "TestBench: Evaluating Class-Level Test Case Generation Capability of Large Language Models"
- Listed in ChatUniTest's curated paper list (2024)
- A benchmark specifically designed for evaluating LLM-based test generation

---

## 6. Summary: Dataset Overlap Across Papers

The following diagram shows which projects appear in which paper's benchmark:

```
Project                HITS   ChatUniTest  RefTest  ChatTester(RQ7)
---------------------------------------------------------------
Commons-CLI              X         X           X
Commons-CSV              X                     (possible)
Commons-Collections      X                     X
Commons-Codec            X                     (possible)
Commons-DBUtils                                X
Commons-Validator                              X
Gson                     X                     (possible)
JDom2                    X
Datafaker                X                     X
Event-ruler              X
Binance-connector                  X           X
Ecommerce-microservice             X
Jsoup                                      X
Cucumber-expressions                        X
OpenAPI-diff                                X
RTree                                       X
Ice4j                                       X
Morel                                       X
zappos-json                                           X
tabula-java                                           X
jInstagram                                            X
```

---

## 7. Practical Recommendations for Reproducing RefTest

To reconstruct the RefTest 12-project dataset:

1. **From HITS paper (arXiv:2408.11324)**: Download specific versions:
   - Commons-CLI 1.5.0, Commons-CSV 1.10.0, Gson 2.10.1, Commons-codec (3a6873e),
     Commons-collections4 4.5.0-M1, JDom2 2.0.6, Datafaker 1.9.0

2. **From ChatUniTest paper (arXiv:2305.04764)**: Additional projects:
   - Binance-connector 2.0.0, Ecommerce-microservice (commit 695a6d4)

3. **From GitHub (RefTest's additional 8)**: Clone:
   - https://github.com/jhy/jsoup
   - https://github.com/OpenAPITools/openapi-diff
   - https://github.com/cucumber/cucumber-expressions
   - https://github.com/davidmoten/rtree
   - https://github.com/jitsi/ice4j
   - https://github.com/hydromatic/morel
   - https://github.com/apache/commons-dbutils
   - https://github.com/apache/commons-validator

4. **For evaluation**: Use ChatUniTest maven plugin with different phaseTypes:
   ```bash
   mvn chatunitest:method -DselectMethod=ClassName#methodName -DphaseType=CHATTESTER
   mvn chatunitest:method -DselectMethod=ClassName#methodName -DphaseType=HITS
   ```

---

## 8. Key Observations

1. **No standardized benchmark**: Unlike NLP (GLUE, SuperGLUE) or code generation (HumanEval),
   there is no universally adopted benchmark for Java unit test generation. Each paper
   constructs its own dataset.

2. **Dataset size is small**: Most papers use 4-12 projects with tens to hundreds of methods.
   This is significantly smaller than benchmarks in other SE tasks.

3. **Training data contamination concern**: Papers split projects by whether they appear
   in the LLM training data (e.g., GPT-3.5 cutoff). This is increasingly important as
   newer models may have seen all these projects.

4. **HITS is not a standalone tool/repo**: HITS exists only as a `phaseType` in the
   ChatUniTest maven plugin. There is no separate GitHub repository for HITS.

5. **ChatTester's 185-project benchmark is the largest**: But the full dataset is not
   publicly available on the GitHub repo (only the tool code is there).

6. **Defects4J is underutilized for test generation**: Despite being the de facto standard
   for fault localization and program repair, it is rarely used as a benchmark for
   unit test generation (the projects' existing test suites serve as ground truth instead).
