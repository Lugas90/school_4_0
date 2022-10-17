package ru.hogvarts.school_4_0.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import ru.hogvarts.school_4_0.model.Faculty;
import ru.hogvarts.school_4_0.repository.FacultyRepository;

import java.util.Collection;
import java.util.Optional;

@Service
@Profile("production")
public class FacultyService implements FacultyServiceInterface {
    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    Logger logger = LoggerFactory.getLogger(FacultyService.class);

    public Faculty addFaculty(Faculty faculty) {
        logger.info("method called addFaculty");
        return facultyRepository.save(faculty);
    }

    public Faculty getFaculty(Long id) {
        logger.info("method called getFaculty by id #" + id);
        return facultyRepository.findById(id).orElse(null);
    }

    public Faculty editFaculty(Long id, Faculty faculty) {
        logger.info("method called editFaculty by id #" + id);
        Optional<Faculty> optional = facultyRepository.findById(id);
        if (optional.isPresent()) {
            Faculty fromDB = optional.get();
            fromDB.setName(fromDB.getName());
            fromDB.setColor(fromDB.getColor());
            return facultyRepository.save(fromDB);
        }
        logger.error("there is not faculty by id #" + id);
        return null;
    }

    public void deleteFaculty(Long id) {
        logger.info("method called deleteFaculty by id #" + id);
        facultyRepository.deleteById(id);
    }

    public Collection<Faculty> getFacultyToColor(String color) {
        logger.info("method called getFacultyToColor by color " + color);
        return facultyRepository.findFacultyByColor(color);
    }

    public Collection<Faculty> getAll() {
        logger.info("method called getAll faculties");
        return facultyRepository.findAll();
    }

    public Collection<Faculty> findFacultyByColorIgnoreCaseOrNameIgnoreCase(String color, String name) {
        logger.info("method called findFacultyByColorIgnoreCaseOrNameIgnoreCase by color " + color + "and name " + name);
        return facultyRepository.findFacultyByColorIgnoreCaseOrNameIgnoreCase(color, name);
    }
}
