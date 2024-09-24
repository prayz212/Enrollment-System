package vidiec.hands_on_practice.enrollment.lab.poc.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vidiec.hands_on_practice.enrollment.lab.poc.entities.Course;

import java.util.UUID;

@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {
    @Query("SELECT c FROM Course c WHERE " +
            "LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Course> findCoursesWithSearchKeyword(@Param("keyword") String searchKeyword, Pageable pageable);

    @Modifying
    @Query("UPDATE Course c SET c.isDeleted = true, c.updatedOn = CURRENT_TIMESTAMP WHERE c.id = :courseId")
    void softDelete(@Param("courseId") UUID id);
}
