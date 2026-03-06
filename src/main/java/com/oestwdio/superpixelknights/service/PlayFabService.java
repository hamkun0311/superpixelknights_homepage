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

    public void updateStatusByDisplayName(String displayName, String status) {
        try {
            // [STEP 1] DisplayName으로 PlayFabId 찾기
            String playFabId = getPlayFabId(displayName);
            if (playFabId == null) {
                System.err.println("유저를 찾을 수 없습니다: " + displayName);
                return;
            }

            // [STEP 2] 찾은 PlayFabId로 ReadOnlyData 업데이트
            updateReadOnlyData(playFabId, status);
            System.out.println("PlayFab 업데이트 성공: " + displayName + " -> " + status);

        } catch (Exception e) {
            System.err.println("PlayFab 처리 중 오류 발생: " + e.getMessage());
        }
    }

    // 닉네임으로 ID 조회 API
    private String getPlayFabId(String displayName) {
        // [수정] Server -> Admin / GetAccountInfo -> GetUserAccountInfo
        String url = String.format("https://%s.playfabapi.com/Admin/GetUserAccountInfo", titleId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-SecretKey", secretKey); // Admin API도 동일한 시크릿 키를 사용합니다.
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        // [참고] Admin/GetUserAccountInfo는 TitleDisplayName 파라미터를 지원합니다.
        body.put("TitleDisplayName", displayName);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

            if (response.getBody() != null && response.getBody().get("data") != null) {
                Map data = (Map) response.getBody().get("data");
                Map userInfo = (Map) data.get("UserInfo");
                return (String) userInfo.get("PlayFabId");
            }
        } catch (Exception e) {
            System.err.println("유저 조회 중 오류 발생: " + e.getMessage());
        }
        return null;
    }

    // ID로 데이터 업데이트 API
    private void updateReadOnlyData(String playFabId, String status) {
        String url = String.format("https://%s.playfabapi.com/Server/UpdateUserReadOnlyData", titleId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-SecretKey", secretKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("PlayFabId", playFabId);
        body.put("Data", Map.of("LastReportStatus", status));

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        restTemplate.postForObject(url, request, String.class);
    }
}