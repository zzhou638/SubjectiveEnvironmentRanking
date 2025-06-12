package cn.edu.hkustgz.subjectenvironmentranking.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "image_comparisons")
public class ImageComparison {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String leftImageId;

    @Column(nullable = false)
    private String rightImageId;

    @Column(nullable = false)
    private LocalDateTime comparisonTime;

    // 默认构造函数
    public ImageComparison() {}

    public ImageComparison(String leftImageId, String rightImageId) {
        this.leftImageId = leftImageId;
        this.rightImageId = rightImageId;
        this.comparisonTime = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLeftImageId() {
        return leftImageId;
    }

    public void setLeftImageId(String leftImageId) {
        this.leftImageId = leftImageId;
    }

    public String getRightImageId() {
        return rightImageId;
    }

    public void setRightImageId(String rightImageId) {
        this.rightImageId = rightImageId;
    }

    public LocalDateTime getComparisonTime() {
        return comparisonTime;
    }

    public void setComparisonTime(LocalDateTime comparisonTime) {
        this.comparisonTime = comparisonTime;
    }
}
