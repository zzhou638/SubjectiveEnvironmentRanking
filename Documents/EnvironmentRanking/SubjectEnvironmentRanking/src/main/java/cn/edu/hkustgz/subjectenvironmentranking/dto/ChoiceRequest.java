package cn.edu.hkustgz.subjectenvironmentranking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import cn.edu.hkustgz.subjectenvironmentranking.entity.Choice;

public class ChoiceRequest {
    @JsonProperty("left_image_id")
    private String leftImageId;

    @JsonProperty("right_image_id")
    private String rightImageId;

    @JsonProperty("choice")
    private Choice.ChoiceType choice;

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

    public Choice.ChoiceType getChoice() {
        return choice;
    }

    public void setChoice(Choice.ChoiceType choice) {
        this.choice = choice;
    }
}
