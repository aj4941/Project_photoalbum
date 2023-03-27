package com.squarecross.diary.controller;

import com.squarecross.diary.dto.AlbumDto;
import com.squarecross.diary.dto.UserDto;
import com.squarecross.diary.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/album")
public class AlbumController {

    @Autowired
    AlbumService albumService;

    @GetMapping
    public String getAlbumList(Model model,
                            HttpSession session) {
        UserDto userDto = (UserDto) session.getAttribute("userDto");
        List<AlbumDto> albumDtos = albumService.getAlbumList(userDto.getLoginId());
        model.addAttribute("albumDtos", albumDtos);
        return "album/album";
    }

//    @GetMapping("/create")
//    public String createAlbum(@ModelAttribute AlbumDto albumDto) {
//
//    }
}
