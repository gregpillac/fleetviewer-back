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
-- Sites ENI pour Demo
    ('10 rue de Paris',  NULL, '44000', 'Nantes', NULL),
    ('12 avenue Victor', NULL, '35000', 'Rennes', NULL),
    ('1 chemin du Parc', NULL, '29000', 'Quimper',NULL),
    ('99 boulevard Sud', NULL, '79000', 'Niort', NULL);

INSERT INTO place_types (name) VALUES ('Site');

INSERT INTO places (name, is_public, place_type_id, address_id) VALUES
    ('Campus Nantes', true, 1, 1),
    ('Campus Rennes', true, 1, 2),
    ('Campus Quimper',true, 1, 3),
    ('Campus Niort', true, 1, 4);

INSERT INTO persons (first_name, last_name, email, phone, address_id, place_id) VALUES
-- Tous les rôles avec le meme site pour tests
    ('FleetViewer', 'Admin', 'admin@example.com', '0610000001', 1, 1),
    ('FleetViewer', 'Manager', 'manager@example.com', '0620000002', 2, 1),
    ('FleetViewer', 'User', 'user@example.com', '0630000003', 3, 1),
    ('FleetViewer', 'Default', 'default@example.com', '0640000004', 4, 1);


INSERT INTO roles (role_id, description) VALUES
    ('ROLE_ADMIN', 'Administrateur système avec tous les droits'),
    ('ROLE_MANAGER', 'Gestionnaire avec droits intermédiaires'),
    ('ROLE_USER', 'Utilisateur standard'),
    ('ROLE_DEFAULT', 'Utilisateur standard avec droits limités');

INSERT INTO app_users (username, password, role_id, enabled, person_id) VALUES
    ('fadmin', '$2a$10$.i.yksKgavPNoKwRpsFJXewIRDwkevD28QzMe7sq0eBrKiqnEtElO', 'ROLE_ADMIN', true, 1), --mdp = admin
    ('fmanager',  '$2a$10$FIzo.uV19uNZJ3VVV3Sr2eOIcO08FwgHibt4AFCeOpGAPEaO03sQ.', 'ROLE_MANAGER',  true, 2), --mdp = manager
    ('fuser',     '$2a$10$YzAwCQwO6imqWTayy8Ir8.htkBQQ8We9MnOjhfk2WJeDBFHlWFyTO', 'ROLE_USER',     true, 3), --mdp = user
    ('fdefault',  '$2a$10$WDeu79o3MA3RGQfLFgAg5.1zQqz.xMlTcR1SrMfV.byud0EdSA6JW', 'ROLE_DEFAULT', true, 4); --mdp = default

INSERT INTO public.vehicles(
    vehicle_id, license_plate, brand, model, seats, mileage, is_roadworthy, is_insurance_valid, place_id)
VALUES
    (1, 'BP-701-WY', 'Toyota', 'Yaris', 5, 10000, true, true, 1),
    (2, 'DJ-408-CX', 'Renault', 'Clio', 5, 25000, true, true, 1),
    (3, 'CK-755-GB', 'Peugeot', '208', 5, 25000, true, true, 2);
