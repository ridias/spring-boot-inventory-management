package ad.inventory.api.users;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPasswordUsedRepository extends JpaRepository<UserPasswordUsed, Long>{

	@Query(value = "SELECT id as id, password_used as passwordUsed, created_at as createdAt, id_user as idUser "
			+ "FROM users_password_used "
			+ "WHERE id_user = ?1 "
			+ "ORDER BY created_at", nativeQuery = true)
	List<UserPasswordUsedMinimal> getAllByIdUser(Long idUser);
	
	@Query(value = "SELECT * "
			+ "FROM users_password_used "
			+ "WHERE id_user = ?1 "
			+ "ORDER BY created_at ASC "
			+ "LIMIT 1", nativeQuery = true)
	List<UserPasswordUsed> getOldestPasswordUsedByIdUser(Long idUser);
}
