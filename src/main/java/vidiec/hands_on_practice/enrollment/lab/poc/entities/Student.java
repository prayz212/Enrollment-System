package vidiec.hands_on_practice.enrollment.lab.poc.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;
import vidiec.hands_on_practice.enrollment.lab.poc.commons.enums.Gender;

import java.util.List;
import java.util.UUID;

@Entity
@Table(
        name = "student",
        uniqueConstraints = { @UniqueConstraint(name = "student_email_unique", columnNames = "edu_email")}
)
@SQLRestriction("is_deleted <> TRUE")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString(callSuper = true)
public class Student extends BaseEntity {

    @Id
    @Column(name = "id", updatable = false)
    private UUID id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "gender", nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "edu_email", nullable = false)
    private String email;

    @OneToMany(mappedBy = "student", cascade = { CascadeType.ALL })
    private List<Enrollment> enrollments;
}