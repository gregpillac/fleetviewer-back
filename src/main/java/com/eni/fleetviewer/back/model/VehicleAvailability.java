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
@Table(name = "vehicle_availability")
public class VehicleAvailability {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_status_id")
    private VehicleStatus vehicleStatus;
}
