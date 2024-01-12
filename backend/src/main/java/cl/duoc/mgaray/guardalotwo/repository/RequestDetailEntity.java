package cl.duoc.mgaray.guardalotwo.repository;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "request_details")
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn (name = "request_id", nullable = false)
    private RequestEntity request;
    @ManyToOne
    @JoinColumn (name = "product_id", nullable = false)
    private ProductEntity product;
    @Column (name = "quantity", nullable = false)
    private Long quantity;
}
