package com.kunis.linkuni.dto.url;

import com.kunis.linkuni.dto.tag.TagAddDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UrlAddDto {
    private String url;
    private String memo;

    private String category_id;

    private List<TagAddDto> tagList;
}
