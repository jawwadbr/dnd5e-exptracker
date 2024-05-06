INSERT INTO user(username, email, password, active, role, created_at, uuid) VALUES ('admin', 'admin@gmail.com', '$2a$10$feQKfzQ2O1vJ2qdoLcr0ju6CoWp.ufN7l1g31SgzDEuyWIsT1Gkdi', true, 'ROLE_ADMIN', '2024-02-04 21:00:00', UNHEX(REPLACE('3f2877a0-4a2b-4e5d-8b5e-3b5a07859a05', '-', '')))
INSERT INTO user(username, email, password, active, role, created_at, uuid) VALUES ('user', 'user@gmail.com', '$2a$10$7hdkudPilcXS/A3lh0y5/uBEwRhG3JJyxN0RY6HeMNaJWyaToHpHW', true, 'ROLE_USER', '2024-02-04 21:00:00', UNHEX(REPLACE('3f2877a0-4a2b-4e5d-8b5e-3b5a07859a10', '-', '')))

INSERT INTO campaign(uuid, name, description, created_at, creator_user_id) VALUES (UNHEX(REPLACE('3f2877a0-4a2b-4e5d-8b5e-3b5a07859a03', '-', '')), 'Eldoria', 'A', '2024-02-04 22:00:00', 1)
INSERT INTO campaign(uuid, name, description, created_at, creator_user_id) VALUES (UNHEX(REPLACE('3f2877a0-4a2b-4e5d-8b5e-3b5a07859a04', '-', '')), 'A', 'B', '2024-02-04 22:00:00', 1)
INSERT INTO campaign(uuid, name, description, created_at, creator_user_id) VALUES (UNHEX(REPLACE('3f2877a0-4a2b-4e5d-8b5e-3b5a07859a06', '-', '')), 'Z', 'Z', '2024-02-04 22:00:00', 1)

INSERT INTO campaign_joined_users(campaign_id, user_id) VALUES (1, 1)
INSERT INTO campaign_joined_users(campaign_id, user_id) VALUES (1, 2)
INSERT INTO campaign_joined_users(campaign_id, user_id) VALUES (2, 1)
INSERT INTO campaign_joined_users(campaign_id, user_id) VALUES (3, 1)

INSERT INTO class(name, uuid) VALUES ('Barbarian', UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440000', '-', '')));
INSERT INTO class(name, uuid) VALUES ('Bard', UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440001', '-', '')));
INSERT INTO class(name, uuid) VALUES ('Cleric', UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440002', '-', '')));
INSERT INTO class(name, uuid) VALUES ('Druid', UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440003', '-', '')));
INSERT INTO class(name, uuid) VALUES ('Fighter', UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440004', '-', '')));
INSERT INTO class(name, uuid) VALUES ('Monk', UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440005', '-', '')));
INSERT INTO class(name, uuid) VALUES ('Paladin', UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440006', '-', '')));
INSERT INTO class(name, uuid) VALUES ('Ranger', UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440007', '-', '')));
INSERT INTO class(name, uuid) VALUES ('Rogue', UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440008', '-', '')));
INSERT INTO class(name, uuid) VALUES ('Sorcerer', UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440009', '-', '')));
INSERT INTO class(name, uuid) VALUES ('Warlock', UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440010', '-', '')));
INSERT INTO class(name, uuid) VALUES ('Wizard', UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440011', '-', '')));
INSERT INTO class(name, uuid) VALUES ('Artificer', UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440012', '-', '')));
INSERT INTO class(name, uuid) VALUES ('Blood Hunter', UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440013', '-', '')));

INSERT INTO race(name, uuid) VALUES ('Dragonborn', UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440014', '-', '')));
INSERT INTO race(name, uuid) VALUES ('Dwarf', UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440015', '-', '')));
INSERT INTO race(name, uuid) VALUES ('Elf', UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440016', '-', '')));
INSERT INTO race(name, uuid) VALUES ('Gnome', UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440017', '-', '')));
INSERT INTO race(name, uuid) VALUES ('Half-Elf', UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440018', '-', '')));
INSERT INTO race(name, uuid) VALUES ('Halfling', UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440019', '-', '')));
INSERT INTO race(name, uuid) VALUES ('Half-Orc', UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440020', '-', '')));
INSERT INTO race(name, uuid) VALUES ('Human', UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440021', '-', '')));
INSERT INTO race(name, uuid) VALUES ('Tiefling', UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440022', '-', '')));
INSERT INTO race(name, uuid) VALUES ('Aarakocra', UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440023', '-', '')));
INSERT INTO race(name, uuid) VALUES ('Aasimar', UNHEX(REPLACE('550e8400-e29b-41d4-a716-446655440024', '-', '')));

INSERT INTO player_character(id, experience_points, campaign_id, class_id, race_id, user_id, uuid, character_name, active) VALUES (1, 2000, 1, 1, 1, 1, UNHEX(REPLACE('83f8634b-33df-4f67-b659-66b55cd8534f', '-', '')), 'Mirlon', true)
INSERT INTO player_character(id, experience_points, campaign_id, class_id, race_id, user_id, uuid, character_name, active) VALUES (2, 2900, 1, 3, 6, 2, UNHEX(REPLACE('3f2877a0-4a2b-4e5d-8b5e-3b5a07859a06', '-', '')), 'Makan', true)
