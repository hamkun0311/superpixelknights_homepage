package com.oestwdio.superpixelknights.domain; // 본인의 패키지 경로에 맞게 수정

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Inquiry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname; // 유니티에서 넘어올 닉네임
    private String category; // BUG, FEEDBACK 등
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String status = "PENDING";
    private LocalDateTime createdAt = LocalDateTime.now();

    // Inquiry.java에 추가
    @OneToMany(mappedBy = "inquiry", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

}