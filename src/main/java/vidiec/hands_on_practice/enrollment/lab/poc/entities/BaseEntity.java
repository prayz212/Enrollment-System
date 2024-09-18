package vidiec.hands_on_practice.enrollment.lab.poc.entities;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Getter @Setter
@MappedSuperclass
public class BaseEntity {
    @Column(name = "is_deleted")
    @ColumnDefault("false")
    private boolean isDeleted;

    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn = LocalDateTime.now();

    @Column(name = "updated_on")
    @ColumnDefault("null")
    private LocalDateTime updatedOn;
}
