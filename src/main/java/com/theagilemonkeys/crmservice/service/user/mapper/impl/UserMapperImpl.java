package com.theagilemonkeys.crmservice.service.user.mapper.impl;

import com.theagilemonkeys.crmservice.domain.User;
import com.theagilemonkeys.crmservice.service.user.dto.UserDTO;
import com.theagilemonkeys.crmservice.service.user.mapper.UserMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements UserMapper {
    @Override
    public UserDTO toDTO(User user) {
        return UserDTO.builder()
                .name(user.name())
                .surname(user.surname())
                .email(user.email())
                .imageUrl(user.imageUrl())
                .build();
    }
}
