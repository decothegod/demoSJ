package com.example.demoSmartJob.service.user;

import com.example.demoSmartJob.dto.UserDTO;
import com.example.demoSmartJob.dto.request.LoginRequest;
import com.example.demoSmartJob.dto.request.UserRequest;

import java.util.List;

public interface UserService {
    UserDTO register(UserRequest request);

    UserDTO login(LoginRequest request);

    List<UserDTO> getAllUsers();

    UserDTO getUserByUUID(String UUID);
}
