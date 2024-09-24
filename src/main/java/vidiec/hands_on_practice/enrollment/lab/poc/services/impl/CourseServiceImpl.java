package vidiec.hands_on_practice.enrollment.lab.poc.services.impl;

import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import vidiec.hands_on_practice.enrollment.lab.poc.commons.exceptions.InvalidArgumentException;
import vidiec.hands_on_practice.enrollment.lab.poc.commons.exceptions.NotFoundException;
import vidiec.hands_on_practice.enrollment.lab.poc.dto.*;
import vidiec.hands_on_practice.enrollment.lab.poc.entities.Course;
import vidiec.hands_on_practice.enrollment.lab.poc.mappers.CourseMapper;
import vidiec.hands_on_practice.enrollment.lab.poc.repositories.CourseRepository;
import vidiec.hands_on_practice.enrollment.lab.poc.services.CourseService;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private static final Set<String> ALLOWED_SORT_COLUMNS = Set.of("name", "description", "createdOn", "updatedOn");

    private final CourseRepository courseRepository;

    private final CourseMapper courseMapper;

    private final Validator validator;

    @Override
    public CourseDetailDto getCourseDetail(UUID id) {
        log.info("Getting course detail by idL {}", id);
        Optional<Course> course = courseRepository.findById(id);

        return course.map(courseMapper::toCourseDetailDto)
                .orElseThrow(() -> new NotFoundException("Cannot find course with id: " + id));
    }

    @Override
    public Pagination<List<CourseDto>> getCourses(QueryCoursesDto requestDto) {
        log.info("Getting courses in page: {}, size: {}, keyword: {}", requestDto.getPage(), requestDto.getSize(), requestDto.getSearchKeyword());

        Set<ConstraintViolation<QueryCoursesDto>> violations = validator.validate(requestDto);
        if (!violations.isEmpty())
            throw new InvalidArgumentException(violations);

        // Default sort column is name
        String sortColumn = requestDto.getSortBy().isEmpty() ? "name" : requestDto.getSortBy();
        if (!ALLOWED_SORT_COLUMNS.contains(sortColumn)) {
            throw new InvalidArgumentException("Invalid sort column. Allowed values: " + ALLOWED_SORT_COLUMNS);
        }

        Sort sort = requestDto.getSortDirection().equalsIgnoreCase(Sort.Direction.DESC.name())
                ? Sort.by(sortColumn).descending()
                : Sort.by(sortColumn).ascending();

        Pageable pageable = PageRequest.of(requestDto.getPage() - 1, requestDto.getSize(), sort);

        Page<Course> coursePages;
        if (requestDto.getSearchKeyword().isEmpty()) {
            coursePages = courseRepository.findAll(pageable);
        }
        else {
            coursePages = courseRepository.findCoursesWithSearchKeyword(requestDto.getSearchKeyword(), pageable);
        }

        if (requestDto.getPage() > coursePages.getTotalPages())
            throw new InvalidArgumentException("Invalid page number. Value must be less than " + coursePages.getTotalPages());

        List<CourseDto> courses = coursePages.stream().map(courseMapper::toCourseDto).toList();
        return Pagination.<List<CourseDto>>builder()
                .currentPage(requestDto.getPage())
                .totalPages(coursePages.getTotalPages())
                .totalItems(coursePages.getTotalElements())
                .data(courses)
                .build();
    }

    @Override
    @Transactional
    public UUID addNewCourse(CourseRequestDto requestDto) {
        Set<ConstraintViolation<CourseRequestDto>> violations = validator.validate(requestDto);
        if (!violations.isEmpty()) {
            throw new InvalidArgumentException(violations);
        }

        log.info("Adding new course {}", requestDto);
        Course course = courseMapper.toCourseEntity(requestDto);
        course.setId(UUID.randomUUID());

        courseRepository.save(course);
        return course.getId();
    }

    @Override
    @Transactional
    public CourseDetailDto updateCourse(UUID id, CourseRequestDto requestDto) {
        Set<ConstraintViolation<CourseRequestDto>> violations = validator.validate(requestDto);
        if (!violations.isEmpty()) {
            throw new InvalidArgumentException(violations);
        }

        log.info("Updating course with id: {}", id);
        Optional<Course> courseOpt = courseRepository.findById(id);
        if (courseOpt.isEmpty())
            throw new NotFoundException("Cannot find course with id: " + id);

        Course course = courseOpt.get();
        course.setName(requestDto.getName());
        course.setDescription(requestDto.getDescription());
        course.setUpdatedOn(LocalDateTime.now());

        courseRepository.save(course);
        return courseMapper.toCourseDetailDto(course);
    }

    @Override
    @Transactional
    public UUID deleteCourse(UUID id) {
        log.info("Deleting course with id: {}", id);
        Optional<Course> courseOpt = courseRepository.findById(id);

        if (courseOpt.isEmpty())
            throw new NotFoundException("Cannot find course with id: " + id);

        courseRepository.softDelete(id);
        return id;
    }
}
