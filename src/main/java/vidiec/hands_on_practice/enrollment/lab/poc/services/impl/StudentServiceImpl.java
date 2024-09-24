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
import vidiec.hands_on_practice.enrollment.lab.poc.commons.constants.PaginationConstants;
import vidiec.hands_on_practice.enrollment.lab.poc.commons.exceptions.InvalidArgumentException;
import vidiec.hands_on_practice.enrollment.lab.poc.commons.exceptions.NotFoundException;
import vidiec.hands_on_practice.enrollment.lab.poc.dto.Pagination;
import vidiec.hands_on_practice.enrollment.lab.poc.dto.StudentRequestDto;
import vidiec.hands_on_practice.enrollment.lab.poc.dto.StudentDetailDto;
import vidiec.hands_on_practice.enrollment.lab.poc.dto.StudentDto;
import vidiec.hands_on_practice.enrollment.lab.poc.entities.Student;
import vidiec.hands_on_practice.enrollment.lab.poc.mappers.StudentMapper;
import vidiec.hands_on_practice.enrollment.lab.poc.repositories.StudentRepository;
import vidiec.hands_on_practice.enrollment.lab.poc.services.StudentService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private static final String EMAIL_SUFFIX = "@adnovum.edu.vn";
    private static final Set<String> ALLOWED_SORT_COLUMNS = Set.of("fullName", "address", "createdOn", "updatedOn");

    private final StudentRepository studentRepository;

    private final StudentMapper studentMapper;

    private final Validator validator;

    @Override
    public StudentDetailDto getStudentDetail(UUID studentId) {
        log.info("Getting student detail by id: {}", studentId);
        Optional<Student> student = studentRepository.findById(studentId);

        return student.map(studentMapper::toStudentDetailDto)
                .orElseThrow(() -> new NotFoundException("Cannot find student with id: " + studentId));
    }

    @Override
    public Pagination<List<StudentDto>> getAllStudents(int pageNo, int pageSize, String sortBy, String sortDirection) {
        log.info("Getting students in page: {}, size = {}", pageNo, pageSize);

        if (pageSize < 1 || pageSize > PaginationConstants.MAXIMUM_PAGE_SIZE) {
            throw new InvalidArgumentException("Invalid page size. Value must be in range 1 to " + PaginationConstants.MAXIMUM_PAGE_SIZE);
        }

        // Default sort column is fullName
        String sortColumn = sortBy.isEmpty() ? "fullName" : sortBy;
        if (!ALLOWED_SORT_COLUMNS.contains(sortColumn)) {
            throw new InvalidArgumentException("Invalid sort column. Allowed values: " + ALLOWED_SORT_COLUMNS);
        }

        // Default direction is ascending
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.DESC.name())
                ? Sort.by(sortColumn).descending()
                : Sort.by(sortColumn).ascending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        Page<Student> studentPages = studentRepository.findAll(pageable);
        if (pageNo < 1 || pageNo > studentPages.getTotalPages())
            throw new InvalidArgumentException("Invalid page number. Value must be in range 1 to " + studentPages.getTotalPages());

        List<StudentDto> students = studentPages
                .getContent()
                .stream()
                .map(studentMapper::toStudentDto)
                .toList();

        return Pagination.<List<StudentDto>>builder()
                .currentPage(pageNo)
                .totalPages(studentPages.getTotalPages())
                .totalItems(studentPages.getTotalElements())
                .data(students)
                .build();
    }

    @Override
    @Transactional
    public UUID addNewStudent(StudentRequestDto requestDto) {
        Set<ConstraintViolation<StudentRequestDto>> violations = validator.validate(requestDto);
        if (!violations.isEmpty()) {
            throw new InvalidArgumentException(violations);
        }

        log.info("Adding new student: {}", requestDto);
        Student student = studentMapper.toStudentEntity(requestDto);
        student.setId(UUID.randomUUID());
        student.setEmail(requestDto.getFirstName().toLowerCase() + "." + requestDto.getLastName().toLowerCase() + EMAIL_SUFFIX);

        studentRepository.save(student);
        return student.getId();
    }

    @Override
    @Transactional
    public StudentDetailDto updateStudent(UUID studentId, StudentRequestDto requestDto) {
        Set<ConstraintViolation<StudentRequestDto>> violations = validator.validate(requestDto);
        if (!violations.isEmpty())
            throw new InvalidArgumentException(violations);

        log.info("Updating student with id: {}", studentId);
        Optional<Student> studentOpt = studentRepository.findById(studentId);

        if (studentOpt.isEmpty())
            throw new NotFoundException("Cannot find student with id: " + studentId);

        Student student = studentOpt.get();
        student.setFullName(requestDto.getFirstName()+ " " + requestDto.getLastName());
        student.setGender(requestDto.getGender());
        student.setAddress(requestDto.getAddress());
        student.setEmail(requestDto.getFirstName().toLowerCase() + "." + requestDto.getLastName().toLowerCase() + EMAIL_SUFFIX);
        student.setUpdatedOn(LocalDateTime.now());
        studentRepository.save(student);

        return studentMapper.toStudentDetailDto(student);
    }

    @Override
    @Transactional
    public UUID deleteStudent(UUID studentId) {
        log.info("Deleting student by id: {}", studentId);
        Optional<Student> studentOpt = studentRepository.findById(studentId);

        if (studentOpt.isEmpty())
            throw new NotFoundException("Cannot find student with id: " + studentId);

        studentRepository.softDelete(studentId);
        return studentId;
    }
}
