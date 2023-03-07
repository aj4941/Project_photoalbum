package com.squarecross.photoalbum.repository.Impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.squarecross.photoalbum.domain.Photo;
import com.squarecross.photoalbum.domain.QPhoto;

import javax.persistence.EntityManager;
import java.util.List;

import static com.squarecross.photoalbum.domain.QPhoto.*;

public class PhotoRepositoryImpl implements PhotoRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public PhotoRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<Photo> searchTop4(Long Id) {
        return queryFactory
                .selectFrom(photo)
                .where(photo.album.albumId.eq(Id))
                .orderBy(photo.uploadedAt.desc())
                .limit(4)
                .fetch();
    }
}
