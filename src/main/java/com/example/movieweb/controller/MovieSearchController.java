package com.example.movieweb.controller;

import com.example.movieweb.service.MovieApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MovieSearchController {

    private final MovieApiService movieApiService;

    @Autowired
    public MovieSearchController(MovieApiService movieApiService) {
        this.movieApiService = movieApiService;
    }

    @GetMapping("/api/search/movies")
    public List<MovieApiService.MovieInfo> searchMovies(@RequestParam String query) {
        return movieApiService.searchMovies(query);
    }
}