package ad.inventory.api.security.sessions;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ad.inventory.api.system.CSystemService;
import ad.inventory.api.users.UserLocal;
import ad.inventory.api.users.UserLocalRepository;

@Service
public class LoginSecurityService {

	@Autowired
	private SessionAttemptRepository sessionRepository;
	@Autowired
	private UserBlockRepository userBlockRepository;
	@Autowired
	private UserLocalRepository userRepository;
	
	@Autowired
	private CSystemService systemService;
	
	
	public void recordLoginAttempt(String email, String ipAddress, boolean success) {
		UserLocal user = userRepository.findByEmailAndActiveAndIsDeleted(email, true, false).orElse(null);
		
		SessionAttempt attempt = new SessionAttempt();
	    attempt.setIpAddress(ipAddress);
	    attempt.setUser(user);
	    attempt.setAttemptTime(LocalDateTime.now());
	    attempt.setSuccess(success);
	    sessionRepository.save(attempt);
	    
	    if(!success) {
	    	this.checkAndBlockIfNeeded(user, ipAddress);
	    }
	}
	
    protected void checkAndBlockIfNeeded(UserLocal user, String ipAddress) {
    	var maxAttempsConfig = this.systemService.getMaxAttempsLogin();
    	var maxMinutesBlockConfig  = this.systemService.getMinsUserBlocked();
    	int maxAttemps = Integer.parseInt(maxAttempsConfig.getName());
    	int minutesBlock = Integer.parseInt(maxMinutesBlockConfig.getName());

        LocalDateTime cutoff = LocalDateTime.now().minusMinutes(minutesBlock);
        if (isCurrentlyBlocked(user, minutesBlock)) {
            return;
        }
        
        var attemptsByUserId = user == null ? null : sessionRepository.getLastLimitAttempts(user.getId(), cutoff, 5);
        int userAttempts = this.countFailedAttemps(attemptsByUserId, maxAttemps);
        
        if (user != null && userAttempts >= maxAttemps) {
            createBlock(user, ipAddress, "USER", minutesBlock);
        }
    }
    
    protected void createBlock(UserLocal user, String ipAddress, String blockType, int minutesBlock) {
        UserBlock block = new UserBlock();
        block.setUser(user);
        block.setIp(ipAddress);
        block.setBlockStartTime(LocalDateTime.now());
        block.setDurationMinutes(minutesBlock);
        block.setBlockType(blockType);
        userBlockRepository.save(block);
    }
    
    public boolean isCurrentlyBlocked(UserLocal user, int maxMinutesBlock) {
    	LocalDateTime since = LocalDateTime.now().minusMinutes(maxMinutesBlock);
    	var usersBlocks = userBlockRepository.getActiveBlocks(user.getId(), since);
    	return !usersBlocks.isEmpty();
    }

    public int getRemainingAttemps(UserLocal user) {
    	var maxAttempsConfig = this.systemService.getMaxAttempsLogin();
    	var maxMinutesBlockConfig  = this.systemService.getMinsUserBlocked();
    	int maxAttemps = Integer.parseInt(maxAttempsConfig.getName());
    	int minutesBlock = Integer.parseInt(maxMinutesBlockConfig.getName());

        LocalDateTime cutoff = LocalDateTime.now().minusMinutes(minutesBlock);
        
        if (isCurrentlyBlocked(user, minutesBlock)) {
            return 0;
        }
        
        var attemptsByUserId = user == null ? null : sessionRepository.getLastLimitAttempts(user.getId(), cutoff, 5);
        int userAttempts = this.countFailedAttemps(attemptsByUserId, maxAttemps);

        int totalAttempts = userAttempts;
        return (Math.max(maxAttemps - totalAttempts, 0));
    }
    
    public long getRemainingSecondsBlocked(UserLocal user) {
    	var maxMinutesBlockConfig  = this.systemService.getMinsUserBlocked();
    	int minutesBlock = Integer.parseInt(maxMinutesBlockConfig.getName());
    	LocalDateTime since = LocalDateTime.now().minusMinutes(minutesBlock);
    	
    	var usersBlocks = userBlockRepository.getActiveBlocks(user.getId(), since);
    	
    	if(!usersBlocks.isEmpty()) {
    		var userBlocked = usersBlocks.get(0);
    		var startBlock = userBlocked.getBlockStartTime();
    		var now = LocalDateTime.now();
    		var numSeconds = Math.max(0L, ChronoUnit.SECONDS.between(startBlock, now));
    		return numSeconds >= (minutesBlock * 60) ? 0 : (minutesBlock * 60) - numSeconds;
    	}
    	
    	return 0;
    }
    
    public int countFailedAttemps(List<SessionAttempt> attempts, int maxFailedAttempts) {
    	if(attempts == null) {
    		return 0;
    	}
    	
    	int count = 0;
    	for(int i = 0; i < attempts.size(); i++) {
    		var attempt = attempts.get(i);
    		if(attempt.getSuccess() && count < maxFailedAttempts) {
    			return count;
    		}else {
    			count++;
    		}
    		
    		if(count >= maxFailedAttempts) {
    			break;
    		}
    	}
    	
    	return count;
    }
}
