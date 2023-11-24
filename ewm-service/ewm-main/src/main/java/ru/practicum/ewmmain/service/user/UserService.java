package ru.practicum.ewmmain.service.user;
import org.springframework.data.domain.PageRequest;
import ru.practicum.dto.user.NewUserRequest;
import ru.practicum.dto.user.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getUsers(List<Long> ids, PageRequest pageRequest);

    UserDto registerUser(NewUserRequest newUserRequest);

    void deleteUser(long userId);

    UserDto getUserDto(long userId);

    void existsUserById(long userId);
}
