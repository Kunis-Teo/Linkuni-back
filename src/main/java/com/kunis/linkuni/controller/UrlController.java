package com.kunis.linkuni.controller;

import com.kunis.linkuni.dto.ResponseDTO;
import com.kunis.linkuni.dto.tag.TagDTO;
import com.kunis.linkuni.dto.url.*;
import com.kunis.linkuni.entity.Url;
import com.kunis.linkuni.entity.UrlTag;
import com.kunis.linkuni.service.CategoryService;
import com.kunis.linkuni.service.UrlService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/url")
@RequiredArgsConstructor
public class UrlController {

    private final UrlService urlService;
    private final CategoryService categoryService;

    @PutMapping
    public ResponseEntity<UrlDTO> updateUrl(@AuthenticationPrincipal String userId,
                                            @RequestBody UrlUpdateDTO updateDTO) {

        Url entity = Url.builder()
                .id(updateDTO.getId())
                .url(updateDTO.getUrl())
                .memo(updateDTO.getMemo())
                .build();

        Url updateUrl = urlService.update(entity, userId, updateDTO.getCategory_id(), updateDTO.getTagDTOList());

        List<TagDTO> tagDTOList = new ArrayList<>();
        List<UrlTag> urlTagList = updateUrl.getUrlTagList();
        for(int i=0;i<urlTagList.size();i++){
            tagDTOList.add(new TagDTO(
                    urlTagList.get(i).getTag().getId(),
                    urlTagList.get(i).getTag().getName()));
        }

        UrlDTO responseUrlDTO = UrlDTO.builder()
                .id(updateUrl.getId())
                .url(updateUrl.getUrl())
                .memo(updateUrl.getMemo())
                .isStarred(updateUrl.getIsStarred())
                .isWatched(updateUrl.getIsWatched())
                .createAt(updateUrl.getCreateAt())
                .watchedAt(updateUrl.getWatchedAt())
                .category_id(updateUrl.getCategory().getId())
                .tagDTOList(tagDTOList)
                .build();

        return ResponseEntity.ok(responseUrlDTO);
    }

    @ApiOperation(value = "url ??????", notes = "url path id??? url??????")
    @DeleteMapping("/delete/{urlId}")
    public ResponseEntity<String> deleteUrl(@AuthenticationPrincipal String userId,
                                                           @PathVariable String urlId){
        try{
            urlService.deleteUrl(urlId);
            return ResponseEntity.ok("success delete url");

        } catch (Exception e) {

            return ResponseEntity.ok("fail delete url");
        }

    }

    @ApiOperation(value = "url ??????", notes = "???????????? true, watched_at ?????? update")
    @GetMapping("/{urlId}")
    public ResponseEntity<UrlDTO> selectUrl(@AuthenticationPrincipal String userId,
                                          @PathVariable String urlId){
        Url url = urlService.watchUrl(urlId);

        UrlDTO responseUrlDTO = UrlDTO.builder()
                .id(url.getId())
                .url(url.getUrl())
                .memo(url.getMemo())
                .isStarred(url.getIsStarred())
                .isWatched(url.getIsWatched())
                .createAt(url.getCreateAt())
                .watchedAt(url.getWatchedAt())
                .category_id(url.getCategory().getId())
                .build();

        return ResponseEntity.ok(responseUrlDTO);
    }

    @ApiOperation(value = "url ?????????", notes = "url??? ????????? ???????????? ??????, ???????????? ????????? ??????")
    @GetMapping("/star/{urlId}")
    public ResponseEntity<UrlDTO> setStar(@AuthenticationPrincipal String userId,
                                            @PathVariable String urlId){
        Url url = urlService.setStart(urlId);

        UrlDTO responseUrlDTO = UrlDTO.builder()
                .id(url.getId())
                .url(url.getUrl())
                .memo(url.getMemo())
                .isStarred(url.getIsStarred())
                .isWatched(url.getIsWatched())
                .createAt(url.getCreateAt())
                .watchedAt(url.getWatchedAt())
                .category_id(url.getCategory().getId())
                .build();

        return ResponseEntity.ok(responseUrlDTO);
    }

    @ApiOperation(value = "?????? ???????????? url ??????", notes = "url path id ??????????????? ?????? url ??????")
    @GetMapping("/list/{categoryId}")
    public ResponseEntity<UrlListDTO> getUrlListByCategory(@AuthenticationPrincipal String userId,
                                                           @PathVariable String categoryId){
        UrlListDTO urlListDTO = urlService.getUrlListByCategory(categoryId);

        return ResponseEntity.ok(urlListDTO);
    }

    @ApiOperation(value = "?????? url ??????", notes = "???????????? ??? url, tag ??????")
    @GetMapping("/list")
    public ResponseEntity<UrlAllListDto> getAllUrlList(@AuthenticationPrincipal String userId){
        UrlAllListDto urlAllListDto = urlService.getUrlAllList(userId);

        return ResponseEntity.ok(urlAllListDto);
    }

    @ApiOperation(value = "????????? url ??????", notes = "???????????? ??????, ??????  ?????? ??????")
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

            return ResponseEntity.ok(addUrl); // ?????? ??????
        } catch (Exception e) {
            // ????????? ?????? ?????? bad ???????????? ??????.
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }
}

