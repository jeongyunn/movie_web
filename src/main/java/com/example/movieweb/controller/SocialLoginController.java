package com.example.movieweb.controller;

import com.example.movieweb.model.User;
import com.example.movieweb.service.UserService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Map;

@RestController
public class SocialLoginController {

    private final UserService userService;

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
            // Handle security exception
            e.printStackTrace();
        } catch (IOException e) {
            // Handle IO exception
            e.printStackTrace();
        }
        return null;
    }

    @PostMapping("/api/auth/kakao")
    public String kakaoLogin(@RequestBody Map<String, String> data) {
        String accessToken = data.get("accessToken");
        // TODO: 카카오 Access Token을 사용하여 사용자 정보 가져오는 로직 구현
        // 임시 Mock 데이터 유지
        String socialId = "kakao_" + System.currentTimeMillis();
        String name = "Kakao User";
        String email = "kakao.user@example.com";

        User user = userService.saveOrUpdate(socialId, "kakao", name, email);
        System.out.println("카카오 로그인 사용자 저장/업데이트: " + user.getId());
        return "카카오 로그인 성공";
    }
}