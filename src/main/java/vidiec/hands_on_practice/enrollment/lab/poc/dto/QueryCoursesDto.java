package vidiec.hands_on_practice.enrollment.lab.poc.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import vidiec.hands_on_practice.enrollment.lab.poc.commons.constants.PaginationConstants;

@Data
public class QueryCoursesDto {

    @Min(value = 1, message = "Invalid page number. Value must be greater than 1")
    private int page = Integer.parseInt(PaginationConstants.DEFAULT_PAGE_NUMBER);

    @Min(value = 1, message = "Invalid page size. Value must be greater than 1")
    @Max(value = PaginationConstants.MAXIMUM_PAGE_SIZE, message = "Invalid page size. Value must be less than " + PaginationConstants.MAXIMUM_PAGE_SIZE)
    private int size = Integer.parseInt(PaginationConstants.DEFAULT_PAGE_SIZE);

    private String sortBy = PaginationConstants.DEFAULT_SORT_COLUMN;

    private String sortDirection = PaginationConstants.DEFAULT_SORT_DIRECTION;

    private String searchKeyword = PaginationConstants.DEFAULT_SEARCH_KEYWORD;

}
