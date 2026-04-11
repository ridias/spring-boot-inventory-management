package ad.inventory.api.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;

import ad.inventory.api.roles.Role;
import ad.inventory.shared.dtos.SimpleDto;
import ad.inventory.shared.env.GeneralVariables;


public class JwtUtils {

	@Value("${inventory.app.jwtSecret}")
	private String jwtSecret;
	@Value("${inventory.app.jwtExpirationMs}")
	private Integer jwtExpiration;
	
	private static final String ISSUER = "inventory-msb";
	private static final String CLAIM_DETAILS = "details";
	
	private static final Logger log = LoggerFactory.getLogger(JwtUtils.class);
	
	public String generateToken(Long userId, String username, String email, Role userRole) {
		return this.generateGlobalToken(userId, username, email, userRole.getId(), userRole.getName());
	}
	
	public String generateToken(Long userId, String username, String email, SimpleDto userRole) {
		return this.generateGlobalToken(userId, username, email, userRole.getId(), userRole.getName());
	}
	
	public String generateToken(Long userId, String username, String email, Long roleId, String roleName) {
		return this.generateGlobalToken(userId, username, email, roleId, roleName);
	}
	
	public String generateRefreshToken(String token) {
		Long userId = this.getUserIdFromJwtToken(token);
		String username = this.getUsernameFromJwtToken(token);
		String email = this.getEmailFromJwtToken(token);
		String roleName = this.getRoleNameFromJwtToken(token);
		Long roleId = this.getRoleIdFromJwtToken(token);
		return this.generateToken(userId, username, email, roleId, roleName);
	}
	
	public String getUsernameFromJwtToken(String token) {
		return this.claimAsString(token, GeneralVariables.KEY_USERNAME);
	}
	
	public String getEmailFromJwtToken(String token) {
		return this.claimAsString(token, GeneralVariables.KEY_EMAIL);
	}
	
	public Long getGroupIdFromJwtToken(String token) {
		return this.claimAsLong(token, GeneralVariables.KEY_ROLE_ID);
	}
	
	public Long getUserIdFromJwtToken(String token) {
		return this.claimAsLong(token, GeneralVariables.KEY_USER_ID);
	}
	
	public Long getRoleIdFromJwtToken(String token) {
		return this.claimAsLong(token, GeneralVariables.KEY_ROLE_ID);
	}
	
	public String getRoleNameFromJwtToken(String token) {
		return this.claimAsString(token, GeneralVariables.KEY_ROLE_NAME);
	}
	
	public boolean validateToken(String token) {
		JWTVerifier verifier = createVerifier(ISSUER);
		try {
			verifier.verify(token);
			return true;
		}catch(TokenExpiredException e) {
			log.error("TokenExpiredException: " + e.getMessage());
		}catch(JWTVerificationException e) {
			log.error("JWTVerificationException: " + e.getMessage());
		}
		
		return false;
	}
	
	public boolean isTokenExpiring(String token) {
		JWTVerifier verifier = createVerifier(ISSUER);
		try {
			DecodedJWT decodedJWT = verifier.verify(token);
			Date expiration = decodedJWT.getExpiresAt();
			return expiration.getTime() - System.currentTimeMillis() < 10 * 60 * 1000;
		}catch(JWTVerificationException e) {
			log.error("JWTVerificationException: " + e.getMessage());
		}
		
		return false;
	}
		
	protected String generateGlobalToken(Long userId, String username, String email, Long roleId, String roleName) {
		Algorithm algorithm = Algorithm.HMAC256(this.jwtSecret);
		
		Map<String, Object> values = new HashMap<>();
		values.put(GeneralVariables.KEY_USER_ID, userId);
		values.put(GeneralVariables.KEY_USERNAME, username);
		values.put(GeneralVariables.KEY_EMAIL, email);
		values.put(GeneralVariables.KEY_ROLE_NAME, roleName);
		values.put(GeneralVariables.KEY_ROLE_ID, roleId);
		
		return JWT.create()
			.withIssuer(ISSUER)
			.withSubject(username)
			.withClaim(CLAIM_DETAILS, values)
			.withIssuedAt(new Date())
			.withExpiresAt(new Date(System.currentTimeMillis() + jwtExpiration))
			.sign(algorithm);
	}
	
	protected String claimAsString(String token, String key) {
		JWTVerifier verifier = createVerifier(ISSUER);
		try {
			DecodedJWT decodedJWT = verifier.verify(token);
			var claims = decodedJWT.getClaim(CLAIM_DETAILS);
			return (String) claims.asMap().get(key);
		}catch(JWTVerificationException e) {
			log.error(e.getMessage());
		}
		
		return "";
	}
	
	protected Long claimAsLong(String token, String key) {
		JWTVerifier verifier = createVerifier(ISSUER);
		long value = -1L;
		try {
			DecodedJWT decodedJWT = verifier.verify(token);
			var claims = decodedJWT.getClaim(CLAIM_DETAILS);
			var valueInt = (Integer) claims.asMap().get(key);
			if(valueInt != null) {
				value = valueInt;
			}
		}catch(JWTVerificationException e) {
			log.error(e.getMessage());
		}
		
		return value;
	}
	
	protected JWTVerifier createVerifier(String issuer) {
		
		Algorithm algorithm = Algorithm.HMAC256(this.jwtSecret);
		return JWT.require(algorithm)
			.withIssuer(issuer)
			.build();
	}
}
