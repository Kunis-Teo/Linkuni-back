package com.kunis.linkuni.controller;

import com.kunis.linkuni.dto.*;
import com.kunis.linkuni.dto.category.CategoryAddDTO;
import com.kunis.linkuni.dto.category.CategoryListDTO;
import com.kunis.linkuni.entity.Category;
import com.kunis.linkuni.service.CategoryService;
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

    @GetMapping("/list")
    public ResponseEntity<CategoryListDTO> getAllCategories(@AuthenticationPrincipal String userId) {
        CategoryListDTO categoryListDTO = categoryService.getCategoryList(userId);

        return ResponseEntity.ok(categoryListDTO);
    }

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
