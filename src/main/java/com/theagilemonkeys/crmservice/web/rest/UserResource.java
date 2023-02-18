package com.theagilemonkeys.crmservice.web.rest;

import com.theagilemonkeys.crmservice.service.user.UserService;
import com.theagilemonkeys.crmservice.service.user.dto.UserDTO;
import com.theagilemonkeys.crmservice.service.user.request.UpdateUserRequest;
import com.theagilemonkeys.crmservice.service.user.request.UserRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.*;

@RestController
@AllArgsConstructor
public class UserResource {
    private static final Logger log = getLogger(UserResource.class);
    private final UserService userService;

    /**
     * GET  /users : get all the users.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of users in body
     */
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers(Pageable pageable) {
        log.info("REST request to get all Users");
        return ok(userService.findAll(pageable));
    }

    /**
     * POST  /users : Create a new user.
     *
     * @param userRequest the request to create the user
     * @return the ResponseEntity with status 201 (Created) and with body the new user
     */
    @PostMapping("/users")
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserRequest userRequest) {
        log.info("REST request to create a user : {}", userRequest);
        UserDTO result = userService.create(userRequest);
        return status(CREATED).body(result);
    }

    /**
     * PUT  /users : Updates an existing user.
     *
     * @param id          the id of the user to update
     * @param userRequest the request to update the user
     * @return the ResponseEntity with status 201 (Created) and with body the updated user
     */
    @PutMapping("/users/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUserRequest userRequest) {
        log.info("REST request to update a user : {}", userRequest);
        UserDTO result = userService.update(id, userRequest);
        return status(CREATED).body(result);
    }

    /**
     * DELETE  /users/:id : delete the "id" user.
     *
     * @param id the id of the user to delete
     * @return the ResponseEntity with status 204 (NO_CONTENT)
     */
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("REST request to delete a user : {}", id);
        userService.delete(id);
        return noContent().build();
    }

    /**
     * PUT  /users/:id/admin : toggle the admin status of the user
     * @param id the id of the user to toggle
     * @return the ResponseEntity with status 201 (Created) and with body the updated user
     */
    @PutMapping("/users/{id}/admin")
    public ResponseEntity<UserDTO> toggleAdmin(@PathVariable Long id) {
        log.info("REST request to set a user as admin : {}", id);
        UserDTO result = userService.toggleAdmin(id);
        return ok(result);
    }

}
