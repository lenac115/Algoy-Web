package com.example.algoyweb.model.entity.planner;

import com.example.algoyweb.model.dto.planner.PlannerDto;
import com.example.algoyweb.model.entity.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Planner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime startAt;

    @Column(nullable = false)
    private LocalDateTime endAt;

    @Column(nullable = false)
    private String link;

    @Column(nullable = false)
    private Status status;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public enum Status {
        TODO,
        IN_PROGRESS,
        DONE
    }

    public void updatePlan(PlannerDto plannerDto) {
        this.title = plannerDto.getTitle();
        this.content = plannerDto.getContent();
        this.startAt = plannerDto.getStartAt();
        this.endAt = plannerDto.getEndAt();
        this.link = plannerDto.getLink();
        this.status = plannerDto.getStatus();
        this.updateAt = LocalDateTime.now();
    }

    public void connectUser(User user) {
        this.user = user;
        user.connectPlanner(this);
    }
}