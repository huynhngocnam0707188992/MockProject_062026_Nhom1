package com.eldercare.modules.carelevel.service;

import com.eldercare.modules.carelevel.dto.request.CreateCareLevelRateRequest;
import com.eldercare.modules.carelevel.dto.request.UpdateCareLevelRateRequest;
import com.eldercare.modules.carelevel.dto.request.UpdateCareLevelRequest;
import com.eldercare.modules.carelevel.dto.response.CareLevelRateResponse;
import com.eldercare.modules.carelevel.dto.response.CareLevelResponse;

import java.util.List;

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