package ad.inventory.api.security.authorization_routes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequirePermission {

    long permissionId();
    
    /**
     * Action required (read, create, update, delete)
     */
    String action() default "read";
    String message() default "Access denied";
}
