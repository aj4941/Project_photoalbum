package com.squarecross.diary.repository;

import com.squarecross.diary.domain.Photo;
import com.squarecross.diary.repository.Impl.PhotoRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long>, PhotoRepositoryCustom {

    int countByAlbum_AlbumId(Long AlbumId);
    Photo findByAlbum_AlbumId(Long albumId);
    Optional<Photo> findByFileNameAndAlbum_AlbumId(String photoName, Long albumId);
}
