package ad.inventory.api.wirehouses;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;

@Repository
public interface WirehouseRepository extends JpaRepository<Wirehouse, Long>, JpaSpecificationExecutor<Wirehouse> {

	@Modifying
	@Transactional
	@Query(value = "UPDATE wirehouses SET is_deleted = 't', deleted_at = now() WHERE id = ?1", nativeQuery = true)
	void updateAsDeletedById(Long id);
}
