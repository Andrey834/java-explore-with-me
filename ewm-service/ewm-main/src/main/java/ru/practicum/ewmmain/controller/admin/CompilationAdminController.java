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
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.dto.compilation.UpdateCompilationRequest;
import ru.practicum.ewmmain.service.compilation.CompilationAdminServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/compilations")
public class CompilationAdminController {
    private final CompilationAdminServiceImpl service;

    @PostMapping()
    public ResponseEntity<CompilationDto> saveCompilation(@Valid @RequestBody NewCompilationDto newCompilationDto,
                                                          HttpServletRequest request) {

        CompilationDto compilationDto = service.saveCompilation(newCompilationDto);

        log.info("Admin {} Compilation №{} from ip address: {}",
                request.getMethod(), compilationDto.getId(), request.getRemoteAddr());

        return ResponseEntity.status(HttpStatus.CREATED).body(compilationDto);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable(name = "compId") long compId, HttpServletRequest request) {

        log.info("Admin {} Compilation №{} from ip address: {}", request.getMethod(), compId, request.getRemoteAddr());

        service.deleteCompilation(compId);
    }

    @PatchMapping("/{compId}")
    public ResponseEntity<CompilationDto> updateCompilation(@PathVariable(name = "compId") long compId,
                                                            @Valid @RequestBody UpdateCompilationRequest updCompReq,
                                                            HttpServletRequest request) {

        log.info("Admin {} Compilation №{} from ip address: {}", request.getMethod(), compId, request.getRemoteAddr());

        return ResponseEntity.ok(service.updateCompilation(compId, updCompReq));
    }
}
