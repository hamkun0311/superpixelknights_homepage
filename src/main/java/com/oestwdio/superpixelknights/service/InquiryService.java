package com.oestwdio.superpixelknights.service;

import com.oestwdio.superpixelknights.domain.Inquiry;
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
}