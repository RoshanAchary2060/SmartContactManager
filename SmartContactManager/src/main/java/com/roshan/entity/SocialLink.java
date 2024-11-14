package com.roshan.entity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class SocialLink {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String link;
    private String title;

    @ManyToOne
    private Contacts contact;
}