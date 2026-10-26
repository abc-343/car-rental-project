-- Seed data, loaded on every startup (H2 is in-memory so it resets every restart)
-- ids are not given so H2 auto-generates them (1, 2, 3, ...)

-- Cars
INSERT INTO cars (make, model, car_year, daily_rate, status) VALUES ('Toyota', 'Corolla', 2024, 59.99, 'AVAILABLE');
INSERT INTO cars (make, model, car_year, daily_rate, status) VALUES ('Honda', 'Civic', 2023, 57.50, 'RENTED');
INSERT INTO cars (make, model, car_year, daily_rate, status) VALUES ('Ford', 'Escape', 2022, 74.99, 'AVAILABLE');
INSERT INTO cars (make, model, car_year, daily_rate, status) VALUES ('Tesla', 'Model 3', 2024, 119.99, 'RENTED');
INSERT INTO cars (make, model, car_year, daily_rate, status) VALUES ('Hyundai', 'Elantra', 2023, 52.00, 'AVAILABLE');
INSERT INTO cars (make, model, car_year, daily_rate, status) VALUES ('Mazda', 'CX-5', 2024, 79.99, 'AVAILABLE');
INSERT INTO cars (make, model, car_year, daily_rate, status) VALUES ('Chevrolet', 'Malibu', 2021, 55.00, 'MAINTENANCE');
INSERT INTO cars (make, model, car_year, daily_rate, status) VALUES ('Kia', 'Sportage', 2023, 72.50, 'AVAILABLE');
INSERT INTO cars (make, model, car_year, daily_rate, status) VALUES ('Nissan', 'Rogue', 2022, 69.99, 'RENTED');
INSERT INTO cars (make, model, car_year, daily_rate, status) VALUES ('Volkswagen', 'Jetta', 2023, 58.75, 'AVAILABLE');
INSERT INTO cars (make, model, car_year, daily_rate, status) VALUES ('BMW', 'X3', 2024, 139.99, 'AVAILABLE');
INSERT INTO cars (make, model, car_year, daily_rate, status) VALUES ('Subaru', 'Outback', 2022, 84.00, 'AVAILABLE');

-- Customers
INSERT INTO customers (first_name, last_name, email, phone) VALUES ('John', 'Smith', 'john.smith@email.com', '514-555-0101');
INSERT INTO customers (first_name, last_name, email, phone) VALUES ('Emma', 'Tremblay', 'emma.tremblay@email.com', '514-555-0102');
INSERT INTO customers (first_name, last_name, email, phone) VALUES ('Liam', 'Gagnon', 'liam.gagnon@email.com', '438-555-0103');
INSERT INTO customers (first_name, last_name, email, phone) VALUES ('Olivia', 'Roy', 'olivia.roy@email.com', '514-555-0104');
INSERT INTO customers (first_name, last_name, email, phone) VALUES ('Noah', 'Cote', 'noah.cote@email.com', '450-555-0105');
INSERT INTO customers (first_name, last_name, email, phone) VALUES ('Sophia', 'Bouchard', 'sophia.bouchard@email.com', '514-555-0106');
INSERT INTO customers (first_name, last_name, email, phone) VALUES ('William', 'Gauthier', 'william.gauthier@email.com', '438-555-0107');
INSERT INTO customers (first_name, last_name, email, phone) VALUES ('Ava', 'Morin', 'ava.morin@email.com', '514-555-0108');
INSERT INTO customers (first_name, last_name, email, phone) VALUES ('Lucas', 'Lavoie', 'lucas.lavoie@email.com', '450-555-0109');
INSERT INTO customers (first_name, last_name, email, phone) VALUES ('Mia', 'Fortin', 'mia.fortin@email.com', '514-555-0110');
INSERT INTO customers (first_name, last_name, email, phone) VALUES ('Ethan', 'Gagne', 'ethan.gagne@email.com', '438-555-0111');
INSERT INTO customers (first_name, last_name, email, phone) VALUES ('Chloe', 'Ouellet', 'chloe.ouellet@email.com', '514-555-0112');

-- Rentals (total_cost = number of days * car daily_rate)
INSERT INTO rentals (customer_id, car_id, start_date, end_date, total_cost, status) VALUES (1, 1, '2026-09-01', '2026-09-05', 239.96, 'COMPLETED');
INSERT INTO rentals (customer_id, car_id, start_date, end_date, total_cost, status) VALUES (1, 3, '2026-10-10', '2026-10-12', 149.98, 'COMPLETED');
INSERT INTO rentals (customer_id, car_id, start_date, end_date, total_cost, status) VALUES (2, 2, '2026-10-20', '2026-10-27', 402.50, 'ACTIVE');
INSERT INTO rentals (customer_id, car_id, start_date, end_date, total_cost, status) VALUES (3, 4, '2026-10-22', '2026-10-29', 839.93, 'ACTIVE');
INSERT INTO rentals (customer_id, car_id, start_date, end_date, total_cost, status) VALUES (4, 5, '2026-08-15', '2026-08-18', 156.00, 'COMPLETED');
INSERT INTO rentals (customer_id, car_id, start_date, end_date, total_cost, status) VALUES (5, 6, '2026-11-01', '2026-11-04', 239.97, 'RESERVED');
INSERT INTO rentals (customer_id, car_id, start_date, end_date, total_cost, status) VALUES (6, 9, '2026-10-23', '2026-10-30', 489.93, 'ACTIVE');
INSERT INTO rentals (customer_id, car_id, start_date, end_date, total_cost, status) VALUES (7, 8, '2026-07-01', '2026-07-08', 507.50, 'COMPLETED');
INSERT INTO rentals (customer_id, car_id, start_date, end_date, total_cost, status) VALUES (8, 10, '2026-11-10', '2026-11-15', 293.75, 'RESERVED');
INSERT INTO rentals (customer_id, car_id, start_date, end_date, total_cost, status) VALUES (9, 11, '2026-09-12', '2026-09-14', 279.98, 'CANCELLED');
INSERT INTO rentals (customer_id, car_id, start_date, end_date, total_cost, status) VALUES (10, 12, '2026-12-20', '2026-12-27', 588.00, 'RESERVED');
INSERT INTO rentals (customer_id, car_id, start_date, end_date, total_cost, status) VALUES (2, 1, '2026-06-05', '2026-06-06', 59.99, 'COMPLETED');
