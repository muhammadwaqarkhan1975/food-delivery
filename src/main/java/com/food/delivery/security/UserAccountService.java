package com.food.delivery.security;

import com.food.delivery.domain.UserAccount;
import com.food.delivery.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;


public class UserAccountService implements UserDetailsService
{
    private final UserAccountRepository userAccountRepository;



    @Autowired
    public UserAccountService(UserAccountRepository userAccountRepository)
    {
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        UserAccount userAccount = userAccountRepository.findUserAccountByEmail(username);

        assert userAccount != null;
        return new SecurityUser(userAccount.getWebId(), userAccount.getKey(), userAccount.getEmail(), userAccount.getPassword(), new ArrayList<>());
    }
}
