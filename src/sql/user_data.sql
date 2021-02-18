CREATE TABLE IF NOT EXISTS user_data(
	user_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	role_id BIGINT NOT NULL,
    FOREIGN KEY(role_id) REFERENCES user_role(role_id),
    person_id BIGINT NOT NULL,
    FOREIGN KEY (person_id) REFERENCES person(person_id),

    username VARCHAR(20) NOT NULL,
    p_word VARCHAR(20) NOT NULL UNIQUE,
    active_status TINYINT(1),

    last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    last_update_by VARCHAR(25)
    );