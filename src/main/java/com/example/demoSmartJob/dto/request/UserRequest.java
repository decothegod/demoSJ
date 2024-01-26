package com.example.demoSmartJob.dto.request;

import com.example.demoSmartJob.dto.PhoneDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    String name;
    String password;
    String email;
    List<PhoneDTO> phones;
}
