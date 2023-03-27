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


}
