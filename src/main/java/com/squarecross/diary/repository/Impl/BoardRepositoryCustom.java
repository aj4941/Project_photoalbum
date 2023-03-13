package com.squarecross.diary.repository.Impl;

import com.squarecross.diary.domain.Board;

import java.util.List;

public interface BoardRepositoryCustom {

    List<Board> search(String Id);
}
