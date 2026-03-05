package com.oestwdio.superpixelknights.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

/**
 * 패치노트 엔티티 (Multi-language 지원)
 * DB 테이블: patch_notes
 */
@Entity
@Table(name = "patch_notes")
@Getter
@Setter
@NoArgsConstructor // 기본 생성자 (JPA 필수)
public class PatchNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String version;

    // --- 한국어 데이터 ---
    @Column(name = "title_ko", nullable = false, length = 200)
    private String titleKo;

    @Column(name = "content_ko", nullable = false, columnDefinition = "TEXT")
    private String contentKo;

    // --- 영어 데이터 ---
    @Column(name = "title_en", nullable = false, length = 200)
    private String titleEn;

    @Column(name = "content_en", nullable = false, columnDefinition = "TEXT")
    private String contentEn;

    // --- 일본어 데이터 ---
    @Column(name = "title_jp", nullable = false, length = 200)
    private String titleJp;

    @Column(name = "content_jp", nullable = false, columnDefinition = "TEXT")
    private String contentJp;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * 엔티티가 영속화(Persist)되기 전 호출되는 콜백
     * 생성 시간을 자동으로 주입합니다.
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}