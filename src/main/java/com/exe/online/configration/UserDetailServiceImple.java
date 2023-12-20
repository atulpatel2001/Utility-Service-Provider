package com.exe.online.configration;

import com.exe.online.dao.UserRepository;
import com.exe.online.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailServiceImple implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.getUserByUserName(username);
        if (user == null) {
            throw new UsernameNotFoundException("could Not Found !!! ");
        }
        CustomeUserDetail customUserDetail = new CustomeUserDetail(user);
        return customUserDetail;
    }
}
