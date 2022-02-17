package com.moviereviewer.controller;

import com.moviereviewer.dto.MovieDto;
import com.moviereviewer.model.Movie;
import com.moviereviewer.service.MovieService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("movies")
public class Controller {

    private final MovieService movieService;

    public Controller(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public List<MovieDto> getAllMovies() {
        return movieService.findAll();
    }

    @PostMapping
    public void save(@RequestBody MovieDto movieDto) {
        movieService.save(movieDto);
    }
}
