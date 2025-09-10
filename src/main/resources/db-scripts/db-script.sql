-- SQL DROP
DROP SCHEMA public CASCADE;
CREATE SCHEMA public;


-- Table 'addresses' (Adresses)
create table addresses (
    address_id BIGSERIAL PRIMARY KEY,
    address_line_1 varchar(100) NOT NULL, -- rue
    address_line_2 varchar(100), -- complément
    postal_code varchar(10)  NOT NULL,
    city varchar(50)  NOT NULL,
    gps_coords json
);

-- Table 'place_types' (types de Lieu)
create table place_types (
    place_type_id BIGSERIAL PRIMARY KEY,
    name varchar(80) NOT NULL
);

-- Table 'document_type' (types de Document)
CREATE TABLE document_types (
    document_type_id BIGSERIAL PRIMARY KEY,
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

-- Table 'places' (Lieux)
CREATE TABLE places (
    place_id BIGSERIAL PRIMARY KEY,
    name varchar(100) NOT NULL,
    is_public boolean NOT NULL,
    place_type_id bigint NOT NULL,
    address_id bigint NOT NULL,
    CONSTRAINT fk_places_types FOREIGN KEY (place_type_id) REFERENCES place_types(place_type_id),
    CONSTRAINT fk_places_addresses FOREIGN KEY (address_id) REFERENCES addresses(address_id)
);

-- Table 'persons' (Personnes)
CREATE TABLE persons (
    person_id BIGSERIAL PRIMARY KEY,
    first_name varchar(60) NOT NULL,
    last_name varchar(80) NOT NULL,
    email varchar(100),
    phone varchar(20),
    address_id bigint,
    place_id bigint,
    CONSTRAINT fk_person_address FOREIGN KEY (address_id) REFERENCES addresses(address_id),
    CONSTRAINT fk_persons_places FOREIGN KEY (place_id) REFERENCES places(place_id) ON DELETE SET NULL,
    CONSTRAINT uk_persons_address UNIQUE (address_id)
);
-- Table 'vehicles' (Véhicules)
CREATE TABLE vehicles (
    vehicle_id BIGSERIAL PRIMARY KEY,
    license_plate varchar(50) NOT NULL,
    brand varchar(50) NOT NULL,
    model varchar(50) NOT NULL,
    seats int NOT NULL,
    mileage bigint NOT NULL,
    is_roadworthy boolean NOT NULL,
    is_insurance_valid boolean NOT NULL,
    place_id bigint NOT NULL,
    CONSTRAINT fk_vehicles_places FOREIGN KEY (place_id) REFERENCES places(place_id)
);

-- Table 'app_users' (Utilisateurs)
CREATE TABLE app_users (
    user_id BIGSERIAL PRIMARY KEY,
    username varchar(50) NOT NULL,
    password varchar(255) NOT NULL,
    enabled boolean DEFAULT true NOT NULL,
    role_id varchar(50) NOT NULL,
    person_id bigint NOT NULL,
    CONSTRAINT fk_users_roles FOREIGN KEY (role_id) REFERENCES roles(role_id),
    CONSTRAINT fk_users_persons FOREIGN KEY (person_id) REFERENCES persons(person_id)ON DELETE CASCADE,
    CONSTRAINT uk_app_users_username UNIQUE (username),
    CONSTRAINT uk_app_users_person UNIQUE (person_id)
);

CREATE TYPE reservation_status_enum AS ENUM (
    'PENDING',
    'CONFIRMED',
    'REJECTED',
    'CANCELLED',
    'UNAVAILABLE'
);

-- Table 'reservations' (Réservations)
CREATE TABLE reservations (
    reservation_id BIGSERIAL PRIMARY KEY,
    departure_id bigint NOT NULL,
    arrival_id bigint NOT NULL,
    start_date timestamp NOT NULL,
    end_date timestamp NOT NULL,
    reservation_status reservation_status_enum NOT NULL,
    vehicle_id bigint,
    person_id bigint NOT NULL,
    CONSTRAINT fk_reservations_departure FOREIGN KEY (departure_id) REFERENCES places(place_id),
    CONSTRAINT fk_reservations_arrival FOREIGN KEY (arrival_id) REFERENCES places(place_id),
    CONSTRAINT fk_reservations_vehicles FOREIGN KEY (vehicle_id) REFERENCES vehicles(vehicle_id),
    CONSTRAINT fk_reservations_persons FOREIGN KEY (person_id) REFERENCES persons(person_id)
);

-- Table 'documents' (Documents)
CREATE TABLE documents (
    document_id BIGSERIAL PRIMARY KEY,
    valid_until date,
    document_type_id bigint NOT NULL,
    vehicle_id bigint,
    person_id bigint,
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
    reservation_id bigint,
    person_id bigint,
    place_id bigint,
    date_time timestamp,
    PRIMARY KEY (reservation_id, person_id),
    CONSTRAINT fk_itinerary_reservations FOREIGN KEY (reservation_id) REFERENCES reservations(reservation_id),
    CONSTRAINT fk_itinerary_persons FOREIGN KEY (person_id) REFERENCES persons(person_id),
    CONSTRAINT fk_itinerary_places FOREIGN KEY (place_id) REFERENCES places(place_id)
);

-- Table 'vehicle_keys' (Clés de Véhicule)
CREATE TABLE vehicle_keys  (
    vehicle_key_id BIGSERIAL PRIMARY KEY,
    tag_label VARCHAR(64),
    place_id bigint NOT NULL,
    vehicle_id bigint NOT NULL,
    CONSTRAINT fk_keys_places FOREIGN KEY (place_id) REFERENCES places(place_id),
    CONSTRAINT fk_keys_vehicles FOREIGN KEY (vehicle_id) REFERENCES vehicles(vehicle_id)
);

-- Table 'rideshares' (Covoiturages)
CREATE TABLE rideshares (
    person_id bigint,
    reservation_id bigint,
    PRIMARY KEY (person_id, reservation_id),
    CONSTRAINT fk_rideshares_persons FOREIGN KEY (person_id) REFERENCES persons(person_id),
    CONSTRAINT fk_rideshares_reservations FOREIGN KEY (reservation_id) REFERENCES reservations(reservation_id)
);

-- Table 'favorite_places' (Lieux favoris)
CREATE TABLE favorite_places (
    person_id bigint,
    place_id bigint,
    PRIMARY KEY (person_id, place_id),
    CONSTRAINT fk_favorite_persons FOREIGN KEY (person_id) REFERENCES persons(person_id),
    CONSTRAINT fk_favorite_places FOREIGN KEY (place_id) REFERENCES places(place_id)
);

-- Table 'roles_authorities' (Permissions par rôles)
CREATE TABLE roles_authorities (
    role_id varchar(50),
    authority_id varchar(50),
    PRIMARY KEY (role_id, authority_id),
    CONSTRAINT fk_authorities_roles FOREIGN KEY (role_id) REFERENCES roles(role_id),
    CONSTRAINT fk_roles_authorities FOREIGN KEY (authority_id) REFERENCES authorities(authority_id)
);

-- Fonction de vérification : s'assure que le lieu associé à une personne est bien de type 'Site'
CREATE FUNCTION check_place_type_site_for_person() RETURNS trigger AS $$
BEGIN
    IF NEW.place_id IS NOT NULL THEN
        -- Vérifie si le lieu pointé est bien de type 'Site'
        PERFORM 1
        FROM places p
                 JOIN place_types pt ON p.place_type_id = pt.place_type_id
        WHERE p.place_id = NEW.place_id AND pt.name = 'Site';

        -- Si aucun résultat trouvé, on lève une erreur
        IF NOT FOUND THEN
            RAISE EXCEPTION 'Le lieu associé à une personne doit être de type "Site"';
        END IF;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger lié à la table 'persons' : appelé avant chaque INSERT ou UPDATE
CREATE TRIGGER trg_check_person_place
    BEFORE INSERT OR UPDATE ON persons
    FOR EACH ROW
EXECUTE FUNCTION check_place_type_site_for_person();

-- Fonction de vérification : s'assure que le lieu associé à un véhicule est bien de type 'Site'
CREATE FUNCTION check_place_type_site_for_vehicle() RETURNS trigger AS $$
BEGIN
    IF NEW.place_id IS NOT NULL THEN
        -- Vérifie si le lieu pointé est bien de type 'Site'
        PERFORM 1
        FROM places p
                 JOIN place_types pt ON p.place_type_id = pt.place_type_id
        WHERE p.place_id = NEW.place_id AND pt.name = 'Site';

        -- Si aucun résultat trouvé, on lève une erreur
        IF NOT FOUND THEN
            RAISE EXCEPTION 'Le lieu associé à un véhicule doit être de type "Site"';
        END IF;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger lié à la table 'vehicles' : appelé avant chaque INSERT ou UPDATE
CREATE TRIGGER trg_check_vehicle_place
    BEFORE INSERT OR UPDATE ON vehicles
    FOR EACH ROW
EXECUTE FUNCTION check_place_type_site_for_vehicle();

