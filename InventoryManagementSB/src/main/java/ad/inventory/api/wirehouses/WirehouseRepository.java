package ad.inventory.api.wirehouses;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WirehouseRepository extends JpaRepository<Wirehouse, Long> {

}
