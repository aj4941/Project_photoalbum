package com.squarecross.photoalbum.service;

import com.squarecross.photoalbum.domain.Photo;
import com.squarecross.photoalbum.dto.PhotoDto;
import com.squarecross.photoalbum.mapper.PhotoMapper;
import com.squarecross.photoalbum.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class PhotoService {

    @Autowired
    private PhotoRepository photoRepository;

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
}
