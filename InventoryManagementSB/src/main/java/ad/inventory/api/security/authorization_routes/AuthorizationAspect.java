package ad.inventory.api.security.authorization_routes;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import ad.inventory.api.security.CookieHelper;
import ad.inventory.api.security.JwtUtils;
import ad.inventory.api.security.authorities.AuthorityService;
import ad.inventory.api.security.authorities.RequestCheckMultiplePermissionsDto;
import ad.inventory.api.security.authorities.RequestCheckPermissionDto;
import ad.inventory.shared.env.GeneralErrors;
import ad.inventory.shared.exceptions.ForbiddenThrowable;

@Aspect
@Component
public class AuthorizationAspect {

	@Autowired
	private AuthorityService authorityService;
	@Autowired
	private JwtUtils jwtUtils;
	@Autowired
	private CookieHelper cookieHelper;
	
	@Around("@annotation(requirePermission)")
    public Object validateSinglePermission(ProceedingJoinPoint joinPoint, RequirePermission requirePermission) throws Throwable {
		
        String token = this.getTokenCookieFromRequest();
        String groupName = jwtUtils.getRoleNameFromJwtToken(token);
        
        var request = new RequestCheckPermissionDto();
        request.setGroupName(groupName);
        request.setPermissionId(requirePermission.permissionId());
        request.setAction(requirePermission.action());
        
        boolean hasPermission = authorityService.doesHavePermission(request);
        
        if (!hasPermission) {
            throw new ForbiddenThrowable(
                GeneralErrors.ERR_403_SERVICE_ACCESS_RESTRICTED_CODE, 
                GeneralErrors.ERR_403_SERVICE_ACCESS_RESTRICTED_MSG
            );
        }
        
        return joinPoint.proceed();
    }
	
    @Around("@annotation(requireAnyPermission)")
    public Object validateMultiplePermissions(ProceedingJoinPoint joinPoint, RequireAnyPermission requireAnyPermission) throws Throwable {
        String token = this.getTokenCookieFromRequest();
        String groupName = jwtUtils.getRoleNameFromJwtToken(token);
        
        var request = new RequestCheckMultiplePermissionsDto();
        request.setNameGroup(groupName);
        request.setAction(requireAnyPermission.action());
        request.setIdPermissions(Arrays.stream(requireAnyPermission.permissionIds())
                .boxed()
                .toList());
        
        var response = authorityService.doesHaveAtLeastOnePermission(request);
        
        if (!response.isSuccess() || !Boolean.TRUE.equals(response.getItems().get(0))) {
            throw new ForbiddenThrowable(
            	GeneralErrors.ERR_403_SERVICE_ACCESS_RESTRICTED_CODE, 
                GeneralErrors.ERR_403_SERVICE_ACCESS_RESTRICTED_MSG
            );
        }
        
        return joinPoint.proceed();
    }
    
    private String getTokenCookieFromRequest() throws Throwable {
    	ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return this.cookieHelper.getToken(attributes.getRequest());
    }
}
