package com.eldercare.modules.carelevel.admin.service.impl;

import com.eldercare.modules.carelevel.admin.dto.request.CreateCareLevelRateRequest;
import com.eldercare.modules.carelevel.admin.dto.request.UpdateCareLevelRateRequest;
import com.eldercare.modules.carelevel.admin.dto.request.UpdateCareLevelRequest;
import com.eldercare.modules.carelevel.admin.dto.response.CareLevelRateResponse;
import com.eldercare.modules.carelevel.admin.dto.response.CareLevelResponse;
import com.eldercare.modules.carelevel.admin.entity.CareLevel;
import com.eldercare.modules.carelevel.admin.entity.CareLevelRate;
import com.eldercare.modules.carelevel.admin.repository.CareLevelRateRepository;
import com.eldercare.modules.carelevel.admin.repository.CareLevelRepository;
import com.eldercare.modules.carelevel.admin.service.CareLevelService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CareLevelServiceImpl implements CareLevelService {

    private final CareLevelRepository careLevelRepository;
    private final CareLevelRateRepository careLevelRateRepository;

    // ============================
    // API 19
    // GET /admin/care-levels
    // ============================
    @Override
    public List<CareLevelResponse> getCareLevels() {
        return careLevelRepository.findAll()
                .stream()
                .map(this::mapToCareLevelResponse)
                .toList();
    }

    // ============================
    // API 20
    // PATCH /admin/care-levels/{careLevelId}
    // ============================
    @Override
    public CareLevelResponse updateCareLevel(
            Long careLevelId,
            UpdateCareLevelRequest request
    ) {
        CareLevel careLevel = careLevelRepository.findById(careLevelId)
                .orElseThrow(() ->
                        new RuntimeException("Care level not found with id: " + careLevelId)
                );

        careLevel.setIsDeleted(request.getIsDeleted());

        CareLevel updatedCareLevel = careLevelRepository.save(careLevel);

        return mapToCareLevelResponse(updatedCareLevel);
    }

    // ============================
    // API 21
    // GET /admin/care-level-rates?care_level_id={id}
    // ============================
    @Override
public List<CareLevelRateResponse> getCareLevelRates(Long careLevelId) {

    List<CareLevelRate> rates;

    if (careLevelId != null) {
        rates = careLevelRateRepository.findByCareLevel_Id(careLevelId);
    } else {
        rates = careLevelRateRepository.findAll();
    }

    return rates.stream()
            .map(this::mapToCareLevelRateResponse)
            .toList();
}
    // ============================
    // API 22
    // POST /admin/care-level-rates
    // ============================
    @Override
    public CareLevelRateResponse createCareLevelRate(
            CreateCareLevelRateRequest request
    ) {
        CareLevel careLevel = careLevelRepository
                .findById(request.getCareLevelId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Care level not found with id: "
                                        + request.getCareLevelId()
                        )
                );

        CareLevelRate rate = CareLevelRate.builder()
                .careLevel(careLevel)
                .facilityId(request.getFacilityId())
                .dailyRate(request.getDailyRate())
                .effectiveFrom(request.getEffectiveFrom())
                .effectiveTo(null)
                .build();

        CareLevelRate savedRate = careLevelRateRepository.save(rate);

        return mapToCareLevelRateResponse(savedRate);
    }

    // ============================
    // API 23
    // PUT /admin/care-level-rates/{rateId}
    // ============================
    @Override
    public CareLevelRateResponse updateCareLevelRate(
            Long rateId,
            UpdateCareLevelRateRequest request
    ) {
        CareLevelRate rate = careLevelRateRepository.findById(rateId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Care level rate not found with id: " + rateId
                        )
                );

        if (request.getCareLevelId() != null) {
            CareLevel careLevel = careLevelRepository
                    .findById(request.getCareLevelId())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Care level not found with id: "
                                            + request.getCareLevelId()
                            )
                    );

            rate.setCareLevel(careLevel);
        }

        if (request.getFacilityId() != null) {
            rate.setFacilityId(request.getFacilityId());
        }

        if (request.getDailyRate() != null) {
            rate.setDailyRate(request.getDailyRate());
        }

        if (request.getEffectiveFrom() != null) {
            rate.setEffectiveFrom(request.getEffectiveFrom());
        }

        rate.setEffectiveTo(request.getEffectiveTo());

        CareLevelRate updatedRate = careLevelRateRepository.save(rate);

        return mapToCareLevelRateResponse(updatedRate);
    }

    // ============================
    // API 24
    // DELETE /admin/care-level-rates/{rateId}
    // ============================
    @Override
    public void deleteCareLevelRate(Long rateId) {
        if (!careLevelRateRepository.existsById(rateId)) {
            throw new RuntimeException(
                    "Care level rate not found with id: " + rateId
            );
        }

        careLevelRateRepository.deleteById(rateId);
    }

    // ============================
    // API 25
    // POST /admin/care-level-rates/seed
    // ============================
    @Override
    public List<CareLevelRateResponse> seedSampleCareLevelRates() {
        CareLevel careLevel1 = careLevelRepository
                .findById(1L)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Care level id 1 not found"
                        )
                );

        CareLevel careLevel4 = careLevelRepository
                .findById(4L)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Care level id 4 not found"
                        )
                );

        CareLevelRate rate1 = CareLevelRate.builder()
                .careLevel(careLevel1)
                .facilityId(1L)
                .dailyRate(new java.math.BigDecimal("150.00"))
                .effectiveFrom(java.time.LocalDate.now())
                .effectiveTo(null)
                .build();

        CareLevelRate rate2 = CareLevelRate.builder()
                .careLevel(careLevel4)
                .facilityId(1L)
                .dailyRate(new java.math.BigDecimal("285.00"))
                .effectiveFrom(java.time.LocalDate.now())
                .effectiveTo(null)
                .build();

        List<CareLevelRate> savedRates = careLevelRateRepository.saveAll(
                List.of(rate1, rate2)
        );

        return savedRates.stream()
                .map(this::mapToCareLevelRateResponse)
                .toList();
    }

    // ============================
    // Private Mappers
    // ============================
    private CareLevelResponse mapToCareLevelResponse(
            CareLevel careLevel
    ) {
        return CareLevelResponse.builder()
                .id(careLevel.getId())
                .levelCode(careLevel.getLevelCode())
                .levelName(careLevel.getLevelName())
                .isDeleted(careLevel.getIsDeleted())
                .build();
    }

    private CareLevelRateResponse mapToCareLevelRateResponse(
            CareLevelRate rate
    ) {
        return CareLevelRateResponse.builder()
                .id(rate.getId())
                .careLevelId(rate.getCareLevel().getId())
                .facilityId(rate.getFacilityId())
                .dailyRate(rate.getDailyRate())
                .effectiveFrom(rate.getEffectiveFrom())
                .effectiveTo(rate.getEffectiveTo())
                .build();
    }
}