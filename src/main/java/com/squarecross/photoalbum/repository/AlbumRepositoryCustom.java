package com.squarecross.photoalbum.repository;

import com.squarecross.photoalbum.domain.Album;

import java.util.List;

public interface AlbumRepositoryCustom {
    public List<Album> search(String keyword, String sort, String orderBy);
}
