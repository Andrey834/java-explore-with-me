package ru.practicum.ewmmain.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.dto.user.UserDto;
import ru.practicum.ewmmain.model.User;

import java.util.List;

@Repository
public interface UserDao extends JpaRepository<User, Long> {
    @Query(value = "SELECT new ru.practicum.dto.user.UserDto(u.id, u.email, u.name) " +
                   "FROM User u " +
                   "WHERE :ids is null OR u.id IN :ids ")
    List<UserDto> getAllUserDto(@Param(value = "ids") List<Long> ids, PageRequest pageRequest);
}
