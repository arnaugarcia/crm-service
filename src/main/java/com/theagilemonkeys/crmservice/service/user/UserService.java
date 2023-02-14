package com.theagilemonkeys.crmservice.service.user;

import com.theagilemonkeys.crmservice.service.user.dto.UserDTO;
import com.theagilemonkeys.crmservice.service.user.request.UserRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    UserDTO create(UserRequest userRequest);

    List<UserDTO> findAll(Pageable pageable);
}
