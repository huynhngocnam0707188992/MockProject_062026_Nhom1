package com.eldercare.modules.careplan_management.careplan_design.controller;

import com.eldercare.modules.careplan_management.careplan_design.dto.searchCarePlanDTO.SearchCarePlanRequestDTO;
import com.eldercare.modules.careplan_management.careplan_design.dto.searchCarePlanDTO.SearchCarePlanResponseDTO;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eldercare.common.enums.CarePlanStatusEnum;
import com.eldercare.common.response.ApiResponse;
import com.eldercare.modules.careplan_management.careplan_design.dto.activeCarePlanDTO.ActiveCarePlanRequestDTO;
import com.eldercare.modules.careplan_management.careplan_design.dto.activeCarePlanDTO.ActiveCarePlanResponseDTO;
import com.eldercare.modules.careplan_management.careplan_design.dto.discontinueCarePlanDTO.DiscontinueCarePlanRequestDTO;
import com.eldercare.modules.careplan_management.careplan_design.dto.discontinueCarePlanDTO.DiscontinueCarePlanResponseDTO;
import com.eldercare.modules.careplan_management.careplan_design.dto.getCarePlanDetailDTO.GetCarePlanDetailRequestDTO;
import com.eldercare.modules.careplan_management.careplan_design.dto.getCarePlanDetailDTO.GetCarePlanDetailResponseDTO;
import com.eldercare.modules.careplan_management.careplan_design.dto.listCarePlansDTO.ListCarePlanRequestDTO;
import com.eldercare.modules.careplan_management.careplan_design.dto.listCarePlansDTO.ListCarePlanResponseDTO;
import com.eldercare.modules.careplan_management.careplan_design.dto.markSignificantChangeDTO.MarkSignificantChangeResponseDTO;
import com.eldercare.modules.careplan_management.careplan_design.dto.markSignificantChangeDTO.MarkSignificantChangeRequestDTO;
import com.eldercare.modules.careplan_management.careplan_design.service.ICarePlanService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/care-plans")
public class CarePlanController {
    private final ICarePlanService carePlanService;

    public CarePlanController(ICarePlanService carePlanService) {
        this.carePlanService = carePlanService;
    }

    @PatchMapping("/{carePlanId}/activate")
    public ResponseEntity<ApiResponse<ActiveCarePlanResponseDTO>> activateCarePlan(@PathVariable int carePlanId) {
        ApiResponse<ActiveCarePlanResponseDTO> response = ApiResponse
                .success(this.carePlanService.activateCarePlan(new ActiveCarePlanRequestDTO(carePlanId)));
        response.setMessage("Care plan " + carePlanId + "activated");
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{carePlanId}/discontinue")
    public ResponseEntity<ApiResponse<DiscontinueCarePlanResponseDTO>> discontinueCarePlan(
            @PathVariable int carePlanId) {
        ApiResponse<DiscontinueCarePlanResponseDTO> response = ApiResponse
                .success(this.carePlanService.discontinueCarePlan(new DiscontinueCarePlanRequestDTO(carePlanId)));
        response.setMessage("Care plan " + carePlanId + " deactivated");
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{carePlanId}/significant-change")
    public ResponseEntity<ApiResponse<MarkSignificantChangeResponseDTO>> markSignificantChange(
            @PathVariable int carePlanId) {
        ApiResponse<MarkSignificantChangeResponseDTO> response = ApiResponse
                .success(this.carePlanService.markSignificantChange(new MarkSignificantChangeRequestDTO(carePlanId)));
        response.setMessage("Significant change flag updated");
        return ResponseEntity.ok(response);
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse<ListCarePlanResponseDTO>> listCarePlans(
            @RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "20") Integer size,
            @RequestParam(required = false) Long residentId, @RequestParam(required = false) CarePlanStatusEnum status,
            @RequestParam(required = false) Boolean significantChangeFlag,
            @RequestParam(defaultValue = "updatedAt") String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction sortDir) {
        ListCarePlanRequestDTO request = new ListCarePlanRequestDTO(page, size, residentId, status,
                significantChangeFlag, sortBy, sortDir);
        ApiResponse<ListCarePlanResponseDTO> response = ApiResponse
                .success(this.carePlanService.listCarePlans(request));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{carePlanId}")
    public ResponseEntity<ApiResponse<GetCarePlanDetailResponseDTO>> getCarePlanDetail(@PathVariable int carePlanId) {
        GetCarePlanDetailRequestDTO requestDTO = new GetCarePlanDetailRequestDTO();
        requestDTO.id = carePlanId;

        ApiResponse<GetCarePlanDetailResponseDTO> response = ApiResponse
                .success(this.carePlanService.getCarePlanDetail(requestDTO));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<SearchCarePlanResponseDTO>>> searchCarePlan(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) CarePlanStatusEnum status,
            @RequestParam(required = false) Boolean significantChangeFlag,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        SearchCarePlanRequestDTO requestDTO = new SearchCarePlanRequestDTO();

        requestDTO.keyword = keyword;
        requestDTO.status = status;
        requestDTO.significantChangeFlag = significantChangeFlag;
        requestDTO.page = page;
        requestDTO.size = size;

        ApiResponse<List<SearchCarePlanResponseDTO>> response = ApiResponse.success(
                this.carePlanService.searchCarePlan(requestDTO));
        return ResponseEntity.ok(response);
    }
}
