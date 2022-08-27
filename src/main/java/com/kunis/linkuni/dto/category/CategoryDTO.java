package com.kunis.linkuni.dto.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CategoryDTO {
    private String id;
    private String name;
    private Boolean isPinned;
}
