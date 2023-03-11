package com.squarecross.diary.repository.Impl;

import com.squarecross.diary.domain.Album;

import java.util.List;

public interface AlbumRepositoryCustom {

    public List<Album> search(String keyword, String sort, String orderBy);
}
