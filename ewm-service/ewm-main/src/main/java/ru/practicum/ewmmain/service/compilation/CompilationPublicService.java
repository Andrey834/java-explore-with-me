package ru.practicum.ewmmain.service.compilation;

import org.springframework.data.domain.PageRequest;
import ru.practicum.dto.compilation.CompilationDto;

import java.util.List;

public interface CompilationPublicService {
    List<CompilationDto> getCompilations(PageRequest pageRequest);

    CompilationDto getCompilation(long compId);
}
