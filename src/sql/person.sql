CREATE TABLE IF NOT EXISTS person(
	person_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    role_id BIGINT NOT NULL DEFAULT 4,
    FOREIGN KEY(role_id) REFERENCES user_role(role_id),
    #user_id BIGINT NOT NULL,
    #FOREIGN KEY(user_id) REFERENCES user_data(user_id),
	f_name VARCHAR(25) NOT NULL,
    l_name VARCHAR(25) NOT NULL,
    phone VARCHAR (12) NOT NULL,
    email VARCHAR (50) NOT NULL,

    address_id BIGINT NOT NULL,
    FOREIGN KEY(address_id) REFERENCES address(address_id),

    start_date DATE NOT NULL,
    birthday DATE NOT NULL,

    last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    last_update_by VARCHAR(25)

    );