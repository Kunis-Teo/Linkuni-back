package com.kunis.linkuni.dto.url;

import com.kunis.linkuni.dto.tag.TagDTO;
import com.kunis.linkuni.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UrlDTO {
    private String id;
    private String url;
    private String memo;
    private Boolean isStarred;
    private Boolean isWatched;
    private LocalDateTime createAt;
    private LocalDateTime watchedAt;
    private String category_id;
    private List<TagDTO> tagDTOList;

    public UrlDTO(String id, String url,
                  String memo, Boolean isStarred,
                  Boolean isWatched, LocalDateTime createAt,
                  LocalDateTime watchedAt, String category_id) {
        this.id = id;
        this.url = url;
        this.memo = memo;
        this.isStarred = isStarred;
        this.isWatched = isWatched;
        this.createAt = createAt;
        this.watchedAt = watchedAt;
        this.category_id = category_id;
    }
}
