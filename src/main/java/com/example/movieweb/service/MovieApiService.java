package com.example.movieweb.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class MovieApiService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${tmdb.api.key}")
    private String tmdbApiKey;

    private static final String BASE_URL = "https://api.themoviedb.org/3";
    private static final String SEARCH_MOVIE_PATH = "/search/movie";

    public List<MovieInfo> searchMovies(String query) {
        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL + SEARCH_MOVIE_PATH)
                .queryParam("api_key", tmdbApiKey)
                .queryParam("query", query)
                .queryParam("language", "ko-KR") // 한국어 결과
                .build()
                .toUriString();

        String response = restTemplate.getForObject(url, String.class);
        return parseMovieSearchResults(response);
    }

    private List<MovieInfo> parseMovieSearchResults(String response) {
        List<MovieInfo> movies = new ArrayList<>();
        try {
            JsonNode root = objectMapper.readTree(response);
            JsonNode results = root.path("results");
            for (JsonNode movieNode : results) {
                MovieInfo movieInfo = new MovieInfo();
                movieInfo.setTitle(movieNode.path("title").asText());
                movieInfo.setOverview(movieNode.path("overview").asText());
                movieInfo.setPosterPath(movieNode.path("poster_path").asText());
                movieInfo.setReleaseDate(movieNode.path("release_date").asText());
                movies.add(movieInfo);
            }
        } catch (IOException e) {
            System.err.println("TMDB API 응답 파싱 실패: " + e.getMessage());
        }
        return movies;
    }

    // 내부 클래스: 영화 정보를 담는 DTO
    public static class MovieInfo {
        private String title;
        private String overview;
        private String posterPath;
        private String releaseDate;

        // Getter 및 Setter
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getOverview() { return overview; }
        public void setOverview(String overview) { this.overview = overview; }
        public String getPosterPath() { return posterPath; }
        public void setPosterPath(String posterPath) { this.posterPath = posterPath; }
        public String getReleaseDate() { return releaseDate; }
        public void setReleaseDate(String releaseDate) { this.releaseDate = releaseDate; }
    }
}