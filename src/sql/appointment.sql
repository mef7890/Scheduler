    CREATE TABLE IF NOT EXISTS appointment(

	appointment_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    tutor_id BIGINT NOT NULL,
    FOREIGN KEY(tutor_id) REFERENCES person(person_id),
    student_id BIGINT NOT NULL,
    FOREIGN KEY(student_id) REFERENCES person(person_id),

    apt_start DATETIME NOT NULL,
    apt_end DATETIME NOT NULL,

    subject_focus VARCHAR(45) NOT NULL,
    scheduling_note VARCHAR (500),
    summary VARCHAR (500),

    last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    last_update_by VARCHAR(25)

    );