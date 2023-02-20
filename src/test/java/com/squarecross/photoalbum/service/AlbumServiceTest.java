package com.squarecross.photoalbum.service;

import com.squarecross.photoalbum.domain.Album;
import com.squarecross.photoalbum.domain.Photo;
import com.squarecross.photoalbum.dto.AlbumDto;
import com.squarecross.photoalbum.repository.AlbumRepository;
import com.squarecross.photoalbum.repository.PhotoRepository;
import jdk.swing.interop.SwingInterOpUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AssertionsKt;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
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
        Album findNotAlbum = albumService.getAlbumByName("xxxxx");

        // then
        assertThat(findAlbum).isEqualTo(savedAlbum);
        assertEquals(findNotAlbum, null);
    }
    @Test
    void testPhotoCount() {
        Album album = new Album();
        album.setAlbumName("테스트");
        Album savedAlbum = albumRepository.save(album);

        Photo photo1 = new Photo();
        photo1.setFileName("사진1"); photo1.setAlbum(savedAlbum);
        photoRepository.save(photo1);

        Photo photo2 = new Photo();
        photo2.setFileName("사진2"); photo2.setAlbum(savedAlbum);
        photoRepository.save(photo2);

        Photo photo3 = new Photo();
        photo3.setFileName("사진3"); photo3.setAlbum(savedAlbum);
        photoRepository.save(photo3);

        AlbumDto albumDto = albumService.getAlbum(savedAlbum.getAlbumId());
        assertThat(albumDto.getCount()).isEqualTo(3);
    }

    @Test
    void testAlbumDelete() throws IOException {
        // given
        Album album = new Album();
        album.setAlbumName("테스트");
        Photo photo = new Photo();
        photo.setFileName("테스트 사진");
        album.addPhoto(photo);

        Album savedAlbum = albumRepository.save(album);
        Long albumId = savedAlbum.getAlbumId();

        Photo savedPhoto = photoRepository.findByAlbum_AlbumId(savedAlbum.getAlbumId());
        Long photoId = savedPhoto.getPhotoId();

        albumService.createAlbumDirectories(savedAlbum);

        // when
        albumService.deleteAlbum(savedAlbum.getAlbumId());

        // then
        Optional<Album> findAlbum = albumRepository.findById(albumId);
        assertEquals(Optional.empty(), findAlbum);

        Optional<Photo> findPhoto = photoRepository.findById(photoId);
        assertEquals(Optional.empty(), findPhoto);
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

    @Test
    void testChangeAlbumName() throws IOException {
        AlbumDto albumDto = new AlbumDto();
        albumDto.setAlbumName("변경전");
        AlbumDto res = albumService.createAlbum(albumDto);

        Long albumId = res.getAlbumId();
        AlbumDto updateDto = new AlbumDto();
        updateDto.setAlbumName("변경후");
        albumService.changeName(albumId, updateDto);

        AlbumDto updatedDto = albumService.getAlbum(albumId);

        assertEquals("변경후", updatedDto.getAlbumName());
    }
}