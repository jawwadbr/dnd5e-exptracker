INSERT INTO user(username, email, password, active, role, created_at, uuid) VALUES ('admin', 'admin@gmail.com', '$2a$10$feQKfzQ2O1vJ2qdoLcr0ju6CoWp.ufN7l1g31SgzDEuyWIsT1Gkdi', true, 'ROLE_ADMIN', '2024-02-04 21:00:00', UNHEX(REPLACE('3f2877a0-4a2b-4e5d-8b5e-3b5a07859a05', '-', '')))

INSERT INTO campaign(uuid, name, description, created_at, creator_user_id) VALUES (UNHEX(REPLACE('3f2877a0-4a2b-4e5d-8b5e-3b5a07859a03', '-', '')), 'Eldoria', 'A', '2024-02-04 22:00:00', 1)
INSERT INTO campaign(uuid, name, description, created_at, creator_user_id) VALUES (UNHEX(REPLACE('3f2877a0-4a2b-4e5d-8b5e-3b5a07859a04', '-', '')), 'A', 'B', '2024-02-04 22:00:00', 1)
INSERT INTO campaign(uuid, name, description, created_at, creator_user_id) VALUES (UNHEX(REPLACE('3f2877a0-4a2b-4e5d-8b5e-3b5a07859a06', '-', '')), 'Z', 'Z', '2024-02-04 22:00:00', 1)

INSERT INTO campaign_joined_users(campaign_id, user_id) VALUES (1, 1)
INSERT INTO campaign_joined_users(campaign_id, user_id) VALUES (2, 1)
INSERT INTO campaign_joined_users(campaign_id, user_id) VALUES (3, 1)

INSERT INTO class(name) VALUES ('Paladin')

INSERT INTO race(name) VALUES ('Human')

INSERT INTO player_character(experience_points, campaign_id, class_id, id, race_id, user_id, uuid, character_name) VALUES (2000, 1, 1, 1, 1, 1, UNHEX(REPLACE('83f8634b-33df-4f67-b659-66b55cd8534f', '-', '')), 'Mirlon')