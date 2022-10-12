package ru.hogvarts.school_4_0.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogvarts.school_4_0.model.Avatar;
import ru.hogvarts.school_4_0.model.Student;
import ru.hogvarts.school_4_0.repository.AvatarRepository;
import ru.hogvarts.school_4_0.repository.StudentRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Optional;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
public class StudentService {

    @Value("${avatars.dir.path}")
    private String avatarsDir;

    private final StudentRepository studentRepository;
    private final AvatarRepository avatarRepository;

    public StudentService(StudentRepository studentRepository, AvatarRepository avatarRepository) {
        this.studentRepository = studentRepository;
        this.avatarRepository = avatarRepository;
    }


    public Student addStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student getStudent(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    public Student editStudent(Long id, Student student) {
        Optional<Student> optional = studentRepository.findById(id);
        if (optional.isPresent()) {
            Student fromDB = optional.get();
            fromDB.setName(fromDB.getName());
            fromDB.setAge(fromDB.getAge());
            return studentRepository.save(fromDB);
        }
        return null;
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    public Collection<Student> findStudentsByAge(int age) {
        return studentRepository.findStudentsByAge(age);
    }

    public Collection<Student> getAll() {
        return studentRepository.findAll();
    }

    public Collection<Student> findStudentsByAgeBetween(int min, int max) {
        return studentRepository.findStudentsByAgeBetween(min, max);
    }

    public Integer getAmountAllStudents() {
        return studentRepository.getAmountAllStudents();
    }

    public Double getAvgAgeStudents() {
        return studentRepository.getAvgAgeStudents();
    }

    public Collection <Student> getLastFiveStudents(){
        return studentRepository.getLastFiveStudents();
    }

    public Avatar findAvatar(long studentId) {
        return avatarRepository.findByStudentId(studentId).orElseThrow();
    }

    public void uploadAvatar(Long studentId, MultipartFile file) throws IOException {
        Student student = getStudent(studentId);
        Path filePath = Path.of(avatarsDir, studentId + "." + getExtension(file.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);
        try (InputStream is = file.getInputStream();
             OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
        ) {
            bis.transferTo(bos);
        }
        Avatar avatar = avatarRepository.findByStudentId(studentId).orElseGet(Avatar::new);
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(file.getSize());
        avatar.setMediaType(file.getContentType());
        avatar.setData(file.getBytes());

        avatarRepository.save(avatar);
    }

    public ResponseEntity <Collection<Avatar>> getAllAvatars (Integer pageNumber, Integer pageSize){
        PageRequest pageRequest = PageRequest.of(pageNumber -1, pageSize);
        Collection <Avatar> avatarList = avatarRepository.findAll(pageRequest).getContent();
        if (avatarList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(avatarList);
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.indexOf("." + 1));
    }
}
