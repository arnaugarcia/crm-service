package com.theagilemonkeys.crmservice.service.user;

import com.theagilemonkeys.crmservice.service.user.dto.UserDTO;
import com.theagilemonkeys.crmservice.service.user.request.UpdateUserRequest;
import com.theagilemonkeys.crmservice.service.user.request.UserRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    /**
     * Create a new user using the given request
     * @param userRequest the request to create the user
     * @return the created user
     */
    UserDTO create(UserRequest userRequest);

    /**
     * Update the user with the given id using the given request
     * @param id the id of the user to update
     * @param userRequest the request to update the user
     * @return the updated user
     */
    UserDTO update(Long id, UpdateUserRequest userRequest);

    List<UserDTO> findAll(Pageable pageable);
}
