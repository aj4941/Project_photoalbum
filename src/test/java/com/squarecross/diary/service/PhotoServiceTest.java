package com.squarecross.diary.service;

import com.squarecross.diary.domain.Album;
import com.squarecross.diary.domain.Photo;
import com.squarecross.diary.dto.PhotoDto;
import com.squarecross.diary.repository.AlbumRepository;
import com.squarecross.diary.repository.PhotoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PhotoServiceTest {

    @Autowired
    AlbumRepository albumRepository;

    @Autowired
    PhotoRepository photoRepository;

    @Autowired
    PhotoService photoService;

    @Test
    void getPhoto() {
        // given
        Album album = new Album();
        album.setAlbumName("테스트");

        Photo photo = new Photo();
        photo.setFileName("테스트");
        album.addPhoto(photo);

        Album savedAlbum = albumRepository.save(album);
        Photo savedPhoto = photoRepository.findByAlbum_AlbumId(savedAlbum.getAlbumId());

        // when
        PhotoDto photoDto = photoService.getPhoto(savedPhoto.getPhotoId());

        // then
        assertEquals("테스트", photoDto.getFileName());
    }
}