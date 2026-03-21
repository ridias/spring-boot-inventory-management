package ad.inventory.api.users;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import jakarta.transaction.Transactional;

public interface UserLocalRepository extends JpaRepository<UserLocal, Long>, JpaSpecificationExecutor<UserLocal>{

	List<UserLocal> findByIsDeletedOrderByUsernameAsc(Boolean isDeleted);
	List<UserLocal> findAllByUsername(String username);
	List<UserLocal> findAllByEmail(String email);
	
	Optional<UserLocal> findByEmailAndActiveAndIsDeleted(String email, Boolean active, Boolean isDeleted);
	
	List<UserLocal> findAllByRecoveryTokenAndActiveAndIsDeleted(String recoveryToken, Boolean active, Boolean isDeleted);
	
	@Query(value = "SELECT * FROM t_users WHERE is_deleted = ?1 order by username asc", nativeQuery = true)
	List<UserLocal> getAllByIsDeleted(Boolean isDeleted);
	
	@Query(value = "SELECT id as id, last_password_renovation as lastPasswordRenovation "
			+ "FROM t_users "
			+ "WHERE id = ?1", nativeQuery = true)
	List<UserLastPasswordRenovation> getLastPasswordRenovationById(Long idUser);
	
	@Modifying @Transactional
	@Query(value = "UPDATE t_users SET last_session = now() WHERE id = ?1", nativeQuery = true)
	void updateLastSessionById(Long id);
	
	@Modifying @Transactional
	@Query(value = "UPDATE t_users SET password = ?1 WHERE id = ?2", nativeQuery = true)
	void updatePasswordById(String password, Long id);
	
	@Modifying @Transactional
	@Query(value = "UPDATE t_users SET is_deleted = 't', deleted_at = now(), id_group = 0 WHERE id = ?1", nativeQuery = true)
	void updateAsDeleted(Long id);
	
	@Modifying @Transactional
	@Query(value = "UPDATE t_users SET username = ?1, email = ?2 WHERE id = ?3", nativeQuery = true)
	void updateUsernameAndEmailById(String username, String email, Long id);
	
	@Modifying @Transactional
	@Query(value = "UPDATE t_users SET code_2fa = ?1, last_time_generated_2fa = ?2 WHERE id = ?3", nativeQuery = true)
	void updateCode2faAndLastTimeGenerated2faById(String code, LocalDateTime lastTime, Long id);
	
	@Modifying @Transactional
	@Query(value = "UPDATE t_users SET retries_2fa = ?1 WHERE id = ?2", nativeQuery = true)
	void updateRetries2faById(Integer retries, Long id);
	
	@Modifying @Transactional
	@Query(value = "UPDATE t_users SET last_email_sent = now(), recovery_token = ?2 WHERE id = ?1", nativeQuery = true)
	void updateLastEmailSentToNowAndTokenById(Long id, String token);
	
	@Modifying @Transactional
	@Query(value = "UPDATE t_users SET last_password_renovation = ?1 WHERE id = ?2", nativeQuery = true)
	void updateLastRenovationPassword(LocalDateTime datetime, Long id);
}
