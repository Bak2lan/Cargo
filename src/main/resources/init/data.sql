INSERT INTO users (account_verified, date_of_birth, email_confirmed, login_disabled, id, email, first_name, last_name,
                   password, phone_number, role, user_image)
VALUES (TRUE, '1990-01-01', TRUE, FALSE, 1, 'admin@mail.com', 'Admin', 'Admin',
        '$2y$10$vgBCuSJaZhJQ1KbkpFjQVeBaEkNH8aTKYGMrNoaq3gxgoPUa2t0DG', '1234567890', 'USER', 'admin.jpg'),

       (TRUE, '1985-05-10', TRUE, FALSE, 2, 'petr@mail.com', 'Petr', 'Asanov',
        '$2y$10$iWIODU1Jno4o2RVKGhuj.elrrV/iE/CAnTCiGPSRA7B4a8Q32.i8O', '0987654321', 'SENDER', 'petr.jpg'),

       (FALSE, '1995-07-15', FALSE, TRUE, 3, 'anna@mail.com', 'Nur', 'Emilisov',
        '$2y$10$gIfl2LM3u3aRZRwPNq7JY.VP6Twf3wRK4eGKXX5xMG19JL5bzpPsG', '1122334455', 'DELIVERY', 'nur.jpg'),

       (TRUE, '1988-12-30', TRUE, FALSE, 4, 'maria@mail.com', 'Marina', 'Egorov',
        '$2y$10$Sd1YybIh8QlaoS2bDkZb.eUqSWF1elBBmFrg44BB8ceUzw39sjDNu', '6677889900', 'USER', 'marina.jpg'),

       (TRUE, '1992-03-20', TRUE, FALSE, 5, 'dmitry@mail.com', 'Dima', 'Kuza',
        '$2y$10$Z8beRchDWb2netKRtwxXDOS3IUj8GRBCFWVFYl2CAqIXfH/wuuNw2', '2233445566', 'DELIVERY', 'dima.jpg'),

       (TRUE, '1993-04-15', TRUE, FALSE, 6, 'elena@mail.com', 'Elena', 'Vasilieva',
        '$2y$10$jRt1B9mJOfkrMJzNvsARauUyr4AxAHdoQncxyvVEbhmBiGZ80EYbe', '3456789012', 'SENDER', 'elena.jpg'),

       (FALSE, '1990-06-10', TRUE, TRUE, 7, 'max@mail.com', 'Max', 'Turov',
        '$2y$10$gYlV4PDhnkKMX5wTvNFNIe7caJS78EuKP2neb1JSzXIA54WcZE6OW', '6789012345', 'DELIVERY', 'max.jpg'),

       (TRUE, '1982-08-25', TRUE, FALSE, 8, 'igor@mail.com', 'Igor', 'Semenov',
        '$2y$10$Nq8gevlWon4vh5f7vOAXxOoSbfGL7wsSfsx1olYKPqOEhiIESIUPy', '7890123456', 'SENDER', 'igor.jpg'),

       (TRUE, '1994-02-12', FALSE, TRUE, 9, 'tatyana@mail.com', 'Tatyana', 'Pavlova',
        '$2y$10$eOQCwBe6U1CWimeHFKulL.GnULKabqjBEhWkeEoOhwHBCKcqVQYmK', '8901234567', 'DELIVERY', 'tatyana.jpg'),

       (FALSE, '1996-11-20', TRUE, FALSE, 10, 'sergey@mail.com', 'Sergey', 'Golovin',
        '$2y$10$Z3qloMxJceVLLBt5MUBSb.EeXNdS1FbOmepzkWXBwNN7wt/1i67wa', '9012345678', 'SENDER', 'sergey.jpg');

INSERT INTO payments (cvc_code, id, user_id, card_number, expiry_date_of_card, status)
VALUES (123, 1, 1, '1234 5678 9876 5432', '2026-05-01', 'SUCCESS'),
       (456, 2, 2, '2345 6789 8765 4321', '2024-11-01', 'PENDING'),
       (789, 3, 1, '3456 7890 7654 3210', '2025-03-01', 'FAILED'),
       (101, 4, 3, '4567 8901 6543 2109', '2027-08-01', 'SUCCESS'),
       (112, 5, 2, '5678 9012 5432 1098', '2025-09-01', 'PENDING'),
       (213, 6, 3, '6789 0123 4321 9876', '2026-01-01', 'SUCCESS'),
       (314, 7, 4, '7890 1234 3210 8765', '2027-04-01', 'FAILED'),
       (415, 8, 5, '8901 2345 2109 7654', '2025-12-01', 'SUCCESS'),
       (516, 9, 6, '9012 3456 1098 6543', '2026-06-01', 'PENDING'),
       (617, 10, 7, '0123 4567 9876 5432', '2027-03-01', 'SUCCESS');


INSERT INTO subscriptions (end_date, price, start_date, id, payment_id, user_id, duration, transport_type)
VALUES ('2025-02-28', 99.0, '2025-01-01', 1, 1, 1, 'ONE_MONTH', 'CAR'),
       ('2025-04-30', 999.0, '2025-02-01', 2, 2, 2, 'THREE_MONTH', 'TRUCK'),
       ('2025-07-01', 1499.0, '2025-01-01', 3, 3, 1, 'SIX_MONTH', 'AIRPLANE'),
       ('2026-01-01', 1999.0, '2025-07-01', 4, 4, 2, 'TWELVE_MONTH', 'CAR'),
       ('2025-11-01', 1099.0, '2025-02-01', 5, 5, 3, 'ONE_MONTH', 'CAR'),
       ('2025-09-30', 1200.0, '2025-03-01', 6, 6, 4, 'THREE_MONTH', 'TRUCK'),
       ('2026-05-01', 1599.0, '2025-04-01', 7, 7, 5, 'SIX_MONTH', 'CAR'),
       ('2026-09-01', 1299.0, '2025-08-01', 8, 8, 6, 'TWELVE_MONTH', 'AIRPLANE'),
       ('2025-08-01', 1100.0, '2025-06-01', 9, 9, 7, 'ONE_MONTH', 'TRUCK'),
       ('2025-12-01', 1350.0, '2025-05-01', 10, 10, 8, 'THREE_MONTH', 'CAR');


INSERT INTO deliveries (arrival_date, dispatch_date, id, user_id, description, from_where, package_type, size, to_where,
                        transport_number, transport_type, truck_size, status,random)
VALUES ('2025-02-10', '2025-02-05', 1, 1, 'Delivery of fragile items', 'Bishkek', 'LUGGAGE', 'M', 'Naryn', '01KG312QWE', 'TRUCK', 'MEDIUM', 'ACTIVE',111),
       ('2025-02-15', '2025-02-10', 2, 2, 'Important documents', 'Bishkek', 'ENVELOPE', 'S', 'Cholpon-Ata', '09KG009KOL', 'CAR', 'SMALL','ARCHIVED',112),
       ('2025-02-20', '2025-02-15', 3, 3, 'Electronics', 'Bishkek', 'BOX', 'L', 'Tamchy', '01KG111ONE', 'AIRPLANE', 'LARGE','ARCHIVED',113),
       ('2025-02-25', '2025-02-20', 4, 1, 'Furniture', 'Talas', 'BOX', 'M', 'Balykchy', '04KG222KGZ', 'TRUCK', 'TRUCK', 'ACTIVE',114),
       ('2025-03-10', '2025-03-05', 5, 2, 'Books', 'Bishkek', 'LUGGAGE', 'M', 'Jalal-Abad', '05KG314TZG', 'CAR', 'SMALL','ARCHIVED',114),
       ('2025-03-15', '2025-03-10', 6, 3, 'Appliances', 'Bishkek', 'ENVELOPE', 'L', 'Karakol', '06KG415HTB', 'TRUCK', 'MEDIUM','ARCHIVED',116),
       ('2025-03-20', '2025-03-15', 7, 4, 'Clothing', 'Bishkek', 'ENVELOPE', 'S', 'Osh', '07KG517IUO', 'AIRPLANE', 'SMALL','ARCHIVED',117),
       ('2025-03-25', '2025-03-20', 8, 5, 'Furniture', 'Bishkek', 'LUGGAGE', 'L', 'Issyk-Kul', '08KG618VGB', 'TRUCK', 'LARGE', 'ACTIVE',118),
       ('2025-04-10', '2025-04-05', 9, 6, 'Toys', 'Bishkek', 'LUGGAGE', 'M', 'Kant', '09KG719GNC', 'CAR', 'MEDIUM', 'ACTIVE',119),
       ('2025-04-15', '2025-04-10', 10, 7, 'Sports Equipment', 'Bishkek', 'LUGGAGE', 'L', 'Naryn', '10KG820TGO', 'TRUCK', 'TRUCK', 'ACTIVE',120);


INSERT INTO sendings (arrival_date, dispatch_date, id, user_id, description, from_where, package_type, size, to_where, status)
VALUES ('2025-02-10', '2025-02-05', 1, 1, 'Sending of legal documents', 'Bishkek', 'ENVELOPE', 'S', 'Alma-Ata','ACTIVE'),
       ('2025-02-15', '2025-02-10', 2, 2, 'Sending of office supplies', 'Bishkek', 'BOX', 'M', 'Ozgon','ACTIVE'),
       ('2025-02-20', '2025-02-15', 3, 3, 'Sending of gifts', 'Bishkek', 'LUGGAGE', 'L', 'Osh', 'ACTIVE'),
       ('2025-02-25', '2025-02-20', 4, 1, 'Sending of electronics', 'Bishkek', 'BOX', 'M', 'Kant', 'ACTIVE'),
       ('2025-03-10', '2025-03-05', 5, 2, 'Sending of documents', 'Bishkek', 'ENVELOPE', 'S', 'Naryn', 'ACTIVE'),
       ('2025-03-15', '2025-03-10', 6, 3, 'Sending of supplies', 'Bishkek', 'BOX', 'M', 'Jalal-Abad', 'ACTIVE'),
       ('2025-03-20', '2025-03-15', 7, 4, 'Sending of gifts', 'Bishkek', 'LUGGAGE', 'L', 'Talas', 'ARCHIVED'),
       ('2025-03-25', '2025-03-20', 8, 5, 'Sending of furniture', 'Bishkek', 'BOX', 'M', 'Balykchy', 'ARCHIVED'),
       ('2025-04-10', '2025-04-05', 9, 6, 'Sending of clothes', 'Bishkek', 'BOX', 'S', 'Issyk-Kul', 'ARCHIVED'),
       ('2025-04-15', '2025-04-10', 10, 7, 'Sending of toys', 'Bishkek', 'BOX', 'M', 'Karakol', 'ARCHIVED');
