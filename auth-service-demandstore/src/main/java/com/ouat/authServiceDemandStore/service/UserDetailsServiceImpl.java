package com.ouat.authServiceDemandStore.service;


import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Deprecated
public class UserDetailsServiceImpl implements UserDetailsService {
	
 //   @Autowired
   // private UserRepository userRepository;

    @Override
    //@Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) {
       // User user = userRepository.findByEmail(email);
    	// User user = new User();
    	String user = null;
    	 email = null;
    	String password = null;
        if (user == null) throw new UsernameNotFoundException(email);
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        return new org.springframework.security.core.userdetails.User(email, password, grantedAuthorities);
    }

}
