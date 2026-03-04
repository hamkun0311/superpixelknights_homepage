package com.oestwdio.superpixelknights.repository;

import com.oestwdio.superpixelknights.domain.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
    // 나중에 특정 유저의 글만 모아보기 위해 미리 정의해둡니다.
    List<Inquiry> findByNicknameOrderByCreatedAtDesc(String nickname);
}