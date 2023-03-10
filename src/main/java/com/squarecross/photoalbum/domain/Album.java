package com.squarecross.photoalbum.domain;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Entity
@Table(name = "album", schema = "photo_album",
        uniqueConstraints = { @UniqueConstraint(columnNames = "album_id") })
@Getter @Setter
public class Album {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "album_id", unique = true, nullable = false)
    private Long albumId;

    private String albumName;

    private Date createdAt;

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Photo> photos = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Album() { }

    public void addPhoto(Photo photo) {
        photos.add(photo);
        photo.setAlbum(this);
    }
}

