package com.theagilemonkeys.crmservice.service.user.mapper;

import com.theagilemonkeys.crmservice.domain.User;
import com.theagilemonkeys.crmservice.service.user.dto.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDTO toDTO(User user) {
        return UserDTO.builder()
                .name(user.name())
                .surname(user.surname())
                .email(user.email())
                .imageUrl(user.imageUrl())
                .roles(user.authoritiesAsArray())
                .build();
    }
}
