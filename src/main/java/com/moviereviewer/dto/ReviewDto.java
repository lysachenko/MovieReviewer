package com.moviereviewer.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ReviewDto {

    private String message;

    private String fullname;

    private boolean isLiked;
}
