package com.moviereviewer.repository;

import com.moviereviewer.enums.Genre;
import com.moviereviewer.model.Category;
import com.moviereviewer.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("select c from Category c where c.genre in (:genres)")
    List<Category> findAllByGenres(@Param("genres") List<Genre> genres);

    @Query("select c from Category c where c.movie in (:movies)")
    List<Category> findAllByMovies(@Param("movies") List<Movie> movies);

    List<Category> findCategoryByMovie(Movie movie);
}
