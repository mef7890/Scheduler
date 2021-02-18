  CREATE TABLE IF NOT EXISTS platform_info(
	platform_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	person_id BIGINT NOT NULL,
    FOREIGN KEY(person_id) REFERENCES person(person_id),

    platform VARCHAR(20),
    handle VARCHAR(20),

    last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    last_update_by VARCHAR(25)
    );