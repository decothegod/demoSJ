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
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    @Transactional
    public UserDTO register(UserRequest request) {
        checkEmailFormat(request.getEmail());
        checkPasswordFormat(request.getPassword());
        List<PhoneDTO> phoneDTOList = (request.getPhones() != null) ? request.getPhones() : List.of();
        checkPhonesFormat(phoneDTOList);
        checkExistEmail(request.getEmail());

        UserDTO userDTO = UserDTO.builder()
                .name(request.getName())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .created(covertDateStr(System.currentTimeMillis()))
                .lastLogin(covertDateStr(System.currentTimeMillis()))
                .isActive(Boolean.TRUE)
                .phones(phoneDTOList)
                .build();

        userRepository.save(createUser(userDTO));

        return userDTO;
    }

    @Override
    @Transactional
    public UserDTO login(LoginRequest request) {
        try {
            checkEmailFormat(request.getEmail());
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            User user = getUser(request.getEmail());
            updateLastLogin(user);

            UserDTO userDTO = convertToDTO(user);
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

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(this::convertToDTO).toList();
    }

    @Override
    public UserDTO getUserByUUID(String UUID) {
        User user = userRepository.findByUUID(UUID)
                .orElseThrow(() -> new ServiceExceptionNotFound(USER_NOT_FOUND_MSG));
        return convertToDTO(user);
    }

    private UserDTO convertToDTO(User user) {
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        userDTO.setId(user.getUUID());
        return userDTO;
    }

    private User createUser(UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        user.setUUID(generateUUID());
        userDTO.setToken(jwtService.getToken(user));
        userDTO.setId(user.getUUID());
        user.setPhones(userDTO.getPhones().stream().map(phoneDTO -> modelMapper.map(phoneDTO, Phone.class)).toList());
        return user;
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ServiceExceptionNotFound(USER_NOT_FOUND_MSG));
    }

    private void updateLastLogin(User user) {
        user.setLastLogin(covertDateStr(System.currentTimeMillis()));
        userRepository.save(user);
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

    private void checkPhonesFormat(List<PhoneDTO> phones) {
        if (phones != null) {
            phones.stream()
                    .filter(phoneDTO -> !StringUtils.isNumeric(phoneDTO.getNumber()))
                    .findFirst()
                    .ifPresent(phoneDTO -> {
                        log.error(INVALID_PHONE_NUMBER_FORMAT_MSG + phoneDTO.getNumber());
                        throw new ServiceExceptionBadRequest(INVALID_PHONE_NUMBER_FORMAT_MSG + phoneDTO.getNumber());
                    });

            phones.stream()
                    .filter(phoneDTO -> !StringUtils.isNumeric(phoneDTO.getCitycode()))
                    .findFirst()
                    .ifPresent(phoneDTO -> {
                        log.error(INVALID_CITY_CODE_FORMAT_MSG + phoneDTO.getCitycode());
                        throw new ServiceExceptionBadRequest(INVALID_CITY_CODE_FORMAT_MSG + phoneDTO.getCitycode());
                    });
        }
    }
}
