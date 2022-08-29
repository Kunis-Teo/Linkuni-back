package com.kunis.linkuni.service;

import com.kunis.linkuni.dto.category.CategoryDTO;
import com.kunis.linkuni.dto.category.CategoryListDTO;
import com.kunis.linkuni.entity.Category;
import com.kunis.linkuni.entity.User;
import com.kunis.linkuni.repository.CategoryRepository;
import com.kunis.linkuni.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    public Category findById(String id){
        return categoryRepository.findById(id).get();
    }

    public Category findByIdFetchjoin(String id) {
        return categoryRepository.findByIdFetch(id);
    }

    public Category registerCategory(Category category, String userId){
        // 로그인한 유저 찾기
        User user = userRepository.findById(userId).get();
        // 로그인한 유저가 동일한 이름의 카테고리가 존재하면 예외 발생
        if(categoryRepository.existsByUserAndName(user, category.getName())){
            log.warn("category name already exists {}", category.getName());
            throw new RuntimeException("category name already exists");
        }
        // 카테고리에 유저 설정
        category.setUser(user);
        return categoryRepository.save(category); // 저장
    }

    public CategoryListDTO getCategoryList(String userId){
        // 로그인 유저가 등록한 모든 카테고리 조회
        List<Category> categoryList = categoryRepository.findByUser(userRepository.findById(userId).get());
        // 반환 객체 생성.
        CategoryListDTO categoryListDTO = new CategoryListDTO();
        // 카테고리 리스트 선언
        List<CategoryDTO> categoryDTOs = new ArrayList<>();
        for(int i=0;i<categoryList.size();i++){
            categoryDTOs.add(new CategoryDTO(
                    categoryList.get(i).getId(),
                    categoryList.get(i).getName(),
                    categoryList.get(i).getIsPinned()));
        }
        // 반환 객체에 카테고리 리스트 저장
        categoryListDTO.setCategoryList(categoryDTOs);

        return categoryListDTO;
    }

    public Category setPine(String categoryId){
        Category category = categoryRepository.findById(categoryId).get();
        if(category.getIsPinned()) category.setIsPinned(false); // 카테고리 핀이 true라면 false로 설정
        else category.setIsPinned(true); // 카테고리 핀이 false라면 true로 설정

        return category;
    }
}
