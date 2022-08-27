package com.kunis.linkuni.controller;

import com.kunis.linkuni.dto.*;
import com.kunis.linkuni.dto.category.CategoryAddDTO;
import com.kunis.linkuni.dto.category.CategoryDTO;
import com.kunis.linkuni.dto.category.CategoryListDTO;
import com.kunis.linkuni.entity.Category;
import com.kunis.linkuni.service.CategoryService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    @ApiOperation(value = "카테고리 핀 고정/해제", notes = "핀이 고정되있으면 해제, 해제되있으면 고정으로 변경")
    @GetMapping("/pine/{categoryId}")
    public ResponseEntity<CategoryDTO> setPine(@AuthenticationPrincipal String userId, @PathVariable String categoryId) {
        Category category = categoryService.setPine(categoryId);
        CategoryDTO categoryDTO = new CategoryDTO(category.getId(), category.getName(), category.getIsPinned());

        return ResponseEntity.ok(categoryDTO);
    }

    @ApiOperation(value = "등록된 모든 카테고리 조회", notes = "로그인 중인 사용자가 등록한 모든 카테고리 조회")
    @GetMapping("/list")
    public ResponseEntity<CategoryListDTO> getAllCategories(@AuthenticationPrincipal String userId) {
        CategoryListDTO categoryListDTO = categoryService.getCategoryList(userId);

        return ResponseEntity.ok(categoryListDTO);
    }

    @ApiOperation(value = "카테고리 추가", notes = "새로운 카테고리 추가")
    @PostMapping("/add")
    public ResponseEntity<?> registerTag(@AuthenticationPrincipal String userId, @RequestBody CategoryAddDTO categoryAddDTO) {
        try {
            Category category = Category.builder()
                    .name(categoryAddDTO.getName())
                    .isPinned(false)
                    .build();

            Category registeredCategory = categoryService.registerCategory(category, userId);

            return ResponseEntity.ok(categoryAddDTO);
        } catch (Exception e) {
            // 예외가 나는 경우 bad 리스폰스 리턴.
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

}
