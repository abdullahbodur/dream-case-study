ALTER TABLE user_progress
    ADD COLUMN country enum ('TURKEY', 'UNITED_STATES', 'UNITED_KINGDOM', 'GERMANY', 'FRANCE') NOT NULL;

UPDATE user_progress
SET country = CASE country_id
                  WHEN 1 THEN 'TURKEY'
                  WHEN 2 THEN 'UNITED_STATES'
                  WHEN 3 THEN 'UNITED_KINGDOM'
                  WHEN 4 THEN 'GERMANY'
                  WHEN 5 THEN 'FRANCE'
    END;

ALTER TABLE user_progress
    DROP FOREIGN KEY fk_country;

ALTER TABLE user_progress
    DROP COLUMN country_id;

DROP TABLE IF EXISTS country;