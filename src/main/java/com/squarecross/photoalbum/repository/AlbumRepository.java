package com.squarecross.photoalbum.repository;

import com.squarecross.photoalbum.domain.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
    Album findByAlbumName(String name);
    List<Album> findByAlbumNameContainingOrderByAlbumNameAsc(String keyword);
    List<Album> findByAlbumNameContainingOrderByAlbumNameDesc(String keyword);
    List<Album> findByAlbumNameContainingOrderByCreatedAtDesc(String keyword); // 최신순 정렬, 과거 순이면 asc
    List<Album> findByAlbumNameContainingOrderByCreatedAtAsc(String keyword);
}
