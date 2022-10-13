SELECT students.name, students.age, faculty.name FROM students
INNER JOIN faculty ON students.faculty_id = faculty_id;

SELECT students.name, students.age FROM students
INNER JOIN avatar a ON students.id = a.student_id;