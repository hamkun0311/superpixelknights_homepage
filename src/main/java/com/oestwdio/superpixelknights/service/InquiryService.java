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
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid inquiry Id"));

        Comment comment = new Comment(inquiry, nickname, content);
        commentRepository.save(comment);

        // 답변이 달리면 상태를 'ANSWERED' 또는 'COMPLETED'로 변경
        inquiry.setStatus("ANSWERED");
    }

    public Inquiry findById(Long id) {
        return inquiryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id=" + id));
    }

}