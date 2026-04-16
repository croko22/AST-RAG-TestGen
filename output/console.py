"""
Output and presentation layer for AST-RAG TestGen.

This module provides a clean interface for terminal output,
abstracting away Rich/TTY details from the rest of the application.
"""

from __future__ import annotations

import sys
from typing import TYPE_CHECKING

if TYPE_CHECKING:
    from rich.console import Console

# Rich for beautiful terminal output
try:
    from rich.console import Console
    from rich.progress import BarColumn, Progress, SpinnerColumn, TextColumn, TimeRemainingColumn
    from rich.syntax import Syntax
    from rich.text import Text

    RICH_AVAILABLE = True
except ImportError:
    RICH_AVAILABLE = False
    Console = None  # type: ignore
    Progress = None  # type: ignore
    SpinnerColumn = None  # type: ignore
    TextColumn = None  # type: ignore
    BarColumn = None  # type: ignore
    TimeRemainingColumn = None  # type: ignore
    Syntax = None  # type: ignore
    Text = None  # type: ignore


class OutputManager:
    """Manages terminal output with Rich fallback to plain text."""

    def __init__(self, use_rich: bool = True):
        """Initialize output manager.

        Args:
            use_rich: Whether to use Rich if available. Falls back to plain text if False.
        """
        self.use_rich = use_rich and RICH_AVAILABLE
        self._console: Console | None = None
        self._stderr_console: Console | None = None

    @property
    def console(self) -> Console | None:
        """Get Rich console instance (lazy initialization)."""
        if self._console is None and self.use_rich and Console is not None:
            self._console = Console()
        return self._console

    @property
    def stderr_console(self) -> Console | None:
        """Get Rich console instance for stderr (lazy initialization)."""
        if self._stderr_console is None and self.use_rich and Console is not None:
            self._stderr_console = Console(stderr=True)
        return self._stderr_console

    def print_header(self, title: str) -> None:
        """Print a formatted header.

        Args:
            title: Header title text.
        """
        if self.console:
            self.console.print(f"\n{'=' * 60}")
            self.console.print(f"[bold cyan]{title}[/bold cyan]")
            self.console.print(f"{'=' * 60}\n")
        else:
            print(f"\n{'=' * 60}")
            print(title)
            print(f"{'=' * 60}\n")

    def print_success(self, message: str) -> None:
        """Print a success message.

        Args:
            message: Success message text.
        """
        if self.console:
            self.console.print(Text(f"✓ {message}", style="bold green"))
        else:
            print(f"✓ {message}")

    def print_error(self, message: str) -> None:
        """Print an error message to stderr.

        Args:
            message: Error message text.
        """
        if self.stderr_console:
            self.stderr_console.print(Text(f"✗ {message}", style="bold red"))
        elif self.console:
            self.console.print(Text(f"✗ {message}", style="bold red"))
        else:
            print(f"✗ {message}", file=sys.stderr)

    def print_info(self, message: str) -> None:
        """Print an info message.

        Args:
            message: Info message text.
        """
        if self.console:
            self.console.print(Text(f"ℹ {message}", style="cyan"))
        else:
            print(f"ℹ {message}")

    def print_code(self, code: str, language: str = "java") -> None:
        """Print code with syntax highlighting.

        Args:
            code: Code to print.
            language: Programming language for syntax highlighting.
        """
        if self.console and Syntax is not None:
            syntax = Syntax(code, language, theme="monokai", line_numbers=True)
            self.console.print(syntax)
        else:
            print(code)

    def print_table(self, headers: list[str], rows: list[list[str]]) -> None:
        """Print a formatted table.

        Args:
            headers: Table headers.
            rows: Table rows.
        """
        if self.console:
            from rich.table import Table

            table = Table(show_header=True, header_style="bold magenta")
            for header in headers:
                table.add_column(header)

            for row in rows:
                table.add_row(*row)

            self.console.print(table)
        else:
            # Fallback to simple text table
            print(" | ".join(headers))
            print("-" * len(" | ".join(headers)))
            for row in rows:
                print(" | ".join(row))

    def create_progress(
        self,
        total: int,
        description: str = "Processing",
    ) -> ProgressContextManager:
        """Create a progress bar context manager.

        Args:
            total: Total number of items.
            description: Progress bar description.

        Returns:
            Progress context manager.
        """
        return ProgressContextManager(
            total=total,
            description=description,
            use_rich=self.use_rich,
            console=self.console,
        )


class ProgressContextManager:
    """Context manager for progress bars."""

    def __init__(
        self,
        total: int,
        description: str,
        use_rich: bool,
        console: Console | None,
    ):
        """Initialize progress context manager.

        Args:
            total: Total number of items.
            description: Progress bar description.
            use_rich: Whether to use Rich progress bar.
            console: Rich console instance.
        """
        self.total = total
        self.description = description
        self.use_rich = use_rich
        self.console = console
        self._progress: Progress | None = None
        self._task_id: str | None = None
        self._current = 0

    def __enter__(self) -> ProgressContextManager:
        """Enter progress context."""
        if self.use_rich and self.console and Progress is not None:
            self._progress = Progress(
                SpinnerColumn(),
                TextColumn("[progress.description]{task.description}"),
                BarColumn(),
                TextColumn("[progress.percentage]{task.percentage:>3.0f}%"),
                TimeRemainingColumn(),
            )
            self._task_id = self._progress.add_task(self.description, total=self.total)
            self._progress.__enter__()
        return self

    def __exit__(self, exc_type, exc_val, exc_tb) -> None:
        """Exit progress context."""
        if self._progress:
            self._progress.__exit__(exc_type, exc_val, exc_tb)

    def update(self, advance: int = 1) -> None:
        """Update progress.

        Args:
            advance: Number of items to advance.
        """
        if self._progress and self._task_id:
            self._progress.update(self._task_id, advance=advance)
        else:
            self._current += advance
            if self.total > 0:
                percent = (self._current / self.total) * 100
                print(f"\r{self.description}: {percent:.0f}%", end="", flush=True)


# Global output manager instance
_global_output: OutputManager | None = None


def get_output() -> OutputManager:
    """Get the global output manager instance.

    Returns:
        Global OutputManager instance.
    """
    global _global_output
    if _global_output is None:
        _global_output = OutputManager()
    return _global_output


def set_output(output: OutputManager) -> None:
    """Set the global output manager instance.

    Args:
        output: OutputManager instance to set as global.
    """
    global _global_output
    _global_output = output
