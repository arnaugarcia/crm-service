package com.theagilemonkeys.crmservice.service.user;

import com.theagilemonkeys.crmservice.service.user.dto.UserDTO;
import com.theagilemonkeys.crmservice.service.user.request.UserRequest;

public interface UserService {

    UserDTO create(UserRequest userRequest);
}
