package cl.duoc.mgaray.guardalotwo.repository;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Table(name = "requests")
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "subsidiary", nullable = false)
    private String subsidiary;
    @Column(name = "address", nullable = false)
    private String address;
    @Column(name = "date", nullable = false)
    private LocalDate date;
    @Column(name = "order_number", nullable = false)
    private Long orderNumber;
    @Column(name = "track_number")
    private String trackNumber;
    @Builder.Default
    @OneToMany(
            mappedBy = "request",
            fetch = FetchType.LAZY,
            orphanRemoval = true,
            cascade = CascadeType.ALL)
    private Set<RequestDetailEntity> details = new HashSet<>();

}
