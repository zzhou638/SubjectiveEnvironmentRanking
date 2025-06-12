package cn.edu.hkustgz.subjectenvironmentranking.repository;

import cn.edu.hkustgz.subjectenvironmentranking.entity.UserLoginSession;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserLoginSessionRepository extends JpaRepository<UserLoginSession, Integer> {
    Optional<UserLoginSession> findBySessionTokenAndIsExpiredFalse(String sessionToken);
}
