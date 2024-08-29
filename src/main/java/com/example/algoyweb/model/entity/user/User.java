package com.example.algoyweb.model.entity.user;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.algoyweb.model.entity.planner.Planner;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(name = "user_name", nullable = false)
  private String firstname;

  @Column(name = "nick_name", nullable = false)
  private String nickname;

  @Column(name = "email", nullable = false)
  private String email;

  @Column(name = "password", nullable = false)
  private String password;

  @Enumerated(EnumType.STRING) // Enum 값을 문자열로 저장
  @Column(name = "role", nullable = false)
  private Role role;

  @Column(name = "is_deleted", nullable = false)
  private Boolean isDeleted;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;

  @OneToMany(mappedBy = "user")
  private List<Planner> plannerList;

  public void connectPlanner(Planner planner) {
    if(this.plannerList == null)
      this.plannerList = new ArrayList<>();
    this.getPlannerList().add(planner);
  }
}