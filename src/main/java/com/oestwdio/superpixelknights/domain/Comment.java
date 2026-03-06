package com.oestwdio.superpixelknights.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inquiry_id")
    private Inquiry inquiry; // 어떤 글에 대한 댓글인지

    private String nickname; // 관리자(admin)

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createdAt = LocalDateTime.now();

    public Comment(Inquiry inquiry, String nickname, String content) {
        this.inquiry = inquiry;
        this.nickname = nickname;
        this.content = content;
    }
}