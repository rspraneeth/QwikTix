package com.qwiktix.data;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class Image {
    @Id
    @SequenceGenerator(sequenceName = "image_sequence",name = "image_sequence",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "image_sequence")
    private long Id;
    private String fileName;
    private String url;
    private boolean isDeleted=false;

    public Image(String fileName, String url) {
        this.fileName = fileName;
        this.url = url;
    }
}
