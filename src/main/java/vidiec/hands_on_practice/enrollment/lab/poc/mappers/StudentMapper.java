package vidiec.hands_on_practice.enrollment.lab.poc.mappers;

import org.mapstruct.*;
import vidiec.hands_on_practice.enrollment.lab.poc.dto.StudentRequestDto;
import vidiec.hands_on_practice.enrollment.lab.poc.dto.StudentDetailDto;
import vidiec.hands_on_practice.enrollment.lab.poc.dto.StudentDto;
import vidiec.hands_on_practice.enrollment.lab.poc.entities.Student;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public abstract class StudentMapper {
    // From Entity to Dto
    public abstract StudentDetailDto toStudentDetailDto(Student student);
    public abstract StudentDto toStudentDto(Student student);

    public abstract List<StudentDto> toStudentDtos(List<Student> students);

    // From Dto to Entity
    public abstract Student toStudentEntity(StudentRequestDto studentRequestDto);

    @BeforeMapping
    protected void enrichStudentFullName(StudentRequestDto studentRequestDto, @MappingTarget Student student) {
        student.setFullName(studentRequestDto.getFirstName() + " " + studentRequestDto.getLastName());
    }
}
