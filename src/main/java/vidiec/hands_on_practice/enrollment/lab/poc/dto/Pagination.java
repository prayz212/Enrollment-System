package vidiec.hands_on_practice.enrollment.lab.poc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
public class Pagination<T> {

    private int currentPage;

    private int totalPages;

    private long totalItems;

    private T data;

}
