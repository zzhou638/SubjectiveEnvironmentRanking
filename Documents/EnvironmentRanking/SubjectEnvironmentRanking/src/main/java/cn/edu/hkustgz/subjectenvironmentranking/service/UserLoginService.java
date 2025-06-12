package cn.edu.hkustgz.subjectenvironmentranking.service;

import cn.edu.hkustgz.subjectenvironmentranking.entity.UserLoginSession;
import cn.edu.hkustgz.subjectenvironmentranking.repository.UserLoginSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.security.SecureRandom;
import java.util.HexFormat;

@Service
public class UserLoginService {

    @Autowired
    private UserLoginSessionRepository userLoginSessionRepository;

    private final SecureRandom secureRandom = new SecureRandom();

    public UserLoginSession createLoginSession(String userAgent, String ip) {
        UserLoginSession session = new UserLoginSession();
        session.setCreatedAt(LocalDateTime.now());
        session.setSessionToken(generateSessionToken());
        session.setUserAgent(userAgent);
        session.setIp(ip);
        session.setIsExpired(false);
        return userLoginSessionRepository.save(session);
    }

    private String generateSessionToken() {
        byte[] tokenBytes = new byte[32];
        secureRandom.nextBytes(tokenBytes);
        return HexFormat.of().formatHex(tokenBytes);
    }

    public UserLoginSession validateSession(String sessionToken) {
        return userLoginSessionRepository.findBySessionTokenAndIsExpiredFalse(sessionToken)
                .orElse(null);
    }
}
