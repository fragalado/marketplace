package com.api.marketplace.daos;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.api.marketplace.enums.Category;
import com.api.marketplace.enums.Level;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Course {

    // Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false, unique = true, updatable = false)
    private UUID uuid = UUID.randomUUID();
    @Column(nullable = false)
    private String title;
    @Column(length = 1000)
    private String description;
    private Category category;
    private float price;
    @Column(length = 500)
    private String thumbnail_url; // URL de la imagen del curso
    private String language;
    private int durationMinutes;
    private Level level;
    private boolean published = false;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime created_at = LocalDateTime.now();
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updated_at;

    // FK
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Instructor del curso
    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    private List<Purchase> purchases;
    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Lesson> lessons;
}
