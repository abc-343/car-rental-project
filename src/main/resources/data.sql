-- Seed data, loaded on every startup (H2 is in-memory so it resets every restart)
-- internal ids are not given so H2 auto-generates them (1, 2, 3, ...)
-- each row also has a public UUID used by the API (car_id / customer_id / rental_id).
-- The seeded UUIDs follow a pattern so they are easy to type: cars ca..., customers cc..., rentals ee...
-- rentals.customer_id / rentals.car_id below are the internal foreign keys (1-12), not the UUIDs

-- Cars
INSERT INTO cars (car_id, make, model, car_year, daily_rate, status) VALUES ('ca000000-0000-4000-a000-000000000001', 'Toyota', 'Corolla', 2024, 59.99, 'AVAILABLE');
INSERT INTO cars (car_id, make, model, car_year, daily_rate, status) VALUES ('ca000000-0000-4000-a000-000000000002', 'Honda', 'Civic', 2023, 57.50, 'RENTED');
INSERT INTO cars (car_id, make, model, car_year, daily_rate, status) VALUES ('ca000000-0000-4000-a000-000000000003', 'Ford', 'Escape', 2022, 74.99, 'AVAILABLE');
INSERT INTO cars (car_id, make, model, car_year, daily_rate, status) VALUES ('ca000000-0000-4000-a000-000000000004', 'Tesla', 'Model 3', 2024, 119.99, 'RENTED');
INSERT INTO cars (car_id, make, model, car_year, daily_rate, status) VALUES ('ca000000-0000-4000-a000-000000000005', 'Hyundai', 'Elantra', 2023, 52.00, 'AVAILABLE');
INSERT INTO cars (car_id, make, model, car_year, daily_rate, status) VALUES ('ca000000-0000-4000-a000-000000000006', 'Mazda', 'CX-5', 2024, 79.99, 'AVAILABLE');
INSERT INTO cars (car_id, make, model, car_year, daily_rate, status) VALUES ('ca000000-0000-4000-a000-000000000007', 'Chevrolet', 'Malibu', 2021, 55.00, 'MAINTENANCE');
INSERT INTO cars (car_id, make, model, car_year, daily_rate, status) VALUES ('ca000000-0000-4000-a000-000000000008', 'Kia', 'Sportage', 2023, 72.50, 'AVAILABLE');
INSERT INTO cars (car_id, make, model, car_year, daily_rate, status) VALUES ('ca000000-0000-4000-a000-000000000009', 'Nissan', 'Rogue', 2022, 69.99, 'RENTED');
INSERT INTO cars (car_id, make, model, car_year, daily_rate, status) VALUES ('ca000000-0000-4000-a000-000000000010', 'Volkswagen', 'Jetta', 2023, 58.75, 'AVAILABLE');
INSERT INTO cars (car_id, make, model, car_year, daily_rate, status) VALUES ('ca000000-0000-4000-a000-000000000011', 'BMW', 'X3', 2024, 139.99, 'AVAILABLE');
INSERT INTO cars (car_id, make, model, car_year, daily_rate, status) VALUES ('ca000000-0000-4000-a000-000000000012', 'Subaru', 'Outback', 2022, 84.00, 'AVAILABLE');

-- Customers
INSERT INTO customers (customer_id, first_name, last_name, email, phone) VALUES ('cc000000-0000-4000-a000-000000000001', 'John', 'Smith', 'john.smith@email.com', '514-555-0101');
INSERT INTO customers (customer_id, first_name, last_name, email, phone) VALUES ('cc000000-0000-4000-a000-000000000002', 'Emma', 'Tremblay', 'emma.tremblay@email.com', '514-555-0102');
INSERT INTO customers (customer_id, first_name, last_name, email, phone) VALUES ('cc000000-0000-4000-a000-000000000003', 'Liam', 'Gagnon', 'liam.gagnon@email.com', '438-555-0103');
INSERT INTO customers (customer_id, first_name, last_name, email, phone) VALUES ('cc000000-0000-4000-a000-000000000004', 'Olivia', 'Roy', 'olivia.roy@email.com', '514-555-0104');
INSERT INTO customers (customer_id, first_name, last_name, email, phone) VALUES ('cc000000-0000-4000-a000-000000000005', 'Noah', 'Cote', 'noah.cote@email.com', '450-555-0105');
INSERT INTO customers (customer_id, first_name, last_name, email, phone) VALUES ('cc000000-0000-4000-a000-000000000006', 'Sophia', 'Bouchard', 'sophia.bouchard@email.com', '514-555-0106');
INSERT INTO customers (customer_id, first_name, last_name, email, phone) VALUES ('cc000000-0000-4000-a000-000000000007', 'William', 'Gauthier', 'william.gauthier@email.com', '438-555-0107');
INSERT INTO customers (customer_id, first_name, last_name, email, phone) VALUES ('cc000000-0000-4000-a000-000000000008', 'Ava', 'Morin', 'ava.morin@email.com', '514-555-0108');
INSERT INTO customers (customer_id, first_name, last_name, email, phone) VALUES ('cc000000-0000-4000-a000-000000000009', 'Lucas', 'Lavoie', 'lucas.lavoie@email.com', '450-555-0109');
INSERT INTO customers (customer_id, first_name, last_name, email, phone) VALUES ('cc000000-0000-4000-a000-000000000010', 'Mia', 'Fortin', 'mia.fortin@email.com', '514-555-0110');
INSERT INTO customers (customer_id, first_name, last_name, email, phone) VALUES ('cc000000-0000-4000-a000-000000000011', 'Ethan', 'Gagne', 'ethan.gagne@email.com', '438-555-0111');
INSERT INTO customers (customer_id, first_name, last_name, email, phone) VALUES ('cc000000-0000-4000-a000-000000000012', 'Chloe', 'Ouellet', 'chloe.ouellet@email.com', '514-555-0112');

-- Rentals (total_cost = number of days * car daily_rate)
INSERT INTO rentals (rental_id, customer_id, car_id, start_date, end_date, total_cost, status) VALUES ('ee000000-0000-4000-a000-000000000001', 1, 1, '2026-09-01', '2026-09-05', 239.96, 'COMPLETED');
INSERT INTO rentals (rental_id, customer_id, car_id, start_date, end_date, total_cost, status) VALUES ('ee000000-0000-4000-a000-000000000002', 1, 3, '2026-10-10', '2026-10-12', 149.98, 'COMPLETED');
INSERT INTO rentals (rental_id, customer_id, car_id, start_date, end_date, total_cost, status) VALUES ('ee000000-0000-4000-a000-000000000003', 2, 2, '2026-10-20', '2026-10-27', 402.50, 'ACTIVE');
INSERT INTO rentals (rental_id, customer_id, car_id, start_date, end_date, total_cost, status) VALUES ('ee000000-0000-4000-a000-000000000004', 3, 4, '2026-10-22', '2026-10-29', 839.93, 'ACTIVE');
INSERT INTO rentals (rental_id, customer_id, car_id, start_date, end_date, total_cost, status) VALUES ('ee000000-0000-4000-a000-000000000005', 4, 5, '2026-08-15', '2026-08-18', 156.00, 'COMPLETED');
INSERT INTO rentals (rental_id, customer_id, car_id, start_date, end_date, total_cost, status) VALUES ('ee000000-0000-4000-a000-000000000006', 5, 6, '2026-11-01', '2026-11-04', 239.97, 'RESERVED');
INSERT INTO rentals (rental_id, customer_id, car_id, start_date, end_date, total_cost, status) VALUES ('ee000000-0000-4000-a000-000000000007', 6, 9, '2026-10-23', '2026-10-30', 489.93, 'ACTIVE');
INSERT INTO rentals (rental_id, customer_id, car_id, start_date, end_date, total_cost, status) VALUES ('ee000000-0000-4000-a000-000000000008', 7, 8, '2026-07-01', '2026-07-08', 507.50, 'COMPLETED');
INSERT INTO rentals (rental_id, customer_id, car_id, start_date, end_date, total_cost, status) VALUES ('ee000000-0000-4000-a000-000000000009', 8, 10, '2026-11-10', '2026-11-15', 293.75, 'RESERVED');
INSERT INTO rentals (rental_id, customer_id, car_id, start_date, end_date, total_cost, status) VALUES ('ee000000-0000-4000-a000-000000000010', 9, 11, '2026-09-12', '2026-09-14', 279.98, 'CANCELLED');
INSERT INTO rentals (rental_id, customer_id, car_id, start_date, end_date, total_cost, status) VALUES ('ee000000-0000-4000-a000-000000000011', 10, 12, '2026-12-20', '2026-12-27', 588.00, 'RESERVED');
INSERT INTO rentals (rental_id, customer_id, car_id, start_date, end_date, total_cost, status) VALUES ('ee000000-0000-4000-a000-000000000012', 2, 1, '2026-06-05', '2026-06-06', 59.99, 'COMPLETED');
