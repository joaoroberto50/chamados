package br.com.bugbuilder.chamado.configs.security;


import javax.transaction.Transactional;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.bugbuilder.chamado.models.UserModel;
import br.com.bugbuilder.chamado.repository.UserRepository;

@Service
@Transactional
public class UserDetailsConfig implements UserDetailsService{
	
	final UserRepository userRepository;
	
	public UserDetailsConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserModel userModel = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User " + username + "not found"));
		
		return new User(userModel.getUsername(), userModel.getPassword(), true, true, true, true, userModel.getAuthorities());
	}

}
