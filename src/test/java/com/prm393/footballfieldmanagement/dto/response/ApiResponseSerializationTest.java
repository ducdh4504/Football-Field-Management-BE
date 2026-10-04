package com.prm393.footballfieldmanagement.dto.response;

import com.prm393.footballfieldmanagement.enums.Role;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.json.JsonMapper;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ApiResponseSerializationTest {

    @Test
    void jackson3PreservesTheApiResponseAndIsActiveFieldNames() throws Exception {
        String json = JsonMapper.shared().writeValueAsString(ApiResponse.success("OK",
                new UserResponse(7L, "Customer", "customer@test.com", "0900000001",
                        null, Role.CUSTOMER, true)));

        assertTrue(json.contains("\"success\":true"));
        assertTrue(json.contains("\"message\":\"OK\""));
        assertTrue(json.contains("\"data\":"));
        assertTrue(json.contains("\"isActive\":true"));
    }
}
