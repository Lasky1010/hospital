package com.test.hospital.data.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDateTime;
import java.util.Objects;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Entity
@Table(name = "patient_note")
public class PatientNote extends BaseEntity {

    @Column(name = "last_modified_date_time")
    private LocalDateTime lastModifiedDateTime;

    @Column(name = "created_date_time")
    private LocalDateTime createdDateTime;

    private String note;

    @Column(name = "old_note_guid")
    private String oldNoteGuid;

    @ManyToOne
    @JoinColumn(name = "created_by_user_id")
    private CompanyUser createdBy;

    @ManyToOne
    @JoinColumn(name = "last_modified_by_user_id")
    private CompanyUser lastModifiedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    @ToString.Exclude
    private PatientProfile patientProfile;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        PatientNote that = (PatientNote) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}