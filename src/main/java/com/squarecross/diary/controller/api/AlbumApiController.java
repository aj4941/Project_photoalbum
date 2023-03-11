package com.squarecross.diary.controller.api;

import com.squarecross.diary.dto.AlbumDto;
import com.squarecross.diary.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/albums")
public class AlbumApiController {

    @Autowired
    AlbumService albumService;

    @GetMapping("/{albumId}")
    public ResponseEntity<AlbumDto> getAlbum(@PathVariable("albumId") long albumId) {
        AlbumDto album = albumService.getAlbum(albumId);
        return new ResponseEntity<>(album, HttpStatus.OK);
    }

    @GetMapping("/query")
    public ResponseEntity<AlbumDto> getAlbumByQuery(@RequestParam long albumId) {
        AlbumDto album = albumService.getAlbum(albumId);
        return new ResponseEntity<>(album, HttpStatus.OK);
    }

    @PostMapping("/json_body")
    public ResponseEntity<AlbumDto> getAlbumByJson(@RequestBody AlbumDto albumDto) {
        AlbumDto album = albumService.getAlbum(albumDto.getAlbumId());
        return new ResponseEntity<>(album, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<AlbumDto> createAlbum(@RequestBody AlbumDto albumDto) throws IOException {
        AlbumDto savedAlbumDto = albumService.createAlbum(albumDto);
        return new ResponseEntity<>(savedAlbumDto, HttpStatus.OK); // body header status
    }

    @GetMapping
    public ResponseEntity<List<AlbumDto>> getAlbumList(
            @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(value = "sort", required = false, defaultValue = "byDate") String sort,
            @RequestParam(value = "orderBy", required = false, defaultValue = "desc") String orderBy) {
        List<AlbumDto> albumDtos = albumService.getAlbumList(keyword, sort, orderBy);
        return new ResponseEntity<>(albumDtos, HttpStatus.OK);
    }

    @PutMapping("/{albumId}")
    public ResponseEntity<AlbumDto> updateAlbum(
            @PathVariable("albumId") long albumId,
            @RequestBody AlbumDto albumDto) {
        AlbumDto res = albumService.changeName(albumId, albumDto);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @DeleteMapping("/{albumId}")
    public ResponseEntity<Void> deleteAlbum(@PathVariable Long albumId) throws IOException {
        albumService.deleteAlbum(albumId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
