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

        if ("test1111#09681".equals(nickname)) {
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

    // 상세 조회 추가 (댓글 목록 포함)
// 상세 조회 메서드 수정
    @GetMapping("/reportDetail/{id}")
    public String reportDetail(@PathVariable Long id, HttpSession session, Model model) {
        Inquiry inquiry = inquiryService.findById(id);
        String nickname = (String) session.getAttribute("userNickname");

        model.addAttribute("inquiry", inquiry);
        model.addAttribute("currentNickname", nickname);

        // [중요] 본인 계정을 관리자로 판단하는 플래그를 모델에 담아주면 HTML이 편해집니다.
        boolean isAdmin = "admin".equals(nickname) || "test1111#09681".equals(nickname);
        model.addAttribute("isAdmin", isAdmin);

        return "reportDetail";
    }

    // 댓글 저장 API
    @PostMapping("/commentSave")
    public String commentSave(@RequestParam("inquiryId") Long inquiryId,
                              @RequestParam("content") String content,
                              HttpSession session) {

        // 1. 세션에서 현재 접속자 닉네임 가져오기
        String nickname = (String) session.getAttribute("userNickname");

        // 2. 권한 체크 (시니어의 방어적 코드: 관리자만 답변 가능)
        if ("test1111#09681".equals(nickname) || "admin".equals(nickname)) {
            // 3. 서비스 호출 (DB 저장 + PlayFab 연동)
            inquiryService.saveComment(inquiryId, nickname, content);
        } else {
            // 권한이 없으면 리스트로 튕겨내기 (또는 에러 메시지)
            return "redirect:/reportList";
        }

        // 4. 답변 작성 후 다시 상세 페이지로 이동
        return "redirect:/reportDetail/" + inquiryId;
    }
}