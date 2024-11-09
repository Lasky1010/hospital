package com.test.hospital.data.entity;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "company_user")
public class CompanyUser extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String login;

}