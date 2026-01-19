package dev.imrob.painelconsulta.controller;

import dev.imrob.painelconsulta.service.PlanoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class LandingController {

    private final PlanoService planoService;

    @GetMapping("/")
    public String landing(Model model) {
        model.addAttribute("planos", planoService.listarPlanosAtivos());
        return "landing";
    }

    @GetMapping("/landing")
    public String landingAlternative(Model model) {
        return landing(model);
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
