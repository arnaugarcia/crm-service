package com.theagilemonkeys.crmservice.service.user.impl;

import com.theagilemonkeys.crmservice.domain.User;
import com.theagilemonkeys.crmservice.repository.AuthorityRepository;
import com.theagilemonkeys.crmservice.repository.UserRepository;
import com.theagilemonkeys.crmservice.service.user.UserService;
import com.theagilemonkeys.crmservice.service.user.dto.UserDTO;
import com.theagilemonkeys.crmservice.service.user.execption.ImmutableUser;
import com.theagilemonkeys.crmservice.service.user.execption.UserAlreadyExists;
import com.theagilemonkeys.crmservice.service.user.execption.UserNotFound;
import com.theagilemonkeys.crmservice.service.user.mapper.UserMapper;
import com.theagilemonkeys.crmservice.service.user.request.UpdateUserRequest;
import com.theagilemonkeys.crmservice.service.user.request.UserRequest;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;

import static com.theagilemonkeys.crmservice.config.AuthoritiesConstants.DEFAULT_USER;
import static com.theagilemonkeys.crmservice.config.AuthoritiesConstants.USER;
import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDTO create(UserRequest userRequest) {
        userRepository.findOneByEmail(userRequest.email())
                .ifPresent(userAlreadyExistsException());

        User user = new User();
        user.setEmail(userRequest.email());
        user.setName(userRequest.name());
        user.setSurname(userRequest.surname());
        user.setPassword(passwordEncoder.encode(userRequest.password()));
        user.addAuthority(authorityRepository.findById(USER).orElseThrow());
        user.setCreatedBy(DEFAULT_USER);

        return saveAndTransformToDTO(user);
    }

    @Override
    public UserDTO update(Long id, UpdateUserRequest userRequest) {
        User user = userRepository.findById(id).orElseThrow(UserNotFound::new);
        if (user.isSuperAdmin()) {
            throw new ImmutableUser();
        }

        user.setName(userRequest.name());
        user.setSurname(userRequest.surname());
        user.setImageUrl(userRequest.imageUrl());

        /**
         * TODO: This is not the best way to get the current user, extract this
         */
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        user.setCreatedBy(principal.getUsername());

        return saveAndTransformToDTO(user);
    }

    private UserDTO saveAndTransformToDTO(User user) {
        return userMapper.toDTO(userRepository.save(user));
    }

    @Override
    public List<UserDTO> findAll(Pageable pageable) {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDTO)
                .collect(toList());
    }

    private static Consumer<User> userAlreadyExistsException() {
        return user -> {
            throw new UserAlreadyExists();
        };
    }
}