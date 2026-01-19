package dev.imrob.painelconsulta.repository;

import dev.imrob.painelconsulta.domain.Plano;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles({"test", "mock"})
class PlanoRepositoryTest {

    @Autowired
    private PlanoRepository planoRepository;

    @Test
    void deveBuscarPlanoPorId() {
        Optional<Plano> plano = planoRepository.findById(1L);

        assertTrue(plano.isPresent());
        assertEquals("Básico", plano.get().getNome());
    }

    @Test
    void deveListarTodosPlanos() {
        Iterable<Plano> planos = planoRepository.findAll();

        assertNotNull(planos);
        assertTrue(planos.iterator().hasNext());
    }
}
