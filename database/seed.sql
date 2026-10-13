-- ============================================================================
-- Smart City Management System - sample seed data
-- ----------------------------------------------------------------------------
-- Apply AFTER schema.sql:
--
--     mysql -u root -p smart_city < database/seed.sql
--
-- All passwords below are BCrypt hashes (cost = 12). The plaintext passwords
-- are listed here ONLY for local development convenience — DO NOT store
-- plaintext passwords in any production deployment.
--
--   aarav@example.com   / password1
--   priya@example.com   / password2
--   rahul@example.com   / password3
--   anjali@example.com   / password4
--   vikram@example.com   / password5
--
-- Hashes were generated with the project's PasswordHasher (cost 12). If you
-- rotate any password, regenerate the hash with:
--
--     java -cp smart-city-core/target/classes:bcrypt.jar:bytes.jar \
--          com.smartcity.security.PasswordHasherDemo <plaintext>
-- ============================================================================

INSERT INTO citizens (name, email, phone, password_hash, address) VALUES
('Aarav Sharma',  'aarav@example.com',  '9876543210', '$2a$12$dreDgdQ4qGav9SkM03yiZOV50Wfw7khfzK9N3dOxiLEt2KqrG5Ava', '12 MG Road, Bangalore'),
('Priya Patel',   'priya@example.com',  '9123456780', '$2a$12$5C1WtG6ZCY7BwSWC7pOS3.LF8ixbqfwsuokLvNzE50CVMhEGAoYv.', '45 Park Street, Kolkata'),
('Rahul Verma',   'rahul@example.com',  '9988776655', '$2a$12$wiYDv1cbPSOehbTZLFD9XeFCU0vd7p55udYtWLsq4OOYe.FEsjG5W', '78 Marine Drive, Mumbai'),
('Anjali Iyer',   'anjali@example.com', '9001122334', '$2a$12$od9y/0402TgOP9Sc46sNRu/Rr8iuy6YkfJVW9JZguQB6HbymVRVMC', '23 Anna Salai, Chennai'),
('Vikram Singh',  'vikram@example.com', '9445566778', '$2a$12$roDUkKziG3AJ8X6VnPgt9u2XeO3PJ7rCfwjoWJsA3P7OI32xeHZyi', '56 Connaught Place, Delhi');

INSERT INTO complaints (citizen_id, subject, description, status) VALUES
(1, 'Street light not working',  'Pole #45 near MG Road has been dark for 3 nights.',                     'Pending'),
(2, 'Garbage collection missed', 'Tuesday pickup was missed on Park Street.',                              'In Progress'),
(3, 'Pothole on Marine Drive',   'Large pothole near junction with risk of accidents.',                  'Pending'),
(1, 'Water leakage',             'Continuous leakage from main pipe near house.',                         'Resolved'),
(4, 'Street dog menace',         'Stray dog pack near Anna Salai is unsafe for children.',                'Pending');

INSERT INTO feedback (citizen_id, rating, comment) VALUES
(1, 5, 'Excellent response time on my complaint. Thank you!'),
(2, 4, 'Garbage collection issue resolved within 2 days.'),
(3, 3, 'Pothole still pending after 5 days. Please expedite.'),
(4, 5, 'Very happy with the new city service portal.');

INSERT INTO services (service_name, description, department, status) VALUES
('Water Supply',       '24x7 municipal water supply for registered households', 'Water Board',     'Active'),
('Garbage Collection', 'Daily door-to-door collection, 6 AM to 9 AM',            'Sanitation Dept', 'Active'),
('Street Lighting',    'Maintenance and installation of street lights',          'Public Works',    'Active'),
('Public Health',      'Free health check-up camps every weekend',               'Health Dept',     'Active'),
('Road Maintenance',   'Pothole repair and resurfacing requests',                'Public Works',    'Maintenance'),
('Sanitation',         'Public toilet cleaning and maintenance',                 'Sanitation Dept', 'Active');
