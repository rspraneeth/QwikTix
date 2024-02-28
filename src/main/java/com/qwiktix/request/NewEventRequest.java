package com.qwiktix.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class NewEventRequest {
    private String eventName;
    private String eventDate;
    private String venue;
    private String description;
    private String ticketPrice;
    private String category;
    private String location;
    private MultipartFile image;
}
