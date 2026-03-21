package ad.inventory.api.security.sessions;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionAttemptRepository extends JpaRepository<SessionAttempt, Long>{

	@Query(value = "SELECT * "
			+ "FROM t_session_attempts "
			+ "WHERE id_user = ?1 AND attempt_time >= ?2 "
			+ "ORDER BY attempt_time DESC "
			+ "LIMIT ?3", nativeQuery = true)
	List<SessionAttempt> getLastLimitAttempts(Long idUser, LocalDateTime since, int limit);
}
