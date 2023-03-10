package com.theagilemonkeys.crmservice.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theagilemonkeys.crmservice.IntegrationTest;
import com.theagilemonkeys.crmservice.domain.Authority;
import com.theagilemonkeys.crmservice.domain.User;
import com.theagilemonkeys.crmservice.repository.UserRepository;
import com.theagilemonkeys.crmservice.service.user.request.UpdateUserRequest;
import com.theagilemonkeys.crmservice.service.user.request.UserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.theagilemonkeys.crmservice.security.AuthoritiesConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@IntegrationTest
@AutoConfigureMockMvc
class UserResourceIT {
    private static final String DEFAULT_EMAIL = "johndoe@localhost";
    private static final String UPDATED_EMAIL = "johndoe2@localhost";
    private static final String DEFAULT_PASSWORD = "P@ssw0rd";
    private static final String DEFAULT_NAME = "John";
    private static final String UPDATED_NAME = "Jane";
    private static final String DEFAULT_SURNAME = "Doe";
    private static final String UPDATED_SURNAME = "Doe2";
    private static final String DEFAULT_IMAGE_URL = "https://placehold.it/50x50";
    private static final String UPDATED_IMAGE_URL = "https://placehold.it/100x100";
    private static final String DEFAULT_ADMIN_EMAIL = "admin@localhost";
    private static final String DEFAULT_SUPER_ADMIN_EMAIL = "sa@localhost";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc restUserMockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private UserRequest request;
    private UpdateUserRequest updateRequest;

    private static final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void init() {
        userRepository.deleteAll();
        request = createUserRequest();
        updateRequest = updateUserRequest();
    }

    private UserRequest createUserRequest() {
        return UserRequest.builder()
                .name(DEFAULT_NAME)
                .surname(DEFAULT_SURNAME)
                .email(DEFAULT_EMAIL)
                .password(DEFAULT_PASSWORD)
                .build();
    }

    private UpdateUserRequest updateUserRequest() {
        return UpdateUserRequest.builder()
                .name(UPDATED_NAME)
                .surname(UPDATED_SURNAME)
                .imageUrl(UPDATED_IMAGE_URL)
                .build();
    }

    private User createDefaultUser() {
        return createDefaultUserByEmail(DEFAULT_EMAIL);
    }

    private User createDefaultUserByEmail(String email) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));
        user.setName(DEFAULT_NAME);
        user.setSurname(DEFAULT_SURNAME);
        user.setImageUrl(DEFAULT_IMAGE_URL);
        user.addAuthority(new Authority(USER));
        return userRepository.save(user);
    }

    private User createDefaultAdmin() {
        User adminUser = createDefaultUserByEmail(DEFAULT_ADMIN_EMAIL);
        adminUser.addAuthority(new Authority(ADMIN));
        return userRepository.save(adminUser);
    }

    private User createDefaultSuperAdmin() {
        User adminUser = createDefaultUserByEmail(DEFAULT_SUPER_ADMIN_EMAIL);
        adminUser.addAuthority(new Authority(ADMIN));
        adminUser.addAuthority(new Authority(SUPER_ADMIN));
        return userRepository.save(adminUser);
    }

    @Test
    @WithMockUser(username = DEFAULT_EMAIL, roles = "ADMIN")
    void should_return_empty_list() throws Exception {
        restUserMockMvc.perform(get("/users")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty())
                .andExpect(content().contentType(APPLICATION_JSON));
    }

    @Test
    void return_users_without_auth() throws Exception {
        restUserMockMvc.perform(get("/users")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = DEFAULT_EMAIL)
    void return_users_as_user() throws Exception {
        restUserMockMvc.perform(get("/users")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = DEFAULT_EMAIL, roles = "ADMIN")
    void should_return_list_with_one_user() throws Exception {
        createDefaultUser();

        restUserMockMvc.perform(get("/users")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value(DEFAULT_EMAIL))
                .andExpect(jsonPath("$[0].name").value(DEFAULT_NAME))
                .andExpect(jsonPath("$[0].surname").value(DEFAULT_SURNAME))
                .andExpect(jsonPath("$[0].imageUrl").value(DEFAULT_IMAGE_URL));
    }


    @Test
    @WithMockUser(username = DEFAULT_EMAIL, roles = "ADMIN")
    void should_create_a_user() throws Exception {
        int databaseSizeBeforeCreate = userRepository.findAll().size();

        restUserMockMvc.perform(post("/users")
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
                .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
                .andExpect(jsonPath("$.surname").value(DEFAULT_SURNAME))
                .andExpect(jsonPath("$.roles").isArray())
                .andExpect(jsonPath("$.roles.length()").value(1));

        List<User> users = userRepository.findAll();
        assertThat(users).hasSize(databaseSizeBeforeCreate + 1);
    }

    @Test
    @WithMockUser(username = DEFAULT_EMAIL, roles = "ADMIN")
    void should_not_update_an_admin() throws Exception {
        User user = createDefaultUser();

        restUserMockMvc.perform(put("/users/{id}", user.id())
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(updateRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
                .andExpect(jsonPath("$.name").value(UPDATED_NAME))
                .andExpect(jsonPath("$.surname").value(UPDATED_SURNAME))
                .andExpect(jsonPath("$.imageUrl").value(UPDATED_IMAGE_URL));
    }

    @Test
    @WithMockUser(username = DEFAULT_EMAIL)
    void should_not_update_a_user_as_user() throws Exception {
        User user = createDefaultUser();

        restUserMockMvc.perform(put("/users/{id}", user.id())
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(updateRequest)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = DEFAULT_EMAIL, roles = "ADMIN")
    void should_update_a_user() throws Exception {
        User user = createDefaultUser();

        restUserMockMvc.perform(put("/users/{id}", user.id())
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(updateRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
                .andExpect(jsonPath("$.name").value(UPDATED_NAME))
                .andExpect(jsonPath("$.surname").value(UPDATED_SURNAME))
                .andExpect(jsonPath("$.imageUrl").value(UPDATED_IMAGE_URL));
    }

    @Test
    @WithMockUser(username = DEFAULT_EMAIL, roles = "ADMIN")
    void should_not_create_a_user_with_existing_email() throws Exception {

        User user = createDefaultUser();

        request = UserRequest.builder()
                .email(user.email())
                .password(DEFAULT_PASSWORD)
                .build();

        restUserMockMvc.perform(post("/users")
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = DEFAULT_EMAIL, roles = "ADMIN")
    void should_not_create_a_user_with_empty_email() throws Exception {

        request = UserRequest.builder()
                .password(DEFAULT_PASSWORD)
                .build();

        restUserMockMvc.perform(post("/users")
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = DEFAULT_EMAIL, roles = "ADMIN")
    void should_not_create_a_user_with_empty_password() throws Exception {

        request = UserRequest.builder()
                .email(DEFAULT_EMAIL)
                .build();

        restUserMockMvc.perform(post("/users")
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = DEFAULT_EMAIL)
    void should_not_create_a_user_with_user_role() throws Exception {
        restUserMockMvc.perform(post("/users")
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = DEFAULT_EMAIL, roles = "ADMIN")
    void should_delete_a_user() throws Exception {
        User user = createDefaultUser();

        int databaseSizeBeforeDelete = userRepository.findAll().size();

        restUserMockMvc.perform(delete("/users/{id}", user.id())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNoContent());

        List<User> users = userRepository.findAll();
        assertThat(users).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @WithMockUser(username = DEFAULT_EMAIL)
    void should_not_delete_a_user_as_user() throws Exception {
        User user = createDefaultUser();

        restUserMockMvc.perform(delete("/users/{id}", user.id())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = DEFAULT_EMAIL, roles = "ADMIN")
    void should_not_delete_that_not_exists() throws Exception {

        restUserMockMvc.perform(delete("/users/{id}", 0)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = DEFAULT_EMAIL)
    void should_not_delete_an_admin_as_user() throws Exception {
        User user = createDefaultUser();
        user.authorities().add(new Authority(ADMIN));
        userRepository.save(user);

        restUserMockMvc.perform(delete("/users/{id}", user.id())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = DEFAULT_EMAIL)
    void should_not_delete_an_super_admin_as_user() throws Exception {
        User user = createDefaultUser();
        user.authorities().add(new Authority(ADMIN));
        user.authorities().add(new Authority(SUPER_ADMIN));
        userRepository.save(user);

        restUserMockMvc.perform(delete("/users/{id}", user.id())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = DEFAULT_ADMIN_EMAIL, roles = "ADMIN")
    void should_transform_to_admin() throws Exception {
        createDefaultAdmin();
        User user = createDefaultUser();

        restUserMockMvc.perform(put("/users/{id}/admin", user.id())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());

        assertThat(userRepository.findOneByEmail(user.email()).get().authorities()).contains(new Authority(ADMIN));
    }

    @Test
    @WithMockUser(username = DEFAULT_EMAIL)
    void should_not_transform_to_admin_as_user() throws Exception {
        createDefaultAdmin();
        User user = createDefaultUser();

        restUserMockMvc.perform(put("/users/{id}/admin", user.id())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = DEFAULT_ADMIN_EMAIL, roles = "ADMIN")
    void should_not_allow_an_admin_to_remove_super_admin_role() throws Exception {
        createDefaultAdmin();
        User superAdmin = createDefaultSuperAdmin();

        restUserMockMvc.perform(put("/users/{id}/admin", superAdmin.id())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}
