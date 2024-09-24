package vidiec.hands_on_practice.enrollment.lab.poc.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValueCheckStrategy;
import vidiec.hands_on_practice.enrollment.lab.poc.dto.CourseDetailDto;
import vidiec.hands_on_practice.enrollment.lab.poc.dto.CourseDto;
import vidiec.hands_on_practice.enrollment.lab.poc.dto.CourseRequestDto;
import vidiec.hands_on_practice.enrollment.lab.poc.entities.Course;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface CourseMapper {
    // From Entity to Dto
    CourseDto toCourseDto(Course course);
    CourseDetailDto toCourseDetailDto(Course course);

    // From Dto to Entity
    Course toCourseEntity(CourseRequestDto courseRequestDto);
}
