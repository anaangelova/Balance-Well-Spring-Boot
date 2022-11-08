package com.example.balancewellspringboot.model;

import com.example.balancewellspringboot.model.identity.EndUser;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private LocalDateTime uploadDate;

    @ManyToOne
    private EndUser endUserOwner;

    public Image(){}

    public Image(String title, EndUser endUserOwner){
        this.title=title;
        this.endUserOwner=endUserOwner;
        this.uploadDate = LocalDateTime.now();
    }
}
