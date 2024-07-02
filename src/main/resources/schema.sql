CREATE TABLE IF NOT EXISTS ratings
(
    id   INTEGER PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS films
(
    id           INTEGER PRIMARY KEY AUTO_INCREMENT,
    name         VARCHAR      NOT NULL,
    description  VARCHAR(200) NOT NULL,
    release_date DATE         NOT NULL,
    duration     INTEGER      NOT NULL CHECK (duration > 0),
    rating_id    INTEGER      NOT NULL,
    FOREIGN KEY (rating_id) REFERENCES ratings (id)
);

CREATE TABLE IF NOT EXISTS genres
(
    id   INTEGER PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS films_genres
(
    film_id  INTEGER NOT NULL,
    genre_id INTEGER NOT NULL,
    PRIMARY KEY (film_id, genre_id),
    FOREIGN KEY (film_id) REFERENCES films (id),
    FOREIGN KEY (genre_id) REFERENCES genres (id)
);

CREATE TABLE IF NOT EXISTS users
(
    id       INTEGER PRIMARY KEY AUTO_INCREMENT,
    email    VARCHAR NOT NULL,
    login    VARCHAR NOT NULL,
    name     VARCHAR,
    birthday DATE    NOT NULL
);

CREATE TABLE IF NOT EXISTS friends
(
    user_id   INTEGER NOT NULL,
    friend_id INTEGER NOT NULL,
    status    BOOLEAN,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (friend_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS likes
(
    film_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    PRIMARY KEY (film_id, user_id),
    FOREIGN KEY (film_id) REFERENCES films (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE INDEX IF NOT EXISTS idx_films_rating_id ON films (rating_id);
CREATE INDEX IF NOT EXISTS idx_films_genres_film_id ON films_genres (film_id);
CREATE INDEX IF NOT EXISTS idx_films_genres_genre_id ON films_genres (genre_id);
CREATE INDEX IF NOT EXISTS idx_friends_user_id ON friends (user_id);
CREATE INDEX IF NOT EXISTS idx_friends_friend_id ON friends (friend_id);
CREATE INDEX IF NOT EXISTS idx_likes_film_id ON likes (film_id);
CREATE INDEX IF NOT EXISTS idx_likes_user_id ON likes (user_id);
CREATE UNIQUE INDEX IF NOT EXISTS USER_EMAIL_UINDEX ON USERS (email);
CREATE UNIQUE INDEX IF NOT EXISTS USER_LOGIN_UINDEX ON USERS (login);
