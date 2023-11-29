package ru.practicum.ewmmain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.ewmmain.enums.StateEvent;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode(exclude = "id")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 2000)
    private String annotation;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category", referencedColumnName = "id")
    private Category category;
    private Long confirmedRequests;
    private LocalDateTime createdOn;
    @Column(length = 7000)
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator", referencedColumnName = "id")
    private User initiator;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "location", referencedColumnName = "id")
    private Location location;
    private boolean paid;
    private Integer participantLimit = 0;
    private LocalDateTime publishedOn;
    private boolean requestModeration;
    @Enumerated(value = EnumType.STRING)
    private StateEvent state;
    private String title;
    private Long views;

    @PrePersist
    private void createdAt() {
        this.createdOn = LocalDateTime.now();
    }
}
