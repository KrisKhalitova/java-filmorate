MERGE INTO rating_mpa (rating_id, name)
    VALUES (1, 'G');
MERGE INTO ratings (id, name)
    VALUES (2, 'PG');
MERGE INTO ratings (id, name)
    VALUES (3, 'PG-13');
MERGE INTO ratings (id, name)
    VALUES (4, 'R');
MERGE INTO ratings (id, name)
    VALUES (5, 'NC-17');

MERGE INTO genres (genres_id, name)
    VALUES (1, 'Комедия');
MERGE INTO genres (id, name)
        VALUES (6, 'Боевик');
MERGE INTO genres (id, name)
   VALUES (2, 'Драма');
MERGE INTO genres (id, name)
    VALUES (3, 'Мультфильм');
MERGE INTO genres (id, name)
    VALUES (4, 'Триллер');
MERGE INTO genres (id, name)
    VALUES (5, 'Документальный');