package com.squarecross.diary.controller.view;

import com.squarecross.diary.dto.BoardDto;
import com.squarecross.diary.dto.UserDto;
import com.squarecross.diary.mapper.BoardMapper;
import com.squarecross.diary.service.BoardService;
import com.squarecross.diary.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/board")
@Slf4j
public class BoardController {

    @Autowired
    private BoardService boardService;

    @GetMapping
    public String getBoardList(Model model, HttpSession session) {
        UserDto userDto = (UserDto) session.getAttribute("userDto");
        List<BoardDto> boardDtos = boardService.getBoard(userDto.getLoginId());
        model.addAttribute("boardDtos", boardDtos);
        return "board/boardList";
    }

    @GetMapping("/write")
    public String writeBoard(@ModelAttribute BoardDto boardDto) {
        return "board/write";
    }

    @PostMapping("/write")
    public String savePost(@ModelAttribute BoardDto boardDto, HttpSession session) {
        UserDto userDto = (UserDto) session.getAttribute("userDto");
        boardService.saveBoard(boardDto, userDto);
        return "redirect:/board";
    }
}
