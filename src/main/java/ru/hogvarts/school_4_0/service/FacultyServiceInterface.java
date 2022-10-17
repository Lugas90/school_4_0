package ru.hogvarts.school_4_0.service;

import ru.hogvarts.school_4_0.model.Faculty;

import java.util.Collection;

public interface FacultyServiceInterface {

    public Faculty addFaculty(Faculty faculty);
    public Faculty getFaculty(Long id);
    public Faculty editFaculty(Long id, Faculty faculty);
    public void deleteFaculty(Long id);
    public Collection<Faculty> getFacultyToColor(String color);
    public Collection<Faculty> getAll();
    public Collection<Faculty> findFacultyByColorIgnoreCaseOrNameIgnoreCase(String color, String name);


}
