package com.example.demoSmartJob.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO implements Serializable {
    private String id;
    private String name;
    private String email;
    private String password;
    private List<PhoneDTO> phones;
    private String created;
    private String modified;
    private String lastLogin;
    private String token;
    private boolean isActive;

}
