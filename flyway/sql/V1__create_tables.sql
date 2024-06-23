CREATE TABLE PET_OWNER
(
    id integer PRIMARY KEY,
    government_id varchar(20),
    first_name varchar(50),
    last_name  varchar(50),
    email varchar(100),
    phone_number varchar(20)
);

CREATE TABLE PET
(
    id integer PRIMARY KEY,
    owner_id integer,
    name varchar(50),
    weight double precision,
    breed varchar(50),
    training_level integer,
    is_vaccinated boolean,
    species varchar(20)
);

CREATE SEQUENCE OWNER_SEQUENCE
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE PET_SEQUENCE
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

