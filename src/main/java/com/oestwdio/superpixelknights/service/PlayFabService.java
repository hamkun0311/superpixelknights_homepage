package com.oestwdio.superpixelknights.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

@Service
public class PlayFabService {

    @Value("${playfab.title.id}")
    private String titleId;

    @Value("${playfab.secret.key}")
    private String secretKey;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * [최종 통합 메서드] 닉네임으로 유저를 찾아 일반 데이터를 업데이트합니다.
     */
    public void updateStatusByDisplayName(String displayName, String status) {
        try {
            // 1. 유저 ID 조회
            String playFabId = getPlayFabId(displayName);

            // [시니어의 팁] ID가 없으면 여기서 멈춰야 400 에러를 안 봅니다.
            if (playFabId == null || playFabId.isEmpty()) {
                System.err.println("❌ PlayFab 업데이트 중단: 닉네임 '" + displayName + "'에 해당하는 유저를 찾을 수 없습니다.");
                return;
            }

            // 2. 일반 데이터(User Data) 업데이트
            updateUserData(playFabId, status);

        } catch (Exception e) {
            System.err.println("❌ PlayFab 통합 처리 오류: " + e.getMessage());
        }
    }

    // 1. Admin API를 통한 PlayFabId 조회
    private String getPlayFabId(String displayName) {
        String url = String.format("https://%s.playfabapi.com/Admin/GetUserAccountInfo", titleId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-SecretKey", secretKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("TitleDisplayName", displayName);

        try {
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

            if (response.getBody() != null && response.getBody().get("data") != null) {
                Map data = (Map) response.getBody().get("data");
                Map userInfo = (Map) data.get("UserInfo");
                return (String) userInfo.get("PlayFabId");
            }
        } catch (Exception e) {
            System.err.println("⚠️ 유저 ID 조회 실패: " + e.getMessage());
        }
        return null;
    }

    // 2. Server API를 통한 일반 데이터(User Data) 업데이트
    private void updateUserData(String playFabId, String status) {
        String url = String.format("https://%s.playfabapi.com/Server/UpdateUserData", titleId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-SecretKey", secretKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("PlayFabId", playFabId);
        body.put("Data", Map.of("LastReportStatus", status));

        try {
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
            restTemplate.postForObject(url, request, String.class);
            System.out.println("✅ PlayFab [User Data] 업데이트 성공: " + playFabId + " (" + status + ")");
        } catch (Exception e) {
            System.err.println("⚠️ 데이터 업데이트 실패: " + e.getMessage());
        }
    }
}