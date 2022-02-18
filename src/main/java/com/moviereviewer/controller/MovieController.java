package com.moviereviewer.controller;

import com.moviereviewer.dto.MovieDto;
import com.moviereviewer.service.MovieService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("movie")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public List<MovieDto> findAll() {
        return movieService.findAll();
    }

    @GetMapping("{id}")
    public MovieDto findById(@PathVariable Long id) {
        return movieService.findById(id);
    }

    @PostMapping
    public void save(@RequestBody MovieDto movieDto) {
        movieService.save(movieDto);
    }
}
