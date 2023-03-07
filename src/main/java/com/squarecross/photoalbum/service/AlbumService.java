package com.squarecross.photoalbum.service;

import com.squarecross.photoalbum.domain.Album;
import com.squarecross.photoalbum.domain.Photo;
import com.squarecross.photoalbum.dto.AlbumDto;
import com.squarecross.photoalbum.mapper.AlbumMapper;
import com.squarecross.photoalbum.repository.AlbumRepository;
import com.squarecross.photoalbum.repository.PhotoRepository;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.squarecross.photoalbum.mapper.AlbumMapper.convertToDto;
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
            AlbumDto albumDto = convertToDto(res.get());
            albumDto.setCount(photoRepository.countByAlbum_AlbumId(albumId)); // photo의 album.getAlbumId()가 albumId인 것들의 개수 출력
            return albumDto;
        } else {
            throw new EntityNotFoundException(String.format("앨범 아이디 %d로 조회 실패", albumId));
        }
    }

    public Album getAlbumByName(String albumName) {
        Album res = albumRepository.findByAlbumName(albumName);
        return res;
    }

    public AlbumDto createAlbum(AlbumDto albumDto) throws IOException {
        Album album = AlbumMapper.convertToModel(albumDto);
        albumRepository.save(album);
        createAlbumDirectories(album);
        return convertToDto(album);
    }

    void createAlbumDirectories(Album album) throws IOException {
        Files.createDirectories(Paths.get(PATH_PREFIX + "/photos/original/" + album.getAlbumId()));
        Files.createDirectories(Paths.get(PATH_PREFIX + "/photos/thumb/" + album.getAlbumId()));
    }

    // 같은 클래스 내에서 사용되지 못하므로 private로 사용하면 안된다.
    public void deleteAlbumDirectories(AlbumDto albumDto) throws IOException {
        Files.delete(Path.of(PATH_PREFIX + "/photos/original/" + albumDto.getAlbumId()));
        Files.delete(Path.of(PATH_PREFIX + "/photos/thumb/" + albumDto.getAlbumId()));
    }
    public void deleteAlbumDirectories(Album album) throws IOException {
        Files.delete(Path.of(PATH_PREFIX + "/photos/original/" + album.getAlbumId()));
        Files.delete(Path.of(PATH_PREFIX + "/photos/thumb/" + album.getAlbumId()));
    }
    public void deleteAlbumFiles(Album album) throws IOException {
        FileUtils.cleanDirectory(new File(Constants.PATH_PREFIX + "/photos/original/" + album.getAlbumId()));
        FileUtils.cleanDirectory(new File(Constants.PATH_PREFIX + "/photos/thumb/" + album.getAlbumId()));
    }

    public List<AlbumDto> getAlbumList(String keyword, String sort, String orderBy) {

        List<Album> albums = albumRepository.search(keyword, sort, orderBy);
        List<AlbumDto> albumDtos = AlbumMapper.convertToDtoList(albums);

        for (AlbumDto albumDto : albumDtos) {
            List<Photo> top4 =
                    photoRepository.findTop4ByAlbum_AlbumIdOrderByUploadedAtDesc(albumDto.getAlbumId());
            albumDto.setThumbUrls(top4.stream()
                                    .map(Photo::getThumbUrl)
                                    .map(c -> PATH_PREFIX + c)
                                    .collect(Collectors.toList()));
        }
        return albumDtos;
    }

    public AlbumDto changeName(Long albumId, AlbumDto albumDto) {
        Optional<Album> album = albumRepository.findById(albumId);
        if (album.isEmpty()) {
            throw new NoSuchElementException(String.format("Album ID %d가 존재하지 않습니다.", albumId));
        }
        Album updatedAlbum = album.get();
        updatedAlbum.setAlbumName(albumDto.getAlbumName());
        return convertToDto(updatedAlbum);
    }

    public void deleteAlbum(Long albumId) throws EntityNotFoundException, IOException {
        Optional<Album> res = albumRepository.findById(albumId);
        if (res.isPresent()) {
            Album album = res.get();
            deleteAlbumFiles(album);
            deleteAlbumDirectories(album);
            albumRepository.deleteById(albumId);
        }
        else {
            throw new EntityNotFoundException(
                    String.format("Album Id %d가 존재하지 않습니다.", albumId));
        }
    }
}
