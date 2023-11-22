package ru.practicum.ewmmain.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.user.NewUserRequest;
import ru.practicum.dto.user.UserDto;
import ru.practicum.ewmmain.mapper.UserMapper;
import ru.practicum.ewmmain.model.User;
import ru.practicum.ewmmain.repository.UserDao;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    @Override
    public List<UserDto> getUsers(List<Long> ids, PageRequest pageRequest) {
        return userDao.getAllUserDto(ids, pageRequest);
    }

    @Override
    @Transactional
    public UserDto registerUser(NewUserRequest newUserRequest) {
        User user = UserMapper.newUserRequestToUser(newUserRequest);
        return UserMapper.userToUserDto(userDao.save(user));
    }

    @Override
    @Transactional
    public void deleteUser(long userId) {
        existsUserById(userId);
        userDao.deleteById(userId);
    }

    @Override
    public UserDto getUserDto(long userId) {
        User user = userDao.findById(userId).orElseThrow(
                () -> new NoSuchElementException("User with ID=" + userId + " not found"));
        return UserMapper.userToUserDto(user);
    }

    @Override
    public void existsUserById(long userId) {
        if (!userDao.existsById(userId)) {
            throw new NoSuchElementException("User with ID=" + userId + " not found");
        }
    }
}