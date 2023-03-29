package com.squarecross.diary.controller;

import com.squarecross.diary.dto.AlbumDto;
import com.squarecross.diary.dto.UserDto;
import com.squarecross.diary.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/album")
public class AlbumController {

    @Autowired
    private AlbumService albumService;

    @GetMapping
    public String getAlbumList(Model model, HttpSession session) {
        UserDto userDto = (UserDto) session.getAttribute("userDto");
        List<AlbumDto> albumDtos = albumService.getAlbumList(userDto.getLoginId());
        AlbumDto albumDto = new AlbumDto();
        model.addAttribute("albumDtos", albumDtos);
        return "album/albumList";
    }

//    @PostMapping("/new")
//    public String saveAlbum(@ModelAttribute AlbumDto albumDto,
//                            BindingResult result,
//                            HttpSession session) {

//        if (result.hasErrors()) {
            // 모달창을 다시 띄우고 오류 메시지 출력
//        }
//    }

}
