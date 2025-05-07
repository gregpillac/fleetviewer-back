package com.eni.fleetviewer.back.model;

import com.eni.fleetviewer.back.model.id.VehicleAvailabilityId;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(VehicleAvailabilityId.class)
@Table(name = "vehicles_availabilities")
public class VehicleAvailability {

    // Clé primaire composite définie dans la classe VehicleAvailabilityId
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_status_id")
    private VehicleStatus vehicleStatus;
}
