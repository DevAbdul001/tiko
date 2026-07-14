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
    organizer_id BIGINT NOT NULL,
  name VARCHAR(255) NOT NULL,
    date DATETIME NOT NULL,
  category_id BIGINT,
  capacity BIGINT NOT NULL,
    location VARCHAR(255) NOT NULL,
    description LONGTEXT,
    image_url VARCHAR(255) ,
    status ENUM('DRAFT','PUBLISHED','CANCELLED','SOLD_OUT'),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE
        CURRENT_TIMESTAMP,

  FOREIGN KEY (category_id) REFERENCES event_categories(id)
    ON DELETE CASCADE,
    FOREIGN KEY (organizer_id) REFERENCES  users(id)
    ON DELETE CASCADE
);

CREATE INDEX idx_events_category
    ON events(category_id);
CREATE  INDEX idx_events_organizer
    ON  events(organizer_id);

CREATE TABLE event_ticket_prices (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    event_id BIGINT NOT NULL,
    ticket_type_id BIGINT NOT NULL,
    price BIGINT NOT NULL,
    quantity INT NOT NULL,

    FOREIGN KEY (event_id) REFERENCES  events(id),
    FOREIGN KEY (ticket_type_id) REFERENCES ticket_types(id)
);

CREATE TABLE  IF NOT EXISTS booking (
    id BIGINT AUTO_INCREMENT PRIMARY KEY ,
    event_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    booking_ref VARCHAR(255) NOT NULL,
    total_amount BIGINT NOT NULL ,
    status ENUM ('PENDING', 'CONFIRMED', 'CANCELLED', 'REFUNDED'),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (event_id) REFERENCES  events(id),
    FOREIGN KEY (user_id) REFERENCES  users(id)
);

CREATE INDEX idx_user_booking ON booking(user_id);
CREATE INDEX idx_event_booking ON booking(event_id);

CREATE TABLE  IF NOT EXISTS booking_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY ,
    booking_id BIGINT NOT NULL,
    event_ticket_price_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    unit_price BIGINT NOT NULL,
    ticket_name VARCHAR(50) NOT NULL,

    FOREIGN KEY ( booking_id ) REFERENCES booking(id),
    FOREIGN KEY (event_ticket_price_id) REFERENCES event_ticket_prices(id)
);