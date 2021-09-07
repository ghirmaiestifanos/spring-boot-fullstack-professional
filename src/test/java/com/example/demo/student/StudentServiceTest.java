package com.example.demo.student;

import com.example.demo.student.exception.BadRequestException;
import com.example.demo.student.exception.StudentNotFoundException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)

class StudentServiceTest {
   @Mock
    private StudentRepository studentRepository;

    private StudentService underTest;
//    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
//        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new StudentService(studentRepository);

    }

//    @AfterEach
//    void tearDown() throws Exception {
//        studentRepository.deleteAll();
//    }

    @Test
    void canGetAllStudents() {
        // when
        underTest.getAllStudents();
        //then
        verify(studentRepository).findAll();
    }

    @Test

    void canAddStudent() {
        //given
        String email = "jamila@gmail.com";
        Student student = new Student("Jamila",
                email,
                Gender.FEMALE);
        //when
        underTest.addStudent(student);


        //then
        ArgumentCaptor<Student> studentArgumentCaptor =
                ArgumentCaptor.forClass(Student.class);

        verify(studentRepository).save(studentArgumentCaptor.capture());
        Student capturedStudent = studentArgumentCaptor.getValue();
        assertThat(capturedStudent).isEqualTo(student);

    }
    @Test

    void willThrowWhenEmailIsTaken() {
        //given
        String email = "jamila@gmail.com";
        Student student = new Student("Jamila",
                email,
                Gender.FEMALE);
       given(studentRepository.selectExistsEmail(student.getEmail())).willReturn(true);
        //when
        //then
     assertThatThrownBy(()->underTest.addStudent(student))
             .isInstanceOf(BadRequestException.class)
             .hasMessage( "Email " + student.getEmail() + " taken");

     verify(studentRepository, never()).save(any());



    }
    @Test
    void willThrowIdDoesNotExist(){
        //given
       Long studentId =14l;
       given(studentRepository.existsById(studentId)).willReturn(false);
       //when
        //then
        assertThatThrownBy(()->underTest.deleteStudent(studentId))
                .isInstanceOf(StudentNotFoundException.class)
                .hasMessage("Student with id " + studentId + " does not exists");
        verify(studentRepository,never()).deleteById(any());


    }

    @Test

    void canDeleteStudent() {
        //given
        Long studentId =14l;
      Student student = new Student(14l,"Tesfay","tesfay14@gmail.com",Gender.MALE);
      Mockito.when(studentRepository.existsById(studentId)).thenReturn(true);
        given(studentRepository.findById(14l)).willReturn(Optional.of(student));
        //when
   Student student1=     underTest.deleteStudent(studentId);
     ArgumentCaptor<Long> argumentCaptor=   ArgumentCaptor.forClass(Long.class);
     verify(studentRepository).deleteById(argumentCaptor.capture());
     Long capturedVal = argumentCaptor.getValue();
     assertThat(studentId).isEqualTo(capturedVal);
     assertThat(student).isEqualTo(student1);
    }
}