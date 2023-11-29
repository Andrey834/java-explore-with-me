package ru.practicum.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.dto.location.NewLocationDto;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class LocationRequestDto {
    private long id;
    private long userId;
    private NewLocationDto location;
    private String status;
}
