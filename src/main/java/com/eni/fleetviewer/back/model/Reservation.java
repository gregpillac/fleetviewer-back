package com.eni.fleetviewer.back.model;

import com.eni.fleetviewer.back.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departure_id", nullable = false)
    private Person departure;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arrival_id", nullable = false)
    private Person arrival;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private java.time.LocalDateTime startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private java.time.LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class) // <-- bind correct en ENUM PG
    @Column(name = "reservation_status",
            nullable = false,
            columnDefinition = "reservation_status_enum")
    private Status reservationStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", nullable = false)
    private Person driver;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItineraryPoint> itineraryPoints = new ArrayList<>();

}
