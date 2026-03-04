package com.oestwdio.superpixelknights.controller;

import com.oestwdio.superpixelknights.domain.Inquiry;
import com.oestwdio.superpixelknights.service.InquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession; // 이 줄을 추가하세요!

@Controller
@RequiredArgsConstructor
public class InquiryController {

    private final InquiryService inquiryService;

    // 작성 페이지 (닉네임 자동 주입)
    @GetMapping("/reportWrite")
    public String reportWriteForm(@RequestParam(value = "nickname", required = false) String nickname,
                                  HttpSession session, Model model) {

        // URL에 닉네임이 있으면 세션에 저장, 없으면 세션에서 꺼내옴
        if (nickname != null) {
            session.setAttribute("userNickname", nickname);
        } else {
            nickname = (String) session.getAttribute("userNickname");
        }

        Inquiry inquiry = new Inquiry();
        inquiry.setNickname(nickname);
        model.addAttribute("inquiry", inquiry);
        return "reportWrite";
    }

    // 저장 처리
    @PostMapping("/reportSave")
    public String reportSave(@ModelAttribute Inquiry inquiry) {
        inquiryService.saveInquiry(inquiry);
        return "redirect:/reportList"; // 저장 후 목록 페이지로 이동
    }

    // 목록 페이지
    @GetMapping("/reportList")
    public String reportList(HttpSession session, Model model) {
        String nickname = (String) session.getAttribute("userNickname");

        if (nickname == null || nickname.isEmpty()) {
            // 닉네임 정보가 없으면 작성 페이지로 보냅니다.
            return "redirect:/reportWrite";
        }

        if ("admin".equals(nickname)) {
            // 관리자는 전체 목록 조회
            model.addAttribute("inquiries", inquiryService.findAllInquiries());
        } else {
            // 일반 사용자는 본인 글만 조회
            model.addAttribute("inquiries", inquiryService.findByNickname(nickname));
        }

        return "reportList";
    }

    //개인정보 처리방침
    @GetMapping("/privacy")
    public String privacy() {
        return "privacy"; // templates/privacy.html 파일을 호출
    }

    @GetMapping({"/", "/index"})
    public String index() {
        return "index";
    }
}