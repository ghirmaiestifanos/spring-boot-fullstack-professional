package com.example.demo.student.controller;

import com.example.demo.student.service.StudentService;
import com.example.demo.student.model.Student;
import com.example.demo.student.exception.ApiException;
import com.example.demo.student.exception.BadRequestException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/students")
@AllArgsConstructor
public class StudentController {
    @Autowired
    Validator validator;
    @Autowired
    private final StudentService studentService;

    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @PostMapping

    public Student addStudent(@Valid @RequestBody Student student, BindingResult result) throws ApiException {
        if (result.hasErrors()) {
            String errorMessage = "";
            List<ObjectError> errors = result.getAllErrors();
            for (ObjectError e : errors) {
                errorMessage += e + "\n";
            }
            throw new BadRequestException(errorMessage);
        }

            Student student1 =studentService.addStudent(student);
            if ( student1 == null) throw new ApiException("problem with our Api");


        return student1;

    }

    @DeleteMapping(path = "/{studentId}")
    public Student deleteStudent(
            @PathVariable("studentId") Long studentId) {
        return studentService.deleteStudent(studentId);
    }

    @PutMapping
    public Student updateStudent(@Valid @RequestBody Student student, BindingResult result) {
        if (result.hasErrors()) {
            String errorMessage = "";
            List<ObjectError> errors = result.getAllErrors();
            for (ObjectError e : errors) {
                errorMessage += e + "\n";
            }
            throw new BadRequestException(errorMessage);
        }
        return studentService.updateStudent(student);
    }


}
