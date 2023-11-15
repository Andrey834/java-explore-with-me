package ru.practicum.ewmmain.service.category;

import org.springframework.data.domain.PageRequest;
import ru.practicum.dto.category.CategoryDto;

import java.util.List;

public interface CategoryPublicService {
    CategoryDto getCategory(long catId);

    List<CategoryDto> getCategories(PageRequest pageRequest);
}
