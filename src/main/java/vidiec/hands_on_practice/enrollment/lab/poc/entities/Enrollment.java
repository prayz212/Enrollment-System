package vidiec.hands_on_practice.enrollment.lab.poc.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "enrollment")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@ToString(callSuper = true)
public class Enrollment extends BaseEntity {

    @EmbeddedId
    private EnrollmentKey id;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @ManyToOne
    @MapsId("studentId")
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @MapsId("courseId")
    @JoinColumn(name = "course_id")
    private Course course;
}
