package com.eldercare.modules.careplan_management.cna_daily_tasks.controller;

import com.eldercare.common.constants.RouteConstants;
import com.eldercare.common.dto.PagedResponse;
import com.eldercare.modules.careplan_management.cna_daily_tasks.dto.request.GroupedTaskQuery;
import com.eldercare.modules.careplan_management.cna_daily_tasks.dto.request.TaskSearchFilter;
import com.eldercare.modules.careplan_management.cna_daily_tasks.dto.response.EnrichedTaskRow;
import com.eldercare.modules.careplan_management.cna_daily_tasks.dto.response.GroupedByCnaCard;
import com.eldercare.modules.careplan_management.cna_daily_tasks.dto.response.GroupedByResidentCard;
import com.eldercare.modules.careplan_management.cna_daily_tasks.service.CareTaskGroupedService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

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
}
