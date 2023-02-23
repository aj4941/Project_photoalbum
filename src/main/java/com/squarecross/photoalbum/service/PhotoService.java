package com.squarecross.photoalbum.service;

import com.squarecross.photoalbum.domain.Album;
import com.squarecross.photoalbum.domain.Photo;
import com.squarecross.photoalbum.dto.PhotoDto;
import com.squarecross.photoalbum.mapper.PhotoMapper;
import com.squarecross.photoalbum.repository.AlbumRepository;
import com.squarecross.photoalbum.repository.PhotoRepository;
import org.apache.tika.Tika;
import org.apache.tika.detect.Detector;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.persistence.EntityNotFoundException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.squarecross.photoalbum.service.Constants.*;

@Service
public class PhotoService {

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private AlbumRepository albumRepository;

    private final String original_path = PATH_PREFIX + "/photos/original";
    private final String thumb_path = PATH_PREFIX + "/photos/thumb";

    public PhotoDto getPhoto(Long photoId) {
        Optional<Photo> res = photoRepository.findById(photoId);
        if (res.isPresent()) {
            Photo photo = res.get();
            PhotoDto photoDto = PhotoMapper.convertToDto(photo);
            return photoDto;
        } else {
            throw new EntityNotFoundException(
                    String.format("사진 파일이 아이디 %d번으로 찾지 못했습니다.", photoId));
        }
    }

    public PhotoDto savePhoto(MultipartFile file, Long albumId) throws IOException {
        Optional<Album> res = albumRepository.findById(albumId);
        if (res.isEmpty()) {
            throw new EntityNotFoundException("앨범이 존재하지 않습니다.");
        }

        String fileName = file.getOriginalFilename(); // 파일 이름을 얻는 메서드
        int fileSize = (int) file.getSize(); // 파일 크기를 얻는 메서드

        fileName = getNextFileName(fileName, albumId); // 같은 파일명이 존재하면 NextFileName 반환
        saveFile(file, albumId, fileName);

        Photo photo = new Photo();
        photo.setOriginalUrl("/photos/original/" + albumId + "/" + fileName);
        photo.setThumbUrl("/photos/thumb/" + albumId + "/" + fileName);
        photo.setFileName(fileName); photo.setFileSize(fileSize);
        photo.setAlbum(res.get());
        Photo createdPhoto = photoRepository.save(photo);
        return PhotoMapper.convertToDto(createdPhoto);
    }

    private String getNextFileName(String fileName, Long albumId) {
        String fileNameNoExt = StringUtils.stripFilenameExtension(fileName); // 파일명에서 확장자 제거
        String ext = StringUtils.getFilenameExtension(fileName); // 파일명에서 확장자만 얻어내기

        Optional<Photo> res = photoRepository.findByFileNameAndAlbum_AlbumId(fileName, albumId);

        int count = 2;
        while (res.isPresent()) {
            fileName = String.format("%s (%d).%s", fileNameNoExt, count, ext);
            res = photoRepository.findByFileNameAndAlbum_AlbumId(fileName, albumId);
            count++;
        }

        return fileName;
    }

    private void saveFile(MultipartFile file, Long albumId, String fileName) throws IOException {
        String filePath = albumId + "/" + fileName;
        // 원본 이미지를 original 사진 경로에 저장
        Files.copy(file.getInputStream(), Paths.get(original_path + "/" + filePath));

        // 이미지 사이즈를 THUMB_SIZE * THUMB_SIZE로 수정
        BufferedImage thumbImg = Scalr.resize(ImageIO.read(file.getInputStream()),
                THUMB_SIZE, THUMB_SIZE);

        // Resize된 썸네일 사진을 넣기 위해 파일을 만들고 썸네일 이미지 저장
        File thumbFile = new File(thumb_path + "/" + filePath);
        String ext = StringUtils.getFilenameExtension(fileName);
        if (ext == null) {
            throw new IllegalArgumentException("No Extention");
        }
        ImageIO.write(thumbImg, ext, thumbFile);
    }

    public boolean checkFile(MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        String mimeType = new Tika().detect(inputStream);
        inputStream.close();
        return mimeType.equals("jpg");
    }

    public File getImageFile(Long photoId) {
        Optional<Photo> res = photoRepository.findById(photoId);
        if (res.isEmpty()) {
            throw new EntityNotFoundException(
                    String.format("사진을 ID로 찾을 수 없습니다."));
        }
        return new File(PATH_PREFIX + res.get().getOriginalUrl());
    }

    public List<PhotoDto> getPhotoList(String keyword, String sort, String orderBy) {
        List<Photo> photos;
        if (Objects.equals(sort, "byDate")) {
            if (Objects.equals(orderBy, "asc")) {
                photos = photoRepository.findByFileNameContainingOrderByUploadedAtDesc(keyword);
            } else {
                photos = photoRepository.findByFileNameContainingOrderByUploadedAtAsc(keyword);
            }
        } else if (Objects.equals(sort, "byName")){
            if (Objects.equals(orderBy, "asc")) {
                photos = photoRepository.findByFileNameContainingOrderByFileNameAsc(keyword);
            } else {
                photos = photoRepository.findByFileNameContainingOrderByFileNameDesc(keyword);
            }
        } else {
            throw new EntityNotFoundException("알 수 없는 정렬 기준입니다.");
        }

        List<PhotoDto> photoDtos = PhotoMapper.convertToDtoList(photos);
        return photoDtos;
    }
}
