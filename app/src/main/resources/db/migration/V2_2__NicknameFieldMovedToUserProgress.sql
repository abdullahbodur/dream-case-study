-- Add a new column to the user_progress table
ALTER TABLE user_progress
    ADD COLUMN nickname VARCHAR(255);
-- Copy the nickname values from the user table to the user_progress table
UPDATE user_progress
SET nickname = (SELECT nickname FROM user WHERE user.id = user_progress.id);
-- Remove the nickname column from the user table
ALTER TABLE user
    DROP COLUMN nickname;
-- add unique and not null constraints to the nickname column
ALTER TABLE user_progress
    MODIFY COLUMN nickname VARCHAR(255) NOT NULL UNIQUE;