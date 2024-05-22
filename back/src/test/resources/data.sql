-- Insertion d'un utilisateur Admin avec le mot de passe "test!1234"
INSERT INTO USERS (first_name, last_name, admin, email, password)
VALUES ('Admin', 'Admin', true, 'yoga@studio.com', '$2a$10$.Hsa/ZjUVaHqi0tp9xieMeewrnZxrZ5pQRzddUXE/WjDu2ZThe6Iq');

-- Insertion des enseignants
INSERT INTO TEACHERS (first_name, last_name, created_at, updated_at)
VALUES ('Margot', 'DELAHAYE', NOW(), NOW()),
       ('Helene', 'THIERCELIN', NOW(), NOW());

-- Insertion de nouveaux utilisateurs (participants) avec leur mot de passe
INSERT INTO USERS (last_name, first_name, admin, email, password, created_at, updated_at)
VALUES ('DOE', 'John', false, 'john@email.com', '$2b$12$7nRUhkgWkmGhR/FrRRrn4O7chFb8aoGsBrNzp7NTln74o9KbVx.yy', NOW(), NOW()),
       ('DOE', 'Jane', false, 'jane@email.com', '$2b$12$7nRUhkgWkmGhR/FrRRrn4O7chFb8aoGsBrNzp7NTln74o9KbVx.yy', NOW(), NOW());

-- Insertion d'une SESSION avec Margot DELAHAYE en utilisant son nom complet
INSERT INTO SESSIONS (name, date, description, teacher_id, created_at, updated_at)
VALUES ('Matin', '2024-04-15 10:00:00', 'seance du matin',
        (SELECT id FROM TEACHERS WHERE first_name = 'Margot' AND last_name = 'DELAHAYE' LIMIT 1), NOW(), NOW());

-- Insertion d'une autre SESSION avec Hélène THIERCELIN en utilisant son nom complet
INSERT INTO SESSIONS (name, date, description, teacher_id, created_at, updated_at)
VALUES ('Soir', '2024-04-15 18:00:00', 'seance du soir',
        (SELECT id FROM TEACHERS WHERE first_name = 'Helene' AND last_name = 'THIERCELIN' LIMIT 1), NOW(), NOW());

-- Ajout de l'utilisateur Jane DOE à la dernière SESSION par email
INSERT INTO PARTICIPATE (session_id, user_id)
VALUES ((SELECT id FROM SESSIONS WHERE name='Matin'), (SELECT id FROM USERS WHERE email = 'jane@email.com'));