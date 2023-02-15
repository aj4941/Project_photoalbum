package com.squarecross.photoalbum.domain;

import javax.persistence.*;
import java.util.Date;
@Entity
@Table(name = "Photo", schema = "photo_album", uniqueConstraints = {@UniqueConstraint(columnNames = "photo_id")})
public class Photo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "photo_id", unique = true, nullable = false)
    private Long photo_id;
    private String file_name;
    private String thumb_url;
    private String original_url;
    private int file_size;
    private Date uploaded_at;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id")
    private Album album;
}