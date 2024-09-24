package vidiec.hands_on_practice.enrollment.lab.poc.services.impl;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;
import vidiec.hands_on_practice.enrollment.lab.poc.commons.exceptions.InvalidArgumentException;
import vidiec.hands_on_practice.enrollment.lab.poc.commons.exceptions.NotFoundException;
import vidiec.hands_on_practice.enrollment.lab.poc.dto.Pagination;
import vidiec.hands_on_practice.enrollment.lab.poc.dto.StudentDetailDto;
import vidiec.hands_on_practice.enrollment.lab.poc.dto.StudentDto;
import vidiec.hands_on_practice.enrollment.lab.poc.dto.StudentRequestDto;
import vidiec.hands_on_practice.enrollment.lab.poc.entities.Student;
import vidiec.hands_on_practice.enrollment.lab.poc.mappers.StudentMapperImpl;
import vidiec.hands_on_practice.enrollment.lab.poc.repositories.StudentRepository;
import vidiec.hands_on_practice.enrollment.lab.poc.utils.DataGenerator;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class StudentServiceImplTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private Validator validator;

    @InjectMocks
    private StudentServiceImpl studentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        studentService = new StudentServiceImpl(studentRepository, new StudentMapperImpl(), validator);
    }

    @Test
    void getStudentDetail_shouldReturnStudentDetailDto() {
        // given
        Student student = DataGenerator.getRandomStudent();

        when(studentRepository.findById(any(UUID.class))).thenReturn(Optional.of(student));

        // when
        StudentDetailDto result = studentService.getStudentDetail(student.getId());

        // then
        assertEquals(student.getId(), result.getId());
        assertEquals(student.getFullName(), result.getFullName());
        assertEquals(student.getEmail(), result.getEmail());
    }

    @Test
    void getAllStudents_pageSizeGreaterThanMaximum_shouldThrowInvalidArgumentException() {
        // given
        // when
        InvalidArgumentException exception = assertThrows(
                InvalidArgumentException.class,
                () -> studentService.getAllStudents(0, 100, "", "")
        );

        // then
        assertEquals(exception.getMessage(), "Exceeded maximum page size");
    }

    @Test
    void getAllStudents_sortColumnIsNotAllowed_shouldThrowInvalidArgumentException() {
        // given
        // when
        InvalidArgumentException exception = assertThrows(
                InvalidArgumentException.class,
                () -> studentService.getAllStudents(0, 10, "someNotAllowedColumn", "")
        );

        // then
        assertTrue(exception.getMessage().contains("Invalid sort column."));
    }

    @Test
    void getAllStudents_pageNoIsGreaterThanTotalPages_shouldThrowInvalidArgumentException() {
        // given
        int pageNo = 2;
        int pageSize = 5;
        List<Student> students = new ArrayList<>(DataGenerator.getRandomStudents(5));
        Page<Student> studentPage = new PageImpl<>(
                new ArrayList<>(),
                PageRequest.of(pageNo - 1, pageSize),
                students.size()
        );
        when(studentRepository.findAll(any(Pageable.class)))
                .thenReturn(studentPage);

        // when
        InvalidArgumentException exception = assertThrows(
                InvalidArgumentException.class,
                () -> studentService.getAllStudents(2, 5, "", "")
        );

        // then
        assertTrue(exception.getMessage().contains("Exceeded maximum page number"));
    }

    @Test
    void getAllStudents_validArguments_shouldReturnStudentPaginationList() {
        // given
        int pageNo = 2;
        int pageSize = 5;
        List<Student> students = new ArrayList<>(DataGenerator.getRandomStudents(10));
        Page<Student> studentPage = new PageImpl<>(
                students.subList(5,10),
                PageRequest.of(pageNo - 1, pageSize),
                students.size()
        );
        when(studentRepository.findAll(any(Pageable.class)))
                .thenReturn(studentPage);

        // when
        Pagination<List<StudentDto>> pagination = studentService.getAllStudents(pageNo, pageSize, "", "");

        // then
        assertEquals(studentPage.getTotalPages(), pagination.getTotalPages());
        assertEquals(studentPage.getTotalElements(), pagination.getTotalItems());
        assertEquals(pageNo, pagination.getCurrentPage());
        assertEquals(pageSize, pagination.getData().size());
    }

    @Test
    void addNewStudent_hasInvalidArguments_shouldThrowInvalidArgumentException() {
        //give
        Set<ConstraintViolation<StudentRequestDto>> violations = Set.of(mock(ConstraintViolation.class));
        when(validator.validate(any(StudentRequestDto.class))).thenReturn(violations);

        // when
        assertThrows(
                InvalidArgumentException.class,
                () -> studentService.addNewStudent(new StudentRequestDto())
        );

        // then
    }

    @Test
    void addNewStudent_validArguments_shouldReturnCreatedStudentUUID() {
        //given
        Student student = DataGenerator.getRandomStudent();
        student.setFullName("John Doe");

        StudentRequestDto studentRequestDto = new StudentRequestDto(
                "John",
                "Doe",
                student.getGender(),
                student.getAddress()
        );

        when(validator.validate(any(StudentRequestDto.class))).thenReturn(new HashSet<>());
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        // when
        UUID createdId = studentService.addNewStudent(studentRequestDto);

        // then
        assertNotNull(createdId);
    }

    @Test
    void updateStudent_hasInvalidArguments_shouldThrowInvalidArgumentException() {
        // give
        Set<ConstraintViolation<StudentRequestDto>> violations = Set.of(mock(ConstraintViolation.class));
        when(validator.validate(any(StudentRequestDto.class))).thenReturn(violations);

        // when
        assertThrows(
                InvalidArgumentException.class,
                () -> studentService.updateStudent(UUID.randomUUID(), new StudentRequestDto())
        );

        // then
    }

    @Test
    void updateStudent_studentNotExisted_shouldThrowNotFoundException() {
        // given
        when(validator.validate(any(StudentRequestDto.class))).thenReturn(new HashSet<>());
        when(studentRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        // when
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> studentService.updateStudent(UUID.randomUUID(), new StudentRequestDto())
        );

        // then
        assertTrue(exception.getMessage().contains("Cannot find student"));
    }

    @Test
    void updateStudent_validArguments_shouldReturnUpdatedStudentDetailDto() {
        // given
        String studentOldName = "Harris New";
        Student student = DataGenerator.getRandomStudent();
        student.setFullName(studentOldName);

        when(validator.validate(any(StudentRequestDto.class))).thenReturn(new HashSet<>());
        when(studentRepository.findById(any(UUID.class))).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenReturn(any(Student.class));

        StudentRequestDto studentRequestDto = new StudentRequestDto(
                "John",
                "Doe",
                student.getGender(),
                student.getAddress()
        );

        // when
        StudentDetailDto updatedStudent = studentService.updateStudent(UUID.randomUUID(), studentRequestDto);

        // then
        assertNotNull(updatedStudent);
        assertEquals(student.getId(), updatedStudent.getId());
        assertNotEquals(studentOldName, updatedStudent.getFullName());
    }

    @Test
    void deleteStudent_studentNotExisted_shouldThrowNotFoundException() {
        // given
        when(studentRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        // when
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> studentService.deleteStudent(UUID.randomUUID())
        );

        // then
        assertTrue(exception.getMessage().contains("Cannot find student"));
    }

    @Test
    void deleteStudent_studentExisted_shouldReturnDeletedStudentId() {
        // given
        Student student = DataGenerator.getRandomStudent();
        when(studentRepository.findById(any(UUID.class))).thenReturn(Optional.of(student));

        // when
        UUID deletedStudentId = studentService.deleteStudent(student.getId());

        // then
        assertEquals(student.getId(), deletedStudentId);
        verify(studentRepository, times(1)).softDelete(any(UUID.class));
    }
}