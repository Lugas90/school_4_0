package ru.hogvarts.school_4_0.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hogvarts.school_4_0.model.Faculty;

import java.util.Collection;

@Repository
public interface FacultyRepository extends JpaRepository <Faculty, Long> {

    Collection<Faculty> findFacultyByColor(String color);

    Collection<Faculty> findFacultyByColorIgnoreCaseOrNameIgnoreCase(String color, String name);
}
