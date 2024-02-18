package com.qwiktix.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateEventRequest {
    private Integer eventId;
    private String eventName;
    private String eventDate;
    private String venue;
    private String description;
    private String ticketPrice;
    private String category;
    private String location;
    private MultipartFile image;
}
