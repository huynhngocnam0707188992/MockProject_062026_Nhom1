package com.eldercare.modules.careplan_management.cna_daily_tasks.controller;

import com.eldercare.common.constants.RouteConstants;
import com.eldercare.common.dto.PagedResponse;
import com.eldercare.modules.careplan_management.cna_daily_tasks.dto.request.*;
import com.eldercare.modules.careplan_management.cna_daily_tasks.dto.response.EnrichedTaskRow;
import com.eldercare.modules.careplan_management.cna_daily_tasks.dto.response.GroupedByCnaCard;
import com.eldercare.modules.careplan_management.cna_daily_tasks.dto.response.GroupedByResidentCard;
import com.eldercare.modules.careplan_management.cna_daily_tasks.dto.response.TaskDetailDto;
import com.eldercare.modules.careplan_management.cna_daily_tasks.service.CareTaskGroupedService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CareTaskGroupedController {

    private final CareTaskGroupedService careTaskGroupedService;

    @PreAuthorize("hasRole('NHA_ADMIN')")
    @GetMapping(RouteConstants.API_TASKS_BY_CNA)
    public ResponseEntity<PagedResponse<List<GroupedByCnaCard>>> getTasksByCna(@Valid @ModelAttribute GroupedTaskQuery query) {
        return ResponseEntity.ok(careTaskGroupedService.getTasksByCna(query));
    }

    @PreAuthorize("hasRole('NHA_ADMIN')")
    @GetMapping(RouteConstants.API_TASKS_BY_RESIDENT)
    public ResponseEntity<PagedResponse<List<GroupedByResidentCard>>> getTasksByResident(@Valid @ModelAttribute GroupedTaskQuery query) {
        return ResponseEntity.ok(careTaskGroupedService.getTasksByResident(query));
    }

    @PreAuthorize("hasRole('NHA_ADMIN')")
    @GetMapping(RouteConstants.API_TASKS_SEARCH)
    public ResponseEntity<PagedResponse<List<EnrichedTaskRow>>> searchTasks(@ModelAttribute TaskSearchFilter filter) {
        Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize());
        return ResponseEntity.ok(careTaskGroupedService.searchTasks(filter, pageable));
    }

    @PreAuthorize("hasRole('NHA_ADMIN') or hasRole('CNA')")
    @GetMapping(RouteConstants.API_INTERVENTION_TASKS)
    public ResponseEntity<PagedResponse<List<TaskDetailDto>>> listTasksByIntervention(
            @PathVariable Long interventionId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(careTaskGroupedService.listTasksByIntervention(interventionId, pageable));
    }

    @PreAuthorize("hasRole('NHA_ADMIN') or hasRole('CNA')")
    @GetMapping(RouteConstants.API_TASK_BY_ID)
    public ResponseEntity<TaskDetailDto> getTaskDetail(@PathVariable Long taskId) {
        return ResponseEntity.ok(careTaskGroupedService.getTaskDetail(taskId));
    }

    @PreAuthorize("hasRole('NHA_ADMIN')")
    @PutMapping(RouteConstants.API_TASK_BY_ID)
    public ResponseEntity<TaskDetailDto> updateTask(
            @PathVariable Long taskId,
            @Valid @RequestBody UpdateTaskRequestDto request) {
        return ResponseEntity.ok(careTaskGroupedService.updateTask(taskId, request));
    }

    @PreAuthorize("hasRole('NHA_ADMIN')")
    @DeleteMapping(RouteConstants.API_TASK_BY_ID)
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        careTaskGroupedService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('NHA_ADMIN')")
    @PatchMapping(RouteConstants.API_TASK_ASSIGN_CNA)
    public ResponseEntity<TaskDetailDto> assignCna(
            @PathVariable Long taskId,
            @Valid @RequestBody AssignCnaRequestDto request) {
        return ResponseEntity.ok(careTaskGroupedService.assignCna(taskId, request.getAssignedCnaId()));
    }

    @PreAuthorize("hasRole('NHA_ADMIN') or hasRole('CNA')")
    @PatchMapping(RouteConstants.API_TASK_COMPLETED)
    public ResponseEntity<TaskDetailDto> completeTask(
            @PathVariable Long taskId,
            @Valid @RequestBody CompleteTaskRequestDto request) {
        return ResponseEntity.ok(careTaskGroupedService.completeTask(taskId, request.getCompletedAt()));
    }

    @PreAuthorize("hasRole('NHA_ADMIN') or hasRole('CNA')")
    @PatchMapping(RouteConstants.API_TASK_MISSED)
    public ResponseEntity<TaskDetailDto> markMissed(@PathVariable Long taskId) {
        return ResponseEntity.ok(careTaskGroupedService.markMissed(taskId));
    }

    @PreAuthorize("hasRole('NHA_ADMIN') or hasRole('CNA')")
    @PatchMapping(RouteConstants.API_TASK_FLAG_ABNORMAL)
    public ResponseEntity<TaskDetailDto> flagAbnormal(
            @PathVariable Long taskId,
            @Valid @RequestBody FlagAbnormalRequestDto request) {
        return ResponseEntity.ok(careTaskGroupedService.flagAbnormal(taskId, request.getIsAbnormalFlagged()));
    }

    @PreAuthorize("hasRole('NHA_ADMIN')")
    @PatchMapping(RouteConstants.API_TASK_RESCHEDULE)
    public ResponseEntity<TaskDetailDto> rescheduleTask(
            @PathVariable Long taskId,
            @Valid @RequestBody RescheduleTaskRequestDto request) {
        return ResponseEntity.ok(careTaskGroupedService.rescheduleTask(taskId, request.getScheduledTime()));
    }
}
