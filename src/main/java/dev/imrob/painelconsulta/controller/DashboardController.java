package dev.imrob.painelconsulta.controller;

import dev.imrob.painelconsulta.dto.request.ChangePasswordRequest;
import dev.imrob.painelconsulta.dto.request.ConsultaRequest;
import dev.imrob.painelconsulta.dto.response.ConsultaResponse;
import dev.imrob.painelconsulta.dto.response.DashboardStats;
import dev.imrob.painelconsulta.dto.response.UserResponse;
import dev.imrob.painelconsulta.security.CustomUserDetails;
import dev.imrob.painelconsulta.service.ConsultaService;
import dev.imrob.painelconsulta.service.PlanoService;
import dev.imrob.painelconsulta.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/dashboard")
public class DashboardController {

    private final UserService userService;
    private final ConsultaService consultaService;
    private final PlanoService planoService;

    @GetMapping
    public String dashboard(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        Long userId = userDetails.getUserId();

        DashboardStats stats = userService.getDashboardStats(userId);
        UserResponse user = userService.buscarPorId(userId);

        model.addAttribute("stats", stats);
        model.addAttribute("user", user);
        model.addAttribute("userName", userDetails.getNome());

        return "dashboard/index";
    }

    @GetMapping("/nova-consulta")
    public String novaConsultaPage(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        model.addAttribute("userName", userDetails.getNome());
        return "dashboard/nova-consulta";
    }

    @PostMapping("/consulta")
    public String realizarConsulta(@AuthenticationPrincipal CustomUserDetails userDetails,
                                    @ModelAttribute ConsultaRequest request,
                                    Model model,
                                    RedirectAttributes redirectAttributes) {
        try {
            ConsultaResponse response = consultaService.realizarConsulta(userDetails.getUserId(), request);
            model.addAttribute("resultado", response);
            model.addAttribute("userName", userDetails.getNome());
            return "dashboard/resultado-consulta";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/dashboard/nova-consulta";
        }
    }

    @GetMapping("/historico")
    public String historico(@AuthenticationPrincipal CustomUserDetails userDetails,
                           @RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "10") int size,
                           @RequestParam(required = false) String tipo,
                           Model model) {
        Long userId = userDetails.getUserId();
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("dataConsulta").descending());

        Page<?> consultas;
        if (tipo != null && !tipo.isEmpty()) {
            consultas = consultaService.getHistoricoByTipo(userId, tipo, pageRequest);
        } else {
            consultas = consultaService.getHistorico(userId, pageRequest);
        }

        model.addAttribute("consultas", consultas);
        model.addAttribute("tipoFiltro", tipo);
        model.addAttribute("userName", userDetails.getNome());

        return "dashboard/historico";
    }

    @GetMapping("/consulta/{id}")
    public String detalheConsulta(@AuthenticationPrincipal CustomUserDetails userDetails,
                                  @PathVariable Long id,
                                  Model model) {
        ConsultaResponse consulta = consultaService.getConsultaById(userDetails.getUserId(), id);
        model.addAttribute("resultado", consulta);
        model.addAttribute("userName", userDetails.getNome());
        return "dashboard/resultado-consulta";
    }

    @GetMapping("/minha-conta")
    public String minhaConta(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        UserResponse user = userService.buscarPorId(userDetails.getUserId());
        DashboardStats stats = userService.getDashboardStats(userDetails.getUserId());

        model.addAttribute("user", user);
        model.addAttribute("stats", stats);
        model.addAttribute("planos", planoService.listarPlanosAtivos());
        model.addAttribute("userName", userDetails.getNome());

        return "dashboard/minha-conta";
    }

    @PostMapping("/alterar-senha")
    public String alterarSenha(@AuthenticationPrincipal CustomUserDetails userDetails,
                               @ModelAttribute ChangePasswordRequest request,
                               RedirectAttributes redirectAttributes) {
        try {
            userService.alterarSenha(userDetails.getUserId(), request);
            redirectAttributes.addFlashAttribute("success", "Senha alterada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/dashboard/minha-conta";
    }
}
