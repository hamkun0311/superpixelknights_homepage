package com.oestwdio.superpixelknights.service;

import com.oestwdio.superpixelknights.domain.Comment;
import com.oestwdio.superpixelknights.domain.Inquiry;
import com.oestwdio.superpixelknights.repository.CommentRepository;
import com.oestwdio.superpixelknights.repository.InquiryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InquiryService {

    private final InquiryRepository inquiryRepository;
    private final CommentRepository commentRepository;
    private final PlayFabService playFabService;       // 플레이팹 서비스 주입 필요

    @Transactional
    public void saveInquiry(Inquiry inquiry) {
        inquiryRepository.save(inquiry);
    }

    public List<Inquiry> findAllInquiries() {
        return inquiryRepository.findAll();
    }

    // 추가된 메서드: 특정 닉네임의 리포트만 가져옵니다.
    public List<Inquiry> findByNickname(String nickname) {
        return inquiryRepository.findByNicknameOrderByCreatedAtDesc(nickname);
    }

    @Transactional
    public void saveComment(Long inquiryId, String nickname, String content) {
        // 1. 원본 리포트 찾기
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new IllegalArgumentException("해당 리포트가 없습니다. id=" + inquiryId));

        // 2. 댓글(Comment) 엔티티 생성 및 저장
        Comment comment = new Comment(inquiry, nickname, content);
        commentRepository.save(comment);

        // 3. 리포트 상태 업데이트 (PENDING -> ANSWERED)
        inquiry.setStatus("ANSWERED");

        // 4. [수정됨] PlayFab 데이터 업데이트 호출
        // CloudScript 대신 Java에서 직접 ID를 찾아서 업데이트하는 새 메서드를 호출합니다.
        // inquiry.getNickname()은 리포트를 작성한 유저의 닉네임입니다.
        playFabService.updateStatusByDisplayName(inquiry.getNickname(), "ANSWERED");
    }

    public Inquiry findById(Long id) {
        return inquiryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id=" + id));
    }

}