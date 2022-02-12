-- ********************************************************************************
-- This script creates the database users and grants them the necessary permissions
-- ********************************************************************************

CREATE USER tech_fitness_blue_db_owner
WITH PASSWORD 'finalcapstone';

GRANT ALL
ON ALL TABLES IN SCHEMA public
TO tech_fitness_blue_db_owner;

GRANT ALL
ON ALL SEQUENCES IN SCHEMA public
TO tech_fitness_blue_db_owner;

CREATE USER tech_fitness_blue_db_app_user
WITH PASSWORD 'finalcapstone';

GRANT SELECT, INSERT, UPDATE, DELETE
ON ALL TABLES IN SCHEMA public
TO tech_fitness_blue_db_app_user;

GRANT USAGE, SELECT
ON ALL SEQUENCES IN SCHEMA public
TO tech_fitness_blue_db_app_user;
