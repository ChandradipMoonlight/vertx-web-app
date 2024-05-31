CREATE TABLE customer (
	id INT AUTO_INCREMENT PRIMARY KEY,
	first_name VARCHAR(255),
	last_name VARCHAR(255),
	age INT,
	created_at TIMESTAMP,
    updated_at TIMESTAMP,
    deleted BOOLEAN
);

CREATE TABLE address (
    id INT AUTO_INCREMENT PRIMARY KEY,
    city VARCHAR(255),
    state VARCHAR(255),
    pin_code INT,
    customer_id INT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    deleted BOOLEAN,
    FOREIGN KEY (customer_id) REFERENCES customer(id)
);

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    f_name VARCHAR(255),
    m_name VARCHAR(255),
    l_name VARCHAR(255),
    age INT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    deleted BOOLEAN
);

ALTER TABLE users
ADD password VARCHAR(255);

ALTER TABLE users
ADD email VARCHAR(255);

ALTER TABLE users
ADD mobile_no VARCHAR(255);

ALTER TABLE employee
ADD gender VARCHAR(255)


