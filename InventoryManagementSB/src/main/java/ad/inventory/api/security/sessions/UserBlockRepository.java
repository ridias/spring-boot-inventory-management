package ad.inventory.api.security.sessions;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBlockRepository extends JpaRepository<UserBlock, Long> {

	@Query(value = "SELECT * "
			+ "FROM t_users_blocks "
			+ "WHERE block_start_time >= ?3 AND "
			+ "((?1 IS NULL AND id_user IS NULL) OR id_user = ?1) AND "
			+ "((?2 IS NULL AND ip IS NULL) OR ip = ?2) "
			+ "ORDER BY block_start_time DESC", nativeQuery = true)
	List<UserBlock> getActiveBlocks(Long idUser, String ip, LocalDateTime since);
	
	@Query(value = "SELECT * "
			+ "FROM t_users_blocks "
			+ "WHERE block_start_time >= ?2 AND "
			+ "((?1 IS NULL AND id_user IS NULL) OR id_user = ?1) "
			+ "ORDER BY block_start_time DESC", nativeQuery = true)
	List<UserBlock> getActiveBlocks(Long idUser, LocalDateTime since);
}
