-- **************************************************************
-- This script destroys the database and associated users
-- **************************************************************

-- The following line terminates any active connections to the database so that it can be destroyed
SELECT pg_terminate_backend(pid)
FROM pg_stat_activity
WHERE datname = 'tech_fitness_blue_db';

DROP DATABASE tech_fitness_blue_db;

DROP USER tech_fitness_blue_db_owner;
DROP USER tech_fitness_blue_db_app_user;
