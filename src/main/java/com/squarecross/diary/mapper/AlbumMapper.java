package com.squarecross.diary.mapper;

import com.squarecross.diary.domain.Album;
import com.squarecross.diary.dto.AlbumDto;

import java.util.List;
import java.util.stream.Collectors;

public class AlbumMapper {

    public static AlbumDto convertToDto(Album album) {
        AlbumDto albumDto = new AlbumDto();
        albumDto.setAlbumName(album.getAlbumName());
        albumDto.setAlbumId(album.getAlbumId());
        albumDto.setCreatedAt(album.getCreatedAt());
        return albumDto;
    }

    public static Album convertToModel(AlbumDto albumDto) {
        Album album = new Album();
        album.setAlbumId(albumDto.getAlbumId());
        album.setAlbumName(albumDto.getAlbumName());
        album.setCreatedAt(albumDto.getCreatedAt());
        return album;
    }

    public static List<AlbumDto> convertToDtoList(List<Album> albums) {
        return albums.stream().map(AlbumMapper::convertToDto)
                .collect(Collectors.toList());
    }
}
