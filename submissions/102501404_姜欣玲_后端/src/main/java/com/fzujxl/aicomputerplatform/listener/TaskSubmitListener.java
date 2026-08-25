package com.fzujxl.aicomputerplatform.listener;

import com.fzujxl.aicomputerplatform.common.ResultCode;
import com.fzujxl.aicomputerplatform.dto.task.TaskOperateDto;
import com.fzujxl.aicomputerplatform.exception.BusinessException;
import com.fzujxl.aicomputerplatform.mapper.task.TaskMapper;
import com.fzujxl.aicomputerplatform.service.email.EmailService;
import com.fzujxl.aicomputerplatform.utils.ModelClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TaskSubmitListener {

    private final ModelClient modelClient;
    private final TaskMapper taskMapper;
    private final EmailService emailService;
    private final RabbitTemplate rabbitTemplate;

    public TaskSubmitListener(ModelClient modelClient, TaskMapper taskMapper, EmailService emailService, RabbitTemplate rabbitTemplate) {
        this.modelClient = modelClient;
        this.taskMapper = taskMapper;
        this.emailService = emailService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "task.submit.queue"),
            exchange = @Exchange(name = "task.submit.exchange",type = ExchangeTypes.TOPIC),
            key = {"task.submit"}
    ))

    public void listenSubmitQueue(TaskOperateDto message){
        log.info("收到任务提交消息:{}", message);

        // TODO 调用API

        //更改状态
        try {
            int result = taskMapper.updateStatus(message.getTaskNo());
            if (result<=0){
                log.error("更新任务状态失败");
                throw new BusinessException(ResultCode.INTERNAL_ERROR,"更新任务状态失败");
            }
        } catch (Exception e) {
            log.error("更新任务状态失败",e);
            throw new BusinessException(ResultCode.INTERNAL_ERROR,"更新任务状态失败");
        }

        String email = taskMapper.selectEmailByUserId(message.getUserId());
        String exchangeName = "task.exchange";
        rabbitTemplate.convertAndSend(exchangeName,"task.notify",email);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "task.notify.queue"),
            exchange = @Exchange(name = "task.submit.exchange",type = ExchangeTypes.TOPIC),
            key = {"task.notify"}
    ))

    public void listenNotifyQueue(String email){
        log.info("收到任务通知消息:{}", email);
        emailService.sendNotifyEmail(email);
    }
}
