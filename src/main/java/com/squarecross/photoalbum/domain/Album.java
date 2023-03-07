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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "album", orphanRemoval = true)
    private List<Photo> photos = new ArrayList<>();

    public Album() { }

    public void addPhoto(Photo photo) {
        photos.add(photo);
        photo.setAlbum(this);
    }
}

