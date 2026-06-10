INSERT IGNORE INTO authgroup (name, description, default_admin, version)
VALUES ('Administrators', 'appNG Administrators group', true, CURRENT_TIMESTAMP);

INSERT IGNORE INTO subject (name, realname, email, language, type, locked, login_attempts, pw_change_policy, digest, salt, version)
VALUES ('admin', 'Administrator', 'admin@example.com', 'en', 'LOCAL_USER', false, 0, 0,
        '$2a$13$4xgCtFeITzrWdT7FSUwVmuDDZ.Z03kD79GcfU1oayPo9crlsm3EPq', null, CURRENT_TIMESTAMP);

INSERT IGNORE INTO subject_authgroup (subject_id, group_id)
SELECT s.id, g.id FROM subject s JOIN authgroup g ON g.name = 'Administrators' WHERE s.name = 'admin';

INSERT IGNORE INTO site (name, host, domain, active, create_repository, reload_count, version)
VALUES ('localhost', 'localhost', 'http://localhost:8080', true, false, 0, CURRENT_TIMESTAMP);
