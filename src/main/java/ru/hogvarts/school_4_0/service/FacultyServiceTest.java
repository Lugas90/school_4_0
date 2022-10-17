package ru.hogvarts.school_4_0.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import ru.hogvarts.school_4_0.model.Faculty;
import ru.hogvarts.school_4_0.repository.FacultyRepository;

import java.math.BigDecimal;
import java.util.Collection;

@Service
@Profile("test")
public class FacultyServiceTest implements FacultyServiceInterface{

    private final FacultyRepository facultyRepository;

    public FacultyServiceTest(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    @Override
    public Faculty addFaculty(Faculty faculty) {
        return null;
    }

    @Override
    public Faculty getFaculty(Long id) {
        return new Faculty(555L, BigDecimal.ONE.toEngineeringString(), BigDecimal.TEN.toEngineeringString());
    }

    @Override
    public Faculty editFaculty(Long id, Faculty faculty) {
        return null;
    }

    @Override
    public void deleteFaculty(Long id) {

    }

    @Override
    public Collection<Faculty> getFacultyToColor(String color) {
        return null;
    }

    @Override
    public Collection<Faculty> getAll() {
        return null;
    }

    @Override
    public Collection<Faculty> findFacultyByColorIgnoreCaseOrNameIgnoreCase(String color, String name) {
        return null;
    }
}
