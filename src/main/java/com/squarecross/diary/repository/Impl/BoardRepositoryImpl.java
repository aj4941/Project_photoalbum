package com.squarecross.diary.repository.Impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.squarecross.diary.domain.Board;

import javax.persistence.EntityManager;
import java.util.List;

import static com.squarecross.diary.domain.QBoard.*;

public class BoardRepositoryImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public BoardRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<Board> search(String Id) {
        return queryFactory
                .selectFrom(board)
                .where(board.user.loginId.eq(Id))
                .fetch();
    }
}
