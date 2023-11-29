package ru.practicum.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import ru.practicum.dto.location.LocationShortDto;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UpdateEventAdminRequest {
    @Length(min = 20, max = 2000)
    private String annotation;
    private Long category;
    @Length(min = 20, max = 7000)
    private String description;
    private String eventDate;
    private LocationShortDto location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private String stateAction;
    @Length(min = 3, max = 120)
    private String title;
}
