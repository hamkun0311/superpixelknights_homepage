package com.oestwdio.superpixelknights.controller;

import com.oestwdio.superpixelknights.service.GameDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class InfoController {
    private final GameDataService gameDataService;

    @GetMapping("/knights")
    public String knights(Model model) {
        model.addAttribute("knights", gameDataService.getKnights());
        return "knights";
    }

    @GetMapping("/scroll")
    public String scrolls(Model model) {
        model.addAttribute("scrolls", gameDataService.getScrolls());
        return "scroll";
    }
}