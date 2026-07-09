package com.eldercare.modules.carelevel.admin.service;

import java.util.List;

import com.eldercare.modules.carelevel.admin.dto.request.CreateCareLevelRateRequest;
import com.eldercare.modules.carelevel.admin.dto.request.UpdateCareLevelRateRequest;
import com.eldercare.modules.carelevel.admin.dto.request.UpdateCareLevelRequest;
import com.eldercare.modules.carelevel.admin.dto.response.CareLevelRateResponse;
import com.eldercare.modules.carelevel.admin.dto.response.CareLevelResponse;

public interface CareLevelService {

    // API 19
    List<CareLevelResponse> getCareLevels();

    // API 20
    CareLevelResponse updateCareLevel(Long careLevelId, UpdateCareLevelRequest request);

    // API 21
    List<CareLevelRateResponse> getCareLevelRates(Long careLevelId);

    // API 22
    CareLevelRateResponse createCareLevelRate(CreateCareLevelRateRequest request);

    // API 23
    CareLevelRateResponse updateCareLevelRate(Long rateId, UpdateCareLevelRateRequest request);

    // API 24
    void deleteCareLevelRate(Long rateId);

    // API 25
    List<CareLevelRateResponse> seedSampleCareLevelRates();

}