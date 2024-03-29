CREATE TABLE IF NOT EXISTS "USERS" (
  "USER_ID" integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  "USER_NAME" varchar,
  "EMAIL" varchar,
  "LOGIN" varchar NOT NULL,
  "BIRTHDAY" date
);

CREATE TABLE IF NOT EXISTS "FRIENDS" (
  "USER_ID" integer REFERENCES USERS (USER_ID) ON DELETE CASCADE,
  "FRIEND_ID" integer REFERENCES USERS (USER_ID) ON DELETE CASCADE,
  PRIMARY KEY (USER_ID, FRIEND_ID)
);

CREATE TABLE IF NOT EXISTS "MPA" (
  "MPA_ID" integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  "MPA_NAME" varchar NOT NULL
);

CREATE TABLE IF NOT EXISTS "FILMS" (
  "FILM_ID" integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  "FILM_NAME" varchar NOT NULL,
  "DESCRIPTION" varchar,
  "RELEASE_DATE" date,
  "DURATION" integer,
  "MPA_ID" integer REFERENCES MPA (MPA_ID)
);

CREATE TABLE IF NOT EXISTS "LIKES" (
  "FILM_ID" integer REFERENCES FILMS (FILM_ID) ON DELETE CASCADE,
  "USER_ID" integer REFERENCES USERS (USER_ID) ON DELETE CASCADE,
  PRIMARY KEY (FILM_ID, USER_ID)
);

CREATE TABLE IF NOT EXISTS "GENRES" (
  "GENRE_ID" integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  "GENRE_NAME" varchar NOT NULL
);

CREATE TABLE IF NOT EXISTS "FILM_GENRES" (
  "FILM_ID" integer REFERENCES FILMS (FILM_ID) ON DELETE CASCADE,
  "GENRE_ID" integer REFERENCES GENRES (GENRE_ID) ON DELETE CASCADE,
  PRIMARY KEY (FILM_ID, GENRE_ID)
);

CREATE UNIQUE INDEX IF NOT EXISTS USER_EMAIL_UINDEX ON USERS (email);
CREATE UNIQUE INDEX IF NOT EXISTS USER_LOGIN_UINDEX ON USERS (login);