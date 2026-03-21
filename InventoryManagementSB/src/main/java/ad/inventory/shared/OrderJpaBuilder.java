package ad.inventory.shared;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Path;

public class OrderJpaBuilder {

	private OrderJpaBuilder() {
		
	}
	
	public static void tryOrderAsVoid(String direction, 
			Path<?> path,
            CriteriaBuilder cb, 
            CriteriaQuery<?> query) {
		if (direction == null) return;
		
		if ("ASC".equalsIgnoreCase(direction)) {
			query.orderBy(cb.asc(path));
		} else if ("DESC".equalsIgnoreCase(direction)) {
			query.orderBy(cb.desc(path));
		}
	}
	
	public static boolean tryOrderAsBool(String direction,
            Path<?> path,
            CriteriaBuilder cb,
            CriteriaQuery<?> query) {
		
		if (direction == null) return false;

		if ("ASC".equalsIgnoreCase(direction)) {
			query.orderBy(cb.asc(path));
			return true;
		} else if ("DESC".equalsIgnoreCase(direction)) {
			query.orderBy(cb.desc(path));
			return true;
		}

		return false;
	}
	
	public static boolean tryOrderAsBool(String direction,
			List<Path<?>> paths,
			CriteriaBuilder cb,
			CriteriaQuery<?> query) {
		if(direction == null) return false;
		
		List<Order> orders = new ArrayList<>();
		if(!paths.isEmpty()) {
			for(int i = 0; i < paths.size(); i++) {
				if ("ASC".equalsIgnoreCase(direction)) {
					orders.add(cb.asc(paths.get(i)));
				} else if ("DESC".equalsIgnoreCase(direction)) {
					orders.add(cb.desc(paths.get(i)));
				}
			}
			
			query.orderBy(orders);
			return true;
		}
		
		return false;
	}
	
	public static boolean tryOrderAdd(String direction, 
			Path<?> path, 
			CriteriaBuilder cb, 
			List<Order> orders) {
	    if (direction == null) return false;

	    if ("ASC".equalsIgnoreCase(direction)) {
	        orders.add(cb.asc(path));
	        return true;
	    } else if ("DESC".equalsIgnoreCase(direction)) {
	        orders.add(cb.desc(path));
	        return true;
	    }
	    return false;
	}
	
	public static boolean tryOrderAdd(String direction,
			List<Path<?>> paths,
			CriteriaBuilder cb,
			List<Order> orders) {
		
		if(direction == null) return false;
		
		if(!paths.isEmpty()) {
			for(int i = 0; i < paths.size(); i++) {
				if ("ASC".equalsIgnoreCase(direction)) {
					orders.add(cb.asc(paths.get(i)));
				} else if ("DESC".equalsIgnoreCase(direction)) {
					orders.add(cb.desc(paths.get(i)));
				}
			}
			
			return true;
		}
		
		return false;
	}
}
