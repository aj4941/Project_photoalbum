package com.squarecross.photoalbum.repository;

import com.squarecross.photoalbum.domain.Album;
import com.squarecross.photoalbum.domain.Photo;
import com.squarecross.photoalbum.repository.Impl.PhotoRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long>, PhotoRepositoryCustom {

    int countByAlbum_AlbumId(Long AlbumId);
    Photo findByAlbum_AlbumId(Long albumId);
    Optional<Photo> findByFileNameAndAlbum_AlbumId(String photoName, Long albumId);

    List<Photo> findByFileNameContainingOrderByUploadedAtDesc(String keyword);
    List<Photo> findByFileNameContainingOrderByUploadedAtAsc(String keyword);
    List<Photo> findByFileNameContainingOrderByFileNameDesc(String keyword);
    List<Photo> findByFileNameContainingOrderByFileNameAsc(String keyword);
}
