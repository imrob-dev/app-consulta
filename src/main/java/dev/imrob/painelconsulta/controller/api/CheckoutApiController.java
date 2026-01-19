package dev.imrob.painelconsulta.controller.api;

import dev.imrob.painelconsulta.dto.request.CheckoutRequest;
import dev.imrob.painelconsulta.dto.response.CheckoutResponse;
import dev.imrob.painelconsulta.service.CheckoutService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/checkout")
public class CheckoutApiController {

    private final CheckoutService checkoutService;

    @PostMapping("/iniciar")
    public ResponseEntity<CheckoutResponse> iniciarCheckout(@Valid @RequestBody CheckoutRequest request) {
        CheckoutResponse response = checkoutService.iniciarCheckout(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{pagamentoId}")
    public ResponseEntity<CheckoutResponse> verificarStatus(@PathVariable Long pagamentoId) {
        CheckoutResponse response = checkoutService.verificarPagamento(pagamentoId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/confirmar/{pagamentoId}")
    public ResponseEntity<CheckoutResponse> confirmarPagamento(@PathVariable Long pagamentoId,
                                                                @RequestParam String senha) {
        CheckoutResponse response = checkoutService.confirmarPagamento(pagamentoId, senha);
        return ResponseEntity.ok(response);
    }

    // Endpoint para simular confirmação (apenas para testes)
    @PostMapping("/simular/{pagamentoId}")
    public ResponseEntity<Void> simularPagamento(@PathVariable Long pagamentoId) {
        checkoutService.simularConfirmacaoPagamento(pagamentoId);
        return ResponseEntity.ok().build();
    }
}
