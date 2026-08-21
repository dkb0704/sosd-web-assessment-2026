package com.fzujxl.aicomputerplatform.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArtWork implements Serializable {
        private Long id;
        private Long userId;
        private String title;
        private String content;
        private String coverUrl;
        private String category;
        private String taskType;
        private String status;
        private Integer isPublic;
        private Long viewCount;
        private Long hotScore;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime createdAt;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime updatedAt;

}
