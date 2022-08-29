package com.kunis.linkuni.dto.url;

import com.kunis.linkuni.dto.tag.TagDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UrlUpdateDTO {
    String id;
    String url;
    String memo;
    String category_id;
    List<TagDTO> tagDTOList;
}
