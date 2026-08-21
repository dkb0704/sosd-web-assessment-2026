package com.fzujxl.aicomputerplatform.mapper.gallery;

import com.fzujxl.aicomputerplatform.dto.gallery.ArtworkQueryRequest;
import com.fzujxl.aicomputerplatform.entity.ArtWork;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GalleryMapper {

    List<ArtWork> selectByCondition(@Param("request") ArtworkQueryRequest request);

    @Select("select id,user_id,title,content,status,is_public,view_count,hot_score,created_at " +
            "from 'artwork' where id = #{id} and deleted = 0 and is_public = 1")
    ArtWork selectById(Long id);
}
