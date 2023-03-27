package com.squarecross.diary.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
@Entity
@Table(name = "Photo")
@Getter @Setter
public class Photo {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "photo_id", unique = true, nullable = false)
    private Long photoId; // camel case (java 코드에서는 camel, DB에서는 snake case를 주로 사용)

    private String fileName;

    private String thumbUrl;
    private String originalUrl;
    private int fileSize;
    private Date uploadedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id")
    private Album album;
}