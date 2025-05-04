-- Table 'address' (Adresse)
create table address (
    address_id serial primary key,
    address_line_1 varchar(100), -- rue
    address_line_2 varchar(100), -- complément
    postal_code varchar(10),
    city varchar(50),
    gps_coords jsonb
);

-- Table 'place_type' (type de Lieu)
create table place_type (
    place_type_id serial primary key,
    name varchar(80)
);

-- Table 'vehicle_status' (statut de Véhicule)
create table vehicle_status (
    vehicle_status_id serial primary key,
    name varchar(50)
);

-- Table 'reservation_status' (statut de Réservation)
create table reservation_status (
    reservation_status_id serial primary key,
    name varchar(50)
);

-- Table 'document_type' (type de Document)
create table document_type (
    document_type_id serial primary key,
    name varchar(50)
);

-- Table 'authority' (Permission selon Spring Security)
create table authority (
    authority_id varchar(80) primary key,
    description varchar(255)
);

-- Table 'role' (Rôle)
create table role (
    role_id varchar(50) primary key,
    description varchar(255)
);

-- Table 'person' (Personne)
create table person (
    person_id serial primary key,
    first_name varchar(60),
    last_name varchar(80),
    email varchar(100),
    phone varchar(20),
    address_id int,
    constraint fk_person_address foreign key (address_id) references address(address_id)
);

-- Table 'place' (Lieu)
create table place (
    place_id serial primary key,
    name varchar(100),
    is_public boolean,
    place_type_id int,
    person_id int,
    address_id int,
    constraint fk_place_type foreign key (place_type_id) references place_type(place_type_id),
    constraint fk_place_person foreign key (person_id) references person(person_id) on delete set null,
    constraint fk_place_address foreign key (address_id) references address(address_id)
);

-- Ajout de la référence vers le lieu dans 'person' (nullable pour éviter tout problème de référence circulaire)
alter table person
    add column place_id int,
    add constraint fk_person_place foreign key (place_id) references place(place_id) on delete set null;

-- Table 'vehicle' (Véhicule)
create table vehicle (
    vehicle_id serial primary key,
    license_plate varchar(50),
    brand varchar(50),
    model varchar(50),
    seats int,
    mileage int,
    is_roadworthy boolean,
    is_insurance_valid boolean,
    place_id int not null,
    constraint fk_vehicle_place foreign key (place_id) references place(place_id)
);

-- Table 'app_user' (Utilisateur)
create table app_user (
    user_id serial primary key,
    username varchar(50),
    password varchar(255),
    enabled boolean default true,
    role_id varchar(50),
    person_id int,
    constraint fk_user_role foreign key (role_id) references role(role_id),
    constraint fk_user_person foreign key (person_id) references person(person_id)
);

-- Table 'reservation' (Réservation)
create table reservation (
    reservation_id serial primary key,
    reservation_date timestamp,
    reservation_status_id int,
    vehicle_id int,
    person_id int,
    constraint fk_reservation_status foreign key (reservation_status_id) references reservation_status(reservation_status_id),
    constraint fk_reservation_vehicle foreign key (vehicle_id) references vehicle(vehicle_id),
    constraint fk_reservation_person foreign key (person_id) references person(person_id)
);

-- Table 'document' (Document)
create table document (
    document_id serial primary key,
    valid_until date,
    document_type_id int not null,
    vehicle_id int,
    person_id int,
    constraint fk_document_type foreign key (document_type_id) references document_type(document_type_id),
    constraint fk_document_vehicle foreign key (vehicle_id) references vehicle(vehicle_id),
    constraint fk_document_person foreign key (person_id) references person(person_id),
    constraint chk_document_target check ( -- Document lié soit à un Véhicule soit à une Personne
        (vehicle_id is not null and person_id is null) or
        (vehicle_id is null and person_id is not null)
    )
);

-- Table 'itinerary_point' (Point d'itinéraire)
create table itinerary_point (
    reservation_id int,
    place_id int,
    date_time timestamp,
    point_type varchar(50),
    primary key (reservation_id, place_id),
    constraint fk_itinerary_reservation foreign key (reservation_id) references reservation(reservation_id),
    constraint fk_itinerary_place foreign key (place_id) references place(place_id)
);

-- Table 'vehicle_key' (Clé de Véhicule)
create table vehicle_key  (
    vehicle_key_id serial primary key,
    place_id int,
    vehicle_id int,
    constraint fk_key_place foreign key (place_id) references place(place_id),
    constraint fk_key_vehicle foreign key (vehicle_id) references vehicle(vehicle_id)
);

-- Table 'vehicle_availability' (Disponibilité du véhicule)
create table vehicle_availability (
    vehicle_id int,
    vehicle_status_id int,
    primary key (vehicle_id, vehicle_status_id),
    constraint fk_availability_vehicle foreign key (vehicle_id) references vehicle(vehicle_id),
    constraint fk_availability_status foreign key (vehicle_status_id) references vehicle_status(vehicle_status_id)
);

-- Table 'rideshare' (Covoiturage)
create table rideshare (
    person_id int,
    reservation_id int,
    primary key (person_id, reservation_id),
    constraint fk_rideshare_person foreign key (person_id) references person(person_id),
    constraint fk_rideshare_reservation foreign key (reservation_id) references reservation(reservation_id)
);

-- Table 'favorite_place' (Lieu favori)
create table favorite_place (
    person_id int,
    place_id int,
    primary key (person_id, place_id),
    constraint fk_favorite_person foreign key (person_id) references person(person_id),
    constraint fk_favorite_place foreign key (place_id) references place(place_id)
);

-- Table 'role_authority' (Permissions par rôle)
create table role_authority (
    role_id varchar(50),
    authority_id varchar(50),
    primary key (role_id, authority_id),
    constraint fk_role_authority_role foreign key (role_id) references role(role_id),
    constraint fk_role_authority_authority foreign key (authority_id) references authority(authority_id)
);
