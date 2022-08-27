package com.kunis.linkuni.dto.url;

import com.kunis.linkuni.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
}
