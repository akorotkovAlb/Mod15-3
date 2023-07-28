CREATE TABLE users(
id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
username VARCHAR(100) NOT NULL UNIQUE CHECK(LENGTH(username)>=2 AND LENGTH(username)<=100),
password VARCHAR(100) NOT NULL CHECK(LENGTH(password)>=2 AND LENGTH(password)<=100),
last_updated_date DATE NOT NULL,
created_date DATE NOT NULL
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