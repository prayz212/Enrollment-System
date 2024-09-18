package vidiec.hands_on_practice.enrollment.lab.poc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vidiec.hands_on_practice.enrollment.lab.poc.entities.Student;

import java.util.UUID;

@Repository
public interface StudentRepository extends JpaRepository<Student, UUID> {
    @Modifying
    @Query("UPDATE Student s SET s.isDeleted = true, s.updatedOn = CURRENT_TIMESTAMP WHERE s.id = :studentId")
    void softDelete(@Param("studentId") UUID id);
}
