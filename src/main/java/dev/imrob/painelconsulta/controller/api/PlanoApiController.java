package dev.imrob.painelconsulta.controller.api;

import dev.imrob.painelconsulta.dto.response.PlanoResponse;
import dev.imrob.painelconsulta.service.PlanoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/planos")
public class PlanoApiController {

    private final PlanoService planoService;

    @GetMapping("/public")
    public ResponseEntity<List<PlanoResponse>> listarPlanosPublicos() {
        return ResponseEntity.ok(planoService.listarPlanosAtivos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlanoResponse> buscarPlano(@PathVariable Long id) {
        return ResponseEntity.ok(planoService.buscarPorId(id));
    }
}
