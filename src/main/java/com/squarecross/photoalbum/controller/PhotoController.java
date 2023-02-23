package com.squarecross.photoalbum.controller;

import com.squarecross.photoalbum.domain.Photo;
import com.squarecross.photoalbum.dto.PhotoDto;
import com.squarecross.photoalbum.repository.PhotoRepository;
import com.squarecross.photoalbum.service.PhotoService;
import org.apache.tika.io.IOUtils;
import org.apache.xmlbeans.impl.piccolo.io.IllegalCharException;
import org.hibernate.engine.jdbc.StreamUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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

    @GetMapping("/download")
    public void downloadPhotos(@RequestParam("photoIds") Long[] photoIds,
                               HttpServletResponse response) throws IOException {

        if (photoIds.length == 1) {
            OutputStream outputStream = response.getOutputStream();
            File file = photoService.getImageFile(photoIds[0]);
            // Response Outputstream에 기록하는 것이 전송을 의미
            IOUtils.copy(new FileInputStream(file), outputStream);
            outputStream.close();
        } else { // 여러장 다운받을 경우 Zip으로 다운로드 받는다.
            ZipOutputStream outputStream = new ZipOutputStream(response.getOutputStream());
            for (Long photoId : photoIds) {
                File file = photoService.getImageFile(photoId);
                outputStream.putNextEntry(new ZipEntry(file.getName()));
                IOUtils.copy(new FileInputStream(file), outputStream);
            }
            outputStream.close();
        }
    }
}
