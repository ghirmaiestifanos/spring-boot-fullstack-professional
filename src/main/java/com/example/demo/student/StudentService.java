package com.example.demo.student;

import com.example.demo.student.exception.ApiException;
import com.example.demo.student.exception.BadRequestException;
import com.example.demo.student.exception.StudentNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@AllArgsConstructor
@Service
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

       return  studentRepository.save(student);
    }

    public Student deleteStudent(Long studentId) {

      Student student =  studentRepository.findById(studentId).orElseThrow(()-> new StudentNotFoundException("Student with id " + studentId + " does not exists"));
        studentRepository.deleteById(studentId);
        return student;
    }
    public Student updateStudent(Student student){
        if(student.getId()==null) throw  new BadRequestException("studentID is required to update");
        if(!studentRepository.existsById(student.getId())){
            throw new StudentNotFoundException(
                    "Student with id " + student.getId() + " does not exists"+" hence we cannot update a record that does not exist");
        }
    return  studentRepository.save(student);

    }
}
