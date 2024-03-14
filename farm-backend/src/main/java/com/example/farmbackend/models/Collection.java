package com.example.farmbackend.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import java.util.Date;

/**
 * Класс модели , представляющий сбор продукции.
 *
 * @author Дмитрий Валяльщиков
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@ToString
@Table(name = "collections")
public class Collection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "employeeId", nullable = false)
    private Long employeeId;
    @Column(name = "productId", nullable = false)
    private Long productId;
    @Column(name = "amount", nullable = false)
    private double amount;
    @CreatedDate
    @Column(name = "date", nullable = false)
    private Date dateCreating;


}
