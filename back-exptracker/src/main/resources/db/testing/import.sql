INSERT INTO user(username, email, password, active, role, created_at, uuid) VALUES ('admin', 'admin@gmail.com', '$2a$10$feQKfzQ2O1vJ2qdoLcr0ju6CoWp.ufN7l1g31SgzDEuyWIsT1Gkdi', true, 'ROLE_ADMIN', '2024-02-04 21:00:00', UNHEX(REPLACE('3f2877a0-4a2b-4e5d-8b5e-3b5a07859a05', '-', '')))
INSERT INTO user(username, email, password, active, role, created_at, uuid) VALUES ('user', 'user@gmail.com', '$2a$10$7hdkudPilcXS/A3lh0y5/uBEwRhG3JJyxN0RY6HeMNaJWyaToHpHW', true, 'ROLE_USER', '2024-02-04 21:00:00', UNHEX(REPLACE('3f2877a0-4a2b-4e5d-8b5e-3b5a07859a10', '-', '')))

INSERT INTO campaign(uuid, name, description, created_at, creator_user_id) VALUES (UNHEX(REPLACE('3f2877a0-4a2b-4e5d-8b5e-3b5a07859a03', '-', '')), 'Eldoria', 'A', '2024-02-04 22:00:00', 1)
INSERT INTO campaign(uuid, name, description, created_at, creator_user_id) VALUES (UNHEX(REPLACE('3f2877a0-4a2b-4e5d-8b5e-3b5a07859a04', '-', '')), 'A', 'B', '2024-02-04 22:00:00', 1)
INSERT INTO campaign(uuid, name, description, created_at, creator_user_id) VALUES (UNHEX(REPLACE('3f2877a0-4a2b-4e5d-8b5e-3b5a07859a06', '-', '')), 'Z', 'Z', '2024-02-04 22:00:00', 1)

INSERT INTO campaign_joined_users(campaign_id, user_id) VALUES (1, 1)
INSERT INTO campaign_joined_users(campaign_id, user_id) VALUES (1, 2)
INSERT INTO campaign_joined_users(campaign_id, user_id) VALUES (2, 1)
INSERT INTO campaign_joined_users(campaign_id, user_id) VALUES (3, 1)

INSERT INTO class(name, uuid) VALUES ('Barbarian', UNHEX(REPLACE('d9b441ed-707b-4161-be2a-8d098f7481a8', '-', '')));
INSERT INTO class(name, uuid) VALUES ('Bard', UNHEX(REPLACE('e81f7b43-ef18-4902-99ae-cc7610cb93ff', '-', '')));
INSERT INTO class(name, uuid) VALUES ('Cleric', UNHEX(REPLACE('a125f1fe-bc62-4cac-abec-da0b61664135', '-', '')));
INSERT INTO class(name, uuid) VALUES ('Druid', UNHEX(REPLACE('ef48aa5c-4259-4456-a76d-e1e9aaa1097c', '-', '')));
INSERT INTO class(name, uuid) VALUES ('Fighter', UNHEX(REPLACE('16692c6e-4014-4fa4-93ed-e2cf0b3e8bee', '-', '')));
INSERT INTO class(name, uuid) VALUES ('Monk', UNHEX(REPLACE('942a46d3-9a9b-450e-bc80-fa704918fd63', '-', '')));
INSERT INTO class(name, uuid) VALUES ('Paladin', UNHEX(REPLACE('20816647-36b3-40b8-acba-2824f9d1760c', '-', '')));
INSERT INTO class(name, uuid) VALUES ('Ranger', UNHEX(REPLACE('926341f5-eef3-4e09-8b78-9101343b0ee5', '-', '')));
INSERT INTO class(name, uuid) VALUES ('Rogue', UNHEX(REPLACE('df5f23b9-a04b-4fab-bddc-28ac8b9bd3fe', '-', '')));
INSERT INTO class(name, uuid) VALUES ('Sorcerer', UNHEX(REPLACE('fa9ebfaf-6420-462b-9798-5ec5aa70a9c1', '-', '')));
INSERT INTO class(name, uuid) VALUES ('Warlock', UNHEX(REPLACE('e1a36f43-d656-4700-8993-b75b12eb36f0', '-', '')));
INSERT INTO class(name, uuid) VALUES ('Wizard', UNHEX(REPLACE('070b837c-e278-4a10-bf95-6a32b47ea569', '-', '')));
INSERT INTO class(name, uuid) VALUES ('Artificer', UNHEX(REPLACE('a708fd16-695b-4c1c-b438-9ec5ffe8e753', '-', '')));
INSERT INTO class(name, uuid) VALUES ('Blood Hunter', UNHEX(REPLACE('8bd61762-6605-4f64-9225-f7259a753436', '-', '')));

INSERT INTO race(name, uuid) VALUES ('Dragonborn', UNHEX(REPLACE('c21ed845-1c4b-4ee6-be1c-6a224bf73840', '-', '')));
INSERT INTO race(name, uuid) VALUES ('Dwarf', UNHEX(REPLACE('1254d511-2cca-4bd0-a282-4590adebbbe2', '-', '')));
INSERT INTO race(name, uuid) VALUES ('Elf', UNHEX(REPLACE('aed0265b-f473-4174-bf5d-32efc9d43081', '-', '')));
INSERT INTO race(name, uuid) VALUES ('Gnome', UNHEX(REPLACE('0923359b-47e7-4978-b38f-c91850baf56a', '-', '')));
INSERT INTO race(name, uuid) VALUES ('Half-Elf', UNHEX(REPLACE('4dacf868-949c-4731-8609-dce19c884b4c', '-', '')));
INSERT INTO race(name, uuid) VALUES ('Halfling', UNHEX(REPLACE('6756bf69-2987-41cf-84cf-daeb9ce10705', '-', '')));
INSERT INTO race(name, uuid) VALUES ('Half-Orc', UNHEX(REPLACE('f5964269-2ab7-432c-bdb3-39690310ff3d', '-', '')));
INSERT INTO race(name, uuid) VALUES ('Human', UNHEX(REPLACE('96e87ea8-7edc-4594-9737-a82bd550c591', '-', '')));
INSERT INTO race(name, uuid) VALUES ('Tiefling', UNHEX(REPLACE('2822276b-b817-485f-a9f4-77bc67f0b77d', '-', '')));
INSERT INTO race(name, uuid) VALUES ('Aarakocra', UNHEX(REPLACE('e5261bc9-c38a-4bce-962d-b764ffb056f7', '-', '')));
INSERT INTO race(name, uuid) VALUES ('Aasimar', UNHEX(REPLACE('777e31c3-f8dc-432c-b729-aa2d189c9abe', '-', '')));
INSERT INTO race(name, uuid) VALUES ('Shadar-Kai', UNHEX(REPLACE('c9f55d34-111c-4299-840c-5af2064968b2', '-', '')));
INSERT INTO race(name, uuid) VALUES ('Changeling', UNHEX(REPLACE('311fbd73-6bf1-4ea8-9a08-b40506fccbab', '-', '')));


INSERT INTO player_character(id, experience_points, campaign_id, class_id, race_id, user_id, uuid, character_name, active) VALUES (1, 2000, 1, 1, 1, 1, UNHEX(REPLACE('83f8634b-33df-4f67-b659-66b55cd8534f', '-', '')), 'Mirlon', true)
INSERT INTO player_character(id, experience_points, campaign_id, class_id, race_id, user_id, uuid, character_name, active) VALUES (2, 2900, 1, 3, 6, 2, UNHEX(REPLACE('3f2877a0-4a2b-4e5d-8b5e-3b5a07859a06', '-', '')), 'Makan', true)

CREATE INDEX expiry_date_index ON invite_code (expiry_date);
