package com.exe.online.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tbl_categories")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long category_id;
    private String category_name;

    @Column(length = 60000)
    private String about;
    private String image;
    private LocalDate addCategoryDate;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "category")
    private List<Service> service = new ArrayList<>();

}
