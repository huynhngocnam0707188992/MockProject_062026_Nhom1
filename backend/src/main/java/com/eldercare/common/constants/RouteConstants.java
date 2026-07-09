package com.eldercare.common.constants;

public final class RouteConstants {

    private RouteConstants() {
        // Prevent instantiation
    }

    // Base API Routes
    public static final String API_PREFIX = "/api";
    public static final String ADMIN_API_PREFIX = "/admin";
    
    // Facility Management Routes
    public static final String API_ADMIN_FACILITY_SETTINGS = ADMIN_API_PREFIX + "/facility-settings";
    public static final String API_ADMIN_ROOMS = API_ADMIN_FACILITY_SETTINGS + "/rooms";
    public static final String API_ADMIN_BEDS = API_ADMIN_FACILITY_SETTINGS + "/beds";
}