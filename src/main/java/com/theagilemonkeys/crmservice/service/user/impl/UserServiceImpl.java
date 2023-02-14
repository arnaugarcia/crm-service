package com.theagilemonkeys.crmservice.service.user.impl;

import com.theagilemonkeys.crmservice.repository.UserRepository;
import com.theagilemonkeys.crmservice.service.user.UserService;
import com.theagilemonkeys.crmservice.service.user.dto.UserDTO;
import com.theagilemonkeys.crmservice.service.user.mapper.UserMapper;
import com.theagilemonkeys.crmservice.service.user.request.UserRequest;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDTO create(UserRequest userRequest) {
        return null;
    }

    @Override
    public List<UserDTO> findAll(Pageable pageable) {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDTO)
                .collect(toList());
    }
}
