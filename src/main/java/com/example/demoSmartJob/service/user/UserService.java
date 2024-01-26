package com.example.demoSmartJob.service.user;

import com.example.demoSmartJob.dto.UserDTO;
import com.example.demoSmartJob.dto.request.LoginRequest;
import com.example.demoSmartJob.dto.request.UserRequest;

public interface UserService {
    UserDTO register(UserRequest request);

    UserDTO login(LoginRequest request);
}
