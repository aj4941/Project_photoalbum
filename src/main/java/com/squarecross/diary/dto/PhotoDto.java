package com.squarecross.diary.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter @Setter
public class PhotoDto {
    private Long photoId;
    private String fileName;
    private int fileSize;
    private String originalUrl;
    private String thumbUrl;
    private Date uploadedAt;
    private Long albumId;
    private List<Long> photoIds;
}
