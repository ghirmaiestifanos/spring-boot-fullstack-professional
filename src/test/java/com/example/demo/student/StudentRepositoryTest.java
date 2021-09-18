package com.example.demo.student;

import com.example.demo.student.model.Gender;
import com.example.demo.student.model.Student;
import com.example.demo.student.repository.StudentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class StudentRepositoryTest {
    @Autowired
private StudentRepository underTest;

    @AfterEach
    void tearDown(){
        underTest.deleteAll();
    }
    @Test
    void itShouldCheckIfStudentEmailExists() {
        //given
        String email = "jamila@gmail.com";
        Student student = new Student("Jamila",
                email,
                Gender.FEMALE);

        underTest.save(student);
        //when
      boolean expected= underTest.selectExistsEmail(email);

        //then
       assertThat(expected).isTrue();
    }
    @Test
    void itShouldCheckIfStudentEmailDoesnotExist(){
        //given
        String email = "jamila@gmail.com";

        //when
    boolean expected = underTest.selectExistsEmail(email);
        //then
        assertThat(expected).isFalse();

    }
}