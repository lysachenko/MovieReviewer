package com.moviereviewer.repository;

import com.moviereviewer.model.Movie;
import com.moviereviewer.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("select r from Review r where r.movie in (:movies)")
    List<Review> findAllByMovies(@Param("movies") List<Movie> movies);
}
