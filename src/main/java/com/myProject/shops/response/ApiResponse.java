package com.myProject.shops.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse {
    public String message;
    public Object Data;
}
