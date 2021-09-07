package com.example.demo.student;

import com.example.demo.student.exception.ApiException;
import com.example.demo.student.exception.BadRequestException;
import com.example.demo.student.exception.StudentNotFoundException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.validation.BindingResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.BDDAssumptions.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(StudentController.class)
public class StudentControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean

    private StudentService studentService;
    @MockBean

    private StudentRepository studentRepository;
    @Autowired
    StudentController studentController;


    @Test
    public void getAllStudentsTest() throws Exception {
        List<Student> list = new ArrayList<>();
        list.add(new Student("TekleM", "teklem@gmail.com", Gender.MALE));
        list.add(new Student("Tareke", "tarekem@gmail.com", Gender.MALE));
        list.add(new Student("Haregu", "haregum@gmail.com", Gender.FEMALE));
        Mockito.when(studentService.getAllStudents()).thenReturn(list);
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/students").accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk()).andDo(print()).andReturn();

        String actualJsonResponse = mvcResult.getResponse().getContentAsString();
        System.out.println(actualJsonResponse);
        String expectedJsonResponse = objectMapper.writeValueAsString(list);
        assertThat(actualJsonResponse).isEqualToIgnoringWhitespace(expectedJsonResponse);

    }

    @Test
    public void deleteStudentById() throws Exception {


        Student studentDeleted = new Student(1L, "TekleM", "teklem@gmail.com", Gender.MALE);

        Mockito.when(studentService.deleteStudent(1L)).thenReturn(studentDeleted);


        MvcResult mvcResult = mockMvc.perform(delete("/api/v1/students/{studentId}", 1L)).andExpect(status().isOk()).andDo(print()).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String actualJsonResponse = mvcResult.getResponse().getContentAsString();
        String expectedJsonResponse = objectMapper.writeValueAsString(studentDeleted);
        assertThat(actualJsonResponse).isEqualTo(expectedJsonResponse);


        Mockito.verify(studentService, Mockito.times(1)).deleteStudent(1L);

    }


    @Test
    public void addStudent() throws Exception {
        Student newStudent = new Student("Tekola", "Tekola@gmail.com", Gender.MALE);
        Student savedStudent = new Student(1L, "Tekola", "Tekola@gmail.com", Gender.MALE);
        Mockito.when(studentService.addStudent(newStudent)).thenReturn(savedStudent);

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/students")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(newStudent))


        ).andExpect(status().isOk())

                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        String actualJsonResponse = mvcResult.getResponse().getContentAsString();
        System.out.println("actual "+actualJsonResponse);
        String expectedJsonResponse = objectMapper.writeValueAsString(savedStudent);
        System.out.println("expected "+expectedJsonResponse);


        assertThat(actualJsonResponse).isEqualToIgnoringWhitespace(expectedJsonResponse);

        assertEquals(200, status);

    }

    @Test
    public void studentNameMustNotBeBlank() throws Exception {
        Student student = new Student("", "Tekola@gmail.com", Gender.MALE);
        String url = "/api/v1/students";
        MvcResult mvcResult = mockMvc.perform(
                post(url).contentType("application/json")
                        .content(objectMapper.writeValueAsString(student))

        ).andExpect(status().isBadRequest())
                .andDo(print()).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(400, status);

        Mockito.verify(studentService, Mockito.times(0)).addStudent(student);
    }

    @Test
    public void emailMustBeValid() throws Exception {
        Student student = new Student("Tekola", null, Gender.MALE);
        String url = "/api/v1/students";
        MvcResult mvcResult = mockMvc.perform(
                post(url).contentType("application/json")
                        .content(objectMapper.writeValueAsString(student))

        ).andExpect(status().isBadRequest())
                .andDo(print()).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(400, status);

        Mockito.verify(studentService, Mockito.times(0)).addStudent(student);
    }

    @Test
    public void genderMustBeProvided() throws Exception {
        Student student = new Student("Tekola", "tekola@gmail.com", null);
        String url = "/api/v1/students";
        MvcResult mvcResult = mockMvc.perform(
                post(url).contentType("application/json")
                        .content(objectMapper.writeValueAsString(student))

        ).andExpect(status().isBadRequest())
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(400, status);

        Mockito.verify(studentService, Mockito.times(0)).addStudent(student);
    }

    @Test
    public void updateStudent() throws Exception {

        String url = "/api/v1/students";


        Student s1 = new Student(null, "Hansu", "hansu@gmail.com", Gender.FEMALE);

        Mockito.when(studentService.updateStudent(s1)).thenThrow(new BadRequestException("studentID is required to update"));
        MvcResult mvcResult = mockMvc.perform(put(url).contentType("application/json").content(objectMapper.writeValueAsString(s1))).andDo(print()).andExpect(status().isBadRequest()).andReturn();


        int status = mvcResult.getResponse().getStatus();
        assertEquals(400, status);
        Mockito.verify(studentRepository, Mockito.times(0)).save(s1);
        assertThatThrownBy(() -> studentService.updateStudent(s1)).isInstanceOf(BadRequestException.class);

        Student s2 = new Student(5L, "Hansu", "hansu@gmail.com", Gender.FEMALE);
        Mockito.when(studentService.updateStudent(s2)).thenThrow(new StudentNotFoundException("Student with id " + s2.getId() + " does not exists" + " hence we cannot update a record that does not exist"));
//       Mockito.when(studentRepository.existsById(s2.getId())).thenThrow(new StudentNotFoundException("Student with id " + s2.getId() + " does not exists" + " hence we cannot update a record that does not exist"));
        MvcResult mvcResult1 = mockMvc.perform(put(url).contentType("application/json").content(objectMapper.writeValueAsString(s2))).andDo(print()).andExpect(status().isNotFound()).andReturn();
        int status1 = mvcResult1.getResponse().getStatus();
        assertEquals(404, status1);
//        Mockito.verify(studentRepository, Mockito.times(1)).existsById(s2.getId());
        Mockito.verify(studentRepository, Mockito.times(0)).save(s2);

        Student student = new Student(1L, "Hansu", "hansu@gmail.com", Gender.FEMALE);
        Mockito.when(studentService.updateStudent(student)).thenReturn(student);
        MvcResult mvcResult2 = mockMvc.perform(put(url)
                .contentType("application/json").
                        content(objectMapper.writeValueAsString(student)))
                .andDo(print()).andExpect(status().isOk()).andReturn();

        int status2 = mvcResult2.getResponse().getStatus();
        String jsonActualResponse = mvcResult2.getResponse().getContentAsString();
        String jsonExpectedResponde = objectMapper.writeValueAsString(student);
        assertThat(jsonExpectedResponde).isEqualTo(jsonActualResponse);
        assertEquals(200, status2);
        Mockito.verify(studentService, Mockito.times(1)).updateStudent(student);


    }

    @Test
    public void validatePayloadName() throws Exception {
        String url = "/api/v1/students";

        Student student = new Student("", "Hansu", Gender.FEMALE);
        MvcResult mvcResult = mockMvc.perform(put(url).contentType("application/json")
                .content(objectMapper.writeValueAsString(student)))
                .andDo(print()).andExpect(status().isBadRequest()).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(400, status);
        Mockito.verify(studentService, Mockito.times(0)).updateStudent(student);
    }

    @Test
    public void willThrowApiException() throws Exception {
        String url = "/api/v1/students";
        Student student = new Student("Hansu", "hansu@gmail.com", Gender.FEMALE);

        BDDMockito.given(studentService.addStudent(student)).willReturn(null);
        MvcResult mvcResult = mockMvc.perform(post(url).contentType("application/json").content(objectMapper.writeValueAsString(student))).andDo(print()).andExpect(status().isInternalServerError()).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(500, status);
        assertEquals(ApiException.class, mvcResult.getResolvedException().getCause().getClass());
    }

}