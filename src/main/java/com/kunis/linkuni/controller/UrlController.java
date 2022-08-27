package com.kunis.linkuni.controller;

import com.kunis.linkuni.dto.ResponseDTO;
import com.kunis.linkuni.dto.url.UrlAddDto;
import com.kunis.linkuni.dto.url.UrlDTO;
import com.kunis.linkuni.dto.url.UrlListDTO;
import com.kunis.linkuni.entity.Url;
import com.kunis.linkuni.service.CategoryService;
import com.kunis.linkuni.service.UrlService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/url")
@RequiredArgsConstructor
public class UrlController {

    private final UrlService urlService;
    private final CategoryService categoryService;

    @PutMapping
    public ResponseEntity<String> updateUrl(@AuthenticationPrincipal String userId,
                                        @RequestBody UrlDTO urlDTO) {

        Url entity = new Url();
        entity.setId(urlDTO.getId());
        entity.setUrl(urlDTO.getUrl());
        entity.setMemo(urlDTO.getMemo());
        //entity.setCategory(urlDTO.getCategory_id());

        List<Url> entities = urlService.update(entity, userId, urlDTO.getCategory_id());

        // (6) ResponseDTO를 리턴한다.
        return ResponseEntity.ok("success");
    }


    @ApiOperation(value = "url 삭제", notes = "url path id인 url삭제")
    @DeleteMapping("/delete/{urlId}")
    public ResponseEntity<String> deleteUrl(@AuthenticationPrincipal String userId,
                                                           @PathVariable String urlId){
        urlService.deleteUrl(userId);

        return ResponseEntity.ok("success delete");
    }

    @ApiOperation(value = "url 북마크", notes = "url이 북마크 상태라면 해제, 아니라면 북마크 설정")
    @GetMapping("/star/{urlId}")
    public ResponseEntity<UrlDTO> setStar(@AuthenticationPrincipal String userId,
                                            @PathVariable String urlId){
        Url url = urlService.setStart(urlId);
        UrlDTO urlDTO = new UrlDTO(url.getId(), url.getUrl(), url.getMemo(), url.getIsStarred(), url.getIsWatched(),
                url.getCreateAt(), url.getWatchedAt(), url.getCategory().getId());

        return ResponseEntity.ok(urlDTO);
    }

    @ApiOperation(value = "선택 카테고리 url 조회", notes = "url path id 카테고리에 속한 url 조회")
    @GetMapping("/list/{categoryId}")
    public ResponseEntity<UrlListDTO> getUrlListByCategory(@AuthenticationPrincipal String userId,
                                                           @PathVariable String categoryId){
        UrlListDTO urlListDTO = urlService.getUrlListByCategory(categoryId);

        return ResponseEntity.ok(urlListDTO);
    }

    @ApiOperation(value = "새로운 url 추가", notes = "카테고리 필수, 테그  선택 사항")
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

