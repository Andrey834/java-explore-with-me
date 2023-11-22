package ru.practicum.ewmmain.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.ewmmain.model.Category;

import java.util.List;

@Repository
public interface CategoryDao extends JpaRepository<Category, Long> {
    @Query(value = "SELECT new ru.practicum.dto.category.CategoryDto(c.id, c.name) " +
                   "FROM Category c " +
                   "WHERE c.id = :catId")
    CategoryDto getCategoryDto(@Param(value = "catId") long catId);

    @Query(value = "SELECT new ru.practicum.dto.category.CategoryDto(c.id, c.name) " +
                   "FROM Category c")
    List<CategoryDto> getCategories(PageRequest pageRequest);
}
