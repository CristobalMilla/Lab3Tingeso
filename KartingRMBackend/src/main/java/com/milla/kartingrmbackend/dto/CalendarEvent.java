package com.milla.kartingrmbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalendarEvent {
    private String title;
    private LocalDateTime start;
    private LocalDateTime end;
    private String clientName;
}
