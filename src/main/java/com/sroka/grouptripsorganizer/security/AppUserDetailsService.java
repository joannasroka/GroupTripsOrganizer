package com.sroka.grouptripsorganizer.security;

import com.sroka.grouptripsorganizer.entity.account_activation.AccountStatus;
import com.sroka.grouptripsorganizer.entity.user.User;
import com.sroka.grouptripsorganizer.repository.UserRepository;
import com.sroka.grouptripsorganizer.service.LoginAttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final LoginAttemptService loginAttemptService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        loginAttemptService.checkIfUsernameLocked(username);

        User user = userRepository.findByEmailAndAccountStatus(username, AccountStatus.ACTIVE).orElseThrow(() -> new UsernameNotFoundException(username));

        return new AppUserDetails(user.getId(), user.getEmail(), user.getPassword(),
                true, true, true, true);
    }
}