package com.example.demoSmartJob.service.user;

import com.example.demoSmartJob.dto.PhoneDTO;
import com.example.demoSmartJob.dto.UserDTO;
import com.example.demoSmartJob.dto.request.LoginRequest;
import com.example.demoSmartJob.dto.request.UserRequest;
import com.example.demoSmartJob.exception.ServiceExceptionBadRequest;
import com.example.demoSmartJob.exception.ServiceExceptionNotFound;
import com.example.demoSmartJob.exception.ServiceExceptionUnauthorized;
import com.example.demoSmartJob.model.Phone;
import com.example.demoSmartJob.model.User;
import com.example.demoSmartJob.repository.UserRepository;
import com.example.demoSmartJob.service.jwt.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.example.demoSmartJob.util.Constants.*;
import static com.example.demoSmartJob.util.UtilsServices.*;


@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Value("${passwordRegexPattern.regexp}")
    private String passwordRegex;
    @Value("${emailRegexPattern.regexp}")
    private String emailRegex;

    @Override
    public UserDTO register(UserRequest request) {
        checkExistEmail(request.getEmail());
        checkEmailFormat(request.getEmail());
        checkPasswordFormat(request.getPassword());

        List<Phone> phones = new ArrayList<>();
        for (PhoneDTO phoneDTO : request.getPhones()) {
            phones.add(modelMapper.map(phoneDTO, Phone.class));
        }

        UserDTO userDTO = UserDTO.builder()
                .name(request.getName())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .created(covertDateStr(System.currentTimeMillis()))
                .lastLogin(covertDateStr(System.currentTimeMillis()))
                .isActive(Boolean.TRUE)
                .phones(request.getPhones())
                .build();

        User user = convertToEntity(userDTO);
        user.setUUID(generateUUID());
        user.setPhones(phones);
        userDTO.setToken(jwtService.getToken(user));
        userDTO.setId(user.getUUID());
        userRepository.save(user);


        return userDTO;
    }

    @Override
    public UserDTO login(LoginRequest request) {
        try {
            User user = getUser(request.getEmail());

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            UserDTO userDTO = convertToDto(user);
            updateLastLogin(request.getEmail(), userDTO);
            userDTO.setToken(jwtService.getToken(user));
            userDTO.setId(user.getUUID());

            return userDTO;

        } catch (ExpiredJwtException e) {
            log.error(e.getMessage());
            throw new ServiceExceptionUnauthorized(e.getMessage());
        } catch (AuthenticationException e) {
            log.error(BAD_CREDENTIALS_MSG);
            throw new ServiceExceptionUnauthorized(BAD_CREDENTIALS_MSG);
        }
    }

    private UserDTO convertToDto(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    private User convertToEntity(UserDTO userDto) {
        return modelMapper.map(userDto, User.class);
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ServiceExceptionNotFound(USER_NOT_FOUND_MSG));
    }

    private void updateLastLogin(String username, UserDTO userDTO) {
        if (userRepository.findByEmail(username).isPresent()) {
            userRepository.findByEmail(username)
                    .map(user -> {
                        user.setLastLogin(covertDateStr(System.currentTimeMillis()));
                        userDTO.setLastLogin(user.getLastLogin());
                        return userRepository.save(user);
                    });
        }
    }

    private void checkExistEmail(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            log.error(USER_EXIST_MSG);
            throw new ServiceExceptionBadRequest(USER_EXIST_MSG);
        }
    }

    private void checkEmailFormat(String email) {
        if (!validateValueWithRegex(email, emailRegex)) {
            log.error(INVALID_FORMAT_EMAIL_MSG);
            throw new ServiceExceptionBadRequest(INVALID_FORMAT_EMAIL_MSG);
        }
    }

    private void checkPasswordFormat(String password) {
        if (!validateValueWithRegex(password, passwordRegex)) {
            log.error(INVALID_FORMAT_PASSWORD_MSG);
            throw new ServiceExceptionBadRequest(INVALID_FORMAT_PASSWORD_MSG);
        }
    }

}
