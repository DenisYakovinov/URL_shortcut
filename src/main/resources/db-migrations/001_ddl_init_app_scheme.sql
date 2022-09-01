CREATE TABLE IF NOT EXISTS site
(
    id           BIGSERIAL PRIMARY KEY,
    name         TEXT,
    login        TEXT UNIQUE,
    password     TEXT UNIQUE,
    registration BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS urls
(
    id BIGSERIAL PRIMARY KEY,
    url TEXT UNIQUE,
    code TEXT UNIQUE,
    site_id BIGINT NOT NULL,
    visit_count BIGINT DEFAULT 0
);

ALTER TABLE urls ADD CONSTRAINT fk_url_site FOREIGN KEY (site_id) REFERENCES site(id)
    ON DELETE CASCADE ON UPDATE CASCADE;

