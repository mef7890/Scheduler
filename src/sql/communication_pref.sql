    CREATE TABLE IF NOT EXISTS communication_pref(
	preference_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	person_id BIGINT NOT NULL,
    FOREIGN KEY(person_id) REFERENCES person(person_id),

    receive_alert TINYINT(1) NOT NULL DEFAULT 0,
    receive_summary TINYINT(1) NOT NULL DEFAULT 0,

    last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    last_update_by VARCHAR(25)
    );