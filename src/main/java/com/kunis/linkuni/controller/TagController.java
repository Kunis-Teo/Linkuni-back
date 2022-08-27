package com.kunis.linkuni.controller;

import com.kunis.linkuni.dto.ResponseDTO;
import com.kunis.linkuni.dto.category.CategoryListDTO;
import com.kunis.linkuni.dto.tag.TagDTO;
import com.kunis.linkuni.dto.tag.TagListDTO;
import com.kunis.linkuni.entity.Tag;
import com.kunis.linkuni.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/tag")
public class TagController {

    private final TagService tagService;

    @GetMapping("/list")
    public ResponseEntity<TagListDTO> registerTag(@AuthenticationPrincipal String userId) {
        TagListDTO tagListDTO = tagService.getTagList(userId);

        return ResponseEntity.ok(tagListDTO);
    }

    @PostMapping("/add")
    public ResponseEntity<?> registerTag(@AuthenticationPrincipal String userId, @RequestBody TagDTO tagDTO) {
        try {
            Tag tag = Tag.builder()
                    .name(tagDTO.getName())
                    .createdAt(LocalDateTime.now())
                    .build();

            Tag registredTag = tagService.registerTag(tag, userId);

            return ResponseEntity.ok(tagDTO);
        } catch (Exception e) {
            // 예외가 나는 경우 bad 리스폰스 리턴.
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }
}