package com.squarecross.diary.service;

import com.squarecross.diary.domain.Album;
import com.squarecross.diary.domain.Photo;
import com.squarecross.diary.dto.PhotoDto;
import com.squarecross.diary.repository.AlbumRepository;
import com.squarecross.diary.repository.PhotoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PhotoServiceTest {
}