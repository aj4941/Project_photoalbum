package com.squarecross.diary.repository;

import com.squarecross.diary.domain.Board;
import com.squarecross.diary.repository.Impl.BoardRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom {

}
