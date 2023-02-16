package com.squarecross.photoalbum.service;

import com.squarecross.photoalbum.domain.Album;
import com.squarecross.photoalbum.repository.AlbumRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AssertionsKt;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AlbumServiceTest {
    @Autowired
    AlbumRepository albumRepository;
    @Autowired
    AlbumService albumService;
    @Test
    void getAlbum() {
        Album album = new Album();
        album.setAlbumName("테스트");
        Album savedAlbum = albumRepository.save(album);

        Album resAlbum = albumService.getAlbum(savedAlbum.getAlbumId());
        assertEquals("테스트", resAlbum.getAlbumName());
    }
    @Test
    void getAlbumByName() {
        // given
        Album hasAlbum = new Album();
        hasAlbum.setAlbumName("테스트");
        Album savedAlbum = albumRepository.save(hasAlbum);

        // when
        Album findAlbum = albumService.getAlbumByName("테스트");

        // then
        assertThat(findAlbum).isEqualTo(savedAlbum);



        // given
        Album hasNotAlbum = new Album();
        hasNotAlbum.setAlbumName("테스트2");

        // when
        Album savedAlbum2 = albumRepository.save(hasNotAlbum);

        // then
        assertThrows(EntityNotFoundException.class,
                () -> albumService.getAlbumByName("xxxxx"));
    }
}