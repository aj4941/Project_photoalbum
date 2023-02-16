package com.squarecross.photoalbum.mapper;

import com.squarecross.photoalbum.domain.Album;
import com.squarecross.photoalbum.dto.AlbumDto;
public class AlbumMapper {
    public static AlbumDto convertToDto(Album album) {
        AlbumDto albumDto = new AlbumDto();
        albumDto.setAlbumName(album.getAlbumName());
        albumDto.setAlbumId(album.getAlbumId());
        albumDto.setCreatedAt(album.getCreatedAt());
        return albumDto;
    }
}
