package ad.inventory.api.actions;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionRepository extends JpaRepository<Action, Long> {

	List<Action> findAllByIsDeletedOrderByNameAsc(Boolean isDeleted);
}
