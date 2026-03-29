package com.revrecon.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Plan {
    @Id
    private Long id;
    private String name;
    private String prices;
    private Instant createdAt;
    private Instant updatedAt;
}
