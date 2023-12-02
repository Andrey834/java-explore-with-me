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
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.ewmmain.service.compilation.CompilationPublicService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/compilations")
public class CompilationPublicController {
    private final CompilationPublicService service;

    @GetMapping
    public ResponseEntity<List<CompilationDto>> getCompilations(
            @RequestParam(name = "from", required = false, defaultValue = "0") @Min(0) Integer from,
            @RequestParam(name = "size", required = false, defaultValue = "10") @Min(1) @Max(100) Integer size,
            HttpServletRequest request) {

        log.info("Public {} Compilations from ip address: {}", request.getMethod(), request.getRemoteAddr());

        return ResponseEntity.ok(service.getCompilations(PageRequest.of(from, size)));
    }

    @GetMapping("/{compId}")
    ResponseEntity<CompilationDto> getCompilation(@PathVariable(name = "compId") long compId,
                                                  HttpServletRequest request) {

        log.info("Public {} Compilation №{} from ip address: {}",
                request.getMethod(), compId, request.getRemoteAddr());

        return ResponseEntity.ok(service.getCompilation(compId));
    }
}
