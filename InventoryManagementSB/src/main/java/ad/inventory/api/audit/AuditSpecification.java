package ad.inventory.api.audit;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import ad.inventory.shared.OrderJpaBuilder;
import ad.inventory.shared.env.GeneralParams;
import jakarta.persistence.criteria.Predicate;

public class AuditSpecification<T> {

	public Specification<T> getFiltered(RequestPaginationAuditDto request){
		return (root, criteriaQuery, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();
			this.addSearchPredicates(request, root, criteriaBuilder, predicates);
			this.addActionPredicates(request, root, criteriaBuilder, predicates);
			this.addUsersPredicates(request, root, criteriaBuilder, predicates);
			this.addRangeDatePredicate(request, root, criteriaBuilder, predicates);
			this.addOrderByClause(request, criteriaQuery, criteriaBuilder, root);
			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		};
	}
	
	public Specification<T> getFiltered(RequestPaginationAuditDto request, List<Long> idUsers){
		return (root, criteriaQuery, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();
			this.addSearchPredicates(request, root, criteriaBuilder, predicates);
			this.addActionPredicates(request, root, criteriaBuilder, predicates);
			this.addUsersPredicates(idUsers, root, predicates);
			this.addRangeDatePredicate(request, root, criteriaBuilder, predicates);
			this.addOrderByClause(request, criteriaQuery, criteriaBuilder, root);
			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		};
	}
	
	private void addSearchPredicates(RequestPaginationAuditDto request, 
			jakarta.persistence.criteria.Root<T> root,
			jakarta.persistence.criteria.CriteriaBuilder criteriaBuilder,
			List<Predicate> predicates) {
		
		if(request.getSearch() != null && !request.getSearch().isEmpty()) {
			predicates.add(criteriaBuilder.or(
				criteriaBuilder.like(criteriaBuilder.lower(root.get(GeneralParams.PARAM_USER).get(GeneralParams.PARAM_USERNAME)), 
						("%" + request.getSearch() + "%").toLowerCase()),
				criteriaBuilder.like(criteriaBuilder.lower(root.get(GeneralParams.PARAM_ACTION).get(GeneralParams.PARAM_NAME)), 
						("%" + request.getSearch() + "%").toLowerCase()),
				criteriaBuilder.like(criteriaBuilder.lower(root.get(GeneralParams.PARAM_IP)), 
						("%" + request.getSearch() + "%").toLowerCase()),
				criteriaBuilder.like(criteriaBuilder.lower(root.get("requestAsText")),
						("%" + request.getSearch() + "%").toLowerCase())
			));
		}
	}

	private void addActionPredicates(RequestPaginationAuditDto request, 
			jakarta.persistence.criteria.Root<T> root,
			jakarta.persistence.criteria.CriteriaBuilder criteriaBuilder,
			List<Predicate> predicates) {
		
		if(request.getIdAction() != null) {
			predicates.add(criteriaBuilder.equal(root.get(GeneralParams.PARAM_ACTION).get(GeneralParams.PARAM_ID), request.getIdAction()));
		}
	}
	
	private void addUsersPredicates(RequestPaginationAuditDto request, 
			jakarta.persistence.criteria.Root<T> root,
			jakarta.persistence.criteria.CriteriaBuilder criteriaBuilder,
			List<Predicate> predicates) {
		
		if(request.getIdUser() != null) {
			predicates.add(criteriaBuilder.equal(root.get(GeneralParams.PARAM_USER).get(GeneralParams.PARAM_ID), request.getIdUser()));
		}
	}
	
	private void addUsersPredicates(List<Long> idUsers, 
			jakarta.persistence.criteria.Root<T> root,
			List<Predicate> predicates) {
		
		if(idUsers != null && !idUsers.isEmpty()) {
			predicates.add(root.get(GeneralParams.PARAM_USER).get(GeneralParams.PARAM_ID).in(idUsers));
		}
	}

	private void addRangeDatePredicate(RequestPaginationAuditDto request, 
			jakarta.persistence.criteria.Root<T> root,
			jakarta.persistence.criteria.CriteriaBuilder criteriaBuilder,
			List<Predicate> predicates) {
		if(request.getStart() != null && request.getEnd() != null) {
			predicates.add(criteriaBuilder.between(root.get(GeneralParams.PARAM_EXECUTED_AT), request.getStart(), request.getEnd()));
		}
	}
	
	
	private void addOrderByClause(RequestPaginationAuditDto request, 
			jakarta.persistence.criteria.CriteriaQuery<?> cq,
            jakarta.persistence.criteria.CriteriaBuilder cb,
            jakarta.persistence.criteria.Root<T> root) {
		
		if(OrderJpaBuilder.tryOrderAsBool(request.getOrderByAction(), root.get(GeneralParams.PARAM_ACTION).get(GeneralParams.PARAM_NAME), cb, cq))
			return;
		
		if(OrderJpaBuilder.tryOrderAsBool(request.getOrderByBrowser(), root.get(GeneralParams.PARAM_BROWSER), cb, cq))
			return;
		
		if(OrderJpaBuilder.tryOrderAsBool(request.getOrderByData(), root.get(GeneralParams.PARAM_EXECUTED_AT), cb, cq))
			return;
		
		if(OrderJpaBuilder.tryOrderAsBool(request.getOrderByIp(), root.get(GeneralParams.PARAM_IP), cb, cq))
			return;
		
		if(OrderJpaBuilder.tryOrderAsBool(request.getOrderByUser(), root.get(GeneralParams.PARAM_USER).get(GeneralParams.PARAM_USERNAME), cb, cq))
			return;
		
		cq.orderBy(cb.asc(root.get(GeneralParams.PARAM_ID)));
	}
}
