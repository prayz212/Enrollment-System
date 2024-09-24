package vidiec.hands_on_practice.enrollment.lab.poc.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class CourseRequestDto {

    @NotBlank(message = "Name must not be null or empty")
    private String name;

    @NotBlank(message = "Description must not be null or empty")
    private String description;

}
