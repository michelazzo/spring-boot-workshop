package nl.nn.workshop.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import nl.nn.workshop.AbstractIntegrationTest;
import nl.nn.workshop.model.Student;
import nl.nn.workshop.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@AutoConfigureMockMvc
public class StudentControllerIntegrationTest extends AbstractIntegrationTest {

  @Autowired
  private StudentRepository studentRepository;

  @Autowired
  protected MockMvc mvc;

  @Test
  void testCreateStudent_whenStudentInfoIsProvided_shouldCreateAndReturnSC200() throws Exception {
    LocalDate birthday = LocalDate.of(1643, 1, 4);
    String studentName = "Isaac Newton";

    Student student = new Student();
    student.setBirthday(birthday);
    student.setName(studentName);

    Student saved = studentRepository.save(student);
    assertThat(saved.getId()).isNotNull();

    RequestBuilder request =
        MockMvcRequestBuilders
            .post("/students")
            .content(GSON.toJson(student))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

    MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
    assertThat(response.getStatus()).isEqualTo(200);

    Student fromResponse = GSON.fromJson(response.getContentAsString(), Student.class);
    assertThat(fromResponse.getBirthday()).isEqualTo(birthday);
    assertThat(fromResponse.getName()).isEqualTo(studentName);
  }

  @Test
  void testGetStudent_whenStudentExists_shouldGetAndReturnSC200() throws Exception {
    LocalDate birthday = LocalDate.of(1643, 1, 4);
    String studentName = "Isaac Newton";

    Student student = new Student();
    student.setBirthday(birthday);
    student.setName(studentName);

    Student saved = studentRepository.save(student);

    RequestBuilder request =
        MockMvcRequestBuilders
            .get("/students/{id}", saved.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

    MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
    assertThat(response.getStatus()).isEqualTo(200);

    Student fromResponse = GSON.fromJson(response.getContentAsString(), Student.class);
    assertThat(fromResponse.getName()).isEqualTo(studentName);
    assertThat(fromResponse.getBirthday()).isEqualTo(birthday);
    assertThat(fromResponse.getId()).isEqualTo(saved.getId());
  }

  @Test
  void testGetStudent_whenStudentDoesNotExist_shouldSendMessageAndReturnSC404() throws Exception {
    LocalDate birthday = LocalDate.of(1643, 1, 4);
    String studentName = "Isaac Newton";

    Student student = new Student();
    student.setBirthday(birthday);
    student.setName(studentName);

    Student saved = studentRepository.save(student);

    RequestBuilder request =
        MockMvcRequestBuilders
            .get("/students/{id}", saved.getId() + 1)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

    MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
    assertThat(response.getStatus()).isEqualTo(404);
    assertThat(response.getErrorMessage()).isEqualTo(String.format("student with id %d not found", saved.getId() + 1));
  }

  @Test
  void testPutStudent_whenStudentExists_shouldUpdateAndReturnSC200() throws Exception {
    LocalDate birthday = LocalDate.of(1643, 2, 4);
    String name = "Isaac Newton";

    Student student = new Student();
    student.setBirthday(birthday);
    student.setName(name);

    String updatedName = "Sir Isaac Newton";
    LocalDate updatedBirthday = LocalDate.of(1643, 1, 4);

    Student studentUpdate = new Student();
    studentUpdate.setName(updatedName);
    studentUpdate.setBirthday(updatedBirthday);

    Student saved = studentRepository.save(student);

    RequestBuilder request =
        MockMvcRequestBuilders
            .put("/students/{id}", saved.getId())
            .content(GSON.toJson(studentUpdate))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

    MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
    assertThat(response.getStatus()).isEqualTo(200);

    Student fromResponse = GSON.fromJson(response.getContentAsString(), Student.class);
    assertThat(fromResponse.getName()).isEqualTo(updatedName);
    assertThat(fromResponse.getBirthday()).isEqualTo(updatedBirthday);
    assertThat(fromResponse.getId()).isEqualTo(saved.getId());
  }

  @Test
  void testPutStudent_whenStudentDoesNotExist_shouldSendMessageAndReturnSC404() throws Exception {
    LocalDate birthday = LocalDate.of(1643, 1, 4);
    String studentName = "Isaac Newton";

    Student student = new Student();
    student.setBirthday(birthday);
    student.setName(studentName);

    Student saved = studentRepository.save(student);

    RequestBuilder request =
        MockMvcRequestBuilders
            .put("/students/{id}", saved.getId() + 1)
            .content(GSON.toJson(student))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

    MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
    assertThat(response.getStatus()).isEqualTo(404);
    assertThat(response.getErrorMessage()).isEqualTo(String.format("student with id %d not found", saved.getId() + 1));
  }

  @Test
  void testDeleteStudent_whenStudentExists_shouldDeleteAndReturnSC204() throws Exception {
    LocalDate birthday = LocalDate.of(1643, 1, 4);
    String studentName = "Isaac Newton";

    Student student = new Student();
    student.setBirthday(birthday);
    student.setName(studentName);

    Student saved = studentRepository.save(student);

    RequestBuilder request =
        MockMvcRequestBuilders
            .delete("/students/{id}", saved.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

    MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
    assertThat(response.getStatus()).isEqualTo(204);
    assertThat(response.getContentLength()).isEqualTo(0);
  }

  @Test
  void testDeleteStudent_whenStudentDoesNotExist_shouldSendMessageAndReturnSC404() throws Exception {
    LocalDate birthday = LocalDate.of(1643, 1, 4);
    String studentName = "Isaac Newton";

    Student student = new Student();
    student.setBirthday(birthday);
    student.setName(studentName);

    Student saved = studentRepository.save(student);

    RequestBuilder request =
        MockMvcRequestBuilders
            .delete("/students/{id}", saved.getId() + 1)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

    MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
    assertThat(response.getStatus()).isEqualTo(404);
    assertThat(response.getErrorMessage()).isEqualTo(String.format("student with id %d not found", saved.getId() + 1));
  }

}