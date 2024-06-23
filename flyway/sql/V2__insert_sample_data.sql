-- Insert pet owners
INSERT INTO PET_OWNER (id, government_id, first_name, last_name, email, phone_number) VALUES
(NEXTVAL('OWNER_SEQUENCE'), '123456789', 'John', 'Doe', 'john.doe@example.com', '123-456-7890'),
(NEXTVAL('OWNER_SEQUENCE'), '987654321', 'Jane', 'Smith', 'jane.smith@example.com', '987-654-3210'),
(NEXTVAL('OWNER_SEQUENCE'), '456789123', 'Bob', 'Johnson', 'bob.johnson@example.com', '456-789-1230');

-- Insert pets
INSERT INTO PET (id, owner_id, name, weight, breed, training_level, is_vaccinated, species) VALUES
(NEXTVAL('PET_SEQUENCE'), 1, 'Rex', 30.0, 'Labrador', 3, true, 'DOG'),
(NEXTVAL('PET_SEQUENCE'), 2, 'Bella', 25.0, 'Golden Retriever', 2, true, 'DOG'),
(NEXTVAL('PET_SEQUENCE'), 2, 'Max', 20.0, 'Bulldog', 1, false, 'DOG'),
(NEXTVAL('PET_SEQUENCE'), 3, 'Charlie', 15.0, 'Beagle', 3, true, 'DOG'),
(NEXTVAL('PET_SEQUENCE'), 3, 'Molly', 10.0, 'Persian', 2, true, 'CAT');