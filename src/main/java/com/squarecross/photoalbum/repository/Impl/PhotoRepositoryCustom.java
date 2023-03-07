package com.squarecross.photoalbum.repository.Impl;

import com.squarecross.photoalbum.domain.Photo;

import java.util.List;

public interface PhotoRepositoryCustom {

    List<Photo> searchTop4(Long Id);
    List<Photo> search(String keyword, String sort, String orderBy);
}
