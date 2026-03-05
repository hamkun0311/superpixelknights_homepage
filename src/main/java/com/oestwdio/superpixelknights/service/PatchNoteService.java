package com.oestwdio.superpixelknights.service;

import com.oestwdio.superpixelknights.entity.PatchNote;
import com.oestwdio.superpixelknights.repository.PatchNoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatchNoteService {
    private final PatchNoteRepository repository;

    public Slice<PatchNote> getPatchNotes(int page, int size) {
        return repository.findAllByOrderByCreatedAtDesc(PageRequest.of(page, size));
    }
}