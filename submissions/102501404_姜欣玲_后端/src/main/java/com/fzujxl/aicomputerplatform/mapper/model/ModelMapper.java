package com.fzujxl.aicomputerplatform.mapper.model;

import com.fzujxl.aicomputerplatform.entity.Model;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ModelMapper {

    @Insert("insert into model (model_name, model_key, model_type, cost_points, description, status, " +
            "deleted, sort_order, created_time, updated_time) values (#{modelName}, #{modelKey}, #{modelType}, #{costPoints}, #{description}, #{status}, " +
            " #{deleted}, #{sortOrder}, #{createdTime}, #{updatedTime})")
    int insert(Model model);

    @Select("select model_key from model where model_key= #{modelKey} and deleted = 0")
    Model selectByModelKey(String modelKey);

    @Update("update model set status=#{status} where id=#{id} and deleted=0 and status!= #{status}")
    int updateStatusById(Long id,Integer status);

    @Update("update model set cost_points=#{costPoints} where id=#{id} and deleted=0 and cost_points= #{pastCostPoints}")
    int updateCostPointsById(Long id,Long costPoints,Long pastCostPoints);

    @Update("update model set deleted=1 where id=#{id} and deleted=0")
    int deleteById(Long id);
}
