package com.squarecross.photoalbum.service;

import com.squarecross.photoalbum.domain.Album;
import com.squarecross.photoalbum.domain.Photo;
import com.squarecross.photoalbum.dto.PhotoDto;
import com.squarecross.photoalbum.mapper.PhotoMapper;
import com.squarecross.photoalbum.repository.AlbumRepository;
import com.squarecross.photoalbum.repository.PhotoRepository;
import org.apache.tika.Tika;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.persistence.EntityNotFoundException;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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
        Album album = res.get();
        String fileName = file.getOriginalFilename(); // 파일 이름을 얻는 메서드
        int fileSize = (int) file.getSize(); // 파일 크기를 얻는 메서드

        fileName = getNextFileName(fileName, albumId); // 같은 파일명이 존재하면 NextFileName 반환
        saveFile(file, albumId, fileName);

        Photo photo = new Photo();
        photo.setOriginalUrl("/photos/original/" + albumId + "/" + fileName);
        photo.setThumbUrl("/photos/thumb/" + albumId + "/" + fileName);
        photo.setFileName(fileName); photo.setFileSize(fileSize);
        album.addPhoto(photo);

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

    public List<PhotoDto> movePhotos(
            Long fromAlbumId, Long toAlbumId, List<Long> photoIds) throws IOException {
        // 1. photo의 fileName 을 가져오고 같은 이름을 toAlbumId에서 체킹하여 fileName 수정
        // 2. photo의 original, thumb 주소를 toAlbumId에 맞춰서 저장
        // 3. photo의 원본을 File들로 저장하고 toAlbumId로 옮기기
        // 4. fromAlbumId에 있던 원본 파일들 삭제
        List<Photo> photos = new ArrayList<>();
        for (Long photoId : photoIds) {
            Optional<Photo> res = photoRepository.findById(photoId);
            if (res.isPresent()) {
                // photo를 얻어내고 초기 주소에 대한 파일을 얻음
                Photo photo = res.get();
                File originalFile = new File(PATH_PREFIX + photo.getOriginalUrl());
                File thumbFile = new File(PATH_PREFIX + photo.getThumbUrl());

                // 파일 이름을 받은 후 겹치는 이름을 피해서 새 이름을 만든 후 photo의 url 변경
                String fileName = photo.getFileName();
                fileName = getNextFileName(fileName, toAlbumId);
                photo.setOriginalUrl("/photos/original/" + toAlbumId + "/" + fileName);
                photo.setThumbUrl("/photos/thumb/" + toAlbumId + "/" + fileName);

                // 변경된 url에 대한 파일도 받음
                File originalChangeFile = new File(PATH_PREFIX + photo.getOriginalUrl());
                File thumbChangeFile = new File(PATH_PREFIX + photo.getThumbUrl());

                // 기존에 있던 사진 내용을 변경 주소로 옮김
                Files.copy(originalFile.toPath(), originalChangeFile.toPath());
                Files.copy(thumbFile.toPath(), thumbChangeFile.toPath());

                // 원본 주소에 있는 파일 삭제
                deleteFiles(String.valueOf(originalFile.toPath()));
                deleteFiles(String.valueOf(thumbFile.toPath()));

                // 변경된 사진 추가
                photos.add(photo);
            } else {
                throw new EntityNotFoundException("에러");
            }
        }
        return PhotoMapper.convertToDtoList(photos);
    }

    private void deleteFiles(String url) throws IOException {
        Files.delete(Path.of(url));
    }

    public List<PhotoDto> deletePhotos(List<Long> photoIds) throws IOException {
        List<Photo> photos = new ArrayList<>();
        for (Long photoId : photoIds) {
            Optional<Photo> res = photoRepository.findById(photoId);
            if (res.isPresent()) {
                Photo photo = res.get(); photos.add(photo);
                deleteFiles(PATH_PREFIX + photo.getOriginalUrl());
                deleteFiles(PATH_PREFIX + photo.getThumbUrl());
                photoRepository.deleteById(photoId);
            } else {
                throw new EntityNotFoundException("에러");
            }
        }
        return PhotoMapper.convertToDtoList(photos);
    }
}
