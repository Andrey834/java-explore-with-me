package ru.practicum.ewmmain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewmmain.model.Compilation;

@Repository
public interface CompilationDao extends JpaRepository<Compilation, Long> {
}
