package vidiec.hands_on_practice.enrollment.lab.poc.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vidiec.hands_on_practice.enrollment.lab.poc.dto.*;
import vidiec.hands_on_practice.enrollment.lab.poc.services.CourseService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping("{courseId}")
    public CourseDetailDto getCourseDetail(@PathVariable("courseId") UUID id) {
        return courseService.getCourseDetail(id);
    }

    @PostMapping("query")
    public Pagination<List<CourseDto>> getAllCourses(@RequestBody QueryCoursesDto requestDto) {
        return courseService.getCourses(requestDto);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public UUID addCourse(@RequestBody CourseRequestDto requestDto) {
        return courseService.addNewCourse(requestDto);
    }

    @PutMapping("{courseId}")
    public CourseDetailDto updateCourse(
            @PathVariable("courseId") UUID id,
            @RequestBody CourseRequestDto requestDto
    ) {
        return courseService.updateCourse(id, requestDto);
    }

    @DeleteMapping("{courseId}")
    public UUID deleteCourse(@PathVariable("courseId") UUID id) {
        return courseService.deleteCourse(id);
    }
}
