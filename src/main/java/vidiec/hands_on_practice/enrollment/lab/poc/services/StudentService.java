package vidiec.hands_on_practice.enrollment.lab.poc.services;

import vidiec.hands_on_practice.enrollment.lab.poc.dto.Pagination;
import vidiec.hands_on_practice.enrollment.lab.poc.dto.StudentRequestDto;
import vidiec.hands_on_practice.enrollment.lab.poc.dto.StudentDetailDto;
import vidiec.hands_on_practice.enrollment.lab.poc.dto.StudentDto;

import java.util.List;
import java.util.UUID;

public interface StudentService {
    StudentDetailDto getStudentDetail(UUID studentId);

    Pagination<List<StudentDto>> getAllStudents(int pageNo, int pageSize, String sortBy, String sortDirection);

    UUID addNewStudent(StudentRequestDto studentDto);

    StudentDetailDto updateStudent(UUID studentId, StudentRequestDto studentDto);

    UUID deleteStudent(UUID studentId);
}
