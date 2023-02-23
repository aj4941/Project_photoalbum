package com.squarecross.photoalbum.mapper;

import com.squarecross.photoalbum.domain.Photo;
import com.squarecross.photoalbum.dto.PhotoDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PhotoMapper {

    public static PhotoDto convertToDto(Photo photo) {
        PhotoDto photoDto = new PhotoDto();
        photoDto.setPhotoId(photo.getPhotoId());
        photoDto.setFileName(photo.getFileName());
        photoDto.setFileSize(photo.getFileSize());
        photoDto.setUploadedAt(photo.getUploadedAt());
        photoDto.setThumbUrl(photo.getThumbUrl());
        photoDto.setOriginalUrl(photo.getOriginalUrl());
        photoDto.setAlbumId(photo.getAlbum().getAlbumId());
        return photoDto;
    }

    public static List<PhotoDto> convertToDtoList(List<Photo> photos) {
        List<PhotoDto> photoDtos = photos.stream().map(PhotoMapper::convertToDto)
                .collect(Collectors.toList());
        return photoDtos;
    }
}
