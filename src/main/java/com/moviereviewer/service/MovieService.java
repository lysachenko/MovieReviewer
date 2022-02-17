package com.moviereviewer.service;

import com.moviereviewer.dto.MovieDto;
import com.moviereviewer.dto.ReviewDto;
import com.moviereviewer.enums.Genre;
import com.moviereviewer.model.Category;
import com.moviereviewer.model.Movie;
import com.moviereviewer.model.Rate;
import com.moviereviewer.model.Review;
import com.moviereviewer.repository.CategoryRepository;
import com.moviereviewer.repository.MovieRepository;
import com.moviereviewer.repository.RateRepository;
import com.moviereviewer.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.averagingDouble;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final CategoryRepository categoryRepository;
    private final RateRepository rateRepository;
    private final ReviewRepository reviewRepository;

    public MovieService(
        final MovieRepository movieRepository,
        final CategoryRepository categoryRepository,
        final RateRepository rateRepository,
        final ReviewRepository reviewRepository
    ) {
        this.movieRepository = movieRepository;
        this.categoryRepository = categoryRepository;
        this.rateRepository = rateRepository;
        this.reviewRepository = reviewRepository;
    }

    public List<MovieDto> findAll() {
        final List<Movie> movies = movieRepository.findAll();

        final Map<Movie, List<String>> groupedByCategories = categoryRepository.findAllByMovies(movies)
            .stream()
            .collect(groupingBy(Category::getMovie, mapping(category -> category.getGenre().getName(), toList())));

        final Map<Movie, Double> groupedByRating = rateRepository.findAllByMovies(movies)
            .stream()
            .collect(groupingBy(Rate::getMovie, averagingDouble(Rate::getVote)));

        Map<Movie, List<ReviewDto>> groupedByReviews = reviewRepository.findAllByMovies(movies)
            .stream()
            .collect(groupingBy(Review::getMovie,
                mapping(review -> new ReviewDto()
                    .setFullname(review.getUser().getFirstName() + " " + review.getUser().getLastName())
                    .setMessage(review.getMessage())
                    .setLiked(review.isLiked()), toList())));

        return movies.stream()
            .map(movie -> new MovieDto()
                .setName(movie.getName())
                .setDirector(movie.getDirector())
                .setDescription(movie.getDescription())
                .setCategories(groupedByCategories.get(movie))
                .setRating(groupedByRating.get(movie))
                .setReviews(groupedByReviews.get(movie))
            )
            .collect(toList());
    }

    public void save(MovieDto movieDto) {
        List<Genre> genres = movieDto.getCategories()
            .stream()
            .map(Genre::from)
            .collect(toList());

        List<Category> categories = categoryRepository.findAllByGenres(genres);

        Movie movie = new Movie()
            .setName(movieDto.getName())
            .setDescription(movieDto.getDescription())
            .setDirector(movieDto.getDirector())
            .setCategories(categories);

        movieRepository.save(movie);
    }
}
