package cn.edu.hkustgz.subjectenvironmentranking.controller;

import cn.edu.hkustgz.subjectenvironmentranking.entity.UserLoginSession;
import cn.edu.hkustgz.subjectenvironmentranking.service.UserLoginService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class UserLoginController {

    @Autowired
    private UserLoginService userLoginService;

    @PostMapping("/login")
    public ResponseEntity<?> login(HttpServletRequest request, HttpServletResponse response) {
        // 获取用户代理和IP地址
        String userAgent = request.getHeader("User-Agent");
        String ip = getClientIp(request);

        // 创建登录会话
        UserLoginSession session = userLoginService.createLoginSession(userAgent, ip);

        // 创建Cookie
        Cookie sessionCookie = new Cookie("session_token", session.getSessionToken());
        sessionCookie.setPath("/");
        sessionCookie.setHttpOnly(true);
        sessionCookie.setMaxAge(30 * 24 * 60 * 60); // 30天有效期
        response.addCookie(sessionCookie);

        return ResponseEntity.ok().body("{\"message\": \"登录成功\"}");
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
