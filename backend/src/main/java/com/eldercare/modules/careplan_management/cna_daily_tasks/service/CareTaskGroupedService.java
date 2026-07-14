package com.eldercare.modules.careplan_management.cna_daily_tasks.service;

import com.eldercare.common.dto.PagedResponse;
import com.eldercare.modules.careplan_management.cna_daily_tasks.dto.request.GroupedTaskQuery;
import com.eldercare.modules.careplan_management.cna_daily_tasks.dto.request.TaskSearchFilter;
import com.eldercare.modules.careplan_management.cna_daily_tasks.dto.response.GroupedByCnaCard;
import com.eldercare.modules.careplan_management.cna_daily_tasks.dto.response.GroupedByResidentCard;
import com.eldercare.modules.careplan_management.cna_daily_tasks.dto.response.EnrichedTaskRow;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface CareTaskGroupedService {
    PagedResponse<List<GroupedByCnaCard>> getTasksByCna(GroupedTaskQuery query);
    PagedResponse<List<GroupedByResidentCard>> getTasksByResident(GroupedTaskQuery query);
    PagedResponse<List<EnrichedTaskRow>> searchTasks(TaskSearchFilter filter, Pageable pageable);
}
