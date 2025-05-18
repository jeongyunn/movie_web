package com.example.movieweb.controller;

import com.example.movieweb.model.User;
import com.example.movieweb.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Map;

@RestController
public class SocialLoginController {

    private final UserService userService;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${google.client.id}")
    private String googleClientId;

    @Autowired
    public SocialLoginController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/api/auth/google")
    public String googleLogin(@RequestBody Map<String, String> credential) {
        String idTokenString = credential.get("credential");
        GoogleIdToken.Payload payload = verifyGoogleIdToken(idTokenString);

        if (payload != null) {
            String socialId = payload.getSubject();
            String name = (String) payload.get("name");
            String email = payload.getEmail();

            User user = userService.saveOrUpdate(socialId, "google", name, email);
            System.out.println("Google 로그인 사용자 저장/업데이트: " + user.getId());
            return "Google 로그인 성공";
        } else {
            return "Google ID 토큰 검증 실패";
        }
    }

    private GoogleIdToken.Payload verifyGoogleIdToken(String idTokenString) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                    .setAudience(Collections.singletonList(googleClientId))
                    .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken != null) {
                return idToken.getPayload();
            }
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @PostMapping("/api/auth/kakao")
    public String kakaoLogin(@RequestBody Map<String, String> data) {
        String accessToken = data.get("accessToken");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer " + accessToken);

        String userInfoEndpoint = "https://kapi.kakao.com/v2/user/me";
        String response = restTemplate.postForObject(userInfoEndpoint, null, String.class, headers);

        try {
            JsonNode root = objectMapper.readTree(response);
            Long socialId = root.path("id").asLong();
            String nickname = root.path("properties").path("nickname").asText();
            String email = root.path("kakao_account").path("email").asText();

            User user = userService.saveOrUpdate("kakao_" + socialId, "kakao", nickname, email);
            System.out.println("카카오 로그인 사용자 저장/업데이트: " + user.getId());
            return "카카오 로그인 성공";

        } catch (Exception e) {
            System.err.println("카카오 사용자 정보 파싱 실패: " + e.getMessage());
            return "카카오 로그인 실패 - 사용자 정보 조회 오류";
        }
    }
}