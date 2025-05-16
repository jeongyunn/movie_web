package com.example.movieweb.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String socialId; // Google 또는 카카오에서 제공하는 고유 ID
    private String provider; // "google" 또는 "kakao"
    private String name;
    private String email;

    // 기본 생성자
    public User() {
    }

    // 생성자
    public User(String socialId, String provider, String name, String email) {
        this.socialId = socialId;
        this.provider = provider;
        this.name = name;
        this.email = email;
    }

    // Getter 및 Setter 메서드 (Alt + Insert 또는 Cmd + N 단축키 활용)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSocialId() {
        return socialId;
    }

    public void setSocialId(String socialId) {
        this.socialId = socialId;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}