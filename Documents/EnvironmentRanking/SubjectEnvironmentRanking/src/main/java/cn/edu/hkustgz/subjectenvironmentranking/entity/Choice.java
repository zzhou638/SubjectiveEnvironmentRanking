package cn.edu.hkustgz.subjectenvironmentranking.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "choices")
public class Choice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChoiceType choice;

    @Column(name = "`left`", nullable = false, length = 36)
    private String leftImageId;

    @Column(name = "`right`", nullable = false, length = 36)
    private String rightImageId;

    @Column
    private LocalDateTime timestamp;

    @Column(name = "voter_unique_id", nullable = false, length = 36)
    private String voterUniqueId;

    @Column(length = 45)
    private String ip;

    public enum ChoiceType {
        left, right
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ChoiceType getChoice() {
        return choice;
    }

    public void setChoice(ChoiceType choice) {
        this.choice = choice;
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

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getVoterUniqueId() {
        return voterUniqueId;
    }

    public void setVoterUniqueId(String voterUniqueId) {
        this.voterUniqueId = voterUniqueId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
