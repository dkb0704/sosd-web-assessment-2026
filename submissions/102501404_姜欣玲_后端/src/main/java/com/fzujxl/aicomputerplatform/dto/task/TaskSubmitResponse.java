package com.fzujxl.aicomputerplatform.dto.task;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

public record TaskSubmitResponse (
        @JsonSerialize(using = ToStringSerializer.class)
        Long taskNo
){}
