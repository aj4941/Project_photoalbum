package com.squarecross.photoalbum.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "album", schema = "photo_album", uniqueConstraints = {@UniqueConstraint(columnNames = "album_id")})
@Getter @Setter
public class Album {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "album_id", unique = true, nullable = false)
    private Long albumId;
    @Column(name = "album_name", unique = false, nullable = false)
    private String albumName;
    @Column(name = "created_at", unique = false, nullable = true)
    @CreationTimestamp
    private Date createdAt;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "album") // 매핑관계 연결시에는 항상 LAZY로 연결한다.
    private List<Photo> photos = new ArrayList<>();
}
