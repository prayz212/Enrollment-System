package vidiec.hands_on_practice.enrollment.lab.poc.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import vidiec.hands_on_practice.enrollment.lab.poc.commons.constants.PaginationConstants;
import vidiec.hands_on_practice.enrollment.lab.poc.dto.Pagination;
import vidiec.hands_on_practice.enrollment.lab.poc.dto.StudentRequestDto;
import vidiec.hands_on_practice.enrollment.lab.poc.dto.StudentDetailDto;
import vidiec.hands_on_practice.enrollment.lab.poc.dto.StudentDto;
import vidiec.hands_on_practice.enrollment.lab.poc.services.StudentService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping("{studentId}")
    public StudentDetailDto getStudent(@PathVariable("studentId") UUID id) {
        return studentService.getStudentDetail(id);
    }

    @GetMapping
    public Pagination<List<StudentDto>> getStudents(
            @RequestParam(value = "page", defaultValue = PaginationConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "size", defaultValue = PaginationConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = PaginationConstants.DEFAULT_SORT_COLUMN, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = PaginationConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDirection
    ) {
        return studentService.getAllStudents(pageNo, pageSize, sortBy, sortDirection);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UUID createStudent(@RequestBody StudentRequestDto createDto) {
        return studentService.addNewStudent(createDto);
    }

    @PutMapping("{studentId}")
    public StudentDetailDto updateStudent(
            @PathVariable("studentId") UUID id,
            @RequestBody StudentRequestDto updateDto
    ) {
        return studentService.updateStudent(id, updateDto);
    }

    @DeleteMapping("{studentId}")
    public UUID deleteStudent(@PathVariable("studentId") UUID id) {
        return studentService.deleteStudent(id);
    }
}
