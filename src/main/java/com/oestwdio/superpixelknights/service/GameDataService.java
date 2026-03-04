package com.oestwdio.superpixelknights.service;

import com.oestwdio.superpixelknights.model.Knight;
import com.oestwdio.superpixelknights.model.Scroll;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class GameDataService {
    private List<Knight> knights = new ArrayList<>();
    private List<Scroll> scrolls = new ArrayList<>();

    @PostConstruct
    public void init() {
        this.knights = loadKnights();
        this.scrolls = loadScrolls();
    }

    private List<Knight> loadKnights() {
        List<Knight> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new ClassPathResource("static/data/Knight.csv").getInputStream(), StandardCharsets.UTF_8))) {
            br.readLine(); // 헤더 스킵
            String line;
            while ((line = br.readLine()) != null) {
                String[] v = line.split(",");
                // 인덱스: 0:ID, 1:KR, 2:EN, 3:JP, 4:DescKR, 5:DescEN, 6:DescJP, 7:HP, 8:ATK, 9:Cost, 13:Type
                list.add(new Knight(
                        v[0], v[1], v[2], v[3],
                        v[4], v[5], v[6],
                        Integer.parseInt(v[7]), Integer.parseInt(v[8]), Integer.parseInt(v[9]), v[13]
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    private List<Scroll> loadScrolls() {
        List<Scroll> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new ClassPathResource("static/data/Scroll.csv").getInputStream(), StandardCharsets.UTF_8))) {
            br.readLine(); // 헤더 스킵
            String line;
            while ((line = br.readLine()) != null) {
                String[] v = line.split(",");
                if (v.length < 9) continue;

                String formula = v[8]; // 수치 데이터 (예: 20*%)

                // IColumn을 실제 Formula 값으로 치환 (KR, EN, JP 모두 적용)
                String descKr = v[5].replace("IColumn", formula);
                String descEn = v[6].replace("IColumn", formula);
                String descJp = v[7].replace("IColumn", formula);

                list.add(new Scroll(
                        v[0], v[1], v[2], v[3], v[4],
                        descKr, descEn, descJp, formula
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public List<Knight> getKnights() { return knights; }
    public List<Scroll> getScrolls() { return scrolls; }
}