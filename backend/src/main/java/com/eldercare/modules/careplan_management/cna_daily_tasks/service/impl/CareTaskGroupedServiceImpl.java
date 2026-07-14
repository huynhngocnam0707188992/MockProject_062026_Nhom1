package com.eldercare.modules.careplan_management.cna_daily_tasks.service.impl;

import com.eldercare.common.dto.PagedResponse;
import com.eldercare.modules.careplan_management.cna_daily_tasks.dto.request.GroupedTaskQuery;
import com.eldercare.modules.careplan_management.cna_daily_tasks.dto.request.TaskSearchFilter;
import com.eldercare.modules.careplan_management.cna_daily_tasks.dto.response.EnrichedTaskRow;
import com.eldercare.modules.careplan_management.cna_daily_tasks.dto.response.GroupedByCnaCard;
import com.eldercare.modules.careplan_management.cna_daily_tasks.dto.response.GroupedByResidentCard;
import com.eldercare.modules.careplan_management.cna_daily_tasks.mapper.CareTaskGroupedMapper;
import com.eldercare.modules.careplan_management.cna_daily_tasks.repository.CareTaskRepository;
import com.eldercare.modules.careplan_management.cna_daily_tasks.service.CareTaskGroupedService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CareTaskGroupedServiceImpl implements CareTaskGroupedService {

    private final CareTaskRepository careTaskRepository;
    private final CareTaskGroupedMapper careTaskGroupedMapper;

    @Override
    public PagedResponse<List<GroupedByCnaCard>> getTasksByCna(GroupedTaskQuery query) {
        List<EnrichedTaskRow> allTasks = fetchTasksForGrouping(query);

        Map<Long, List<EnrichedTaskRow>> groupedByCnaId = allTasks.stream()
                .collect(Collectors.groupingBy(task -> task.getAssignedCnaId() != null ? task.getAssignedCnaId() : -1L));

        List<GroupedByCnaCard> cards = groupedByCnaId.entrySet().stream()
                .map(entry -> careTaskGroupedMapper.toGroupedByCnaCard(
                        entry.getKey() == -1L ? null : entry.getKey(), 
                        entry.getValue()
                ))
                .collect(Collectors.toList());

        return createPagedResponse(cards, query.getPage(), query.getSize());
    }

    @Override
    public PagedResponse<List<GroupedByResidentCard>> getTasksByResident(GroupedTaskQuery query) {
        List<EnrichedTaskRow> allTasks = fetchTasksForGrouping(query);

        Map<Long, List<EnrichedTaskRow>> groupedByResidentId = allTasks.stream()
                .collect(Collectors.groupingBy(EnrichedTaskRow::getResidentId));

        List<GroupedByResidentCard> cards = groupedByResidentId.entrySet().stream()
                .map(entry -> careTaskGroupedMapper.toGroupedByResidentCard(
                        entry.getKey(), 
                        entry.getValue()
                ))
                .collect(Collectors.toList());

        return createPagedResponse(cards, query.getPage(), query.getSize());
    }

    private List<EnrichedTaskRow> fetchTasksForGrouping(GroupedTaskQuery query) {
        OffsetDateTime startOfDay = query.getDate() != null ? query.getDate().atStartOfDay().atOffset(ZoneOffset.UTC) : null;
        OffsetDateTime endOfDay = query.getDate() != null ? query.getDate().plusDays(1).atStartOfDay().atOffset(ZoneOffset.UTC) : null;

        return careTaskRepository.findEnrichedTasksForGrouping(
                startOfDay, endOfDay, query.getStatus(), query.getTaskType(),
                query.getResidentId(), query.getAssignedCnaId(), query.getIsAbnormalFlagged()
        );
    }

    private <T> PagedResponse<List<T>> createPagedResponse(List<T> data, int page, int size) {
        int start = Math.min(page * size, data.size());
        int end = Math.min((start + size), data.size());
        List<T> pagedCards = data.subList(start, end);
        
        int totalPages = (int) Math.ceil((double) data.size() / size);
        
        return PagedResponse.of(
            pagedCards,
            200,
            "Success",
            page,
            totalPages,
            size,
            data.size()
        );
    }

    @Override
    public PagedResponse<List<EnrichedTaskRow>> searchTasks(TaskSearchFilter filter, Pageable pageable) {
        OffsetDateTime startOfDay = filter.getFromDate() != null ? filter.getFromDate().atStartOfDay().atOffset(ZoneOffset.UTC) : null;
        OffsetDateTime endOfDay = filter.getToDate() != null ? filter.getToDate().plusDays(1).atStartOfDay().atOffset(ZoneOffset.UTC) : null;

        Page<EnrichedTaskRow> page = careTaskRepository.searchEnrichedTasks(
                startOfDay, endOfDay, filter.getStatus(), filter.getTaskType(),
                filter.getResidentId(), filter.getAssignedCnaId(), filter.getIsAbnormalFlagged(), pageable
        );
        return PagedResponse.of(
            page.getContent(),
            200,
            "Success",
            page.getNumber(),
            page.getTotalPages(),
            page.getSize(),
            page.getTotalElements()
        );
    }
}
