package com.squarecross.diary.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Getter @Setter
public class BoardDto {
    private Long boardId;

    @NotEmpty
    private String title;

    @NotEmpty
    private String content;

    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private String author;
}
