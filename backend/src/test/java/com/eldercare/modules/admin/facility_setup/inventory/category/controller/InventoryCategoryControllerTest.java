package com.eldercare.modules.admin.facility_setup.inventory.category.controller;

import com.eldercare.common.constants.RouteConstants;
import com.eldercare.common.dto.PagedResponse;
import com.eldercare.modules.admin.facility_setup.inventory.category.controller.InventoryCategoryController;
import com.eldercare.modules.admin.facility_setup.inventory.category.dto.request.InventoryCategoryRequest;
import com.eldercare.modules.admin.facility_setup.inventory.category.dto.response.InventoryCategoryResponse;
import com.eldercare.modules.admin.facility_setup.inventory.category.service.InventoryCategoryServiceInterface;
import tools.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
// SỬ DỤNG IMPORT MỚI CỦA SPRING BOOT 4.X THAY CHO MOCKBEAN
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = InventoryCategoryController.class, 
    excludeAutoConfiguration = {
        org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration.class,
        org.springframework.boot.security.autoconfigure.UserDetailsServiceAutoConfiguration.class
    })
@DisplayName("Inventory Category Controller API Integration Tests")
class InventoryCategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // DÙNG @MockitoBean THAY VÌ @MockBean
    @MockitoBean
    private InventoryCategoryServiceInterface inventoryCategoryService;

    private InventoryCategoryRequest validRequest;
    private InventoryCategoryResponse categoryResponse;

    @BeforeEach
    void setUp() {
        validRequest = new InventoryCategoryRequest();
        validRequest.setCategoryName("Medical Supplies");
        validRequest.setDescription("Supplies for medical purposes.");

        categoryResponse = new InventoryCategoryResponse();
        categoryResponse.setId(1L);
        categoryResponse.setCategoryName("Medical Supplies");
        categoryResponse.setDescription("Supplies for medical purposes.");
    }

    @Nested
    @DisplayName("Endpoint: GET /api/admin/inventory/categories")
    class GetAllInventoryCategoriesTests {

        @Test
        // @WithMockUser(username = "admin", roles = {"ADMIN"})
        @DisplayName("Happy Case: Should return 200 OK with a paged list for valid pagination")
        void getAllInventoryCategories_withValidParams_shouldReturn200AndPagedResponse() throws Exception {
            int page = 0;
            int size = 10;
           

            mockMvc.perform(get(RouteConstants.API_ADMIN_INVENTORY_CATEGORIES)
                            .param("page", String.valueOf(page))
                            .param("size", String.valueOf(size))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].id").value(categoryResponse.getId()))
                    .andExpect(jsonPath("$.content[0].categoryName").value(categoryResponse.getCategoryName()))
                    .andExpect(jsonPath("$.page").value(page))
                    .andExpect(jsonPath("$.size").value(size));
        }

        @Test
        @DisplayName("Error Case: Should return 400 Bad Request for negative page number")
        void getAllInventoryCategories_withNegativePage_shouldReturn400() throws Exception {
            mockMvc.perform(get(RouteConstants.API_ADMIN_INVENTORY_CATEGORIES)
                            .param("page", "-1")
                            .param("size", "10"))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Error Case: Should return 400 Bad Request for zero size")
        void getAllInventoryCategories_withZeroSize_shouldReturn400() throws Exception {
            mockMvc.perform(get(RouteConstants.API_ADMIN_INVENTORY_CATEGORIES)
                            .param("page", "0")
                            .param("size", "0"))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("Endpoint: POST /api/admin/inventory/categories")
    class CreateInventoryCategoryTests {

        @Test
        @DisplayName("Happy Case: Should return 201 Created with the new category")
        void createInventoryCategory_withValidRequest_shouldReturn201AndCategory() throws Exception {
            given(inventoryCategoryService.createInventoryCategory(any(InventoryCategoryRequest.class)))
                    .willReturn(categoryResponse);

            mockMvc.perform(post(RouteConstants.API_ADMIN_INVENTORY_CATEGORIES)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRequest)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(categoryResponse.getId()))
                    .andExpect(jsonPath("$.categoryName").value(categoryResponse.getCategoryName()));
        }

        @Test
        @DisplayName("Error Case: Should return 400 Bad Request when name is blank")
        void createInventoryCategory_withBlankName_shouldReturn400() throws Exception {
            validRequest.setCategoryName("   ");

            mockMvc.perform(post(RouteConstants.API_ADMIN_INVENTORY_CATEGORIES)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRequest)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Error Case: Should return 400 Bad Request when name is null")
        void createInventoryCategory_withNullName_shouldReturn400() throws Exception {
            validRequest.setCategoryName(null);

            mockMvc.perform(post(RouteConstants.API_ADMIN_INVENTORY_CATEGORIES)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRequest)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("Endpoint: GET /api/admin/inventory/categories/{categoryId}")
    class GetInventoryCategoryByIdTests {

        @Test
        @DisplayName("Happy Case: Should return 200 OK with the category for a valid ID")
        void getInventoryCategoryById_withValidId_shouldReturn200AndCategory() throws Exception {
            long validId = 1L;
            given(inventoryCategoryService.getInventoryCategoryById(validId)).willReturn(categoryResponse);

            mockMvc.perform(get(RouteConstants.API_ADMIN_INVENTORY_CATEGORIES + "/{categoryId}", validId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(validId))
                    .andExpect(jsonPath("$.categoryName").value(categoryResponse.getCategoryName()));
        }

        @Test
        @DisplayName("Error Case: Should return 400 Bad Request for a zero ID")
        void getInventoryCategoryById_withZeroId_shouldReturn400() throws Exception {
            mockMvc.perform(get(RouteConstants.API_ADMIN_INVENTORY_CATEGORIES + "/{categoryId}", 0L))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Error Case: Should return 400 Bad Request for a negative ID")
        void getInventoryCategoryById_withNegativeId_shouldReturn400() throws Exception {
            mockMvc.perform(get(RouteConstants.API_ADMIN_INVENTORY_CATEGORIES + "/{categoryId}", -1L))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("Endpoint: PUT /api/admin/inventory/categories/{categoryId}")
    class UpdateInventoryCategoryTests {

        @Test
        @DisplayName("Happy Case: Should return 200 OK with updated category")
        void updateInventoryCategory_withValidIdAndRequest_shouldReturn200AndUpdatedCategory() throws Exception {
            long validId = 1L;
            given(inventoryCategoryService.updateInventoryCategory(anyLong(), any(InventoryCategoryRequest.class)))
                    .willReturn(categoryResponse);

            mockMvc.perform(put(RouteConstants.API_ADMIN_INVENTORY_CATEGORIES + "/{categoryId}", validId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.categoryName").value(categoryResponse.getCategoryName()));
        }

        @Test
        @DisplayName("Error Case: Should return 400 Bad Request for a zero ID")
        void updateInventoryCategory_withZeroId_shouldReturn400() throws Exception {
            mockMvc.perform(put(RouteConstants.API_ADMIN_INVENTORY_CATEGORIES + "/{categoryId}", 0L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRequest)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Error Case: Should return 400 Bad Request for an invalid request body")
        void updateInventoryCategory_withInvalidRequest_shouldReturn400() throws Exception {
            validRequest.setCategoryName("");

            mockMvc.perform(put(RouteConstants.API_ADMIN_INVENTORY_CATEGORIES + "/{categoryId}", 1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRequest)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("Endpoint: DELETE /api/admin/inventory/categories/{categoryId}")
    class DeleteInventoryCategoryTests {

        @Test
        @DisplayName("Happy Case: Should return 204 No Content for a valid ID")
        void deleteInventoryCategory_withValidId_shouldReturn204() throws Exception {
            long validId = 1L;
            doNothing().when(inventoryCategoryService).deleteInventoryCategory(validId);

            mockMvc.perform(delete(RouteConstants.API_ADMIN_INVENTORY_CATEGORIES + "/{categoryId}", validId))
                    .andExpect(status().isNoContent());

            verify(inventoryCategoryService).deleteInventoryCategory(validId);
        }
    }
}