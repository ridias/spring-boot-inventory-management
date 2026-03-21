package ad.inventory.api.security;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("api/master/v1")
public class CsrfController {

	@GetMapping("/csrf-token")
    public CsrfToken csrfToken(HttpServletRequest request, HttpServletResponse response) {
		return (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    }
	
}
