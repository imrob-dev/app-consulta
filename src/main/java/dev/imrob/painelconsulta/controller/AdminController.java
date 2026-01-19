package dev.imrob.painelconsulta.controller;

import dev.imrob.painelconsulta.dto.request.UserRegistrationRequest;
import dev.imrob.painelconsulta.dto.request.UserUpdateRequest;
import dev.imrob.painelconsulta.dto.response.UserResponse;
import dev.imrob.painelconsulta.security.CustomUserDetails;
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

import java.math.BigDecimal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final PlanoService planoService;

    @GetMapping
    public String adminDashboard(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        model.addAttribute("userName", userDetails.getNome());
        return "admin/index";
    }

    // ================== USUÁRIOS ==================

    @GetMapping("/usuarios")
    public String listarUsuarios(@AuthenticationPrincipal CustomUserDetails userDetails,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size,
                                 @RequestParam(required = false) String search,
                                 Model model) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").descending());

        Page<UserResponse> usuarios;
        if (search != null && !search.isEmpty()) {
            usuarios = userService.pesquisarUsuarios(search, pageRequest);
        } else {
            usuarios = userService.listarUsuarios(pageRequest);
        }

        model.addAttribute("usuarios", usuarios);
        model.addAttribute("search", search);
        model.addAttribute("planos", planoService.listarTodosPlanos());
        model.addAttribute("userName", userDetails.getNome());

        return "admin/usuarios";
    }

    @GetMapping("/usuarios/novo")
    public String novoUsuarioForm(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        model.addAttribute("planos", planoService.listarPlanosAtivos());
        model.addAttribute("userName", userDetails.getNome());
        return "admin/usuario-form";
    }

    @PostMapping("/usuarios/novo")
    public String criarUsuario(@ModelAttribute UserRegistrationRequest request,
                               RedirectAttributes redirectAttributes) {
        try {
            userService.criarUsuarioAdmin(request);
            redirectAttributes.addFlashAttribute("success", "Usuário criado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/usuarios";
    }

    @GetMapping("/usuarios/{id}")
    public String editarUsuarioForm(@AuthenticationPrincipal CustomUserDetails userDetails,
                                    @PathVariable Long id, Model model) {
        UserResponse user = userService.buscarPorId(id);
        model.addAttribute("usuario", user);
        model.addAttribute("planos", planoService.listarTodosPlanos());
        model.addAttribute("userName", userDetails.getNome());
        return "admin/usuario-form";
    }

    @PostMapping("/usuarios/{id}")
    public String atualizarUsuario(@PathVariable Long id,
                                   @ModelAttribute UserUpdateRequest request,
                                   RedirectAttributes redirectAttributes) {
        try {
            userService.atualizarUsuario(new UserUpdateRequest(
                    id, request.nome(), request.email(), request.telefone(),
                    request.role(), request.active(), request.planoId()
            ));
            redirectAttributes.addFlashAttribute("success", "Usuário atualizado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/usuarios";
    }

    @PostMapping("/usuarios/{id}/ativar")
    public String ativarUsuario(@PathVariable Long id,
                                @RequestParam Long planoId,
                                RedirectAttributes redirectAttributes) {
        try {
            userService.ativarUsuario(id, planoId);
            redirectAttributes.addFlashAttribute("success", "Usuário ativado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/usuarios";
    }

    @PostMapping("/usuarios/{id}/desativar")
    public String desativarUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.desativarUsuario(id);
            redirectAttributes.addFlashAttribute("success", "Usuário desativado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/usuarios";
    }

    // ================== PLANOS ==================

    @GetMapping("/planos")
    public String listarPlanos(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        model.addAttribute("planos", planoService.listarTodosPlanos());
        model.addAttribute("userName", userDetails.getNome());
        return "admin/planos";
    }

    @GetMapping("/planos/novo")
    public String novoPlanoForm(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        model.addAttribute("userName", userDetails.getNome());
        return "admin/plano-form";
    }

    @PostMapping("/planos/novo")
    public String criarPlano(@RequestParam String nome,
                             @RequestParam String descricao,
                             @RequestParam BigDecimal preco,
                             @RequestParam Integer limiteConsultas,
                             @RequestParam Integer validadeDias,
                             RedirectAttributes redirectAttributes) {
        try {
            planoService.criarPlano(nome, descricao, preco, limiteConsultas, validadeDias);
            redirectAttributes.addFlashAttribute("success", "Plano criado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/planos";
    }

    @GetMapping("/planos/{id}")
    public String editarPlanoForm(@AuthenticationPrincipal CustomUserDetails userDetails,
                                  @PathVariable Long id, Model model) {
        model.addAttribute("plano", planoService.buscarPorId(id));
        model.addAttribute("userName", userDetails.getNome());
        return "admin/plano-form";
    }

    @PostMapping("/planos/{id}")
    public String atualizarPlano(@PathVariable Long id,
                                 @RequestParam String nome,
                                 @RequestParam String descricao,
                                 @RequestParam BigDecimal preco,
                                 @RequestParam Integer limiteConsultas,
                                 @RequestParam Integer validadeDias,
                                 @RequestParam(defaultValue = "true") boolean active,
                                 RedirectAttributes redirectAttributes) {
        try {
            planoService.atualizarPlano(id, nome, descricao, preco, limiteConsultas, validadeDias, active);
            redirectAttributes.addFlashAttribute("success", "Plano atualizado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/planos";
    }

    @PostMapping("/planos/{id}/desativar")
    public String desativarPlano(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            planoService.desativarPlano(id);
            redirectAttributes.addFlashAttribute("success", "Plano desativado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/planos";
    }
}
