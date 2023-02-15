package com.theagilemonkeys.crmservice.web.rest;

import com.theagilemonkeys.crmservice.service.user.UserService;
import com.theagilemonkeys.crmservice.service.user.dto.UserDTO;
import com.theagilemonkeys.crmservice.service.user.request.UserRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@AllArgsConstructor
public class UserResource {
    private static final Logger log = getLogger(UserResource.class);
    private final UserService userService;

    /**
     * GET  /users : get all the users.
     * @return the ResponseEntity with status 200 (OK) and the list of users in body
     */
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers(Pageable pageable) {
        log.info("REST request to get all Users");
        return ResponseEntity.ok(userService.findAll(pageable));
    }

    /**
     * POST  /users : Create a new user.
     * @param userRequest the request to create the user
     * @return the ResponseEntity with status 201 (Created) and with body the new user
     */
    @PostMapping("/users")
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserRequest userRequest) {
        log.info("REST request to create a user : {}", userRequest);
        UserDTO result = userService.create(userRequest);
        return ResponseEntity.status(CREATED).body(result);
    }

}
