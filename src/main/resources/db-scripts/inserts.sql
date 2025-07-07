-- Truncate ALL tables, respecting FK constraints
TRUNCATE TABLE
    roles_authorities,
    favorite_places,
    rideshares,
    vehicles_availabilities,
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
    reservation_status,
    vehicle_status,
    place_types,
    addresses
    RESTART IDENTITY CASCADE;

-- Insertions pour tests de connexion
INSERT INTO addresses (address_line_1, address_line_2, postal_code, city, gps_coords) VALUES
    ('10 rue de Paris',  NULL, '75001', 'Paris', NULL),
    ('12 avenue Victor', NULL, '69002', 'Lyon', NULL),
    ('1 chemin du Parc', NULL, '31000', 'Toulouse',NULL),
    ('99 boulevard Sud', NULL, '06000', 'Nice', NULL);

INSERT INTO place_types (name) VALUES ('Site');
INSERT INTO places (name, is_public, place_type_id, person_id, address_id) VALUES
    ('Siège Paris', true, 1, NULL, 1),
    ('Agence Lyon', true, 1, NULL, 2),
    ('Annexe Toulouse',true, 1, NULL, 3),
    ('Bureau Nice', true, 1, NULL, 4);

INSERT INTO persons (first_name, last_name, email, phone, address_id, place_id) VALUES
    ('FleetViewer', 'Admin', 'admin@example.com', '0610000001', 1, 1),
    ('FleetViewer', 'Manager', 'manager@example.com', '0620000002', 2, 2),
    ('FleetViewer', 'User', 'user@example.com', '0630000003', 3, 3),
    ('FleetViewer', 'Default', 'default@example.com', '0640000004', 4, 4);


INSERT INTO roles (role_id, description) VALUES
    ('ROLE_ADMIN', 'Administrateur système avec tous les droits'),
    ('ROLE_MANAGER', 'Gestionnaire avec droits intermédiaires'),
    ('ROLE_USER', 'Utilisateur standard'),
    ('ROLE_DEFAULT', 'Utilisateur standard avec droits limités');

INSERT INTO app_users (username, password, role_id, enabled, person_id) VALUES
    ('fadmin', '$2a$10$dcX.8i2fuu5oNZ3fyp0L6u0EivZk0Bg0NSHeso.KE/FuWSgSJtI0S', 'ROLE_ADMIN', true, 1), --mdp = admin
    ('fmanager',  '$2a$10$Dc3Ve3bF9D8iMaEgsczqOeqLzapBPmlDbhvP2.7sTlrQFLR0s9NF.', 'ROLE_MANAGER',  true, 2), --mdp = manager
    ('fuser',     '$2a$10$1FDWbYjXsSCVDrIdK9WX.ubIJItX.JomSIeGHFW2XTul1XakeZJOy', 'ROLE_USER',     true, 3), --mdp = user
    ('fdefault',  '$2a$10$onJD8DVYaijoNMHbXw36xOtjG6OBUjf03oD0XGynf.heV7Puloo4u', 'ROLE_DEFAULT', true, 4); --mdp = default