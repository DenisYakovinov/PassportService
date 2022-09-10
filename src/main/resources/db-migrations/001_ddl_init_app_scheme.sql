CREATE TABLE IF NOT EXISTS passport
(
    id           BIGSERIAL PRIMARY KEY,
    full_name    TEXT NOT NULL,
    series       INT NOT NULL,
    number       INT NOT NULL,
    issue_date   DATE,
    birth_date   DATE,
    expired      DATE
);
ALTER TABLE passport
    ADD CONSTRAINT unique_series_number UNIQUE (series, number);

