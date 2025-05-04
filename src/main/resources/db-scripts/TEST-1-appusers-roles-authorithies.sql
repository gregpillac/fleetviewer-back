-- Suppression des tables dans l'ordre inverse de leur dépendance
DROP TABLE IF EXISTS roles_authorities;
DROP TABLE IF EXISTS app_users;
DROP TABLE IF EXISTS roles;
DROP TABLE IF EXISTS authorities;


-- Table 'authorities' (Permissions selon Spring Security)
create table authorities (
    authority_id varchar(80) primary key,
    description varchar(255)
);

-- Table 'roles' (Rôle)
create table roles (
    role_id varchar(50) primary key,
    description varchar(255)
);

-- Création de la table de relation entre rôles et authorities (many-to-many)
CREATE TABLE roles_authorities (
    role_id VARCHAR(50) NOT NULL,
    authority_id VARCHAR(50) NOT NULL,
    PRIMARY KEY (role_id, authority_id),
    CONSTRAINT fk_role_auth_role FOREIGN KEY (role_id) REFERENCES roles(role_id) ON DELETE CASCADE,
    CONSTRAINT fk_role_auth_authority FOREIGN KEY (authority_id) REFERENCES authorities(authority_id) ON DELETE CASCADE
);


-- Table 'app_users' (Utilisateur)
create table app_users (
    user_id serial primary key,
    username varchar(50),
    password varchar(255),
    enabled boolean default true,
    role_id varchar(50),
    constraint fk_user_role foreign key (role_id) references roles(role_id)
);

-- Insertions pour tests de connexion
INSERT INTO roles (role_id, description) VALUES
    ('ROLE_ADMIN', 'Administrateur système avec tous les droits'),
    ('ROLE_MANAGER', 'Gestionnaire avec droits intermédiaires'),
    ('ROLE_USER', 'Utilisateur standard'),
    ('ROLE_DEFAULT', 'Utilisateur standard avec droits limités');

INSERT INTO app_users (username, password, role_id, enabled) VALUES
    ('admin', '$2a$10$W51oznW.2hAL5z6souxyNO8YBCBOI7JUnLEthcG3INfGH4Ty5.cti', 'ROLE_ADMIN', true);
