package com.moviereviewer.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class MovieDto {

    private String name;

    private String director;

    private String description;

    private List<String> categories;

    private Double rating;

    private List<ReviewDto> reviews;
}
