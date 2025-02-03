-- Таблица пользователей (User)
INSERT INTO users (account_verified, date_of_birth, email_confirmed, login_disabled, id, email, first_name, last_name,
                   password, phone_number, role)
VALUES (TRUE, '1990-01-01', TRUE, FALSE, 1, 'admin@gmail.com', 'Admin', 'Admin', '$2a$12$bhEvguyAwhAySYNdIB8piuPkvyTeyFZZTctNT0sgZJYqrWvruwpnq' , '1234567890', 'DELIVERY'), --admin123!
       (TRUE, '1985-05-10', TRUE, FALSE, 2, 'petr@gmail.com', 'Петр', '', '$2a$12$hjywwr/Y1uXCkm0cjrm0Ber3UVGxOzTKKsajqsogjSjrcpSCB7qJi', '0987654321', 'SENDER'), --password456
       (FALSE, '1995-07-15', FALSE, TRUE, 3, 'anna@gmail.com', 'Nur', 'Emilisov', '$2a$12$KE0RusEcXszOadcqUHCs9eD8cE23yUH6ypI3Ns1G.XNoaXxMyKjQy', '1122334455', --password789
        'DELIVERY'),
       (TRUE, '1988-12-30', TRUE, FALSE, 4, 'maria@gmail.com', 'Marina', 'Egorov', '$2a$12$m1t467FdqBt3i97f4bsVjuzUMlgpzpX2gxt.eA1jm9DnUbq/UuGKG', '6677889900', --password101
        'SENDER'),
       (TRUE, '1992-03-20', TRUE, FALSE, 5, 'dmitry@gmail.com', 'Dima', 'Kuza', '$2a$12$yKNnjh49cOBQBcQcwstqmuUwGt0/8yNY/pUvZF41EjxQprWzHlA2a', '2233445566', --password202
        'DELIVERY');

-- Таблица платежей (Payment)
INSERT INTO payments (cvc_code, id, user_id, card_number, expiry_date_of_card, status)
VALUES (123, 1, 1, '1234 5678 9876 5432', '2026-05-01', 'SUCCESS'),
       (456, 2, 2, '2345 6789 8765 4321', '2024-11-01', 'PENDING'),
       (789, 3, 1, '3456 7890 7654 3210', '2025-03-01', 'FAILED'),
       (101, 4, 3, '4567 8901 6543 2109', '2027-08-01', 'SUCCESS');

-- Таблица подписок (Subscription)
INSERT INTO subscriptions (end_date, price, start_date, id, payment_id, user_id, duration, transport_type)
VALUES ('2025-02-28', 99.0, '2025-01-01', 1, 1, 1, 'ONE_MONTH', 'CAR'),
       ('2025-04-30', 999.0, '2025-02-01', 2, 2, 2, 'THREE_MONTH', 'TRUCK'),
       ('2025-07-01', 1499.0, '2025-01-01', 3, 3, 1, 'SIX_MONTH', 'AIRPLANE'),
       ('2026-01-01', 1999.0, '2025-07-01', 4, 4, 2, 'TWELVE_MONTH', 'CAR');

-- Таблица доставок (Delivery)
INSERT INTO deliveries (arrival_date, dispatch_date, id, user_id, description, from_where, package_type, size, to_where,
                        transport_number, transport_type, truck_size)
VALUES ('2025-02-10', '2025-02-05', 1, 1, 'Delivery of fragile items', 'Bishkek', 'LUGGAGE', 'M', 'Naryn',
        '01KG312QWE', 'TRUCK', 'MEDIUM'),
       ('2025-02-15', '2025-02-10', 2, 2, 'Important documents', 'Bishkek', 'ENVELOPE', 'S', 'Cholpon-Ata',
        '09KG009KOL',
        'CAR', 'SMALL'),
       ('2025-02-20', '2025-02-15', 3, 3, 'Electronics', 'Bishkek', 'BOX', 'L', 'Tamchy', '01KG111ONE', 'AIRPLANE',
        'LARGE'),
       ('2025-02-25', '2025-02-20', 4, 1, 'Furniture', 'Talas', 'BOX', 'M', 'Balykchy', '04KG222KGZ', 'TRUCK', 'TRUCK');

-- Таблица отправлений (Sending)
INSERT INTO sendings (arrival_date, dispatch_date, id, user_id, description, from_where, package_type, size, to_where)
VALUES ('2025-02-10', '2025-02-05', 1, 1, 'Sending of legal documents', 'Bishkek', 'ENVELOPE', 'S', 'Alma-Ata'),
       ('2025-02-15', '2025-02-10', 2, 2, 'Sending of office supplies', 'Bishkek', 'BOX', 'M', 'Ozgon'),
       ('2025-02-20', '2025-02-15', 3, 3, 'Sending of gifts', 'Bishkek', 'LUGGAGE', 'L', 'Osh'),
       ('2025-02-25', '2025-02-20', 4, 1, 'Sending of electronics', 'Bishkek', 'BOX', 'M', 'Kant');

INSERT INTO users_subscriptions (subscriptions_id, user_id)
VALUES (1, 1),
       (3, 3),
       (2, 2),
       (4, 4),
       (2, 5);