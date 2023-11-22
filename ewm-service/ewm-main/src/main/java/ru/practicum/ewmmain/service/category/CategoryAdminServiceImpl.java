package ru.practicum.ewmmain.service.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.ewmmain.mapper.CategoryMapper;
import ru.practicum.ewmmain.model.Category;
import ru.practicum.ewmmain.repository.CategoryDao;

import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CategoryAdminServiceImpl implements CategoryAdminService {
    private final CategoryDao categoryDao;

    @Override
    public CategoryDto addCategory(NewCategoryDto newCategoryDto) {
        Category category = Category.builder().name(newCategoryDto.getName()).build();
        return CategoryMapper.categoryToCategoryDto(categoryDao.save(category));
    }

    @Override
    public void deleteCategory(long catId) {
        existsById(catId);
        categoryDao.deleteById(catId);
    }

    @Override
    public CategoryDto updateCategory(long catId, CategoryDto categoryDto) {
        existsById(catId);
        categoryDto.setId(catId);
        Category category = categoryDao.save(CategoryMapper.categoryDtoToCategory(categoryDto));
        return CategoryMapper.categoryToCategoryDto(category);
    }

    private void existsById(long catId) {
        if (!categoryDao.existsById(catId)) {
            throw new NoSuchElementException("Category with ID=" + catId + " not found");
        }
    }
}
