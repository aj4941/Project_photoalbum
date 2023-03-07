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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.squarecross.photoalbum.service.Constants.PATH_PREFIX;
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

        Photo photo1 = new Photo();
        photo1.setFileName("사진1");

        Photo photo2 = new Photo();
        photo2.setFileName("사진2");

        Photo photo3 = new Photo();
        photo3.setFileName("사진3");

        album.addPhoto(photo1); album.addPhoto(photo2); album.addPhoto(photo3);
        Album savedAlbum = albumRepository.save(album); // cascade.ALL 이므로 photo도 저장됨

        AlbumDto albumDto = albumService.getAlbum(savedAlbum.getAlbumId());
        assertThat(albumDto.getCount()).isEqualTo(3);
    }

    @Test
    void testAlbumDelete() throws IOException {

        // given
        long albumId = 2;

        // when
        albumService.deleteAlbum(albumId);
        Path path = Paths.get(PATH_PREFIX + "/photos/original/" + albumId);
        Path thumbPath = Paths.get(PATH_PREFIX + "/photos/thumb/" + albumId);

        // then
        assertTrue(Files.notExists(path));
        assertTrue(Files.notExists(thumbPath));
    }

    @Test
    void testAlbumCreate() throws IOException {
        // given
        AlbumDto albumDto = new AlbumDto();
        albumDto.setAlbumName("테스트");

        // when
        AlbumDto createAlbumDto = albumService.createAlbum(albumDto);
        Path path = Paths.get(PATH_PREFIX + "/photos/original/" + createAlbumDto.getAlbumId());
        Path thumbPath = Paths.get(PATH_PREFIX + "/photos/thumb/" + createAlbumDto.getAlbumId());

        // then
        assertTrue(Files.exists(path));
        assertTrue(Files.exists(thumbPath));
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
        List<Album> resDate = albumRepository.search("aaa", "byDate", "asc");
        assertEquals("aaaa", resDate.get(0).getAlbumName());
        assertEquals("aaab", resDate.get(1).getAlbumName());
        assertEquals(2, resDate.size());

        List<Album> resName = albumRepository.search("aaa", "byName", "desc");
        assertEquals("aaab", resName.get(0).getAlbumName());
        assertEquals("aaaa", resName.get(1).getAlbumName());
        assertEquals(2, resName.size());
    }

    @Test
    void testChangeAlbumName() throws IOException {

        // given
        AlbumDto albumDto = new AlbumDto();
        albumDto.setAlbumName("변경전");
        Long albumId = albumService.createAlbum(albumDto).getAlbumId();

        AlbumDto updateDto = new AlbumDto();
        updateDto.setAlbumName("변경후");

        // when
        AlbumDto findAlbumDto = albumService.changeName(albumId, updateDto);

        // then
        assertEquals("변경후", findAlbumDto.getAlbumName());
    }
}