package dev.imrob.painelconsulta.controller.api;

import dev.imrob.painelconsulta.dto.request.UserRegistrationRequest;
import dev.imrob.painelconsulta.dto.response.UserResponse;
import dev.imrob.painelconsulta.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserApiController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registrarUsuarioExterno(@Valid @RequestBody UserRegistrationRequest request) {
        UserResponse response = userService.registrarUsuario(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> buscarUsuario(@PathVariable Long id) {
        UserResponse response = userService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }
}
