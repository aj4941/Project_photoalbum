package com.squarecross.diary.service;

import com.squarecross.diary.domain.Album;
import com.squarecross.diary.domain.Photo;
import com.squarecross.diary.dto.PhotoDto;
import com.squarecross.diary.mapper.PhotoMapper;
import com.squarecross.diary.repository.AlbumRepository;
import com.squarecross.diary.repository.PhotoRepository;
import org.apache.tika.Tika;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.persistence.EntityNotFoundException;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.squarecross.diary.service.Constants.*;

@Service
@Transactional
public class PhotoService {

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private AlbumRepository albumRepository;


}
