package api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class EstimateResponse {
    @JsonProperty("_id")
    private String id;
    private Status status;
    private Statistic statistic;
    private String originalId;
    private List<String> riskHistory;
    private List<StatusHistory> statusHistory;
    private String created;
    private String linkToCRM;
    private List<Object> phases;
    private int risk;
    private int useOfStandart;
    private List<Object> files;
    private List<Object> departments;
    private List<Object> approvers;
    private List<Object> managers;
    private String locale;
    private List<Object> authors;
    private Boolean isLocked;
    private String description;
    private String name;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class User {
        @JsonProperty("_id")
        private String id;
        private String name;
        private String role;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class Status {
        private String name;
        private String value;

        @JsonProperty("_id")
        private String id;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class Statistic {
        @JsonProperty("_id")
        private String id;

        private int workhoursPercents;
        private int maxHours;
        private int minHours;

        @JsonProperty("E95")
        private int e95;

        @JsonProperty("E68")
        private int e68;

        @JsonProperty("E50")
        private int e50;
        private List<Integer> reduced;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class StatusHistory {
        @JsonProperty("_id")
        private String id;
        private Status status;
        private User user;
        private String created;
    }
}

