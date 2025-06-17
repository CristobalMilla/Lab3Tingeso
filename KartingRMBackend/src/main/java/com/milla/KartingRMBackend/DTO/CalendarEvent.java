package com.milla.KartingRMBackend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalendarEvent {
    private String title;
    private Date start;
    private Date end;
    private String clientName;
}
