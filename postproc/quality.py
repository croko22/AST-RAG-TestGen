"""Quality assessment heuristics for generated Java unit tests."""

from __future__ import annotations

import re
from dataclasses import dataclass, field

_ASSERTION_PATTERNS = [
    r"assertTrue\s*\(",
    r"assertFalse\s*\(",
    r"assertEquals\s*\(",
    r"assertNotEquals\s*\(",
    r"assertNull\s*\(",
    r"assertNotNull\s*\(",
    r"assertSame\s*\(",
    r"assertNotSame\s*\(",
    r"assertThrows\s*\(",
    r"assertArrayEquals\s*\(",
    r"assertIterableEquals\s*\(",
    r"assertTimeout\s*\(",
    r"assertTimeoutPreemptively\s*\(",
    r"fail\s*\(",
    r"verify\s*\(",
    r"verifyNoMoreInteractions\s*\(",
    r"verifyNoInteractions\s*\(",
    r"thenReturn\s*\(",
    r"thenThrow\s*\(",
]

_ASSERTION_RE = re.compile("|".join(_ASSERTION_PATTERNS))

_TRIVIAL_METHOD_PATTERNS = [
    r"void\s+test\w*Getter",
    r"void\s+test\w*Setter",
    r"void\s+test\w*Get\w+\(\)",
    r"void\s+test\w*Set\w+\(\)",
]

_TRIVIAL_METHOD_RE = re.compile("|".join(_TRIVIAL_METHOD_PATTERNS))

_TEST_METHOD_RE = re.compile(r"@Test\b", re.MULTILINE)

_HARDCODED_PATTERN = re.compile(
    r'"(?:test|foo|bar|baz|dummy|sample|abc|123|hello|world|xxx|yyy|zzz|asdf|qwerty)"',
    re.IGNORECASE,
)

_DUPLICATE_THRESHOLD = 3


@dataclass(slots=True)
class QualityReport:
    score: float
    trivial_flag: bool
    assertion_count: int
    test_count: int
    issues: list[str] = field(default_factory=list)
    assertion_density: float = 0.0
    meaningfulness: float = 0.0
    diversity: float = 0.0


def assess_test_quality(
    test_code: str,
    quality_threshold: float = 0.5,
) -> QualityReport:
    test_count = _count_tests(test_code)
    assertion_count = _count_assertions(test_code)
    issues: list[str] = []

    if test_count == 0:
        return QualityReport(
            score=0.0,
            trivial_flag=True,
            assertion_count=0,
            test_count=0,
            issues=["No @Test methods found"],
        )

    assertion_density = _compute_assertion_density(assertion_count, test_count)
    meaningfulness = _compute_meaningfulness(test_code, test_count, issues)
    diversity = _compute_diversity(test_code, issues)

    raw = assertion_density * 0.4 + meaningfulness * 0.3 + diversity * 0.3
    score = min(raw, 100.0)

    trivial_flag = score < quality_threshold * 100

    if trivial_flag:
        issues.append(f"Quality score {score:.1f} below threshold {quality_threshold * 100:.1f}")

    return QualityReport(
        score=score,
        trivial_flag=trivial_flag,
        assertion_count=assertion_count,
        test_count=test_count,
        issues=issues,
        assertion_density=assertion_density,
        meaningfulness=meaningfulness,
        diversity=diversity,
    )


def _count_tests(code: str) -> int:
    return len(_TEST_METHOD_RE.findall(code))


def _count_assertions(code: str) -> int:
    return len(_ASSERTION_RE.findall(code))


def _compute_assertion_density(assertions: int, tests: int) -> float:
    if tests == 0:
        return 0.0
    ratio = assertions / tests
    return min(ratio / 3.0, 1.0) * 100


def _compute_meaningfulness(code: str, test_count: int, issues: list[str]) -> float:
    score = 100.0

    methods = _split_test_methods(code)

    trivial_count = 0
    empty_count = 0
    no_assert_count = 0

    for method_body in methods:
        if _is_trivial_method(method_body):
            trivial_count += 1
        if _is_empty_method(method_body):
            empty_count += 1
        if not _has_assertions(method_body):
            no_assert_count += 1

    if empty_count > 0:
        pct = (empty_count / test_count) * 100
        issues.append(f"{empty_count} empty test method(s) ({pct:.0f}%)")
        score -= pct

    if trivial_count > 0:
        pct = (trivial_count / test_count) * 50
        issues.append(f"{trivial_count} trivial test method(s)")
        score -= pct

    if no_assert_count > 0:
        pct = (no_assert_count / test_count) * 60
        issues.append(f"{no_assert_count} test(s) without assertions")
        score -= pct

    return max(score, 0.0)


def _compute_diversity(code: str, issues: list[str]) -> float:
    methods = _split_test_methods(code)
    if len(methods) <= 1:
        return 100.0

    stripped_bodies = [m.strip() for m in methods if m.strip()]
    if not stripped_bodies:
        return 100.0

    duplicates = 0
    seen: set[int] = set()
    for i, body in enumerate(stripped_bodies):
        if i in seen:
            continue
        for j in range(i + 1, len(stripped_bodies)):
            if j in seen:
                continue
            if _are_similar(body, stripped_bodies[j]):
                duplicates += 1
                seen.add(j)

    if duplicates > 0:
        dup_ratio = duplicates / len(stripped_bodies)
        issues.append(f"{duplicates} duplicate/near-duplicate test(s) detected")
        return max(100.0 * (1.0 - dup_ratio), 0.0)

    return 100.0


def _split_test_methods(code: str) -> list[str]:
    test_starts = [m.start() for m in _TEST_METHOD_RE.finditer(code)]
    if not test_starts:
        return []

    methods: list[str] = []
    for i, start in enumerate(test_starts):
        end = test_starts[i + 1] if i + 1 < len(test_starts) else len(code)
        methods.append(code[start:end])

    return methods


def _is_trivial_method(body: str) -> bool:
    return bool(_TRIVIAL_METHOD_RE.search(body))


def _is_empty_method(body: str) -> bool:
    brace_depth = 0
    started = False
    content_chars = 0
    for ch in body:
        if ch == "{":
            brace_depth += 1
            started = True
        elif ch == "}":
            brace_depth -= 1
            if started and brace_depth == 0:
                break
        elif started and brace_depth >= 1 and ch not in " \t\n\r":
            content_chars += 1

    return content_chars == 0


def _has_assertions(body: str) -> bool:
    return bool(_ASSERTION_RE.search(body))


def _are_similar(a: str, b: str) -> bool:
    a_stripped = _normalize_for_comparison(a)
    b_stripped = _normalize_for_comparison(b)
    if a_stripped == b_stripped:
        return True
    return _edit_distance_ratio(a_stripped, b_stripped) < 0.15


def _normalize_for_comparison(s: str) -> str:
    s = re.sub(r"@Test\s*(?:\([^)]*\))?\s*", "", s)
    s = re.sub(r"public\s+void\s+\w+", "METHOD", s)
    s = re.sub(r"\s+", " ", s).strip()
    return s


def _edit_distance_ratio(a: str, b: str) -> float:
    if not a and not b:
        return 0.0
    if not a or not b:
        return 1.0

    len_a, len_b = len(a), len(b)
    if abs(len_a - len_b) > max(len_a, len_b) * 0.5:
        return 1.0

    prev = list(range(len_b + 1))
    for i in range(1, len_a + 1):
        curr = [i] + [0] * len_b
        for j in range(1, len_b + 1):
            cost = 0 if a[i - 1] == b[j - 1] else 1
            curr[j] = min(curr[j - 1] + 1, prev[j] + 1, prev[j - 1] + cost)
        prev = curr

    return prev[len_b] / max(len_a, len_b)
