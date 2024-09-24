package vidiec.hands_on_practice.enrollment.lab.poc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
public class StudentDto {

    private UUID id;

    private String fullName;

    private String email;

}
