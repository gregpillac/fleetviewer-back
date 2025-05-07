-- Table 'addresses' (Adresses)
create table addresses (
    address_id SERIAL PRIMARY KEY,
    address_line_1 varchar(100) NOT NULL, -- rue
    address_line_2 varchar(100), -- complément
    postal_code varchar(10)  NOT NULL,
    city varchar(50)  NOT NULL,
    gps_coords jsonb
);

-- Table 'place_types' (types de Lieu)
create table place_types (
    place_type_id SERIAL PRIMARY KEY,
    name varchar(80) NOT NULL
);

-- Table 'vehicle_status' (statuts de Véhicule)
CREATE TABLE vehicle_status (
    vehicle_status_id SERIAL PRIMARY KEY,
    name varchar(50) NOT NULL
);

-- Table 'reservation_status' (statuts de Réservation)
CREATE TABLE reservation_status (
    reservation_status_id SERIAL PRIMARY KEY,
    name varchar(50) NOT NULL
);

-- Table 'document_type' (types de Document)
CREATE TABLE document_types (
    document_type_id SERIAL PRIMARY KEY,
    name varchar(50) NOT NULL
);

-- Table 'authorities' (Permissions selon Spring Security)
CREATE TABLE authorities (
    authority_id varchar(80) PRIMARY KEY,
    description varchar(255)
);

-- Table 'roles' (Rôles)
CREATE TABLE roles (
    role_id varchar(50) PRIMARY KEY,
    description varchar(255)
);

-- Table 'persons' (Personnes)
CREATE TABLE persons (
    person_id SERIAL PRIMARY KEY,
    first_name varchar(60) NOT NULL,
    last_name varchar(80) NOT NULL,
    email varchar(100),
    phone varchar(20),
    address_id int,
    CONSTRAINT fk_person_address FOREIGN KEY (address_id) REFERENCES addresses(address_id)
);

-- Table 'places' (Lieux)
CREATE TABLE places (
    place_id SERIAL PRIMARY KEY,
    name varchar(100) NOT NULL,
    is_public boolean NOT NULL,
    place_type_id int NOT NULL,
    person_id int, -- créé par
    address_id int NOT NULL,
    CONSTRAINT fk_places_types FOREIGN KEY (place_type_id) REFERENCES place_types(place_type_id),
    CONSTRAINT fk_places_persons FOREIGN KEY (person_id) REFERENCES persons(person_id) ON DELETE SET NULL,
    CONSTRAINT fk_places_addresses FOREIGN KEY (address_id) REFERENCES addresses(address_id)
);


-- Table 'vehicles' (Véhicules)
CREATE TABLE vehicles (
    vehicle_id SERIAL PRIMARY KEY,
    license_plate varchar(50) NOT NULL,
    brand varchar(50) NOT NULL,
    model varchar(50) NOT NULL,
    seats int NOT NULL,
    mileage int NOT NULL,
    is_roadworthy boolean NOT NULL,
    is_insurance_valid boolean NOT NULL,
    place_id int NOT NULL,
    CONSTRAINT fk_vehicles_places FOREIGN KEY (place_id) REFERENCES places(place_id)
);

-- Table 'app_users' (Utilisateurs)
CREATE TABLE app_users (
    user_id SERIAL PRIMARY KEY,
    username varchar(50) NOT NULL,
    password varchar(255) NOT NULL,
    enabled boolean DEFAULT true NOT NULL,
    role_id varchar(50) NOT NULL,
    person_id int NOT NULL,
    CONSTRAINT fk_users_roles FOREIGN KEY (role_id) REFERENCES roles(role_id),
    CONSTRAINT fk_users_persons FOREIGN KEY (person_id) REFERENCES persons(person_id)
);

-- Table 'reservations' (Réservations)
CREATE TABLE reservations (
    reservation_id SERIAL PRIMARY KEY,
    reservation_date timestamp NOT NULL,
    reservation_status_id int NOT NULL,
    vehicle_id int NOT NULL,
    person_id int NOT NULL,
    CONSTRAINT fk_reservations_status FOREIGN KEY (reservation_status_id) REFERENCES reservation_status(reservation_status_id),
    CONSTRAINT fk_reservations_vehicles FOREIGN KEY (vehicle_id) REFERENCES vehicles(vehicle_id),
    CONSTRAINT fk_reservations_persons FOREIGN KEY (person_id) REFERENCES persons(person_id)
);

-- Table 'documents' (Documents)
CREATE TABLE documents (
    document_id SERIAL PRIMARY KEY,
    valid_until date,
    document_type_id int NOT NULL,
    vehicle_id int,
    person_id int,
    CONSTRAINT fk_documents_types FOREIGN KEY (document_type_id) REFERENCES document_types(document_type_id),
    CONSTRAINT fk_documents_vehicles FOREIGN KEY (vehicle_id) REFERENCES vehicles(vehicle_id),
    CONSTRAINT fk_documents_persons FOREIGN KEY (person_id) REFERENCES persons(person_id),
    -- Document lié soit à un Véhicule soit à une Personne
    CONSTRAINT chk_documents_target CHECK (
        (vehicle_id IS NOT NULL AND person_id IS NULL) OR
        (vehicle_id IS NULL AND person_id IS NOT NULL)
    )
);

-- Table 'itinerary_points' (Points d'itinéraire)
CREATE TABLE itinerary_points (
    reservation_id int,
    place_id int,
    date_time timestamp NOT NULL,
    point_type varchar(50) NOT NULL,
    PRIMARY KEY (reservation_id, place_id),
    CONSTRAINT fk_itinerary_reservations FOREIGN KEY (reservation_id) REFERENCES reservations(reservation_id),
    CONSTRAINT fk_itinerary_places FOREIGN KEY (place_id) REFERENCES places(place_id)
);

-- Table 'vehicle_keys' (Clés de Véhicule)
CREATE TABLE vehicle_keys  (
    vehicle_key_id SERIAL PRIMARY KEY,
    place_id int,
    vehicle_id int,
    CONSTRAINT fk_keys_places FOREIGN KEY (place_id) REFERENCES places(place_id),
    CONSTRAINT fk_keys_vehicles FOREIGN KEY (vehicle_id) REFERENCES vehicles(vehicle_id)
);

-- Table 'vehicle_availabilities' (Disponibilités du véhicule)
CREATE TABLE vehicle_availabilities (
    vehicle_id int,
    vehicle_status_id int,
    PRIMARY KEY (vehicle_id, vehicle_status_id),
    CONSTRAINT fk_availabilities_vehicles FOREIGN KEY (vehicle_id) REFERENCES vehicles(vehicle_id),
    CONSTRAINT fk_availabilities_status FOREIGN KEY (vehicle_status_id) REFERENCES vehicle_status(vehicle_status_id)
);

-- Table 'rideshares' (Covoiturages)
CREATE TABLE rideshares (
    person_id int,
    reservation_id int,
    PRIMARY KEY (person_id, reservation_id),
    CONSTRAINT fk_rideshares_persons FOREIGN KEY (person_id) REFERENCES persons(person_id),
    CONSTRAINT fk_rideshares_reservations FOREIGN KEY (reservation_id) REFERENCES reservations(reservation_id)
);

-- Table 'favorite_places' (Lieux favoris)
CREATE TABLE favorite_places (
    person_id int,
    place_id int,
    PRIMARY KEY (person_id, place_id),
    CONSTRAINT fk_favorite_persons FOREIGN KEY (person_id) REFERENCES persons(person_id),
    CONSTRAINT fk_favorite_places FOREIGN KEY (place_id) REFERENCES places(place_id)
);

-- Table 'role_authorities' (Permissions par rôles)
CREATE TABLE roles_authorities (
    role_id varchar(50),
    authority_id varchar(50),
    PRIMARY KEY (role_id, authority_id),
    CONSTRAINT fk_authorities_roles FOREIGN KEY (role_id) REFERENCES roles(role_id),
    CONSTRAINT fk_roles_authorities FOREIGN KEY (authority_id) REFERENCES authorities(authority_id)
);


-- Ajout de la référence vers le lieu dans 'persons' (nullable pour éviter tout problème de référence circulaire)
ALTER TABLE persons
    ADD COLUMN place_id int,
    ADD CONSTRAINT fk_persons_places FOREIGN KEY (place_id) REFERENCES places(place_id) ON DELETE SET NULL;

-- Ajout d'une contrainte CHECK pour vérifier que le lieu associé à une personne est bien de type 'Site'
ALTER TABLE persons
    ADD CONSTRAINT chk_persons_places_types
    CHECK (
        EXISTS (
            SELECT 1
            FROM places
            JOIN place_types ON places.place_type_id = place_types.place_type_id
            WHERE places.place_id = persons.place_id
            AND place_types.name = 'Site'
        )
    );

-- Ajout d'une contrainte CHECK pour vérifier que le lieu associé à un véhicule est bien de type 'Site'
ALTER TABLE vehicles
    ADD CONSTRAINT chk_vehicles_places_types
    CHECK (
        EXISTS (
            SELECT 1
            FROM places
            JOIN place_types ON places.place_type_id = place_types.place_type_id
            WHERE places.place_id = vehicles.place_id
            AND place_types.name = 'Site'
        )
    );

