package ru.practicum.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import ru.practicum.dto.location.LocationShortDto;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class NewEventDto {
    @NotBlank
    @Length(min = 20, max = 2000)
    private String annotation;
    private Long category;
    @NotBlank
    @Length(min = 20, max = 7000)
    private String description;
    private String eventDate;
    private LocationShortDto location;
    private boolean paid;
    private Integer participantLimit = 0;
    private boolean requestModeration = true;
    @Length(min = 3, max = 120)
    private String title;
}
