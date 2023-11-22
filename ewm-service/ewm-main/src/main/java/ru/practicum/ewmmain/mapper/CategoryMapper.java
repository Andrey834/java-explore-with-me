package ru.practicum.ewmmain.mapper;

import ru.practicum.dto.category.CategoryDto;
import ru.practicum.ewmmain.model.Category;

public class CategoryMapper {
    public static CategoryDto categoryToCategoryDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public static Category categoryDtoToCategory(CategoryDto categoryDto) {
        return Category.builder()
                .id(categoryDto.getId())
                .name(categoryDto.getName())
                .build();
    }

}
