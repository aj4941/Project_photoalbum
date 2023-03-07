package com.squarecross.photoalbum.repository;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.squarecross.photoalbum.domain.Album;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.squarecross.photoalbum.domain.QAlbum.*;

public class AlbumRepositoryImpl implements AlbumRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public AlbumRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<Album> search(String keyword, String sort, String orderBy) {
        return queryFactory
                .selectFrom(album)
                .where(album.albumName.like("%" + keyword + "%"))
                .orderBy(sortFunc(sort, orderBy))
                .fetch();
    }

    private OrderSpecifier<?> sortFunc(String sort, String orderBy) {
        if (Objects.equals(sort, "byDate")) {
            return Objects.equals(orderBy, "asc") ?
                        album.createdAt.asc() : album.createdAt.desc();
        } else {
            return Objects.equals(orderBy, "asc") ?
                        album.albumName.asc() : album.albumName.desc();
        }
    }
}
