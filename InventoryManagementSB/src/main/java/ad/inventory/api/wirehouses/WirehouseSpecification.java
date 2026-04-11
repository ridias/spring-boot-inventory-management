package ad.inventory.api.wirehouses;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import ad.inventory.shared.OrderJpaBuilder;
import ad.inventory.shared.env.GeneralParams;
import jakarta.persistence.criteria.Predicate;

public class WirehouseSpecification<T> {
	
	public Specification<T> getFilters(RequestPaginationWirehouseDto request){
		return (root, criteriaQuery, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();
			this.addSearchPredicates(request, root, criteriaBuilder, predicates);
			this.addActivePredicate(request, root, criteriaBuilder, predicates);
			this.addOrderByClause(request, criteriaQuery, criteriaBuilder, root);
			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		};
	}
	
	private void addSearchPredicates(RequestPaginationWirehouseDto request, 
			jakarta.persistence.criteria.Root<T> root,
			jakarta.persistence.criteria.CriteriaBuilder criteriaBuilder,
			List<Predicate> predicates) {
		
		if(request.getSearch() != null && !request.getSearch().isEmpty()) {
			predicates.add(
				criteriaBuilder.or(
					criteriaBuilder.like(
						criteriaBuilder.lower(root.get(GeneralParams.PARAM_NAME)), ("%" + request.getSearch() + "%").toLowerCase()), 
					criteriaBuilder.like(
						criteriaBuilder.lower(root.get(GeneralParams.PARAM_ADDRESS)), ("%" + request.getSearch() + "%").toLowerCase())));
		}
	}

	private void addActivePredicate(RequestPaginationWirehouseDto request,
			jakarta.persistence.criteria.Root<T> root,
			jakarta.persistence.criteria.CriteriaBuilder criteriaBuilder,
			List<Predicate> predicates) {
		
		if(request.getActive() != null) {
			predicates.add(criteriaBuilder.equal(root.get(GeneralParams.PARAM_ACTIVE), request.getActive()));
		}
	}
	
	private void addOrderByClause(RequestPaginationWirehouseDto request,
			jakarta.persistence.criteria.CriteriaQuery<?> cq,
            jakarta.persistence.criteria.CriteriaBuilder cb,
            jakarta.persistence.criteria.Root<T> root) {
		
		if(OrderJpaBuilder.tryOrderAsBool(request.getOrderById(), root.get(GeneralParams.PARAM_ID), cb, cq))
			return;
		
		if(OrderJpaBuilder.tryOrderAsBool(request.getOrderByName(), root.get(GeneralParams.PARAM_NAME), cb, cq))
			return;
		
		if(OrderJpaBuilder.tryOrderAsBool(request.getOrderByActive(), root.get(GeneralParams.PARAM_ACTIVE), cb, cq))
			return;
		
		cq.orderBy(cb.asc(root.get(GeneralParams.PARAM_ID)));
	}
}
