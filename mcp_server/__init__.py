"""MCP server for AST-RAG TestGen."""

from __future__ import annotations


def create_server():
    from mcp_server.server import MCPServer

    return MCPServer()
