package com.eldercare.common.constants;

public final class RouteConstants {

    private RouteConstants() {
        // Prevent instantiation
    }

    // Base API Routes
    public static final String API_PREFIX = "/api/v1";
    public static final String ADMIN_API_PREFIX = "/api/v1/admin";
    
    // Facility Management Routes
    public static final String API_ADMIN_FACILITIES = ADMIN_API_PREFIX + "/facility-settings";

    // Task Management Routes
    public static final String API_TASKS = API_PREFIX + "/tasks";
    public static final String API_TASKS_SEARCH = API_TASKS + "/search";
    public static final String API_TASKS_BY_CNA = API_TASKS + "/by-cna";
    public static final String API_TASKS_BY_RESIDENT = API_TASKS + "/by-resident";
}