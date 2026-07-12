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

    // Inventory Management Routes
    public static final String API_ADMIN_INVENTORY = ADMIN_API_PREFIX + "/inventory";

    public static final String API_ADMIN_INVENTORY_CATEGORIES = API_ADMIN_INVENTORY + "/categories";
}