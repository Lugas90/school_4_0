package ru.hogvarts.school_4_0.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogvarts.school_4_0.model.Avatar;
import ru.hogvarts.school_4_0.model.Student;
import ru.hogvarts.school_4_0.service.StudentService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;

@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public Student addStudent(@RequestBody Student student) {
        return studentService.addStudent(student);
    }

    @GetMapping("{id}")
    public ResponseEntity<Student> getStudent(@PathVariable Long id) {
        Student student = studentService.getStudent(id);
        if (student == null) {
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @PutMapping("{id}")
    public ResponseEntity<Student> editStudent(@RequestBody Student student, @PathVariable Long id) {
        Student editorialStudent = studentService.editStudent(id, student);
        if (editorialStudent == null) {
            ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(editorialStudent);
    }

    @DeleteMapping("{id}")
    public ResponseEntity <Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/all")
    public ResponseEntity<Collection<Student>> getAll() {
        return ResponseEntity.ok(studentService.getAll());
    }

    @GetMapping("/amountAllStudents")
    public ResponseEntity <Integer> amountAllStudents(){
        return ResponseEntity.ok(studentService.getAmountAllStudents());
    }

    @GetMapping ("/avgAgeStudents")
    public ResponseEntity <Double> avgAgeStudents(){
        return ResponseEntity.ok(studentService.getAvgAgeStudents());
    }

    @GetMapping ("/fiveLastStudents")
    public ResponseEntity <Collection<Student>> fiveLastStudents (){
        return ResponseEntity.ok(studentService.getLastFiveStudents());
    }

    @GetMapping
    public ResponseEntity<Collection<Student>> findStudentsByAge(@RequestParam(required = false) int age) {
        if (age > 0) {
            return ResponseEntity.ok(studentService.findStudentsByAge(age));
        }
        return ResponseEntity.ok(Collections.emptyList());
    }

    @GetMapping("/ageMinMax")
    public ResponseEntity<Collection<Student>> findStudentsByAgeBetween(@RequestParam(required = false) int min,
                                                                        @RequestParam(required = false) int max) {
        if (min > 0) {
            return ResponseEntity.ok(studentService.findStudentsByAgeBetween(min, max));
        }
        return ResponseEntity.ok(Collections.emptyList());
    }

    @PostMapping(value = "{id}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAvatar(@PathVariable Long id, @RequestParam MultipartFile avatar) throws IOException {
        if (avatar.getSize() > 1024 * 300) {
            return ResponseEntity.badRequest().body("File is to big");
        }
        studentService.uploadAvatar(id, avatar);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "{id}/avatar/preview")
    public ResponseEntity<byte[]> downloadAvatar(@PathVariable Long id) {
        Avatar avatar = studentService.findAvatar(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
        headers.setContentLength(avatar.getData().length);

        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(avatar.getData());
    }

    @GetMapping(value = "/{id}/avatar")
    public void downloadAvatar(@PathVariable Long id, HttpServletResponse response) throws IOException {
        Avatar avatar = studentService.findAvatar(id);

        Path path = Path.of(avatar.getFilePath());

        try (InputStream is = Files.newInputStream(path);
             OutputStream os = response.getOutputStream()) {

            response.setStatus(200);
            response.setContentType(avatar.getMediaType());
            response.setContentLength((int) avatar.getFileSize());
            is.transferTo(os);
        }
    }

    @GetMapping(value = "/allAvatars")
    public ResponseEntity <Collection<Avatar>> getAllAvatars(@RequestParam ("page") Integer pageNumber,
                                                             @RequestParam ("size") Integer pageSize){
        return studentService.getAllAvatars(pageNumber, pageSize);


    }
}
