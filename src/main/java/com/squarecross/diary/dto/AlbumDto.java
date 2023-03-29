package com.squarecross.diary.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter @Setter
public class AlbumDto {
    private Long albumId;

    @NotEmpty
    private String albumName;

    @NotEmpty
    private String thumbUrl;

    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private int count;
}
