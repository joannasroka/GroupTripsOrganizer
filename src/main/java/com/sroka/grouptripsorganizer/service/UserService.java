package com.sroka.grouptripsorganizer.service;

import com.sroka.grouptripsorganizer.mapper.UserMapper;
import com.sroka.grouptripsorganizer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AccountActivationService accountActivationService;
}
