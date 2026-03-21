package ad.inventory.api.roles;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

	Optional<Role> findByName(String name);
	List<Role> findAllByHidden(Boolean hidden);
	
	@Query(value = "SELECT * FROM t_groups as g WHERE g.name = ?1", nativeQuery = true)
	List<Role> getAllByName(String name);
	
	@Query(value = "SELECT * FROM t_groups as g where g.hidden = ?1", nativeQuery = true)
	List<Role> getAllByHidden(Boolean hidden);
	
	@Query(value = "SELECT COUNT(*) "
			+ "FROM t_users as tu, t_groups as tg "
			+ "WHERE tu.id_group = tg.id AND tg.id = ?1", nativeQuery = true)
	Long getTotalUsersByIdGroup(Long idGroup);
	
	@Query(value = "SELECT g.id AS id, g.name AS name, g.description as description, COUNT(u.id) AS total "
			+ "FROM t_groups as g "
			+ "LEFT JOIN t_users as u ON g.id = u.id_group "
			+ "WHERE g.hidden = 'f' "
			+ "GROUP BY g.id, g.name", nativeQuery = true)
	Page<RoleByTotalUsers> findUserCount(Pageable p);
	
	@Query(value = "SELECT g.id AS id, g.name AS name, g.description as description, COUNT(u.id) AS total "
			+ "FROM t_groups as g "
			+ "LEFT JOIN t_users as u ON g.id = u.id_group "
			+ "WHERE (lower(g.name) LIKE lower(CONCAT('%', ?1, '%')) or lower(g.description) LIKE lower(CONCAT('%', ?1, '%'))) AND g.hidden = 'f' "
			+ "GROUP BY g.id, g.name", nativeQuery = true)
	Page<RoleByTotalUsers> findUserCount(String search, Pageable p);
	
	@Query(value = "SELECT COUNT(*) FROM t_groups WHERE hidden = 'f'", nativeQuery = true)
	Long getTotal();
	
	@Query(value = "SELECT COUNT(*) "
			+ "FROM t_groups "
			+ "WHERE (lower(name) LIKE lower(CONCAT('%', ?1, '%')) or lower(description) LIKE lower(CONCAT('%', ?1, '%'))) AND hidden = 'f'", nativeQuery = true)
	Long getTotal(String search);
}
