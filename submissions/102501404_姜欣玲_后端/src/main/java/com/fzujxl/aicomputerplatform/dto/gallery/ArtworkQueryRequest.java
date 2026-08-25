package com.fzujxl.aicomputerplatform.dto.gallery;

import com.fzujxl.aicomputerplatform.validation.SortValid;
import com.fzujxl.aicomputerplatform.validation.ValidCategory;
import com.fzujxl.aicomputerplatform.validation.ValidWorkType;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ArtworkQueryRequest {

    @ValidCategory
    private String category;

    @ValidWorkType
    private String workType;

    @SortValid
    private String sort;

    @Min(1)
    @Max(100)
    private Integer pageSize = 20;

    @Min(1)
    @Max(100)
    private Integer pageNum = 1;

}
