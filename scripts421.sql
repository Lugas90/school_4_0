ALTER TABLE students ADD CONSTRAINT age_constraint CHECK ( age > 16 );

ALTER TABLE students ALTER name SET NOT NULL;

ALTER TABLE students ADD CONSTRAINT name_unique UNIQUE (name);

ALTER TABLE faculty ADD CONSTRAINT color_name_unique UNIQUE (name, color);

ALTER TABLE students ALTER age SET DEFAULT 20;