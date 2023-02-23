package com.squarecross.photoalbum.controller;

import com.squarecross.photoalbum.domain.Photo;
import com.squarecross.photoalbum.dto.PhotoDto;
import com.squarecross.photoalbum.repository.PhotoRepository;
import com.squarecross.photoalbum.service.PhotoService;
import org.apache.xmlbeans.impl.piccolo.io.IllegalCharException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/albums/{albumId}/photos")
public class PhotoController {

    @Autowired
    private PhotoService photoService;

    @GetMapping("/{photoId}")
    public ResponseEntity<PhotoDto> getPhotoInfo(@PathVariable Long photoId) {
        PhotoDto photoDto = photoService.getPhoto(photoId);
        return new ResponseEntity<>(photoDto, HttpStatus.OK);
    }

    // PhotoController
    @PostMapping
    public ResponseEntity<List<PhotoDto>> uploadPhotos(
            @PathVariable("albumId") Long albumId,
            @RequestParam("photos") MultipartFile[] files) throws IOException {
        // photos 입력 필드로 업로드된 파일이 files 배열 객체 안에 저장

        List<PhotoDto> photos = new ArrayList<>();
        for (MultipartFile file : files) {
            PhotoDto photoDto = photoService.savePhoto(file, albumId);
            photos.add(photoDto);
            if (!photoService.checkFile(file)) {
                throw new IllegalArgumentException("이미지 파일이 아닙니다.");
            }
        }

        return new ResponseEntity<>(photos, HttpStatus.OK);
    }
}
