CREATE TABLE IF NOT EXISTS  users (
      id BIGINT AUTO_INCREMENT PRIMARY KEY ,
      name VARCHAR(255) NOT NULL,
      email VARCHAR(255) NOT NULL UNIQUE,
      password_hash VARCHAR(255) NOT NULL,
      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS  event_categories(
    id BIGINT AUTO_INCREMENT PRIMARY KEY ,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO event_categories (name) VALUES
    ('Concert'),
    ('Festival'),
    ('Conference'),
    ('Workshop'),
    ('Seminar'),
    ('Meetup'),
    ('Hackathon'),
    ('Webinar'),
    ('Networking'),
    ('Sports'),
    ('Exhibition'),
    ('Theatre'),
    ('Comedy Show'),
    ('Movie Screening'),
    ('Charity Event'),
    ('Fundraiser'),
    ('Cultural Event'),
    ('Religious Event'),
    ('Educational'),
    ('Community Event');

CREATE  TABLE IF NOT EXISTS ticket_types(
    id BIGINT AUTO_INCREMENT PRIMARY KEY ,
    name varchar(255)
);

INSERT INTO ticket_types (name) VALUES
    ('VVIP'),
    ('VIP'),
    ('REGULAR');

CREATE TABLE IF NOT EXISTS events (
      id BIGINT AUTO_INCREMENT PRIMARY KEY,
      name VARCHAR(255) NOT NULL,
      category_id BIGINT,
      capacity BIGINT NOT NULL,

      INDEX idx_events_category (category_id),

      FOREIGN KEY (category_id) REFERENCES event_categories(id)
);