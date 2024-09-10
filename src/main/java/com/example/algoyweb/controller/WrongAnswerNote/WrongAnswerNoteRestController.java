package com.example.algoyweb.controller.WrongAnswerNote;

import com.example.algoyweb.model.dto.WrongAnswerNote.WrongAnswerNoteDTO;
import com.example.algoyweb.service.WrongAnswerNote.WrongAnswerNoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/algoy/commit")
@RequiredArgsConstructor
public class WrongAnswerNoteRestController {

    private final WrongAnswerNoteService service;

    // 페이지네이션을 지원하는 오답노트 목록 조회
    @GetMapping
    public ResponseEntity<Page<WrongAnswerNoteDTO>> getAllWrongAnswerNotes(@RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
        Page<WrongAnswerNoteDTO> notes = service.findAllWithPagination(page, size);
        return ResponseEntity.ok(notes);
    }

    // ID로 오답노트 조회
    @GetMapping("/{id}")
    public ResponseEntity<WrongAnswerNoteDTO> getWrongAnswerNoteById(@PathVariable Long id) {
        Optional<WrongAnswerNoteDTO> note = service.findById(id);
        return note.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 오답노트 생성
    @PostMapping
    public ResponseEntity<WrongAnswerNoteDTO> createWrongAnswerNote(@RequestBody WrongAnswerNoteDTO dto) {
        // 게시글을 저장
        WrongAnswerNoteDTO savedNote = service.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedNote);
    }

    // 오답노트 수정
    @PutMapping("/{id}")
    public ResponseEntity<WrongAnswerNoteDTO> updateWrongAnswerNote(@PathVariable Long id, @RequestBody WrongAnswerNoteDTO dto) {
        WrongAnswerNoteDTO updatedNote = service.update(id, dto);
        return ResponseEntity.ok(updatedNote);
    }

    // 오답노트 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWrongAnswerNote(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}