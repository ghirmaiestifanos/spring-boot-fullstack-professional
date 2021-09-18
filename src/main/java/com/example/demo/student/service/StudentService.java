package com.example.demo.student.service;

import com.example.demo.student.repository.StudentRepository;
import com.example.demo.student.model.Student;
import com.example.demo.student.exception.BadRequestException;
import com.example.demo.student.exception.StudentNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@AllArgsConstructor
@Service
@Transactional
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;

    public StudentService() {

    }

    public List<Student> getAllStudents() {

        return studentRepository.findAll();
    }

    public Student addStudent(Student student) {
        Boolean existsEmail = studentRepository
                .selectExistsEmail(student.getEmail());
        if (existsEmail) {
            throw new BadRequestException(
                    "Email " + student.getEmail() + " taken");
        }

        return studentRepository.save(student);
    }

    public Student deleteStudent(Long studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new StudentNotFoundException("Student with id " + studentId + " does not exists");
        }
        Student student = studentRepository.findById(studentId).get();
        studentRepository.deleteById(studentId);
        return student;
    }

    public Student updateStudent(Student student) {
        if (student.getId() == null) throw new BadRequestException("studentID is required to update");
        if (!studentRepository.existsById(student.getId())) {
            throw new StudentNotFoundException(
                    "Student with id " + student.getId() + " does not exists" + " hence we cannot update a record that does not exist");
        }
        return studentRepository.save(student);

    }
}
