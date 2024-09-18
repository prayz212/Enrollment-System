package vidiec.hands_on_practice.enrollment.lab.poc.dto;

import lombok.*;
import vidiec.hands_on_practice.enrollment.lab.poc.commons.enums.Gender;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
public class StudentDetailDto {

    private UUID id;

    private String fullName;

    private String email;

    private Gender gender;

    private String address;

    // Consider to add it later
//    private List<Enrollment> enrollments;
}
