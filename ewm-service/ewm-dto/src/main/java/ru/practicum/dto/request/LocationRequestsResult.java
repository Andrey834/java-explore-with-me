package ru.practicum.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class LocationRequestsResult {
    private List<LocationRequestDto> confirmedLocation = new ArrayList<>();
    private List<LocationRequestDto> rejectedLocation = new ArrayList<>();
}
