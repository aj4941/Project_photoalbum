package com.squarecross.photoalbum.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

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
}
