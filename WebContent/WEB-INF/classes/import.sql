insert into Users (id, login, password, creation_time) values (654321, 'joseito', '$2a$10$MN60/6d9FcAI5nTD9faOP.UQ73qVW/bp1L3YTN3EnJXTS3Z.KrSke', NOW());
insert into Roles (user_id, role) values (654321, 'ROLE_USER');
