package com.fzujxl.aicomputerplatform.mapper.task;

import com.fzujxl.aicomputerplatform.dto.task.TaskSubmitRequest;
import com.fzujxl.aicomputerplatform.entity.Task;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TaskMapper {

    @Select("select id, task_no, user_id, model_id, prompt, status, cost_points, created_time," +
            " started_time, finished_time, error_msg from task where status = #{status} " +
            "and deleted = 0 order by id ")
    List<Task> selectByStatus(Integer status);


    @Insert("insert into task (user_id,model_id,task_no,prompt,created_time) values " +
            "(#{userId},#{request.modelId},#{taskNo},#{request.prompt},now())")
    int submitTask(@Param("userId") Long userId, @Param("taskNo") Long taskNo, @Param("request") TaskSubmitRequest request);

    @Update("update task set status = 'COMPLETED' where task_no = #{taskNo} and deleted = 0")
    int updateStatus(@Param("taskNo") Long taskNo);

    @Select("select user.email from 'user' where user.id = #{userId}")
    String selectEmailByUserId(@Param("userId") Long userId);
}
