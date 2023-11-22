package ru.practicum.ewmmain.mapper;

import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.ewmmain.model.Compilation;
import ru.practicum.ewmmain.model.Event;

import java.util.List;
import java.util.stream.Collectors;

public class CompilationMapper {
    public static Compilation newCompilationDtoToCompilation(NewCompilationDto newCompilationDto,
                                                             List<Event> events) {
        return Compilation.builder()
                .title(newCompilationDto.getTitle())
                .pinned(newCompilationDto.isPinned())
                .events(events)
                .build();
    }

    public static CompilationDto compilationToCompilationDto(Compilation compilation) {
        List<EventShortDto> events = compilation.getEvents()
                .stream()
                .map(EventMapper::eventToEventShortDto)
                .collect(Collectors.toList());

        return CompilationDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .events(events)
                .pinned(compilation.isPinned())
                .build();
    }
}
