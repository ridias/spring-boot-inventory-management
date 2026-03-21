package ad.inventory.api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ad.inventory.api.users.UserLocalRepository;

@Service
public class UserDetailsServiceSecurity implements org.springframework.security.core.userdetails.UserDetailsService {

	@Autowired
	private UserLocalRepository repository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		var userInDb = this.repository.findByEmailAndActiveAndIsDeleted(username, true, false);
		
		if(userInDb.isEmpty() || userInDb.get() == null) {
			throw new UsernameNotFoundException(username);
		}
		
		return userInDb.get();
	}
}
