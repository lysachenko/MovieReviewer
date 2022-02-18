package com.moviereviewer.repository;

import com.moviereviewer.model.Movie;
import com.moviereviewer.model.Rate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RateRepository extends JpaRepository<Rate, Long> {

    @Query("select r from Rate r where r.movie in (:movies)")
    List<Rate> findAllByMovies(@Param("movies") List<Movie> movies);

    List<Rate> findRateByMovie(Movie movie);
}
