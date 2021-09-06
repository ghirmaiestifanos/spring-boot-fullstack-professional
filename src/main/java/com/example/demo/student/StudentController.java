package com.example.demo.student;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/students")
@AllArgsConstructor
public class StudentController {
    @Autowired
    private final StudentService studentService;

    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @PostMapping

    public Student addStudent(@Valid @RequestBody Student student) {
     return   studentService.addStudent(student);

    }

    @DeleteMapping(path = "{studentId}")
    public Student deleteStudent(
            @PathVariable("studentId") Long studentId) {
      return  studentService.deleteStudent(studentId);
    }

    @PutMapping
    public Student updateStudent(@Valid @RequestBody Student student) {
      return  studentService.updateStudent(student);
    }


}
