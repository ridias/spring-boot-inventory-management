package ad.inventory.api.users;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import ad.inventory.shared.OrderJpaBuilder;
import ad.inventory.shared.env.GeneralParams;
import jakarta.persistence.criteria.Predicate;

public class UserLocalSpecification<T> {

	public Specification<T> getAll(RequestPaginationUserDto request){
		return (root, criteriaQuery, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();
			this.addSearchPredicates(request, root, criteriaBuilder, predicates);
			this.addGroupPredicate(request, root, criteriaBuilder, predicates);
			this.addActivePredicate(request, root, criteriaBuilder, predicates);
			this.addIsDeletedPredicate(false, root, criteriaBuilder, predicates);
			this.addOrderByClause(request, criteriaQuery, criteriaBuilder, root);
			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		};
	}
	
	public Specification<T> getFiltered(RequestPaginationUserDto request, List<Long> idUsers){
		return (root, criteriaQuery, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();
			this.addSearchPredicates(request, root, criteriaBuilder, predicates);
			this.addUserIdsPredicate(idUsers, root, predicates);
			this.addGroupPredicate(request, root, criteriaBuilder, predicates);
			this.addActivePredicate(request, root, criteriaBuilder, predicates);
			this.addIsDeletedPredicate(false, root, criteriaBuilder, predicates);
			this.addOrderByClause(request, criteriaQuery, criteriaBuilder, root);
			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		};
	}
	
	private void addSearchPredicates(RequestPaginationUserDto request, 
			jakarta.persistence.criteria.Root<T> root,
			jakarta.persistence.criteria.CriteriaBuilder criteriaBuilder,
			List<Predicate> predicates) {
		
		if(request.getSearch() != null && !request.getSearch().isEmpty()) {
			predicates.add(criteriaBuilder.or(
					criteriaBuilder.like(criteriaBuilder.lower(root.get(GeneralParams.PARAM_USERNAME)), 
							("%" + request.getSearch() + "%").toLowerCase()), 
					criteriaBuilder.like(criteriaBuilder.lower(root.get(GeneralParams.PARAM_EMAIL)), 
							("%" + request.getSearch() + "%").toLowerCase()), 
					criteriaBuilder.like(criteriaBuilder.lower(root.get(GeneralParams.PARAM_FIRST_NAME)), 
							("%" + request.getSearch() + "%").toLowerCase()), 
					criteriaBuilder.like(criteriaBuilder.lower(root.get(GeneralParams.PARAM_LAST_NAME)), 
							("%" + request.getSearch() + "%").toLowerCase())
			));
		}
	}
	
	private void addGroupPredicate(RequestPaginationUserDto request,
			jakarta.persistence.criteria.Root<T> root,
			jakarta.persistence.criteria.CriteriaBuilder criteriaBuilder,
			List<Predicate> predicates) {
		
		if(request.getIdGroup() != null) {
			predicates.add(criteriaBuilder.equal(root.get(GeneralParams.PARAM_ROLE).get(GeneralParams.PARAM_ID), request.getIdGroup()));
		}
	}
	
	private void addUserIdsPredicate(List<Long> idUsers,
			jakarta.persistence.criteria.Root<T> root,
			List<Predicate> predicates) {
		
		if(idUsers != null && !idUsers.isEmpty()) {
			predicates.add(root.get(GeneralParams.PARAM_ID).in(idUsers));
		}
	}
	
	private void addActivePredicate(RequestPaginationUserDto request,
			jakarta.persistence.criteria.Root<T> root,
			jakarta.persistence.criteria.CriteriaBuilder criteriaBuilder,
			List<Predicate> predicates) {

		if(request.getActive() != null && !request.getActive().isEmpty()) {
			predicates.add(criteriaBuilder.equal(root.get(GeneralParams.PARAM_ACTIVE), request.getActive().equals("t")));
		}
	}

	
	private void addIsDeletedPredicate(boolean isDeleted,
			jakarta.persistence.criteria.Root<T> root,
			jakarta.persistence.criteria.CriteriaBuilder criteriaBuilder,
			List<Predicate> predicates) {
		
		predicates.add(criteriaBuilder.equal(root.get(GeneralParams.PARAM_IS_DELETED), isDeleted));
	}
	
	private void addOrderByClause(RequestPaginationUserDto request, 
			jakarta.persistence.criteria.CriteriaQuery<?> cq,
            jakarta.persistence.criteria.CriteriaBuilder cb,
            jakarta.persistence.criteria.Root<T> root) {
		
		if(OrderJpaBuilder.tryOrderAsBool(request.getOrderById(), root.get(GeneralParams.PARAM_ID), cb, cq))
			return;
		
		if(OrderJpaBuilder.tryOrderAsBool(request.getOrderByUsername(), root.get(GeneralParams.PARAM_USERNAME), cb, cq))
			return;
		
		if(OrderJpaBuilder.tryOrderAsBool(request.getOrderByEmail(), root.get(GeneralParams.PARAM_EMAIL), cb, cq))
			return;
					
		if(OrderJpaBuilder.tryOrderAsBool(request.getOrderByFirstName(), root.get(GeneralParams.PARAM_FIRST_NAME), cb, cq))
			return;
		
		if(OrderJpaBuilder.tryOrderAsBool(request.getOrderByLastName(), root.get(GeneralParams.PARAM_LAST_NAME), cb, cq))
			return;
		
		if(OrderJpaBuilder.tryOrderAsBool(request.getOrderByGroup(), root.get(GeneralParams.PARAM_ROLE).get(GeneralParams.PARAM_ROLE_NAME), cb, cq))
			return;
		
		if(OrderJpaBuilder.tryOrderAsBool(request.getOrderByActive(), root.get(GeneralParams.PARAM_ACTIVE), cb, cq))
			return;
		
		if(OrderJpaBuilder.tryOrderAsBool(request.getOrderByLastSession(), root.get(GeneralParams.PARAM_LAST_SESSION), cb, cq))
			return;
		
		cq.orderBy(cb.asc(root.get(GeneralParams.PARAM_ID)));	
	}
}
