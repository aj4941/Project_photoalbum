package com.squarecross.diary.service;

import com.squarecross.diary.domain.Album;
import com.squarecross.diary.domain.Photo;
import com.squarecross.diary.dto.AlbumDto;
import com.squarecross.diary.dto.UserDto;
import com.squarecross.diary.mapper.AlbumMapper;
import com.squarecross.diary.repository.AlbumRepository;
import com.squarecross.diary.repository.PhotoRepository;
import org.apache.catalina.UserDatabase;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.squarecross.diary.mapper.AlbumMapper.convertToDto;
import static com.squarecross.diary.service.Constants.*;

@Service
@Transactional
public class AlbumService {

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private PhotoRepository photoRepository;

    public List<AlbumDto> getAlbumList(String loginId) {
        List<Album> albums = albumRepository.findAlbumList(loginId);
        List<AlbumDto> albumDtos = AlbumMapper.convertToDtoList(albums);
        return albumDtos;
    }

    public void saveAlbum(AlbumDto albumDto, UserDto userDto) {
//        Album album = AlbumMapper.convertToModel()
    }

    public void deleteAlbum(Long albumId) {
        albumRepository.deleteById(albumId);
    }
}
