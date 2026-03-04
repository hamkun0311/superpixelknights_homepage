package com.oestwdio.superpixelknights.model;

public record Knight(
        String id,
        String nameKr, String nameEn, String nameJp,
        String descKr, String descEn, String descJp,
        int hp, int atk, int cost, String type
) {}