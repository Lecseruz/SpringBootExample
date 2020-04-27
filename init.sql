DROP TABLE IF EXISTS activity;
DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users
(
    id      SERIAL       NOT NULL PRIMARY KEY,
    country VARCHAR(100) NOT NULL,
    money   BIGINT       NOT NULL DEFAULT 0
);
CREATE TABLE IF NOT EXISTS activity
(
    id      SERIAL primary key,
    user_id int       NOT NULL,
    stage   TIMESTAMP NOT NULL DEFAULT current_timestamp,
    value   BIGINT    NOT NULL DEFAULT 0
);

CREATE INDEX IF NOT EXISTS country_users ON users (country);
CREATE INDEX IF NOT EXISTS activity_user_id ON activity (user_id);

create function check_id_change() returns trigger
    language plpgsql as
$$
BEGIN
    IF old.country <> new.country THEN
        RAISE EXCEPTION 'cannot change country';
    END IF;
    return new;
END;
$$;

CREATE TRIGGER client_update_trigger
    AFTER UPDATE
    ON users
    FOR EACH ROW
EXECUTE PROCEDURE check_id_change();


INSERT into users
values ('1', 'Russia', 21221);
INSERT into users
values ('2', 'Russia', 123);
INSERT into users
values ('3', 'Russia', 12);
INSERT into users
values ('4', 'Russia', 123);
INSERT into users
values ('5', 'Russia', 121);
INSERT into users
values ('6', 'Russia', 2334423);
INSERT into users
values ('7', 'columbia', 23432424);
INSERT into users
values ('8', 'FRANCE', 21221);
INSERT into users
values ('9', 'IRELAND', 21221);
INSERT into users
values ('10', 'UK', 21221);
INSERT into users
values ('11', 'USA', 21221);
INSERT into users
values ('12', 'USA', 21221);