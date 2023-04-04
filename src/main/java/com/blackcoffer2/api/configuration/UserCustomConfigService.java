package com.blackcoffer2.api.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.blackcoffer2.api.Entitys.User;
import com.blackcoffer2.api.Repositorys.UserRepository;

@Service
public class UserCustomConfigService implements UserDetailsService {
	@Autowired
	UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub

		try {
			User user = userRepository.findByName(username).get();
			//System.out.println("User name :  " + username);
			if (user == null) {
				throw new UsernameNotFoundException("Invalid User name or passeord !!");
			} else {

				return new UserCustomConfig(user);

			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}

		return null;
	}

}
