package com.kunis.linkuni.controller;

import com.kunis.linkuni.dto.ResponseDTO;
import com.kunis.linkuni.dto.url.UrlAddDto;
import com.kunis.linkuni.entity.Url;
import com.kunis.linkuni.service.CategoryService;
import com.kunis.linkuni.service.UrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/url")
@RequiredArgsConstructor
public class UrlController {

    private final UrlService urlService;
    private final CategoryService categoryService;

    @PostMapping("/add")
    public ResponseEntity<?> addUrl(@AuthenticationPrincipal String userId, @RequestBody UrlAddDto urlAddDto){
        try {
            Url url = Url.builder()
                    .url(urlAddDto.getUrl())
                    .memo(urlAddDto.getMemo())
                    .category(categoryService.findByIdFetchjoin(urlAddDto.getCategory_id()))
                    .isStarred(false)
                    .isWatched(false)
                    .createAt(LocalDateTime.now())
                    .build();
            Url addUrl = urlService.add(url, userId);
            urlService.addTagToUrl(urlAddDto.getTagList(), addUrl);

            return ResponseEntity.ok(addUrl); // 수정 필요
        } catch (Exception e) {
            // 예외가 나는 경우 bad 리스폰스 리턴.
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }
}
