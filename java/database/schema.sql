BEGIN TRANSACTION;
DROP TABLE IF EXISTS users, meal, food_item, meal_item, user_profile cascade;
DROP SEQUENCE IF EXISTS seq_user_id;
CREATE SEQUENCE seq_user_id
  INCREMENT BY 1
  NO MAXVALUE
  NO MINVALUE
  CACHE 1;
CREATE TABLE users (
	user_id int DEFAULT nextval('seq_user_id'::regclass) NOT NULL,
	username varchar(50) NOT NULL,
	password_hash varchar(200) NOT NULL,
	role varchar(50) NOT NULL,
	CONSTRAINT PK_user PRIMARY KEY (user_id)

);
CREATE TABLE user_profile (
        user_id int NOT NULL,
        first_name varchar(50) NOT NULL,
        last_name varchar(50) NOT NULL,
        age int,
        birthday date,
        height int,
        -- WHAT FIELDS ARE REQUIRED
        current_weight int NOT NULL,
        desired_weight int NOT NULL,
        display_name varchar(50) NOT NULL,
        file_data varchar,
        -- How to store image?
        calorie_goal int NOT NULL,
        meals_per_day int NOT NULL,
        star_count int NOT NULL,
        CONSTRAINT PK_user_profile PRIMARY KEY (user_id),
        CONSTRAINT FK_user FOREIGN KEY (user_id) REFERENCES users (user_ID)
);
CREATE TABLE meal (
        meal_id serial,
        user_id int NOT NULL,
        meal_name varchar(64) NOT NULL,
        meal_date date NOT NULL,
        saved boolean NOT NULL,
        CONSTRAINT PK_meal PRIMARY KEY (meal_id),
        CONSTRAINT FK_meal FOREIGN KEY (user_id) REFERENCES users (user_id)
);
CREATE TABLE food_item (
        food_id int,
        food_type varchar(64) NOT NULL,
        calories decimal NOT NULL,
        carbohydrates decimal NOT NULL,
        fat decimal NOT NULL,
        protein decimal NOT NULL,
        cholesterol decimal NOT NULL,
        sodium decimal NOT NULL,
        fiber decimal NOT NULL,
        sugar decimal NOT NULL,
        CONSTRAINT PK_food_item PRIMARY KEY (food_id)
);
CREATE TABLE meal_item (
        meal_id int NOT NULL,
        food_id int NOT NULL,
        CONSTRAINT PK_meal_items PRIMARY KEY (meal_id, food_id),
        CONSTRAINT FK_meal_items_meal FOREIGN KEY (meal_id) REFERENCES meal (meal_id),
        CONSTRAINT FK_meal_items_food FOREIGN KEY (food_id) REFERENCES food_item (food_id)
);
INSERT INTO users (username,password_hash,role) VALUES ('user','$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC','ROLE_USER');
INSERT INTO users (username,password_hash,role) VALUES ('admin','$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC','ROLE_ADMIN');
COMMIT TRANSACTION;









