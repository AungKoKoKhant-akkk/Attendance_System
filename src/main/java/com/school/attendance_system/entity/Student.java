package com.school.attendance_system.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "students")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String studentCode;

    @Column(nullable = false)
    private String name;

    private String classSection;

    private String faceImagePath;

    private LocalDateTime updatedAt;

    private LocalDateTime createdAt;

    // This method runs before saving a new student
    @PrePersist
    public void beforeCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // This method runs before updating student data
    @PreUpdate
    public void beforeUpdate() {
        this.updatedAt = LocalDateTime.now();
    }


}
