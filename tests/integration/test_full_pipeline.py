"""Integration tests for the full AST-RAG pipeline."""

from unittest.mock import Mock, patch

import pytest

from core import DependencyResolver, JavaFileRetriever, PromptBuilder


class TestFullPipeline:
    """Test the complete pipeline from Java file to generated test."""

    @pytest.fixture
    def mock_java_project(self, tmp_path):
        """Create a realistic mock Java project."""
        src = tmp_path / "src" / "main" / "java" / "com" / "example" / "demo"
        src.mkdir(parents=True)

        # Repository interface
        (src / "UsuarioRepository.java").write_text("""
            package com.example.demo;
            import java.util.Optional;
            public interface UsuarioRepository {
                Usuario save(Usuario usuario);
                Optional<Usuario> findById(Long id);
            }
        """)

        # Entity
        (src / "Usuario.java").write_text("""
            package com.example.demo;
            public class Usuario {
                private Long id;
                private String nombre;
            }
        """)

        # Service to test
        (src / "UsuarioService.java").write_text("""
            package com.example.demo;

            import java.util.Optional;
            import org.springframework.stereotype.Service;

            @Service
            public class UsuarioService {
                private final UsuarioRepository usuarioRepository;

                public UsuarioService(UsuarioRepository usuarioRepository) {
                    this.usuarioRepository = usuarioRepository;
                }

                public Usuario crearUsuario(Usuario usuario) {
                    return usuarioRepository.save(usuario);
                }

                public Optional<Usuario> buscarPorId(Long id) {
                    return usuarioRepository.findById(id);
                }
            }
        """)

        return str(tmp_path)

    @pytest.fixture
    def service_file(self, mock_java_project):
        """Path to the service file to test."""
        return f"{mock_java_project}/src/main/java/com/example/demo/UsuarioService.java"

    def test_end_to_end_pipeline_without_llm(self, service_file, mock_java_project):
        """Test pipeline up to prompt generation (no LLM call)."""
        # Step 1: Parse
        retriever = JavaFileRetriever(mock_java_project)
        parsed = retriever.parse_file(service_file)

        assert parsed.name == "UsuarioService"
        assert parsed.package == "com.example.demo"
        assert len(parsed.methods) >= 2

        # Step 2: Resolve dependencies
        resolver = DependencyResolver(retriever)
        deps = resolver.resolve_dependencies("UsuarioService", max_depth=2)

        assert len(deps) >= 1

        # Step 3: Build prompt
        builder = PromptBuilder(retriever, resolver)
        code, context = builder.build_prompt(service_file, max_dependencies=5)

        assert "UsuarioService" in code
        assert "UsuarioRepository" in context
        assert "save" in context
        assert "findById" in context

    def test_end_to_end_with_mocked_llm(self, service_file, mock_java_project):
        """Test full pipeline with mocked LLM response."""
        mock_client = Mock()
        mock_response = Mock()
        mock_response.content = [
            Mock(
                text="""
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {
    @Mock UsuarioRepository usuarioRepository;
    @InjectMocks UsuarioService usuarioService;

    @Test
    void crearUsuario_debeGuardar() {
        Usuario u = new Usuario();
        usuarioService.crearUsuario(u);
        verify(usuarioRepository).save(u);
    }
}
"""
            )
        ]
        mock_client.messages.create.return_value = mock_response

        with patch("llm.client.LLMClient._init_anthropic"):
            with patch("llm.client_new.LLMClient") as MockLLMClient:
                mock_instance = Mock()
                mock_instance.generate_test.return_value = """
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {
    @Mock UsuarioRepository usuarioRepository;
    @InjectMocks UsuarioService usuarioService;

    @Test
    void crearUsuario_debeGuardar() {
        Usuario u = new Usuario();
        usuarioService.crearUsuario(u);
        verify(usuarioRepository).save(u);
    }
}
"""
                MockLLMClient.return_value = mock_instance

                import tempfile

                from orchestration.generator import generate_test_for_file

                with tempfile.TemporaryDirectory() as output_dir:
                    test_code = generate_test_for_file(
                        java_file_path=service_file,
                        java_project_path=mock_java_project,
                        output_dir=output_dir,
                        max_dependencies=5,
                        llm_provider="anthropic",
                        llm_model="test-model",
                    )

                assert "UsuarioServiceTest" in test_code
                assert "import org.junit.jupiter" in test_code
                assert "verify(usuarioRepository)" in test_code

    def test_dependency_resolution_depth_2_includes_repository(
        self, service_file, mock_java_project
    ):
        """Test that depth=2 includes repository interface."""
        retriever = JavaFileRetriever(mock_java_project)
        resolver = DependencyResolver(retriever)

        deps = resolver.resolve_dependencies("UsuarioService", max_depth=2)
        dep_names = [dep.name for dep in deps]

        # UsuarioRepository should be in dependencies
        assert "UsuarioRepository" in dep_names or any("Repository" in name for name in dep_names)

    def test_prompt_builder_limits_dependencies(self, service_file, mock_java_project):
        """Test that max_dependencies parameter works correctly."""
        retriever = JavaFileRetriever(mock_java_project)
        resolver = DependencyResolver(retriever)
        builder = PromptBuilder(retriever, resolver)

        # Request only 1 dependency
        _, context = builder.build_prompt(service_file, max_dependencies=1)

        # Should truncate - count class names in comments
        import re

        class_matches = re.findall(r"// (\w+)", context)
        # At most 1 actual dependency + possible truncation message
        assert len(class_matches) <= 2

    def test_pipeline_with_complex_java_project(self, tmp_path):
        """Test pipeline with a more complex project structure."""
        src = tmp_path / "src" / "main" / "java" / "com" / "example"
        src.mkdir(parents=True)

        # Create multiple interconnected files
        (src / "Entity.java").write_text("package com.example; public class Entity {}")
        (src / "Repository.java").write_text(
            "package com.example; public interface Repository { Entity findById(Long id); }"
        )
        (src / "Service.java").write_text(
            "package com.example; public class Service { private Repository repo; public Entity get(Long id) { return repo.findById(id); } }"
        )

        retriever = JavaFileRetriever(str(tmp_path))
        resolver = DependencyResolver(retriever)
        builder = PromptBuilder(retriever, resolver)

        service_file = str(src / "Service.java")
        code, context = builder.build_prompt(service_file, max_dependencies=3)

        assert "Service" in code
        assert "Repository" in context
        assert "findById" in context
