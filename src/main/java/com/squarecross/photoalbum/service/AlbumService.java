package com.squarecross.photoalbum.service;

import com.squarecross.photoalbum.domain.Album;
import com.squarecross.photoalbum.domain.Photo;
import com.squarecross.photoalbum.dto.AlbumDto;
import com.squarecross.photoalbum.mapper.AlbumMapper;
import com.squarecross.photoalbum.repository.AlbumRepository;
import com.squarecross.photoalbum.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.squarecross.photoalbum.service.Constants.*;

@Service
public class AlbumService {
    @Autowired
    private AlbumRepository albumRepository;
    @Autowired
    private PhotoRepository photoRepository;
    public AlbumDto getAlbum(Long albumId) {
        Optional<Album> res = albumRepository.findById(albumId);
        if (res.isPresent()) {
            AlbumDto albumDto = AlbumMapper.convertToDto(res.get());
            albumDto.setCount(photoRepository.countByAlbum_AlbumId(albumId));
            return albumDto;
        }
        else
            throw new EntityNotFoundException
                    (String.format("앨범 아이디 %d로 조회되지 않았습니다.", albumId));
    }
    public Album getAlbumByName(String albumName) {
        Optional<Album> res = Optional.ofNullable(albumRepository.findByAlbumName(albumName));
        if (res.isPresent())
            return res.get();
        else
            throw new EntityNotFoundException(
                    String.format("앨범 이름 %s로 조회되지 않았습니다.", albumName));
    }

    public AlbumDto createAlbum(AlbumDto albumDto) throws IOException {
        Album album = AlbumMapper.convertToModel(albumDto);
        albumRepository.save(album);
        createAlbumDirectories(album);
        return AlbumMapper.convertToDto(album);
    }

    private void createAlbumDirectories(Album album) throws IOException {
        Files.createDirectories(Paths.get(PATH_PREFIX + "/photos/original/" + album.getAlbumId()));
        Files.createDirectories(Paths.get(PATH_PREFIX + "/photos/thumb/" + album.getAlbumId()));
    }

    // 같은 클래스 내에서 사용되지 못하므로 private로 사용하면 안된다.
    public void deleteAlbumDirectories(AlbumDto albumDto) throws IOException {
        Files.delete(Path.of(PATH_PREFIX + "/photos/original/" + albumDto.getAlbumId()));
        Files.delete(Path.of(PATH_PREFIX + "/photos/thumb/" + albumDto.getAlbumId()));
    }
    public List<AlbumDto> getAlbumList(String keyword, String sort) {
        List<Album> albums;
        if (Objects.equals(sort, "byName")) {
           albums = albumRepository.findByAlbumNameContainingOrderByAlbumNameAsc(keyword);
        }
        else if (Objects.equals(sort, "byDate")) {
            albums = albumRepository.findByAlbumNameContainingOrderByCreatedAtDesc(keyword);
        }
        else {
            throw new IllegalArgumentException("알 수 없는 정렬 기준입니다.");
        }

        List<AlbumDto> albumDtos = AlbumMapper.convertToDtoList(albums);

        for (AlbumDto albumDto : albumDtos) {
            List<Photo> top4 =
                    photoRepository.findTop4ByAlbum_AlbumIdOrderByUploadedAtDesc(albumDto.getAlbumId());
            albumDto.setThumbUrls(top4.stream()
                                    .map(Photo::getThumb_url)
                                    .map(c -> PATH_PREFIX + c)
                                    .collect(Collectors.toList()));
        }

        return albumDtos;
    }
}
