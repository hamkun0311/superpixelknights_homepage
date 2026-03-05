package com.oestwdio.superpixelknights.controller;

import com.oestwdio.superpixelknights.entity.PatchNote;
import com.oestwdio.superpixelknights.service.PatchNoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class PatchNoteController {
    private final PatchNoteService patchNoteService;

    // 초기 페이지 렌더링 (첫 5개)
    @GetMapping("/patchnotes")
    public String patchNotePage(Model model) {
        model.addAttribute("firstPage", patchNoteService.getPatchNotes(0, 5));
        return "patchnotes";
    }

    // '더 보기' 클릭 시 호출될 API
    @GetMapping("/api/patchnotes")
    @ResponseBody
    public Slice<PatchNote> getPatchNotesApi(@RequestParam(defaultValue = "0") int page) {
        return patchNoteService.getPatchNotes(page, 5);
    }
}