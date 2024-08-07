drop table if exists "film" cascade;
CREATE TABLE if not exists "film"
(
    "id"           INTEGER GENERATED BY DEFAULT AS IDENTITY UNIQUE PRIMARY KEY,
    "name"         varchar(100),
    "description"  varchar(200),
    "release_date" date,
    "duration"     integer,
    "mpa"          integer
);

drop table if exists "genre" cascade;
CREATE TABLE if not exists "genre"
(
    "id"   INTEGER GENERATED BY DEFAULT AS IDENTITY UNIQUE PRIMARY KEY,
    "name" varchar
);

drop table if exists "mpa" cascade;
CREATE TABLE if not exists "mpa"
(
    "id"   INTEGER GENERATED BY DEFAULT AS IDENTITY UNIQUE PRIMARY KEY,
    "name" varchar
);

drop table if exists "films_genres" cascade;
CREATE TABLE if not exists "films_genres"
(
    "film_id"  integer not null,
    "genre_id" integer not null
);

alter table "films_genres"
    add constraint pk_films_genres primary key ("film_id", "genre_id");

drop table if exists "likes" cascade;
CREATE TABLE if not exists "likes"
(
    "user_id" integer not null,
    "film_id" integer not null
);

alter table "likes"
    add constraint pk_likes primary key ("user_id", "film_id");

drop table if exists "user" cascade;
CREATE TABLE if not exists "user"
(
    "id"       INTEGER GENERATED BY DEFAULT AS IDENTITY UNIQUE PRIMARY KEY,
    "email"    varchar
        constraint check_email check ("email" like '%@%.%'),
    "login"    varchar(100),
    "name"     varchar(100),
    "birthday" date
);

drop table if exists "users_friends" cascade;
CREATE TABLE if not exists "users_friends"
(
    "user_id"   integer not null,
    "friend_id" integer not null
);

alter table "users_friends"
    add constraint pk_users_friend primary key ("user_id", "friend_id");

ALTER TABLE "films_genres"
    ADD FOREIGN KEY ("film_id") REFERENCES "film" ("id");

ALTER TABLE "films_genres"
    ADD FOREIGN KEY ("genre_id") REFERENCES "genre" ("id");

ALTER TABLE "film"
    ADD FOREIGN KEY ("mpa") REFERENCES "mpa" ("id");

ALTER TABLE "likes"
    ADD FOREIGN KEY ("film_id") REFERENCES "film" ("id");

ALTER TABLE "likes"
    ADD FOREIGN KEY ("user_id") REFERENCES "user" ("id");

ALTER TABLE "users_friends"
    ADD FOREIGN KEY ("user_id") REFERENCES "user" ("id");

ALTER TABLE "users_friends"
    ADD FOREIGN KEY ("friend_id") REFERENCES "user" ("id");
