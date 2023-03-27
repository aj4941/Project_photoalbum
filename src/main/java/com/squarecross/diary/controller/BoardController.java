package com.squarecross.diary.controller;

import com.squarecross.diary.domain.Board;
import com.squarecross.diary.dto.BoardDto;
import com.squarecross.diary.dto.UserDto;
import com.squarecross.diary.mapper.BoardMapper;
import com.squarecross.diary.service.BoardService;
import com.squarecross.diary.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
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

    @GetMapping("/{id}")
    public String getBoard(@PathVariable("id") Long boardId, Model model) {
        Board board = boardService.findBoard(boardId);
        BoardDto boardDto = BoardMapper.boardToDto(board);
        model.addAttribute("boardDto", boardDto);
        return "board/board";
    }

    @GetMapping("/new")
    public String saveBoard(@ModelAttribute BoardDto boardDto) {
        return "board/save";
    }

    @PostMapping("/new")
    public String saveBoard(@Valid @ModelAttribute BoardDto boardDto,
                            BindingResult result,
                            HttpSession session) {

        if (result.hasErrors()) {
            return "board/save";
        }

        UserDto userDto = (UserDto) session.getAttribute("userDto");
        boardService.saveBoard(boardDto, userDto);
        return "redirect:/board";
    }

    @GetMapping("/edit/{id}")
    public String editBoard(@PathVariable("id") Long boardId, Model model) {
        Board board = boardService.findBoard(boardId);
        BoardDto boardDto = BoardMapper.boardToDto(board);
        model.addAttribute("boardDto", boardDto);
        return "board/save";
    }

    @PostMapping("/edit/{id}")
    public String editBoard(@ModelAttribute BoardDto boardDto,
                              @PathVariable("id") Long boardId,
                              Model model) {
        boardService.updateBoard(boardId, boardDto);
        model.addAttribute("boardDto", boardDto);
        return "board/board";
    }

    @DeleteMapping("/{id}")
    public String deleteBoard(@PathVariable("id") Long boardId) {
        System.out.println("boardId = " + boardId);
        boardService.deleteBoard(boardId);
        return "redirect:/board";
    }
}
