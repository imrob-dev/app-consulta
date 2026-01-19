package dev.imrob.painelconsulta.controller.api;

import dev.imrob.painelconsulta.dto.request.ConsultaRequest;
import dev.imrob.painelconsulta.dto.response.ConsultaResponse;
import dev.imrob.painelconsulta.security.CustomUserDetails;
import dev.imrob.painelconsulta.service.ConsultaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/consultas")
public class ConsultaApiController {

    private final ConsultaService consultaService;

    @PostMapping
    public ResponseEntity<ConsultaResponse> realizarConsulta(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody ConsultaRequest request) {
        ConsultaResponse response = consultaService.realizarConsulta(userDetails.getUserId(), request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/historico")
    public ResponseEntity<Page<?>> listarHistorico(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("dataConsulta").descending());
        return ResponseEntity.ok(consultaService.getHistorico(userDetails.getUserId(), pageRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsultaResponse> buscarConsulta(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id) {
        ConsultaResponse response = consultaService.getConsultaById(userDetails.getUserId(), id);
        return ResponseEntity.ok(response);
    }
}
