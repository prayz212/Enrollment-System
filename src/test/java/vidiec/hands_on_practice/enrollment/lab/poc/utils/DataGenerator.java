package vidiec.hands_on_practice.enrollment.lab.poc.utils;

import org.instancio.Instancio;
import vidiec.hands_on_practice.enrollment.lab.poc.entities.Enrollment;
import vidiec.hands_on_practice.enrollment.lab.poc.entities.Student;

import java.util.List;

import static org.instancio.Select.field;

public class DataGenerator {
    public static Student getRandomStudent() {
        return Instancio.of(Student.class)
                .ignore(field(Student::getEnrollments))
                .create();
    }

    public static List<Student> getRandomStudents(int size) {
        return Instancio.ofList(Student.class)
                .size(size)
                .ignore(field(Student::getEnrollments))
                .create();
    }
}
