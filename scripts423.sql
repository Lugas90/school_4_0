SELECT students.name, students.age, faculty.name FROM students
LEFT JOIN faculty ON students.faculty_id = faculty.id;

SELECT students.name, students.age FROM students
    INNER JOIN avatar a ON students.id = a.student_id;