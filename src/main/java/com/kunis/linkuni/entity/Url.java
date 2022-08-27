package com.kunis.linkuni.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="url")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Url {
    @Id
    @Column(name="url_id")
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String id;

    @Column(nullable = false)
    private String url;

    private String memo;

    private Boolean isStarred;

    private Boolean isWatched;

    private LocalDateTime createAt;

    private LocalDateTime watchedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
