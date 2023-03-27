package com.squarecross.diary.controller;

import com.squarecross.diary.domain.Board;
import com.squarecross.diary.dto.BoardDto;
import com.squarecross.diary.dto.UserDto;
import com.squarecross.diary.mapper.BoardMapper;
import com.squarecross.diary.service.BoardService;
import com.squarecross.diary.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
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
        List<BoardDto> boardDtos = boardService.getBoards(userDto.getLoginId());
        model.addAttribute("boardDtos", boardDtos);
        return "board/boardList";
    }

    @GetMapping("/write")
    public String writeBoard(@ModelAttribute BoardDto boardDto) {
        return "board/write";
    }

    @PostMapping("/write")
    public String saveBoard(@Valid @ModelAttribute BoardDto boardDto,
                            BindingResult result,
                            HttpSession session) {
        if (result.hasErrors()) {
            return "board/write";
        }

        UserDto userDto = (UserDto) session.getAttribute("userDto");
        boardService.saveBoard(boardDto, userDto);
        return "redirect:/board";
    }

    @GetMapping("/{id}")
    public String readBoard(@PathVariable("id") Long boardId, Model model) {
        Board board = boardService.findBoard(boardId);
        BoardDto boardDto = BoardMapper.boardToDto(board);
        model.addAttribute("boardDto", boardDto);
        return "board/read";
    }

    @GetMapping("/update/{id}")
    public String updateBoard(@PathVariable("id") Long boardId, Model model) {
        Board board = boardService.findBoard(boardId);
        BoardDto boardDto = BoardMapper.boardToDto(board);
        model.addAttribute("boardDto", boardDto);
        return "board/write";
    }

    @PostMapping("/update/{id}")
    public String updateBoard(@ModelAttribute BoardDto boardDto,
                              @PathVariable("id") Long boardId,
                              Model model) {
        boardService.updateBoard(boardId, boardDto);
        model.addAttribute("boardDto", boardDto);
        return "board/read";
    }

    @GetMapping("/delete/{id}")
    public String deleteBoard(@PathVariable("id") Long boardId) {
        boardService.deleteBoard(boardId);
        return "redirect:/board";
    }
}
