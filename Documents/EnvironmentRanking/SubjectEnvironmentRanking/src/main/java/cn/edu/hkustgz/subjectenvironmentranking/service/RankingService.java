package cn.edu.hkustgz.subjectenvironmentranking.service;

import cn.edu.hkustgz.subjectenvironmentranking.entity.Choice;
import cn.edu.hkustgz.subjectenvironmentranking.repository.ChoiceRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpServletRequest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class RankingService {
    private final ChoiceRepository choiceRepository;
    private final ResourceLoader resourceLoader;
    private final String imageDirectory;

    public RankingService(ChoiceRepository choiceRepository,
                         ResourceLoader resourceLoader,
                         @Value("${app.image-directory:static/L18_uv_tile}") String imageDirectory) {
        this.choiceRepository = choiceRepository;
        this.resourceLoader = resourceLoader;
        this.imageDirectory = imageDirectory;
    }

    public Map<String, Object> getRandomImagePair() throws IOException {
        List<String> allImageIds = getAllImageIds();
        List<String> comparedImageIds = choiceRepository.findAllComparedImageIds();

        String leftId, rightId;

        // 策略1：优先选择从未被判度过的图片
        List<String> neverComparedImages = new ArrayList<>(allImageIds);
        neverComparedImages.removeAll(comparedImageIds);

        if (neverComparedImages.size() >= 2) {
            // 有至少两张从未被判度过的图片
            Collections.shuffle(neverComparedImages);
            leftId = neverComparedImages.get(0);
            rightId = neverComparedImages.get(1);
        } else {
            // 策略2：尝试找到从未匹配过的图片对
            boolean foundNewPair = false;
            int maxAttempts = 50; // 设置最大尝试次数，避免无限循环
            int attempts = 0;

            do {
                Collections.shuffle(allImageIds);
                leftId = allImageIds.get(0);
                rightId = allImageIds.get(1);

                if (!choiceRepository.existsByLeftImageIdAndRightImageIdOrLeftImageIdAndRightImageId(
                        leftId, rightId, rightId, leftId)) {
                    foundNewPair = true;
                    break;
                }

                attempts++;
            } while (!foundNewPair && attempts < maxAttempts);

            // 策略3：如果前两种策略都失败，直接随机选择两张不同的图片
            if (!foundNewPair) {
                do {
                    Collections.shuffle(allImageIds);
                    leftId = allImageIds.get(0);
                    rightId = allImageIds.get(1);
                } while (leftId.equals(rightId));
            }
        }

        // 构建返回结果
        Map<String, Object> result = new HashMap<>();

        Map<String, Object> leftImage = new HashMap<>();
        leftImage.put("pic", "left");
        leftImage.put("left_image_id", leftId);
        leftImage.put("left_image", getImageBytes(leftId));

        Map<String, Object> rightImage = new HashMap<>();
        rightImage.put("pic", "right");
        rightImage.put("right_image_id", rightId);
        rightImage.put("right_image", getImageBytes(rightId));

        result.put("left", leftImage);
        result.put("right", rightImage);

        return result;
    }

    public void saveChoice(String leftId, String rightId, Choice.ChoiceType choice,
                         String voterUniqueId, String ip) {
        Choice choiceEntity = new Choice();
        choiceEntity.setLeftImageId(leftId);
        choiceEntity.setRightImageId(rightId);
        choiceEntity.setChoice(choice);
        choiceEntity.setVoterUniqueId(voterUniqueId);
        choiceEntity.setIp(ip);
        choiceEntity.setTimestamp(LocalDateTime.now());

        choiceRepository.save(choiceEntity);
    }

    private List<String> getAllImageIds() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:" + imageDirectory);
        Path directoryPath = Paths.get(resource.getURI());

        try (Stream<Path> files = Files.list(directoryPath)) {
            return files
                .filter(path -> path.toString().endsWith(".tif"))
                .map(path -> path.getFileName().toString().replace(".tif", ""))
                .collect(Collectors.toList());
        }
    }

    private byte[] getImageBytes(String imageId) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:" + imageDirectory + "/" + imageId + ".tif");

        // 读取TIF图像
        BufferedImage tifImage = ImageIO.read(resource.getInputStream());

        // 转换为JPG格式
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(tifImage, "jpg", baos);

        return baos.toByteArray();
    }
}
