package dev.imrob.painelconsulta.repository;

import dev.imrob.painelconsulta.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles({"test", "mock"})
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void deveBuscarUsuarioPorId() {
        Optional<User> user = userRepository.findById(1L);

        assertTrue(user.isPresent());
        assertEquals("admin@saas.com", user.get().getEmail());
    }

    @Test
    void deveBuscarUsuarioPorEmail() {
        Optional<User> user = userRepository.findByEmail("user@saas.com");

        assertTrue(user.isPresent());
        assertEquals("Usuário Teste", user.get().getNome());
    }

    @Test
    void deveVerificarExistenciaPorEmail() {
        boolean exists = userRepository.existsByEmail("admin@saas.com");

        assertTrue(exists);
    }
}
