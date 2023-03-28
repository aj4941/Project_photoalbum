package com.squarecross.diary.apicontroller;

import com.squarecross.diary.domain.Board;
import com.squarecross.diary.dto.BoardDto;
import com.squarecross.diary.dto.UserDto;
import com.squarecross.diary.mapper.BoardMapper;
import com.squarecross.diary.service.BoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/board")
@Slf4j
public class BoardApiController {

    @Autowired
    private BoardService boardService;

    @GetMapping
    public ResponseEntity<List<BoardDto>> getBoardList(HttpSession session) {
        UserDto userDto = (UserDto) session.getAttribute("userDto");
        List<BoardDto> boardDtos = boardService.getBoards(userDto.getLoginId());
        return new ResponseEntity<>(boardDtos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBoard(@PathVariable("id") Long boardId) {
        Board board = boardService.findBoard(boardId);
        BoardDto boardDto = BoardMapper.boardToDto(board);
        return new ResponseEntity<>(boardDto, HttpStatus.OK);
    }

    @GetMapping("/new")
    public ResponseEntity<String> saveBoard() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/new")
    public ResponseEntity<?> saveBoard(@Valid @RequestBody BoardDto boardDto,
                                       BindingResult result,
                                       HttpSession session) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(result.getAllErrors(), HttpStatus.BAD_REQUEST);
        }

        UserDto userDto = (UserDto) session.getAttribute("userDto");
        boardService.saveBoard(boardDto, userDto);

        return new ResponseEntity<>(boardDto, HttpStatus.OK);
    }

    @GetMapping("/edit/{id}")
    public ResponseEntity<BoardDto> editBoard(@PathVariable("id") Long boardId) {
        Board board = boardService.findBoard(boardId);
        BoardDto boardDto = BoardMapper.boardToDto(board);
        return new ResponseEntity<>(boardDto, HttpStatus.OK);
    }

    @PostMapping("/edit/{id}")
    public ResponseEntity<?> editBoard(@Valid @RequestBody BoardDto boardDto,
                                              BindingResult result,
                                              @PathVariable("id") Long boardId) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(result.getAllErrors(), HttpStatus.BAD_REQUEST);
        }

        boardService.updateBoard(boardId, boardDto);
        return new ResponseEntity<>(boardDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBoard(@PathVariable("id") Long boardId) {
        boardService.deleteBoard(boardId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
