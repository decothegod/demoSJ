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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {
    private static final String TEST_NAME = "userTest";
    private static final String TEST_EMAIL = "email@test.org";
    private static final String TEST_EMAIL_WRONG = "email@test";
    private static final String TEST_NUMBER = "123467";
    private static final String TEST_CITY_CODE = "1";
    private static final String TEST_CONTRY_CODE = "57";
    private static final String TEST_PASSWORD = "Password12";
    private static final String TEST_PASSWORD_WRONG = "invalidPassword";
    private static final String ENCODED_PASSWORD = "passwordEncoder";
    private static final String TOKEN = "token";
    private static final String TIMESTAMP = "26-01-2024 12:00:00";
    private static final String TEST_UUID = "0e0007b9-b96a-4c8e-b8af-74715e6ff3f2";

    @InjectMocks
    private UserServiceImpl userServiceImpl;
    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtService jwtService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private ModelMapper mapper;

    private User createUser() {
        Phone phone = Phone.builder()
                .id(1L)
                .number(Long.parseLong(TEST_NUMBER))
                .citycode(Integer.parseInt(TEST_CITY_CODE))
                .contrycode(TEST_CONTRY_CODE)
                .build();

        return User.builder()
                .id(1L)
                .UUID(TEST_UUID)
                .name(TEST_NAME)
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .phones(Collections.singletonList(phone))
                .created(TIMESTAMP)
                .modified("")
                .lastLogin(TIMESTAMP)
                .isActive(Boolean.TRUE)
                .build();
    }

    @Test
    public void register_Successful() {
        PhoneDTO phoneDTO = PhoneDTO.builder()
                .number(TEST_NUMBER)
                .citycode(TEST_CITY_CODE)
                .contrycode(TEST_CONTRY_CODE)
                .build();

        UserRequest request = UserRequest.builder()
                .name(TEST_NAME)
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .phones(Collections.singletonList(phoneDTO))
                .build();

        when(passwordEncoder.encode(anyString())).thenReturn(ENCODED_PASSWORD);
        when(mapper.map(any(UserDTO.class), eq(User.class))).thenReturn(createUser());

        UserDTO response = userServiceImpl.register(request);
        assertNotNull(response);
        assertEquals(TEST_NAME, response.getName());
        assertEquals(TEST_EMAIL, response.getEmail());
        assertEquals(ENCODED_PASSWORD, response.getPassword());
    }

    @Test(expected = ServiceExceptionBadRequest.class)
    public void register_UserExists() {
        PhoneDTO phoneDTO = PhoneDTO.builder()
                .number(TEST_NUMBER)
                .citycode(TEST_CITY_CODE)
                .contrycode(TEST_CONTRY_CODE)
                .build();

        UserRequest request = UserRequest.builder()
                .name(TEST_NAME)
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .phones(Collections.singletonList(phoneDTO))
                .build();

        User user = createUser();

        UserDTO userDTO = UserDTO.builder().build();

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.ofNullable(user));
        userServiceImpl.register(request);
    }

    @Test(expected = ServiceExceptionBadRequest.class)
    public void register_InvalidEmail() {
        PhoneDTO phoneDTO = PhoneDTO.builder()
                .number(TEST_NUMBER)
                .citycode(TEST_CITY_CODE)
                .contrycode(TEST_CONTRY_CODE)
                .build();

        UserRequest request = UserRequest.builder()
                .name(TEST_NAME)
                .email(TEST_EMAIL_WRONG)
                .password(TEST_PASSWORD)
                .phones(Collections.singletonList(phoneDTO))
                .build();

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        userServiceImpl.register(request);
    }

    @Test(expected = ServiceExceptionBadRequest.class)
    public void register_register_InvalidPassword() {
        PhoneDTO phoneDTO = PhoneDTO.builder()
                .number(TEST_NUMBER)
                .citycode(TEST_CITY_CODE)
                .contrycode(TEST_CONTRY_CODE)
                .build();

        UserRequest request = UserRequest.builder()
                .name(TEST_NAME)
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD_WRONG)
                .phones(Collections.singletonList(phoneDTO))
                .build();

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        userServiceImpl.register(request);
    }

    @Test
    public void login_Successful() {
        LoginRequest request = LoginRequest.builder()
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .build();

        User user = createUser();

        PhoneDTO phoneDTO = PhoneDTO.builder()
                .number(TEST_NUMBER)
                .citycode(TEST_CITY_CODE)
                .contrycode(TEST_CONTRY_CODE)
                .build();

        UserDTO userDTO = UserDTO.builder()
                .id(TEST_UUID)
                .name(TEST_NAME)
                .email(TEST_EMAIL)
                .password(ENCODED_PASSWORD)
                .phones(Collections.singletonList(phoneDTO))
                .created(TIMESTAMP)
                .modified("")
                .lastLogin(TIMESTAMP)
                .token(TOKEN)
                .isActive(Boolean.TRUE)
                .build();

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.ofNullable(user));
        when(jwtService.getToken(user)).thenReturn(TOKEN);
        when(mapper.map(user, UserDTO.class)).thenReturn(userDTO);

        UserDTO response = userServiceImpl.login(request);
        assertNotNull(response);
        assertEquals(TEST_UUID, response.getId());
        assertEquals(TEST_NAME, response.getName());
        assertEquals(TEST_EMAIL, response.getEmail());
        assertEquals(ENCODED_PASSWORD, response.getPassword());
        assertEquals(TOKEN, response.getToken());

    }

    @Test(expected = ServiceExceptionNotFound.class)
    public void login_UserNotFound() {
        LoginRequest request = LoginRequest.builder()
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD)
                .build();

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        userServiceImpl.login(request);
    }

    @Test(expected = ServiceExceptionUnauthorized.class)
    public void login_PasswordIncorrect() {
        LoginRequest request = LoginRequest.builder()
                .email(TEST_EMAIL)
                .password(TEST_PASSWORD_WRONG)
                .build();

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(createUser()));
        when(authenticationManager.authenticate(usernamePasswordAuthenticationToken))
                .thenThrow(BadCredentialsException.class);

        userServiceImpl.login(request);
    }
}

