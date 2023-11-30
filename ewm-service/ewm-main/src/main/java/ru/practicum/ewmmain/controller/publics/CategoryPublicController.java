package ru.practicum.ewmmain.controller.publics;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.ewmmain.service.category.CategoryPublicService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/categories")
public class CategoryPublicController {
    private final CategoryPublicService service;

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getCategories(
            @RequestParam(name = "from", required = false, defaultValue = "0") @Min(0) Integer from,
            @RequestParam(name = "size", required = false, defaultValue = "10") @Min(1) @Max(100) Integer size,
            HttpServletRequest request) {

        log.info("Public {} Categories from ip address: {}", request.getMethod(), request.getRemoteAddr());

        return ResponseEntity.ok(service.getCategories(PageRequest.of(from, size)));
    }

    @GetMapping("/{catId}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable(name = "catId") long catId,
                                                   HttpServletRequest request) {

        log.info("Public {} Category №{} from ip address: {}", request.getMethod(), catId, request.getRemoteAddr());

        return ResponseEntity.ok(service.getCategory(catId));
    }
}
