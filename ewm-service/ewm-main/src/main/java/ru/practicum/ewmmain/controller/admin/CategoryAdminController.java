package ru.practicum.ewmmain.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.ewmmain.service.category.CategoryAdminService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/categories")
public class CategoryAdminController {
    private final CategoryAdminService service;

    @PostMapping
    public ResponseEntity<CategoryDto> addCategory(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        return ResponseEntity
                .status(201)
                .body(service.addCategory(newCategoryDto));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{catId}")
    public void delete(@PathVariable(name = "catId") long catId) {
        service.deleteCategory(catId);
    }

    @PatchMapping("/{catId}")
    public ResponseEntity<CategoryDto> updateCategory(
            @PathVariable(name = "catId") long catId,
            @Valid @RequestBody CategoryDto categoryDto) {
        return ResponseEntity.ok(service.updateCategory(catId, categoryDto));
    }
}

