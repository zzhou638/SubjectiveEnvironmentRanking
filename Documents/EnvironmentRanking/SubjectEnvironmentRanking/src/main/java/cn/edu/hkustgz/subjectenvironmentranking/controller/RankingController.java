package cn.edu.hkustgz.subjectenvironmentranking.controller;

import cn.edu.hkustgz.subjectenvironmentranking.dto.ChoiceRequest;
import cn.edu.hkustgz.subjectenvironmentranking.entity.Choice;
import cn.edu.hkustgz.subjectenvironmentranking.service.RankingService;
import cn.edu.hkustgz.subjectenvironmentranking.service.UserLoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Cookie;

import java.io.IOException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/ranking")
public class RankingController {
    private static final Logger logger = LoggerFactory.getLogger(RankingController.class);
    private final RankingService rankingService;
    private final UserLoginService userLoginService;

    public RankingController(RankingService rankingService, UserLoginService userLoginService) {
        this.rankingService = rankingService;
        this.userLoginService = userLoginService;
    }

    private String getSessionToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("session_token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    @GetMapping("/get-pair")
    public ResponseEntity<?> getRandomImagePair(HttpServletRequest request) {
        // 验证session
        String sessionToken = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("session_token".equals(cookie.getName())) {
                    sessionToken = cookie.getValue();
                    break;
                }
            }
        }

        if (sessionToken == null || userLoginService.validateSession(sessionToken) == null) {
            return ResponseEntity.status(401).body(Map.of("error", "请先登录"));
        }

        try {
            Map<String, Object> imagePair = rankingService.getRandomImagePair();
            return ResponseEntity.ok(imagePair);
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "获取图片对时发生错误: " + e.getMessage()));
        }
    }

    @PostMapping("/submit-choice")
    public ResponseEntity<?> submitChoice(
            @RequestBody ChoiceRequest choiceRequest,
            HttpServletRequest request) {
        // 获取session token
        String sessionToken = getSessionToken(request);
        if (sessionToken == null || userLoginService.validateSession(sessionToken) == null) {
            logger.warn("未授权的访问尝试，IP: {}", request.getRemoteAddr());
            return ResponseEntity.status(401).body(Map.of("error", "请先登录"));
        }

        // 验证输入
        if (choiceRequest == null ||
            choiceRequest.getLeftImageId() == null ||
            choiceRequest.getRightImageId() == null ||
            choiceRequest.getChoice() == null) {
            logger.warn("无效的请求参数, sessionToken: {}", sessionToken);
            return ResponseEntity.badRequest().body(Map.of("error", "无效的请求参数"));
        }

        // 验证选择的值是否有效
        if (!isValidChoice(choiceRequest.getChoice())) {
            logger.warn("无效的选择值: {}, sessionToken: {}", choiceRequest.getChoice(), sessionToken);
            return ResponseEntity.badRequest().body(Map.of("error", "无效的选择值"));
        }

        try {
            String ip = request.getRemoteAddr();
            logger.info("正在保存用户选择, sessionToken: {}, leftImage: {}, rightImage: {}, choice: {}",
                sessionToken, choiceRequest.getLeftImageId(), choiceRequest.getRightImageId(), choiceRequest.getChoice());

            rankingService.saveChoice(
                choiceRequest.getLeftImageId(),
                choiceRequest.getRightImageId(),
                choiceRequest.getChoice(),
                sessionToken,
                ip
            );

            logger.info("选择保存成功, sessionToken: {}", sessionToken);
            return ResponseEntity.ok(Map.of("message", "选择已保存"));
        } catch (Exception e) {
            logger.error("保存选择时发生错误, sessionToken: " + sessionToken, e);
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "保存选择时发生错误: " + e.getMessage()));
        }
    }

    private boolean isValidChoice(Choice.ChoiceType choice) {
        return choice != null;
    }
}
