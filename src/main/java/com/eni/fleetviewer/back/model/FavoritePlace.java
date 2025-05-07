package com.eni.fleetviewer.back.model;

import com.eni.fleetviewer.back.model.id.FavoritePlaceId;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(FavoritePlaceId.class)
@Table(name = "favorite_places")
public class FavoritePlace {

    // Clé primaire composite définie dans la classe FavoritePlaceId
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id")
    private Person person;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    private Place place;
}
