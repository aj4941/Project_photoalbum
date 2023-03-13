package com.squarecross.diary.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter @Setter
public class AlbumDto {
    private Long albumId;
    private String albumName;
    private Date createdAt;
    private int count;
    private String thumbUrl;
}
