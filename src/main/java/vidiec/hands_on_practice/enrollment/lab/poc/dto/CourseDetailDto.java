package vidiec.hands_on_practice.enrollment.lab.poc.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class CourseDetailDto {

    private UUID id;

    private String name;

    private String description;

}
