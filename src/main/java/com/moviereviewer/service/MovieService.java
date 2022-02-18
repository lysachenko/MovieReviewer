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
import java.util.Optional;

import static java.util.Collections.emptyList;
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
        final List<Category> categoriesByMovies = categoryRepository.findAllByMovies(movies);
        final List<Rate> ratesByMovies = rateRepository.findAllByMovies(movies);
        final List<Review> reviewsByMovies = reviewRepository.findAllByMovies(movies);

        return movies.stream()
            .map(movie -> buildMovieDto(movie, categoriesByMovies, ratesByMovies, reviewsByMovies))
            .collect(toList());
    }

    public MovieDto findById(Long id) {
        final Optional<Movie> optionalMovie = movieRepository.findById(id);

        final Movie movie;
        if (optionalMovie.isPresent()) {
            movie = optionalMovie.get();
        } else {
            return null;
        }

        final List<Category> categoriesByMovie = categoryRepository.findCategoryByMovie(movie);
        final List<Rate> ratingByMovie = rateRepository.findRateByMovie(movie);
        final List<Review> reviewsByMovie = reviewRepository.findReviewByMovie(movie);

        return buildMovieDto(movie, categoriesByMovie, ratingByMovie, reviewsByMovie);
    }

    public void save(MovieDto movieDto) {
        final List<Genre> genres = movieDto.getCategories()
            .stream()
            .map(Genre::from)
            .collect(toList());

        final List<Category> categories = categoryRepository.findAllByGenres(genres);

        Movie movie = new Movie()
            .setName(movieDto.getName())
            .setDescription(movieDto.getDescription())
            .setDirector(movieDto.getDirector())
            .setCategories(categories);

        movieRepository.save(movie);
    }

    private MovieDto buildMovieDto(
        final Movie movie, final List<Category> categoriesByMovie,
        final List<Rate> ratingByMovie, final List<Review> reviewsByMovie
    ) {
        return new MovieDto()
            .setName(movie.getName())
            .setDirector(movie.getDirector())
            .setDescription(movie.getDescription())
            .setCategories(getCategoriesForMovieDto(categoriesByMovie).getOrDefault(movie, emptyList()))
            .setRating(getRatingForMovieDto(ratingByMovie).getOrDefault(movie, null))
            .setReviews(getReviewsForMovieDto(reviewsByMovie).getOrDefault(movie, emptyList()));
    }

    private Map<Movie, List<ReviewDto>> getReviewsForMovieDto(List<Review> movies) {
        return movies
            .stream()
            .collect(groupingBy(Review::getMovie,
                mapping(review -> new ReviewDto()
                    .setFullname(review.getUser().getFirstName() + " " + review.getUser().getLastName())
                    .setMessage(review.getMessage())
                    .setLiked(review.isLiked()), toList())
            ));
    }

    private Map<Movie, Double> getRatingForMovieDto(List<Rate> rates) {
        return rates
            .stream()
            .collect(groupingBy(Rate::getMovie, averagingDouble(Rate::getVote)));
    }

    private Map<Movie, List<String>> getCategoriesForMovieDto(List<Category> categories) {
        return categories
            .stream()
            .collect(groupingBy(Category::getMovie,
                mapping(category -> category.getGenre().getName(), toList())
            ));
    }
}
