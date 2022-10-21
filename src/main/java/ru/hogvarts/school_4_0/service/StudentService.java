package ru.hogvarts.school_4_0.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
public class StudentService {

    @Value("${avatars.dir.path}")
    private String avatarsDir;

    private final Logger logger = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository studentRepository;
    private final AvatarRepository avatarRepository;

    public StudentService(StudentRepository studentRepository, AvatarRepository avatarRepository) {
        this.studentRepository = studentRepository;
        this.avatarRepository = avatarRepository;
    }


    public Student addStudent(Student student) {
        logger.info("method called addStudent");
        return studentRepository.save(student);
    }

    public Student getStudent(Long id) {
        logger.info("method called getStudent by id #" + id);
        return studentRepository.findById(id).orElse(null);
    }

    public Student editStudent(Long id, Student student) {
        logger.info("method called editStudent by id #" + id);
        Optional<Student> optional = studentRepository.findById(id);
        if (optional.isPresent()) {
            Student fromDB = optional.get();
            fromDB.setName(fromDB.getName());
            fromDB.setAge(fromDB.getAge());
            return studentRepository.save(fromDB);
        }
        logger.error("there is not student by id #" + id);
        return null;
    }

    public void deleteStudent(Long id) {
        logger.info("method called deleteStudent by id #" + id);
        studentRepository.deleteById(id);
    }

    public Collection<Student> findStudentsByAge(int age) {
        logger.info("method called findStudentsByAge by age= " + age);
        return studentRepository.findStudentsByAge(age);
    }

    public Collection<Student> getAll() {
        logger.info("method called getAll students");
        return studentRepository.findAll();
    }

    public Collection<Student> findStudentsByAgeBetween(int min, int max) {
        logger.info("method called findStudentsByAgeBetween");
        return studentRepository.findStudentsByAgeBetween(min, max);
    }

    public Integer getAmountAllStudents() {
        logger.info("method called getAmountAllStudents");
        return studentRepository.getAmountAllStudents();
    }

    public Double getAvgAgeStudents() {
        logger.info("method called getAvgAgeStudents");
        return studentRepository.getAvgAgeStudents();
    }

    public Collection<Student> getLastFiveStudents() {
        logger.info("method called getLastFiveStudents");
        return studentRepository.getLastFiveStudents();
    }

    public Avatar findAvatar(long id) {
        logger.info("method called findAvatar by id #" + id);
        return avatarRepository.findByStudentId(id).orElseThrow();
    }

    public void uploadAvatar(Long id, MultipartFile file) throws IOException {
        logger.info("method called uploadAvatar by id #" + id);
        Student student = getStudent(id);
        Path filePath = Path.of(avatarsDir, id + "." + getExtension(file.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);
        try (InputStream is = file.getInputStream();
             OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
        ) {
            bis.transferTo(bos);
        }
        Avatar avatar = avatarRepository.findByStudentId(id).orElseGet(Avatar::new);
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(file.getSize());
        avatar.setMediaType(file.getContentType());
        avatar.setData(file.getBytes());

        avatarRepository.save(avatar);
    }

    public ResponseEntity<Collection<Avatar>> getAllAvatars(Integer pageNumber, Integer pageSize) {
        logger.info("method called getAllAvatars");
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize);
        Collection<Avatar> avatarList = avatarRepository.findAll(pageRequest).getContent();
        if (avatarList.isEmpty()) {
            logger.warn("avatars not found");
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(avatarList);
    }

    private String getExtension(String fileName) {
        logger.info("method called getExtension");
        return fileName.substring(fileName.indexOf("." + 1));
    }

    public List<String> getStudentWithLetterM() {
        List<String> st =
                studentRepository.findAll().stream()
                        .map(student -> student.getName())
                        .filter(s -> s.startsWith("M"))
                        .sorted((s1, s2) -> s1.compareTo(s2))
                        .map(s -> s.toUpperCase())
                        .collect(Collectors.toList());
        return st;
    }

    public Double allAvgAge() {
        return studentRepository.findAll().stream()
                .mapToInt(student -> student.getAge())
                .average()
                .orElse(0);
    }

    public void getAllForThreads() {
        logger.info("Start method getAllForThreads");
        getStudent(1L);
        getStudent(2L);

        Thread thread1 = new Thread(() -> {
            getStudent(3L);
            getStudent(4L);
        });
        thread1.start();

        Thread thread2 = new Thread(() -> {
            getStudent(5L);
            getStudent(6L);
        });
        thread2.start();
    }

    private synchronized void getAllForThreadsSynchronized(List<Student> students) {
        for (Student student : students) {
            logger.info(student.getName());
        }
    }

    public void getAllForThreadsSynchronized() {
        logger.info("Start method getAllForThreadsSynchronized");

        List <Student> students = studentRepository.findAll(PageRequest.of(0,6)).getContent();

        getAllForThreadsSynchronized(students.subList(0, 2));
        new Thread(() -> getAllForThreadsSynchronized(students.subList(2, 4)));
        new Thread(() -> getAllForThreadsSynchronized(students.subList(4, 6)));

    }
}
