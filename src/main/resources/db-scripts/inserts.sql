-- Truncate ALL tables, respecting FK constraints
TRUNCATE TABLE
    roles_authorities,
    favorite_places,
    rideshares,
    vehicle_keys,
    itinerary_points,
    documents,
    reservations,
    app_users,
    vehicles,
    places,
    persons,
    roles,
    authorities,
    document_types,
    place_types,
    addresses
    RESTART IDENTITY CASCADE;

-- Insertions pour tests de connexion
INSERT INTO addresses (address_line_1, address_line_2, postal_code, city, gps_coords) VALUES
-- Adresses pour Demo
    ('10 rue de Paris',  NULL, '44000', 'Nantes', NULL),
    ('12 avenue Victor', NULL, '35000', 'Rennes', NULL),
    ('1 chemin du Parc', NULL, '29000', 'Quimper',NULL),
    ('99 boulevard Sud', NULL, '79000', 'Niort', NULL);

INSERT INTO place_types (name) VALUES
    ('Site'),
    ('Partenaire'),
    ('Evenement');

-- Sites ENI pour Demo;
INSERT INTO places (name, is_public, place_type_id, address_id) VALUES
    ('Campus Nantes', true, 1, 1),
    ('Campus Rennes', true, 1, 2),
    ('Campus Quimper',true, 1, 3),
    ('Campus Niort', true, 1, 4);

INSERT INTO persons (first_name, last_name, email, phone, address_id, place_id) VALUES
    ('Nantais', 'Admin', 'adminN@example.com', '0610000001', 1, 1),
    ('Nantais', 'Manager', 'managerN@example.com', '0620000002', 1, 1),
    ('Nantais', 'User01', 'userN01@example.com', '0630000003', 1, 1),
    ('Nantais', 'User02', 'userN02@example.com', '0630000003', 1, 1),
    ('Nantais', 'User03', 'userN03@example.com', '0630000003', 1, 1),
    ('Nantais', 'Default', 'defaultN@example.com', '0640000004', 1, 1),
    ('Rennais', 'Admin', 'adminR@example.com', '0610000001', 2, 2),
    ('Rennais', 'Manager', 'managerR@example.com', '0620000002', 2, 2),
    ('Rennais', 'User', 'userR@example.com', '0630000003', 2, 2),
    ('Rennais', 'User02', 'userR@example.com', '0630000003', 2, 2),
    ('Rennais', 'Default', 'defaultR@example.com', '0640000004', 2, 2);


INSERT INTO roles (role_id, description) VALUES
    ('ROLE_ADMIN', 'Administrateur système avec tous les droits'),
    ('ROLE_MANAGER', 'Gestionnaire avec droits intermédiaires'),
    ('ROLE_USER', 'Utilisateur standard'),
    ('ROLE_DEFAULT', 'Utilisateur standard avec droits limités');

INSERT INTO app_users (username, password, role_id, enabled, person_id) VALUES
    ('adminN', '$2a$10$.i.yksKgavPNoKwRpsFJXewIRDwkevD28QzMe7sq0eBrKiqnEtElO', 'ROLE_ADMIN', true, 1), --mdp = admin
    ('managerN',  '$2a$10$FIzo.uV19uNZJ3VVV3Sr2eOIcO08FwgHibt4AFCeOpGAPEaO03sQ.', 'ROLE_MANAGER',  true, 2), --mdp = manager
    ('userN01',     '$2a$10$YzAwCQwO6imqWTayy8Ir8.htkBQQ8We9MnOjhfk2WJeDBFHlWFyTO', 'ROLE_USER',     true, 3), --mdp = user
    ('userN02',     '$2a$10$YzAwCQwO6imqWTayy8Ir8.htkBQQ8We9MnOjhfk2WJeDBFHlWFyTO', 'ROLE_USER',     true, 4), --mdp = user
    ('userN03',     '$2a$10$YzAwCQwO6imqWTayy8Ir8.htkBQQ8We9MnOjhfk2WJeDBFHlWFyTO', 'ROLE_USER',     true, 5), --mdp = user
    ('defaultN',  '$2a$10$WDeu79o3MA3RGQfLFgAg5.1zQqz.xMlTcR1SrMfV.byud0EdSA6JW', 'ROLE_DEFAULT', true, 6), --mdp = default
    ('adminR', '$2a$10$.i.yksKgavPNoKwRpsFJXewIRDwkevD28QzMe7sq0eBrKiqnEtElO', 'ROLE_ADMIN', true, 7), --mdp = admin
    ('managerR',  '$2a$10$FIzo.uV19uNZJ3VVV3Sr2eOIcO08FwgHibt4AFCeOpGAPEaO03sQ.', 'ROLE_MANAGER',  true, 8), --mdp = manager
    ('userR01',     '$2a$10$YzAwCQwO6imqWTayy8Ir8.htkBQQ8We9MnOjhfk2WJeDBFHlWFyTO', 'ROLE_USER',     true, 9),
    ('userR02',     '$2a$10$YzAwCQwO6imqWTayy8Ir8.htkBQQ8We9MnOjhfk2WJeDBFHlWFyTO', 'ROLE_USER',     true, 10), --mdp = user
    ('defaultR',  '$2a$10$WDeu79o3MA3RGQfLFgAg5.1zQqz.xMlTcR1SrMfV.byud0EdSA6JW', 'ROLE_DEFAULT', true, 11); --mdp = default

INSERT INTO vehicles(
    vehicle_id, license_plate, brand, model, seats, mileage, is_roadworthy, is_insurance_valid, place_id)
VALUES
    (1, 'BP-701-WY', 'Toyota', 'Yaris', 5, 10000, true, true, 1),
    (2, 'DJ-408-CX', 'Renault', 'Clio', 5, 25000, true, true, 1),
    (3, 'CK-755-GB', 'Peugeot', '208', 5, 25000, true, true, 2),
    (5, 'AK-047-XX', 'Delorean', 'McFly', 2, 1000000, true, true, 2),
    (4, 'US-654-TZ', 'Peugeot', '5008', 5, 25000, true, true, 3);

INSERT INTO vehicle_keys(tag_label, vehicle_id, place_id) VALUES
    ( 'BP-701-WY_01', '1', '1'),
    ( 'BP-701-WY_02', '1', '2'),
    ( 'DJ-408-CX_01', '2', '1');

-- Cas A : départ = Campus Nantes (place_id=1), start_date le 2025-09-15 -> DOIT matcher ("departure = requested")
INSERT INTO reservations (departure_id, arrival_id, start_date, end_date, reservation_status, vehicle_id, person_id)
VALUES (1, 2, '2025-09-15 07:30:00', '2025-09-19 19:00:00', 'CONFIRMED', 1, 1);



