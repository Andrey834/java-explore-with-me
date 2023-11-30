package ru.practicum.ewmmain.service.category;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.ewmmain.repository.CategoryDao;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryPublicServiceImpl implements CategoryPublicService {
    private final CategoryDao categoryDao;

    @Override
    public CategoryDto getCategory(long catId) {
        if (categoryDao.existsById(catId)) return categoryDao.getCategoryDto(catId);
        else throw new NoSuchElementException("Category with ID=" + catId + " not found");
    }

    @Override
    public List<CategoryDto> getCategories(PageRequest pageRequest) {
        return categoryDao.getCategories(pageRequest);
    }
}
