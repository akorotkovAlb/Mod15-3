CREATE TABLE roles(
id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
name VARCHAR(50) NOT NULL
);

CREATE TABLE users(
id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
username VARCHAR(100) NOT NULL UNIQUE CHECK(LENGTH(username)>=2 AND LENGTH(username)<=100),
password VARCHAR(100) NOT NULL,
email VARCHAR(100) NOT NULL,
last_updated_date DATE NOT NULL,
created_date DATE NOT NULL
);

CREATE TABLE user_roles(
id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
user_id BIGINT NOT NULL,
role_id BIGINT NOT NULL
);

CREATE TABLE note(
id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
user_id BIGINT NOT NULL,
title VARCHAR(250) NOT NULL CHECK(LENGTH(title)>=3 AND LENGTH(title)<=250),
content VARCHAR NOT NULL,
last_updated_date DATE NOT NULL,
created_date DATE NOT NULL
);

ALTER TABLE public.note ADD CONSTRAINT note_fk FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;
ALTER TABLE public.user_roles ADD CONSTRAINT user_role_fk FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE;
ALTER TABLE public.user_roles ADD CONSTRAINT role_user_fk FOREIGN KEY (role_id) REFERENCES public.roles(id) ON DELETE CASCADE;

INSERT INTO public.roles (name) VALUES ('SUPER_ADMIN'), ('ADMIN'), ('USER'), ('USER1');

INSERT INTO public.users (username, password, email, last_updated_date, created_date)
VALUES ('admin', '$2a$10$.IeRI/Gy/8UscmtMmMHyDe2PDe0TMLn.9vb6WUSS2FVtzGmEZzcj2', 'admin@gmail.com', NOW(), NOW()),
('user', '$2a$10$uZ/R0BFKt2MqlybEAcO2NebJkdp5qWI3G2SnssNn3HcMHEZAC61Nu', 'user@gmail.com', NOW(), NOW());

INSERT INTO public.user_roles (user_id, role_id) VALUES (1, 2), (2, 3);