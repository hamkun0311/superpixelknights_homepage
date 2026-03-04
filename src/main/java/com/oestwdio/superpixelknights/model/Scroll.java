package com.oestwdio.superpixelknights.model;

public record Scroll(
        String id, String grade,
        String nameKr, String nameEn, String nameJp,
        String descKr, String descEn, String descJp,
        String formula
) {}