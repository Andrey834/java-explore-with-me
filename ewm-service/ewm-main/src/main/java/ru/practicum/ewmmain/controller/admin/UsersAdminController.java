package ru.practicum.ewmmain.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.user.NewUserRequest;
import ru.practicum.dto.user.UserDto;
import ru.practicum.ewmmain.service.user.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/users")
public class UsersAdminController {
    private final UserService service;

    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers(
            @RequestParam(name = "from", required = false, defaultValue = "0") @Min(1) Integer from,
            @RequestParam(name = "size", required = false, defaultValue = "10") @Min(1) @Max(100) Integer size,
            @RequestParam(name = "ids", required = false) List<Long> ids) {
        return ResponseEntity.ok(service.getUsers(ids, PageRequest.of(from, size)));
    }

    @PostMapping
    public ResponseEntity<UserDto> registerUser(@Valid @RequestBody NewUserRequest newUserRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.registerUser(newUserRequest));
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(name = "userId") long userId) {
        service.deleteUser(userId);
    }
}