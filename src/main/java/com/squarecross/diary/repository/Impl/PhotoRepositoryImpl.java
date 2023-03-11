package com.squarecross.diary.repository.Impl;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.squarecross.diary.domain.Photo;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Objects;

import static com.squarecross.diary.domain.QPhoto.*;

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

    public List<Photo> search(String keyword, String sort, String orderBy) {
        return queryFactory
                .selectFrom(photo)
                .where(photo.fileName.like("%" + keyword + "%"))
                .orderBy(sortFunc(sort, orderBy))
                .fetch();
    }

    private OrderSpecifier<?> sortFunc(String sort, String orderBy) {
        if (Objects.equals(sort, "byDate")) {
            return Objects.equals(orderBy, "asc") ?
                    photo.uploadedAt.asc() : photo.uploadedAt.desc();
        } else {
            return Objects.equals(orderBy, "asc") ?
                    photo.fileName.asc() : photo.fileName.desc();
        }
    }
}
