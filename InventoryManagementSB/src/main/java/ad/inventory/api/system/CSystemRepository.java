package ad.inventory.api.system;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;

@Repository
public interface CSystemRepository extends JpaRepository<CSystem, String> {
	
	@Query(value = "SELECT * FROM c_system WHERE key IN ?1", nativeQuery = true)
	List<CSystem> getByListIds(List<String> ids);
	
	@Query(value = "SELECT * FROM c_system WHERE key = ?1", nativeQuery = true)
	Optional<CSystem> getByKey(String key);
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE c_system SET value = ?1 WHERE key = ?2", nativeQuery = true)
	void updateValueByKey(String value, String key);
}
