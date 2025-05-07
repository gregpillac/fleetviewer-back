-- Table 'address' (Adresse)
CREATE TABLE address (
    address_id SERIAL PRIMARY KEY,
    address_line_1 varchar(100) NOT NULL, -- rue
    address_line_2 varchar(100), -- complément
    postal_code varchar(10)  NOT NULL,
    city varchar(50)  NOT NULL,
    gps_coords jsonb
);

-- Table 'place_type' (type de Lieu)
CREATE TABLE place_type (
    place_type_id SERIAL PRIMARY KEY,
    name varchar(80) NOT NULL
);

-- Table 'vehicle_status' (statut de Véhicule)
CREATE TABLE vehicle_status (
    vehicle_status_id SERIAL PRIMARY KEY,
    name varchar(50) NOT NULL
);

-- Table 'reservation_status' (statut de Réservation)
CREATE TABLE reservation_status (
    reservation_status_id SERIAL PRIMARY KEY,
    name varchar(50) NOT NULL
);

-- Table 'document_type' (type de Document)
CREATE TABLE document_type (
    document_type_id SERIAL PRIMARY KEY,
    name varchar(50) NOT NULL
);

-- Table 'authority' (Permission selon Spring Security)
CREATE TABLE authority (
    authority_id varchar(80) PRIMARY KEY,
    description varchar(255)
);

-- Table 'role' (Rôle)
CREATE TABLE role (
    role_id varchar(50) PRIMARY KEY,
    description varchar(255)
);

-- Table 'person' (Personne)
CREATE TABLE person (
    person_id SERIAL PRIMARY KEY,
    first_name varchar(60) NOT NULL,
    last_name varchar(80) NOT NULL,
    email varchar(100),
    phone varchar(20),
    address_id int,
    CONSTRAINT fk_person_address FOREIGN KEY (address_id) REFERENCES address(address_id)
);

-- Table 'place' (Lieu)
CREATE TABLE place (
    place_id SERIAL PRIMARY KEY,
    name varchar(100) NOT NULL,
    is_public boolean NOT NULL,
    place_type_id int NOT NULL,
    person_id int, -- créé par
    address_id int NOT NULL,
    CONSTRAINT fk_place_type FOREIGN KEY (place_type_id) REFERENCES place_type(place_type_id),
    CONSTRAINT fk_place_person FOREIGN KEY (person_id) REFERENCES person(person_id) ON DELETE SET NULL,
    CONSTRAINT fk_place_address FOREIGN KEY (address_id) REFERENCES address(address_id)
);


-- Table 'vehicle' (Véhicule)
CREATE TABLE vehicle (
    vehicle_id SERIAL PRIMARY KEY,
    license_plate varchar(50) NOT NULL,
    brand varchar(50) NOT NULL,
    model varchar(50) NOT NULL,
    seats int NOT NULL,
    mileage int NOT NULL,
    is_roadworthy boolean NOT NULL,
    is_insurance_valid boolean NOT NULL,
    place_id int NOT NULL,
    CONSTRAINT fk_vehicle_place FOREIGN KEY (place_id) REFERENCES place(place_id)
);

-- Table 'app_user' (Utilisateur)
CREATE TABLE app_user (
    user_id SERIAL PRIMARY KEY,
    username varchar(50) NOT NULL,
    password varchar(255) NOT NULL,
    enabled boolean DEFAULT true NOT NULL,
    role_id varchar(50) NOT NULL,
    person_id int NOT NULL,
    CONSTRAINT fk_user_role FOREIGN KEY (role_id) REFERENCES role(role_id),
    CONSTRAINT fk_user_person FOREIGN KEY (person_id) REFERENCES person(person_id)
);

-- Table 'reservation' (Réservation)
CREATE TABLE reservation (
    reservation_id SERIAL PRIMARY KEY,
    reservation_date timestamp NOT NULL,
    reservation_status_id int NOT NULL,
    vehicle_id int NOT NULL,
    person_id int NOT NULL,
    CONSTRAINT fk_reservation_status FOREIGN KEY (reservation_status_id) REFERENCES reservation_status(reservation_status_id),
    CONSTRAINT fk_reservation_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicle(vehicle_id),
    CONSTRAINT fk_reservation_person FOREIGN KEY (person_id) REFERENCES person(person_id)
);

-- Table 'document' (Document)
CREATE TABLE document (
    document_id SERIAL PRIMARY KEY,
    valid_until date,
    document_type_id int NOT NULL,
    vehicle_id int,
    person_id int,
    CONSTRAINT fk_document_type FOREIGN KEY (document_type_id) REFERENCES document_type(document_type_id),
    CONSTRAINT fk_document_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicle(vehicle_id),
    CONSTRAINT fk_document_person FOREIGN KEY (person_id) REFERENCES person(person_id),
    -- Document lié soit à un Véhicule soit à une Personne
    CONSTRAINT chk_document_target CHECK (
        (vehicle_id IS NOT NULL AND person_id IS NULL) OR
        (vehicle_id IS NULL AND person_id IS NOT NULL)
    )
);

-- Table 'itinerary_point' (Point d'itinéraire)
CREATE TABLE itinerary_point (
    reservation_id int,
    place_id int,
    date_time timestamp NOT NULL,
    point_type varchar(50) NOT NULL,
    PRIMARY KEY (reservation_id, place_id),
    CONSTRAINT fk_itinerary_reservation FOREIGN KEY (reservation_id) REFERENCES reservation(reservation_id),
    CONSTRAINT fk_itinerary_place FOREIGN KEY (place_id) REFERENCES place(place_id)
);

-- Table 'vehicle_key' (Clé de Véhicule)
CREATE TABLE vehicle_key  (
    vehicle_key_id SERIAL PRIMARY KEY,
    place_id int,
    vehicle_id int,
    CONSTRAINT fk_key_place FOREIGN KEY (place_id) REFERENCES place(place_id),
    CONSTRAINT fk_key_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicle(vehicle_id)
);

-- Table 'vehicle_availability' (Disponibilité du véhicule)
CREATE TABLE vehicle_availability (
    vehicle_id int,
    vehicle_status_id int,
    PRIMARY KEY (vehicle_id, vehicle_status_id),
    CONSTRAINT fk_availability_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicle(vehicle_id),
    CONSTRAINT fk_availability_status FOREIGN KEY (vehicle_status_id) REFERENCES vehicle_status(vehicle_status_id)
);

-- Table 'rideshare' (Covoiturage)
CREATE TABLE rideshare (
    person_id int,
    reservation_id int,
    PRIMARY KEY (person_id, reservation_id),
    CONSTRAINT fk_rideshare_person FOREIGN KEY (person_id) REFERENCES person(person_id),
    CONSTRAINT fk_rideshare_reservation FOREIGN KEY (reservation_id) REFERENCES reservation(reservation_id)
);

-- Table 'favorite_place' (Lieu favori)
CREATE TABLE favorite_place (
    person_id int,
    place_id int,
    PRIMARY KEY (person_id, place_id),
    CONSTRAINT fk_favorite_person FOREIGN KEY (person_id) REFERENCES person(person_id),
    CONSTRAINT fk_favorite_place FOREIGN KEY (place_id) REFERENCES place(place_id)
);

-- Table 'role_authority' (Permissions par rôle)
CREATE TABLE role_authority (
    role_id varchar(50),
    authority_id varchar(50),
    PRIMARY KEY (role_id, authority_id),
    CONSTRAINT fk_role_authority_role FOREIGN KEY (role_id) REFERENCES role(role_id),
    CONSTRAINT fk_role_authority_authority FOREIGN KEY (authority_id) REFERENCES authority(authority_id)
);


-- Ajout de la référence vers le lieu dans 'person' (nullable pour éviter tout problème de référence circulaire)
ALTER TABLE person
    ADD COLUMN place_id int,
    ADD CONSTRAINT fk_person_place FOREIGN KEY (place_id) REFERENCES place(place_id) ON DELETE SET NULL;

-- Ajout d'une contrainte CHECK pour vérifier que le lieu associé à une personne est bien de type 'Site'
ALTER TABLE person
    ADD CONSTRAINT chk_person_place_type
    CHECK (
        EXISTS (
            SELECT 1
            FROM place
            JOIN place_type ON place.place_type_id = place_type.place_type_id
            WHERE place.place_id = person.place_id
            AND place_type.name = 'Site'
        )
    );

-- Ajout d'une contrainte CHECK pour vérifier que le lieu associé à un véhicule est bien de type 'Site'
ALTER TABLE vehicle
    ADD CONSTRAINT chk_vehicle_place_type
    CHECK (
        EXISTS (
            SELECT 1
            FROM place
            JOIN place_type ON place.place_type_id = place_type.place_type_id
            WHERE place.place_id = vehicle.place_id
            AND place_type.name = 'Site'
        )
    );

