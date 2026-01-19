package dev.imrob.painelconsulta.controller;

import dev.imrob.painelconsulta.dto.request.CheckoutRequest;
import dev.imrob.painelconsulta.dto.response.CheckoutResponse;
import dev.imrob.painelconsulta.service.CheckoutService;
import dev.imrob.painelconsulta.service.PlanoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/checkout")
public class CheckoutController {

    private final CheckoutService checkoutService;
    private final PlanoService planoService;

    @GetMapping
    public String checkoutPage(@RequestParam(required = false) Long planoId, Model model) {
        model.addAttribute("planos", planoService.listarPlanosAtivos());
        if (planoId != null) {
            model.addAttribute("planoSelecionado", planoService.buscarPorId(planoId));
        }
        return "checkout";
    }

    @PostMapping("/iniciar")
    public String iniciarCheckout(@Valid CheckoutRequest request, Model model) {
        CheckoutResponse response = checkoutService.iniciarCheckout(request);
        model.addAttribute("checkout", response);
        model.addAttribute("plano", planoService.buscarPorId(request.planoId()));
        return "checkout-pagamento";
    }

    @GetMapping("/pagamento/{pagamentoId}")
    public String paginaPagamento(@PathVariable Long pagamentoId, Model model) {
        CheckoutResponse checkout = checkoutService.verificarPagamento(pagamentoId);
        model.addAttribute("checkout", checkout);
        return "checkout-pagamento";
    }

    @PostMapping("/confirmar/{pagamentoId}")
    public String confirmarPagamento(@PathVariable Long pagamentoId,
                                      @RequestParam String senha,
                                      Model model) {
        try {
            checkoutService.confirmarPagamento(pagamentoId, senha);
            return "redirect:/checkout/sucesso";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "checkout-pagamento";
        }
    }

    @GetMapping("/sucesso")
    public String sucessoPage() {
        return "checkout-sucesso";
    }
}
