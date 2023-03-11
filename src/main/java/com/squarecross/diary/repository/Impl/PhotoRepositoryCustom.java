package com.squarecross.diary.repository.Impl;

import com.squarecross.diary.domain.Photo;

import java.util.List;

public interface PhotoRepositoryCustom {

    List<Photo> searchTop4(Long Id);
    List<Photo> search(String keyword, String sort, String orderBy);
}
