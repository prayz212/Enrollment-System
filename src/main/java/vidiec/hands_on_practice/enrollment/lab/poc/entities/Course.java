package vidiec.hands_on_practice.enrollment.lab.poc.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(
        name = "course",
        uniqueConstraints = { @UniqueConstraint(name = "course_name_unique", columnNames = "name")}
)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString(callSuper = true)
public class Course extends BaseEntity {

    @Id
    @Column(name = "id", updatable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @OneToMany(mappedBy = "course")
    private List<Enrollment> enrollments;
}
