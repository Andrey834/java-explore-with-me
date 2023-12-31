package ru.practicum.ewmmain.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/categories")
public class CategoryAdminController {
    private final CategoryAdminService service;

    @PostMapping
    public ResponseEntity<CategoryDto> addCategory(@Valid @RequestBody NewCategoryDto newCategoryDto,
                                                   HttpServletRequest request) {

        CategoryDto categoryDto = service.addCategory(newCategoryDto);

        log.info("Admin {} Category №{} from ip address: {}",
                request.getMethod(), categoryDto.getId(), request.getRemoteAddr());

        return ResponseEntity.status(201).body(categoryDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{catId}")
    public void delete(@PathVariable(name = "catId") long catId, HttpServletRequest request) {

        log.info("Admin {} Category №{} from ip address: {}", request.getMethod(), catId, request.getRemoteAddr());

        service.deleteCategory(catId);
    }

    @PatchMapping("/{catId}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable(name = "catId") long catId,
                                                      @Valid @RequestBody CategoryDto categoryDto,
                                                      HttpServletRequest request) {

        log.info("Admin {} Category №{} from ip address: {}", request.getMethod(), catId, request.getRemoteAddr());

        return ResponseEntity.ok(service.updateCategory(catId, categoryDto));
    }
}

