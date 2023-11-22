package ru.practicum.ewmmain.service.compilation;

import org.springframework.data.domain.PageRequest;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.dto.compilation.UpdateCompilationRequest;

import java.util.List;

public interface CompilationAdminService {
    CompilationDto saveCompilation(NewCompilationDto newCompilationDto);

    void deleteCompilation(long compId);

    CompilationDto updateCompilation(long compId, UpdateCompilationRequest updateCompilationRequest);

    List<CompilationDto> getCompilations(PageRequest pageRequest);

    CompilationDto getCompilation(long compId);
}
