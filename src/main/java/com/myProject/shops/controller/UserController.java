package com.myProject.shops.controller;

import com.myProject.shops.dto.UserDto;
import com.myProject.shops.exceptions.AlreadyExistsException;
import com.myProject.shops.exceptions.ResourceNotFoundException;
import com.myProject.shops.model.User;
import com.myProject.shops.request.CreateUserRequest;
import com.myProject.shops.request.UserUpdateRequest;
import com.myProject.shops.response.ApiResponse;
import com.myProject.shops.service.userService.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/users")
public class UserController {
    private final IUserService userService;
    @GetMapping("/{userId}/getUser")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long userId){
        try {
            User user = userService.getUserById(userId);
            UserDto userDto = userService.convertUserToDto(user);
            return ResponseEntity.ok(new ApiResponse("user found",userDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("user not found",e.getMessage()));
        }
    }
    @PostMapping("/createUser")
    public ResponseEntity<ApiResponse> createUser(@RequestBody CreateUserRequest request){
        try {
            User user = userService.creatUser(request);
            UserDto userDto = userService.convertUserToDto(user);
            return ResponseEntity.ok(new ApiResponse("user created successfully",userDto));
        } catch (AlreadyExistsException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new ApiResponse("user already exist",e.getMessage()));
        }
    }

    @PutMapping("/{userId}/updateUser")
    public ResponseEntity<ApiResponse> updateUser(@RequestBody UserUpdateRequest request,@PathVariable Long userId){
        try {
            User user = userService.updateUser(request,userId);
            UserDto userDto = userService.convertUserToDto(user);
            return ResponseEntity.ok(new ApiResponse("user updated successfully",userDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("user not found",e.getMessage()));
        }
    }

    @DeleteMapping("/{userId}/deleteUser")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long userId){
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok(new ApiResponse("user deleted successfully",null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("user not found",e.getMessage()));
        }
    }
}
