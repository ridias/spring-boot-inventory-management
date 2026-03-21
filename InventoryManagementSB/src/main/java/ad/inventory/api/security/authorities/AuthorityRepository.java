package ad.inventory.api.security.authorities;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;


@Repository
public interface AuthorityRepository extends JpaRepository<Authority, AuthorityId>{

	@Query(
		value = "SELECT pt.name as name, 'read' as authType, rhp.active_read as active " +
		"FROM t_pages as pt, groups_have_permissions rhp, t_groups as rt " +
		"WHERE rt.name = ?1 and pt.name = ?2 and rt.id = rhp.id_group and rhp.id_page = pt.id", 
		nativeQuery = true
	)
	public Optional<AuthorityInfo> getPermissionReadByRoleAndPermissionName(String role, String permissionName);
	
	@Query(
			value = "SELECT pt.name as name, 'read' as authType, rhp.active_read as active " +
			"FROM t_pages as pt, groups_have_permissions rhp, t_groups as rt " +
			"WHERE rt.name = ?1 and pt.id = ?2 and rt.id = rhp.id_group and rhp.id_page = pt.id", 
			nativeQuery = true
	)
	public Optional<AuthorityInfo> getPermissionReadByRoleAndPermissionId(String role, Long permissionId);
		
	
	@Query(
		value = "SELECT pt.name as name, 'create' as authType, rhp.active_create as active " +
		"FROM t_pages as pt, groups_have_permissions rhp, t_groups as rt " +
		"WHERE rt.name = ?1 and pt.name = ?2 and rt.id = rhp.id_group and rhp.id_page = pt.id", 
		nativeQuery = true
	)
	public Optional<AuthorityInfo> getPermissionCreateByRoleAndPermissionName(String role, String permissionName);

	@Query(
		value = "SELECT pt.name as name, 'update' as authType, rhp.active_update as active " +
		"FROM t_pages as pt, groups_have_permissions rhp, t_groups as rt " +
		"WHERE rt.name = ?1 and pt.name = ?2 and rt.id = rhp.id_group and rhp.id_page = pt.id", 
		nativeQuery = true
	)
	public Optional<AuthorityInfo> getPermissionUpdateByRoleAndPermissionName(String role, String permissionName);
		
	@Query(
		value = "SELECT pt.name as name, 'delete' as authType, rhp.active_delete as active " +
		"FROM t_pages as pt, groups_have_permissions rhp, t_groups as rt " +
		"WHERE rt.name = ?1 and pt.name = ?2 and rt.id = rhp.id_group and rhp.id_page = pt.id", 
		nativeQuery = true
	)
	public Optional<AuthorityInfo> getPermissionDeleteByRoleAndPermissionName(String role, String permissionName);
	
	
	@Query(value = "SELECT * FROM groups_have_permissions WHERE id_group = ?1", nativeQuery = true)
	public List<Authority> getAllByIdGroup(Long idGroup);
	
	@Query(value = "SELECT * FROM groups_have_permissions WHERE id_group = ?1 and id_page = ?2", nativeQuery = true)
	public List<Authority> getByIdGroupAndIdPermission(Long idGroup, Long idPermission);
	
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM groups_have_permissions WHERE id_group = ?1", nativeQuery = true)
	public void deleteByIdGroup(Long idGroup);
}