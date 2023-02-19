package com.squarecross.photoalbum.service;

import com.squarecross.photoalbum.domain.Album;
import com.squarecross.photoalbum.domain.Photo;
import com.squarecross.photoalbum.dto.AlbumDto;
import com.squarecross.photoalbum.repository.AlbumRepository;
import com.squarecross.photoalbum.repository.PhotoRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AssertionsKt;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AlbumServiceTest {
    @Autowired
    AlbumRepository albumRepository;
    @Autowired
    PhotoRepository photoRepository;
    @Autowired
    AlbumService albumService;
    @Test
    void getAlbum() {
        Album album = new Album();
        album.setAlbumName("테스트");
        Album savedAlbum = albumRepository.save(album);

        AlbumDto resAlbum = albumService.getAlbum(savedAlbum.getAlbumId());
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
    @Test
    void testPhotoCount() {
        Album album = new Album();
        album.setAlbumName("테스트");
        Album savedAlbum = albumRepository.save(album);

        Photo photo1 = new Photo();
        photo1.setFile_name("사진1"); photo1.setAlbum(savedAlbum);
        photoRepository.save(photo1);

        Photo photo2 = new Photo();
        photo2.setFile_name("사진2"); photo2.setAlbum(savedAlbum);
        photoRepository.save(photo2);

        Photo photo3 = new Photo();
        photo3.setFile_name("사진3"); photo3.setAlbum(savedAlbum);
        photoRepository.save(photo3);

        AlbumDto albumDto = albumService.getAlbum(savedAlbum.getAlbumId());
        assertThat(albumDto.getCount()).isEqualTo(3);
    }
    @Test
    void testAlbumCreate() throws IOException {
        AlbumDto albumDto = new AlbumDto();
        albumDto.setAlbumName("테스트");
        AlbumDto createAlbumDto = albumService.createAlbum(albumDto);
        assertThat(albumDto.getAlbumName()).isEqualTo(createAlbumDto.getAlbumName());

        albumService.deleteAlbumDirectories(createAlbumDto);
    }

    @Test
    void testAlbumRepository() throws InterruptedException {
        // given
        Album album1 = new Album();
        Album album2 = new Album();
        album1.setAlbumName("aaaa"); album2.setAlbumName("aaab");

        // when
        albumRepository.save(album1);
        TimeUnit.SECONDS.sleep(1);
        albumRepository.save(album2);

        // then
        List<Album> resDate = albumRepository.findByAlbumNameContainingOrderByCreatedAtDesc("aaa");
        assertEquals("aaab", resDate.get(0).getAlbumName());
        assertEquals("aaaa", resDate.get(1).getAlbumName());
        assertEquals(2, resDate.size());

        List<Album> resName = albumRepository.findByAlbumNameContainingOrderByAlbumNameAsc("aaa");
        assertEquals("aaaa", resName.get(0).getAlbumName());
        assertEquals("aaab", resName.get(1).getAlbumName());
        assertEquals(2, resName.size());
    }
}