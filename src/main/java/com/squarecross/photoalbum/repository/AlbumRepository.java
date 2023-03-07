package com.squarecross.photoalbum.repository;

import com.squarecross.photoalbum.domain.Album;
import com.squarecross.photoalbum.repository.Impl.AlbumRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumRepository
        extends JpaRepository<Album, Long>, AlbumRepositoryCustom {

    Album findByAlbumName(String name);
    void deleteById(Long id);
}
