package com.kunis.linkuni.service;

import com.kunis.linkuni.dto.category.CategoryDTO;
import com.kunis.linkuni.dto.category.CategoryListDTO;
import com.kunis.linkuni.entity.Category;
import com.kunis.linkuni.repository.CategoryRepository;
import com.kunis.linkuni.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        category.setUser(userRepository.findById(userId).get());
        return categoryRepository.save(category);
    }

    public CategoryListDTO getCategoryList(String userId){
        List<Category> categoryList = categoryRepository.findByUser(userRepository.findById(userId).get());
        CategoryListDTO categoryListDTO = new CategoryListDTO();
        List<CategoryDTO> categoryDTOs = new ArrayList<>();
        for(int i=0;i<categoryList.size();i++){
            categoryDTOs.add(new CategoryDTO(
                    categoryList.get(i).getId(),
                    categoryList.get(i).getName(),
                    categoryList.get(i).getIsPinned()));
        }
        categoryListDTO.setCategoryList(categoryDTOs);
        return categoryListDTO;
    }
}
