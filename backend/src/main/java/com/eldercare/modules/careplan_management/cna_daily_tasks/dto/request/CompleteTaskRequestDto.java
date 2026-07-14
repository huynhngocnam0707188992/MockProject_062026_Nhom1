package com.eldercare.modules.careplan_management.cna_daily_tasks.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class CompleteTaskRequestDto {
    @NotNull
    private OffsetDateTime completedAt;
}
