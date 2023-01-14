drop table if exists index;
drop table if exists kontaktperson;
drop table if exists kontakt_liste;

CREATE TABLE IF NOT EXISTS kontakt_liste
(
    id      serial primary key,
    changed TIMESTAMP,
    created DATE
);


CREATE TABLE IF NOT EXISTS index
(
    kontakt_liste integer primary key references kontakt_liste (id),
    vorname       VARCHAR(100),
    nachname      VARCHAR(100),
    erstbefund    DATE
);

CREATE TABLE IF NOT EXISTS kontaktperson
(
    id                   serial primary key,
    kontakt_liste        integer references kontakt_liste (id),
    vorname              VARCHAR(100),
    nachname             VARCHAR(100),
    kontaktinformationen VARCHAR(500)
);


