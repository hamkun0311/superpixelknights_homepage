package com.oestwdio.superpixelknights.repository;

import com.oestwdio.superpixelknights.entity.PatchNote;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatchNoteRepository extends JpaRepository<PatchNote, Long> {
    // 최신순으로 페이징 조회
    Slice<PatchNote> findAllByOrderByCreatedAtDesc(Pageable pageable);
}