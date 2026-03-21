package ad.inventory.api.users;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ad.inventory.api.system.CSystemService;

@Service
public class UserPasswordUsedService {

	@Autowired
	private UserLocalRepository userLocalRepository;
	@Autowired
	private UserPasswordUsedRepository repository;
	@Autowired
	private CSystemService systemService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	/**
	 * Save the user's current password to the password history,
	 * keeping only the maximum number of passwords 
	 * defined in the system configuration.
	 * <p>If the user has reached the limit of stored passwords, 
	 * delete the oldest one before adding the new one.</p>
	 * @param user
	 */
	public void save(UserLocal user) {
		if(user == null) {
			return;
		}
		
		var limitPasswordsReuseRow = systemService.getLimitPasswordsReuse();
		var limitPasswordReuse = Integer.parseInt(limitPasswordsReuseRow.getName());
		var allPasswordsUsed = this.repository.getAllByIdUser(user.getId());
		
		if(allPasswordsUsed.size() >= limitPasswordReuse) {
			this.repository.deleteById(allPasswordsUsed.get(0).getId());
		}
		
		String passwordHashed = user.getPassword();
		var passwordUsed = new UserPasswordUsed();
		passwordUsed.setPasswordUsed(passwordHashed);
		passwordUsed.setCreatedAt(LocalDateTime.now());
		passwordUsed.setUser(user);
		this.repository.save(passwordUsed);
	}
	
	public void save(String passwordHashed, Long idUser) {
		if(passwordHashed == null || idUser == null) {
			return;
		}
		
		var userInDb = this.userLocalRepository.findById(idUser);
		if(userInDb.isEmpty()) {
			return;
		}
		
		var user = userInDb.get();		
		var limitPasswordsReuseRow = systemService.getLimitPasswordsReuse();
		var limitPasswordReuse = Integer.parseInt(limitPasswordsReuseRow.getName());
		var allPasswordsUsed = this.repository.getAllByIdUser(user.getId());
		
		if(allPasswordsUsed.size() >= limitPasswordReuse) {
			this.repository.deleteById(allPasswordsUsed.get(0).getId());
		}
		
		var passwordUsed = new UserPasswordUsed();
		passwordUsed.setPasswordUsed(passwordHashed);
		passwordUsed.setCreatedAt(LocalDateTime.now());
		passwordUsed.setUser(user);
		this.repository.save(passwordUsed);
	}
	
	public void save(Long idUser) {
		if(idUser == null) {
			return;
		}
		
		var userInDb = this.userLocalRepository.findById(idUser);
		if(userInDb.isEmpty()) {
			return;
		}
		
		var user = userInDb.get();		
		var limitPasswordsReuseRow = systemService.getLimitPasswordsReuse();
		var limitPasswordReuse = Integer.parseInt(limitPasswordsReuseRow.getName());
		var allPasswordsUsed = this.repository.getAllByIdUser(user.getId());
		
		if(allPasswordsUsed.size() >= limitPasswordReuse) {
			this.repository.deleteById(allPasswordsUsed.get(0).getId());
		}
		
		var passwordUsed = new UserPasswordUsed();
		passwordUsed.setPasswordUsed(user.getPassword());
		passwordUsed.setCreatedAt(LocalDateTime.now());
		passwordUsed.setUser(user);
		this.repository.save(passwordUsed);
	}
	
	public boolean isPasswordUsed(String passwordPlain, Long idUser) {
		if(passwordPlain == null || idUser == null) {
			return true;
		}
		
		var allPasswordsUsed = this.repository.getAllByIdUser(idUser);
		for(int i = 0; i < allPasswordsUsed.size(); i++) {
			String passwordHashed = allPasswordsUsed.get(i).getPasswordUsed();
			if(passwordEncoder.matches(passwordPlain, passwordHashed)) {
				return true;
			}
		}
		
		return false;
	}
}
