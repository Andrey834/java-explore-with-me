package ru.practicum.ewmmain.service.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.dto.compilation.UpdateCompilationRequest;
import ru.practicum.ewmmain.mapper.CompilationMapper;
import ru.practicum.ewmmain.model.Compilation;
import ru.practicum.ewmmain.model.Event;
import ru.practicum.ewmmain.repository.CompilationDao;
import ru.practicum.ewmmain.repository.EventDao;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompilationAdminServiceImpl implements CompilationAdminService {
    private final CompilationDao compilationDao;
    private final EventDao eventDao;

    @Override
    @Transactional
    public CompilationDto saveCompilation(NewCompilationDto newCompilationDto) {
        List<Event> events = getEventsByIds(newCompilationDto.getEvents());

        Compilation compilation = compilationDao.save(
                CompilationMapper.newCompilationDtoToCompilation(newCompilationDto, events));

        return CompilationMapper.compilationToCompilationDto(compilation);
    }

    @Override
    @Transactional
    public void deleteCompilation(long compId) {
        existsById(compId);
        compilationDao.deleteById(compId);
    }

    @Override
    @Transactional
    public CompilationDto updateCompilation(long compId, UpdateCompilationRequest updCompReq) {
        Compilation oldComp = findById(compId);

        Compilation compilation = oldComp.toBuilder()
                .id(compId)
                .title(updCompReq.getTitle() == null ? oldComp.getTitle() : updCompReq.getTitle())
                .events(updCompReq.getEvents() == null
                        ? oldComp.getEvents()
                        : getEventsByIds(updCompReq.getEvents()))
                .pinned(updCompReq.getPinned() == null ? oldComp.isPinned() : updCompReq.getPinned())
                .build();

        Compilation updateCompilation = compilationDao.save(compilation);
        return CompilationMapper.compilationToCompilationDto(updateCompilation);
    }

    @Override
    public List<CompilationDto> getCompilations(PageRequest pageRequest) {
        return compilationDao.findAll(pageRequest)
                .stream()
                .map(CompilationMapper::compilationToCompilationDto)
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto getCompilation(long compId) {
        return CompilationMapper.compilationToCompilationDto(compilationDao.findById(compId).orElseThrow(
                () -> new NoSuchElementException("Compilation with id= " + compId + " was not found")));
    }

    private Compilation findById(long compId) {
        return compilationDao.findById(compId).orElseThrow(
                () -> new NoSuchElementException("Compilation with id= " + compId + " was not found"));
    }

    private void existsById(long compId) {
        if (!compilationDao.existsById(compId)) {
            throw new NoSuchElementException("Compilation with id= " + compId + " was not found");
        }
    }

    private List<Event> getEventsByIds(List<Long> ids) {
        return eventDao.findAllById(ids);
    }
}
