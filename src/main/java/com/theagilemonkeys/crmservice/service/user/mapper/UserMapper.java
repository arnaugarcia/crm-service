package com.theagilemonkeys.crmservice.service.user.mapper;

import com.theagilemonkeys.crmservice.domain.User;
import com.theagilemonkeys.crmservice.service.user.dto.UserDTO;

public interface UserMapper {
    UserDTO toDTO(User user);
}
