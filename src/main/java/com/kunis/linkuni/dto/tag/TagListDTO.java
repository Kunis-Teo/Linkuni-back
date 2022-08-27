package com.kunis.linkuni.dto.tag;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagListDTO {
    List<TagDTO> tagDTOList = new ArrayList<>();
}
