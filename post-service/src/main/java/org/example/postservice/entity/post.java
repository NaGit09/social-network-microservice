package org.example.postservice.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity

public class post {
    @Id @GeneratedValue( strategy = GenerationType.AUTO)
    private Integer id;
    private UUID user_id;
    private String content ;
    private Boolean isLike ;
    private Boolean isComment ;
    private Boolean isShare ;
    private String modes ;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}
