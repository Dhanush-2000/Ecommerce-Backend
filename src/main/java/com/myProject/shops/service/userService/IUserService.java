package com.myProject.shops.service.userService;

import com.myProject.shops.dto.UserDto;
import com.myProject.shops.model.User;
import com.myProject.shops.request.CreateUserRequest;
import com.myProject.shops.request.UserUpdateRequest;

public interface IUserService {

    User getUserById(Long userId);
    User creatUser(CreateUserRequest request);
    User updateUser(UserUpdateRequest request,Long userId);
    void deleteUser(Long userId);

    UserDto convertUserToDto(User user);

    User getAuthenticatedUser();
}
