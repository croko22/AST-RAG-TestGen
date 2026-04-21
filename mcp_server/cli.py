"""CLI entry point for MCP server."""

from __future__ import annotations

import signal
import sys


def run_serve(
    host: str = "127.0.0.1",
    port: int = 8000,
    transport: str = "stdio",
) -> int:
    try:
        from mcp_server.server import MCPServer
    except ImportError as e:
        print(f"Error loading MCP server: {e}", file=sys.stderr)
        return 1

    from config import get_config

    config = get_config()

    effective_host = host or config.mcp_server.host
    effective_port = port or config.mcp_server.port
    effective_transport = transport or config.mcp_server.transport

    server = MCPServer(config=config)

    _install_signal_handlers()

    try:
        if effective_transport == "stdio":
            server.run_stdio()
        elif effective_transport in ("http", "sse"):
            server.run_http(host=effective_host, port=effective_port)
        else:
            print(
                f"Unknown transport: {effective_transport}. Use 'stdio' or 'http'.", file=sys.stderr
            )
            return 1
    except KeyboardInterrupt:
        pass

    return 0


def _install_signal_handlers():
    def _handler(signum, frame):
        sys.exit(0)

    signal.signal(signal.SIGINT, _handler)
    signal.signal(signal.SIGTERM, _handler)
