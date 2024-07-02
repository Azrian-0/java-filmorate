package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeDbStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

//@JdbcTest If you are looking to load your full application configuration, but use an embedded database, you should consider @SpringBootTest combined with @AutoConfigureTestDatabase rather than this annotation.
@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FilmorateApplicationTests {

    @Autowired
    private FilmDbStorage filmDbStorage;
    @Autowired
    private GenreDbStorage genreDbStorage;
    @Autowired
    private LikeDbStorage likeDbStorage;
    @Autowired
    private MpaDbStorage mpaDbStorage;
    @Autowired
    private UserDbStorage userDbStorage;

    private Film film;
    private Film film1;
    private User user;
    private User user1;
    private User user2;
    private Genre genre;
    private Genre genre1;
    private Mpa mpa;
    private Mpa mpa1;

    @BeforeEach
    void setUp() {
        mpa = Mpa.builder()
                .id(6)
                .name("0+")
                .build();

        mpa1 = Mpa.builder()
                .id(7)
                .name("6+")
                .build();

        genre = Genre.builder()
                .id(7)
                .name("Фантастика")
                .build();

        genre1 = Genre.builder()
                .id(8)
                .name("Мюзикл")
                .build();

        film = Film.builder()
                .name("First Test Film")
                .description("Test description")
                .releaseDate(LocalDate.now().minusYears(1))
                .duration(120)
                .build();
        film.setGenres(new LinkedHashSet<>());
        film.setLikes(new LinkedHashSet<>());
        film.setMpa(Mpa.builder()
                .id(1)
                .name("G")
                .build());

        film1 = Film.builder()
                .name("Second Test Film")
                .description("Test description")
                .releaseDate(LocalDate.now().minusYears(1))
                .duration(120)
                .build();
        film1.setGenres(new LinkedHashSet<>());
        film1.setLikes(new LinkedHashSet<>());
        film1.setMpa(Mpa.builder()
                .id(1)
                .name("G")
                .build());

        user = User.builder()
                .email("test@example1.com")
                .login("TestLogin1")
                .name("Test name")
                .birthday(LocalDate.now().minusYears(1))
                .build();

        user1 = User.builder()
                .email("test1@example2.com")
                .login("TestLogin2")
                .name("Test name")
                .birthday(LocalDate.now().minusYears(1))
                .build();

        user2 = User.builder()
                .email("test1@example3.com")
                .login("TestLogin3")
                .name("Test name")
                .birthday(LocalDate.now().minusYears(1))
                .build();
    }

    @Test
    public void testCreateFilm() {
        Film createdFilm = filmDbStorage.create(film);
        assertNotNull(createdFilm.getId());
        assertEquals("First Test Film", createdFilm.getName());
    }

    @Test
    public void testUpdateFilm() {
        Film createdFilm = filmDbStorage.create(film);
        createdFilm.setName("Updated Name");
        Film updatedFilm = filmDbStorage.update(createdFilm);
        assertEquals("Updated Name", updatedFilm.getName());
    }

    @Test
    public void testGetAllFilms() {
        filmDbStorage.create(film);
        filmDbStorage.create(film1);
        Set<Film> films = filmDbStorage.getAll();
        assertEquals(2, films.size());
    }

    @Test
    public void testGetFilmById() {
        Film createdFilm = filmDbStorage.create(film);
        Film foundFilm = filmDbStorage.getById(createdFilm.getId());
        assertEquals(createdFilm.getId(), foundFilm.getId());
    }

    @Test
    public void testDeleteFilmById() {
        Film createdFilm = filmDbStorage.create(film);
        filmDbStorage.deleteById(createdFilm.getId());
        assertFalse(filmDbStorage.isFilmExist(createdFilm.getId()));
    }

    @Test
    public void testAddLikeToFilm() {
        filmDbStorage.create(film);
        userDbStorage.create(user);
        filmDbStorage.addLike(film.getId(), user.getId());
        assertEquals(1, likeDbStorage.getLikesByFilmId(film.getId()).size());
    }

    @Test
    public void testDeleteLikeFromFilm() {
        filmDbStorage.create(film);
        userDbStorage.create(user);
        filmDbStorage.addLike(film.getId(), user.getId());
        filmDbStorage.deleteLike(film.getId(), user.getId());
        assertEquals(0, likeDbStorage.getLikesByFilmId(film.getId()).size());
    }

    @Test
    public void testGetPopularFilms() {
        filmDbStorage.create(film);
        filmDbStorage.create(film1);
        userDbStorage.create(user);
        filmDbStorage.addLike(film1.getId(), user.getId());
        List<Film> popularFilms = filmDbStorage.getPopular(1);
        assertTrue(popularFilms.contains(film1));
    }

    @Test
    public void testCreateUser() {
        User createdUser = userDbStorage.create(user);
        assertNotNull(createdUser.getId());
        assertEquals("Test name", createdUser.getName());
    }

    @Test
    public void testUpdateUser() {
        User createdUser = userDbStorage.create(user);
        createdUser.setName("Updated Name");
        User updatedUser = userDbStorage.update(createdUser);
        assertEquals("Updated Name", updatedUser.getName());
    }

    @Test
    public void testGetAllUser() {
        userDbStorage.create(user);
        userDbStorage.create(user1);
        Set<User> users = userDbStorage.getAll();
        assertEquals(2, users.size());
    }

    @Test
    public void testGetUserById() {
        User createdUser = userDbStorage.create(user);
        User foundUser = userDbStorage.getById(createdUser.getId());
        assertEquals(createdUser.getId(), foundUser.getId());
    }

    @Test
    public void testDeleteUserById() {
        User createdUser = userDbStorage.create(user);
        userDbStorage.deleteById(createdUser.getId());
        assertFalse(userDbStorage.isUserExist(createdUser.getId()));
    }

    @Test
    public void testAddFriend() {
        userDbStorage.create(user);
        userDbStorage.create(user1);
        userDbStorage.addFriend(user.getId(), user1.getId());
        assertEquals(1, userDbStorage.getFriends(user.getId()).size());
    }

    @Test
    public void testDeleteFriend() {
        userDbStorage.create(user);
        userDbStorage.create(user1);
        userDbStorage.addFriend(user.getId(), user1.getId());
        userDbStorage.deleteFriend(user.getId(), user1.getId());
        assertEquals(0, userDbStorage.getFriends(user.getId()).size());
    }

    @Test
    public void testGetFriends() {
        userDbStorage.create(user);
        userDbStorage.create(user1);
        userDbStorage.addFriend(user.getId(), user1.getId());
        Set<User> foundFriends = userDbStorage.getFriends(user.getId());
        assertTrue(foundFriends.contains(user1));
    }

    @Test
    void testGetMutualFriends() {
        userDbStorage.create(user);
        userDbStorage.create(user1);
        userDbStorage.create(user2);
        userDbStorage.addFriend(user.getId(), user2.getId());
        userDbStorage.addFriend(user1.getId(), user2.getId());
        Set<User> mutualFriends = userDbStorage.getMutualFriends(user.getId(), user1.getId());
        assertTrue(mutualFriends.contains(user2));
    }

    @Test
    public void testCreateGenre() {
        Genre createdGenre = genreDbStorage.create(genre);
        assertNotNull(createdGenre.getId());
        assertEquals("Фантастика", createdGenre.getName());
    }

    @Test
    public void testGetGenreById() {
        Genre createdGenre = genreDbStorage.create(genre);
        Genre foundGenre = genreDbStorage.getById(createdGenre.getId());
        assertEquals(createdGenre.getId(), foundGenre.getId());
    }

    @Test
    public void testUpdateGenre() {
        Genre createdGenre = genreDbStorage.create(genre);
        createdGenre.setName("Updated Name");
        Genre updatedGenre = genreDbStorage.update(createdGenre);
        assertEquals("Updated Name", updatedGenre.getName());
    }

    @Test
    public void testDeleteGenreById() {
        Genre createdGenre = genreDbStorage.create(genre);
        genreDbStorage.deleteById(createdGenre.getId());
        assertFalse(genreDbStorage.checkGenreExist(createdGenre.getId()));
    }

    @Test
    public void testGetAllGenres() {
        genreDbStorage.create(genre);
        genreDbStorage.create(genre1);
        List<Genre> genres = genreDbStorage.getAll();
        assertEquals(8, genres.size());
    }

    @Test
    public void testGetGenresByFilmId() {
        filmDbStorage.create(film);
        genreDbStorage.create(genre);
        genreDbStorage.create(genre1);
        film.setGenres(new LinkedHashSet<>(Arrays.asList(genre, genre1)));
        genreDbStorage.addGenresToFilm(film);
        LinkedHashSet<Genre> genres = genreDbStorage.getGenresByFilmId(film.getId());
        assertEquals(2, genres.size());
        assertTrue(genres.contains(genre) && genres.contains(genre1));
    }

    @Test
    public void testAddGenresToFilm() {
        filmDbStorage.create(film);
        genreDbStorage.create(genre);
        film.setGenres(new LinkedHashSet<>(Collections.singletonList(genre)));
        genreDbStorage.addGenresToFilm(film);
        LinkedHashSet<Genre> genres = genreDbStorage.getGenresByFilmId(film.getId());
        assertEquals(1, genres.size());
        assertTrue(genres.contains(genre));
    }

    @Test
    public void testUpdateGenresToFilm() {
        filmDbStorage.create(film);
        genreDbStorage.create(genre);
        film.setGenres(new LinkedHashSet<>(Collections.singletonList(genre)));
        genreDbStorage.addGenresToFilm(film);
        genreDbStorage.create(genre1);
        film.setGenres(new LinkedHashSet<>(Collections.singletonList(genre1)));
        genreDbStorage.updateGenresToFilm(film);
        LinkedHashSet<Genre> genres = genreDbStorage.getGenresByFilmId(film.getId());
        assertEquals(1, genres.size());
        assertTrue(genres.contains(genre1));
    }

    @Test
    void testCreateMpa() {
        Mpa createdMpa = mpaDbStorage.create(mpa);
        assertNotNull(createdMpa.getId());
        assertEquals("0+", createdMpa.getName());
    }

    @Test
    void testGetMpaById() {
        Mpa createdMpa = mpaDbStorage.create(mpa);
        Mpa foundMpa = mpaDbStorage.getById(createdMpa.getId());
        assertEquals(createdMpa.getId(), foundMpa.getId());
    }

    @Test
    void testUpdateMpa() {
        Mpa createdMpa = mpaDbStorage.create(mpa);
        createdMpa.setName("Updated Name");
        Mpa updatedMpa = mpaDbStorage.update(createdMpa);
        assertEquals("Updated Name", updatedMpa.getName());
    }

    @Test
    void deleteMpaById() {
        Mpa createdMpa = mpaDbStorage.create(mpa);
        mpaDbStorage.deleteById(createdMpa.getId());
        assertFalse(mpaDbStorage.checkMpaExist(createdMpa.getId()));
    }

    @Test
    void getAll() {
        mpaDbStorage.create(mpa);
        mpaDbStorage.create(mpa1);
        List<Mpa> mpas = mpaDbStorage.getAll();
        assertEquals(7, mpas.size());
    }

    @Test
    void getMpaByFilmId() {
        filmDbStorage.create(film);
        Mpa foundMpa = mpaDbStorage.getMpaByFilmId(film.getId());
        Assertions.assertNotNull(foundMpa);
        Assertions.assertEquals(mpaDbStorage.getAll().get(0).getName(), foundMpa.getName());
    }

    @Test
    void addMpaToFilm() {
        filmDbStorage.create(film);
        mpaDbStorage.create(mpa);
        film.setMpa(mpa);
        mpaDbStorage.addMpaToFilm(film);
        Film foundFilm = filmDbStorage.getById(film.getId());
        Assertions.assertNotNull(foundFilm.getMpa());
        Assertions.assertEquals(mpa.getName(), foundFilm.getMpa().getName());
    }

    @Test
    void updateMpaToFilm() {
        filmDbStorage.create(film);
        mpaDbStorage.create(mpa);
        film.setMpa(mpa);
        mpaDbStorage.updateMpaToFilm(film);
        Film updatedFilm = filmDbStorage.getById(film.getId());
        Assertions.assertNotNull(updatedFilm.getMpa());
        Assertions.assertEquals(mpa.getName(), updatedFilm.getMpa().getName());
    }

    @Test
    void testGetLikesByFilmId() {
        filmDbStorage.create(film);
        userDbStorage.create(user);
        filmDbStorage.addLike(film.getId(), user.getId());
        List<Like> likes = likeDbStorage.getLikesByFilmId(film.getId());
        Assertions.assertFalse(likes.isEmpty());
        Assertions.assertEquals(1, likes.size());
        Like firstLike = likes.get(0);
        Assertions.assertEquals(film.getId(), firstLike.getFilmId());
        Assertions.assertEquals(user.getId(), firstLike.getUserId());
    }

    @Test
    void testGetLikesByUserId() {
        filmDbStorage.create(film);
        userDbStorage.create(user);
        filmDbStorage.addLike(film.getId(), user.getId());
        List<Like> likes = likeDbStorage.getLikesByUserId(film.getId());
        Assertions.assertFalse(likes.isEmpty());
        Assertions.assertEquals(1, likes.size());
        Like firstLike = likes.get(0);
        Assertions.assertEquals(user.getId(), firstLike.getUserId());
        Assertions.assertEquals(film.getId(), firstLike.getFilmId());
    }
}