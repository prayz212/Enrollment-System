package vidiec.hands_on_practice.enrollment.lab.poc.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vidiec.hands_on_practice.enrollment.lab.poc.commons.enums.Gender;

@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
public class StudentRequestDto {

    @NotBlank(message = "Firstname must not be null or empty")
    private String firstName;

    @NotBlank(message = "Lastname must not be null or empty")
    private String lastName;

    @NotNull(message = "Gender must not be null")
    private Gender gender;

    @NotBlank(message = "Address must not be null or empty")
    private String address;
}
