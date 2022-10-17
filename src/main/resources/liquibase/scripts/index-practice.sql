-- liquibase formatted sql

-- changeset elugovoy:1
CREATE INDEX students_name_index ON students (name);
CREATE INDEX faculty_name_color_index ON faculty (name, color);