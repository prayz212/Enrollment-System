package vidiec.hands_on_practice.enrollment.lab.poc.services;

import vidiec.hands_on_practice.enrollment.lab.poc.dto.*;

import java.util.List;
import java.util.UUID;

public interface CourseService {
    CourseDetailDto getCourseDetail(UUID id);

    Pagination<List<CourseDto>> getCourses(QueryCoursesDto requestDto);

    UUID addNewCourse(CourseRequestDto requestDto);

    CourseDetailDto updateCourse(UUID id, CourseRequestDto requestDto);

    UUID deleteCourse(UUID id);
}
