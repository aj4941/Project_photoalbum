package com.squarecross.diary.repository;

import com.squarecross.diary.domain.Album;
import com.squarecross.diary.repository.Impl.AlbumRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumRepository
        extends JpaRepository<Album, Long>, AlbumRepositoryCustom {

    Album findByAlbumName(String name);
    void deleteById(Long id);
    int countByAlbumId(Long id);
}
