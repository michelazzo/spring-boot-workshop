package nl.nn.workshop.controller;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;
import nl.nn.workshop.AbstractUnitTest;
import nl.nn.workshop.model.Student;
import nl.nn.workshop.repository.CourseRepository;
import nl.nn.workshop.repository.EnrollmentRepository;
import nl.nn.workshop.repository.StudentRepository;
import nl.nn.workshop.resource.CreateStudentRequestResource;
import nl.nn.workshop.resource.StudentResource;
import nl.nn.workshop.resource.UpdateStudentRequestResource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@MockBean(CourseRepository.class)
@MockBean(EnrollmentRepository.class)
public class StudentControllerUnitTest extends AbstractUnitTest {

  @MockBean
  private StudentRepository studentRepository;

  @Test
  void testCreateStudent_whenStudentInfoIsProvided_shouldCreateAndReturnSC200() throws Exception {
    LocalDate birthday = LocalDate.of(1643, 1, 4);
    String studentName = "Isaac Newton";
    long studentId = 1L;

    CreateStudentRequestResource resource = new CreateStudentRequestResource();
    resource.setBirthday(birthday);
    resource.setName(studentName);

    when(studentRepository.save(any(Student.class))).then(i -> {
      Student s = i.getArgument(0, Student.class);
      s.setId(studentId);
      return s;
    });

    RequestBuilder request =
        MockMvcRequestBuilders
            .post("/students")
            .content(GSON.toJson(resource))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

    MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
    assertThat(response.getStatus()).isEqualTo(200);

    StudentResource fromResponse = GSON.fromJson(response.getContentAsString(), StudentResource.class);
    assertThat(fromResponse.getBirthday()).isEqualTo(birthday);
    assertThat(fromResponse.getName()).isEqualTo(studentName);
  }

  @Test
  void testGetStudent_whenStudentExists_shouldGetAndReturnSC200() throws Exception {
    LocalDate birthday = LocalDate.of(1643, 1, 4);
    String studentName = "Isaac Newton";
    long studentId = 1L;

    Student student = new Student();
    student.setBirthday(birthday);
    student.setName(studentName);
    student.setId(studentId);

    when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));

    RequestBuilder request =
        MockMvcRequestBuilders
            .get("/students/{id}", studentId)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

    MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
    assertThat(response.getStatus()).isEqualTo(200);

    StudentResource fromResponse = GSON.fromJson(response.getContentAsString(), StudentResource.class);
    assertThat(fromResponse.getName()).isEqualTo(studentName);
    assertThat(fromResponse.getBirthday()).isEqualTo(birthday);
    assertThat(fromResponse.getId()).isEqualTo(studentId);
  }

  @Test
  void testGetStudent_whenStudentDoesNotExist_shouldSendMessageAndReturnSC404() throws Exception {
    long studentId = 1L;
    when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

    RequestBuilder request =
        MockMvcRequestBuilders
            .get("/students/{id}", studentId)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

    MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
    assertThat(response.getStatus()).isEqualTo(404);
    assertThat(response.getErrorMessage()).isEqualTo(String.format("student with id %d not found", studentId));
  }

  @Test
  void testPutStudent_whenStudentExists_shouldUpdateAndReturnSC200() throws Exception {
    LocalDate birthday = LocalDate.of(1643, 2, 4);
    String name = "Isaac Newton";
    long studentId = 1L;

    Student student = new Student();
    student.setBirthday(birthday);
    student.setName(name);
    student.setId(studentId);

    String updatedName = "Sir Isaac Newton";
    LocalDate updatedBirthday = LocalDate.of(1643, 1, 4);

    when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
    when(studentRepository.save(any(Student.class))).then(returnsFirstArg());

    UpdateStudentRequestResource studentUpdate = new UpdateStudentRequestResource();
    studentUpdate.setName(updatedName);
    studentUpdate.setBirthday(updatedBirthday);

    RequestBuilder request =
        MockMvcRequestBuilders
            .put("/students/{id}", studentId)
            .content(GSON.toJson(studentUpdate))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

    MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
    assertThat(response.getStatus()).isEqualTo(200);

    StudentResource fromResponse = GSON.fromJson(response.getContentAsString(), StudentResource.class);
    assertThat(fromResponse.getName()).isEqualTo(updatedName);
    assertThat(fromResponse.getBirthday()).isEqualTo(updatedBirthday);
    assertThat(fromResponse.getId()).isEqualTo(studentId);
  }

  @Test
  void testPutStudent_whenStudentDoesNotExist_shouldSendMessageAndReturnSC404() throws Exception {
    LocalDate birthday = LocalDate.of(1643, 1, 4);
    String studentName = "Isaac Newton";
    long studentId = 1L;

    UpdateStudentRequestResource resource = new UpdateStudentRequestResource();
    resource.setBirthday(birthday);
    resource.setName(studentName);

    when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

    RequestBuilder request =
        MockMvcRequestBuilders
            .put("/students/{id}", studentId)
            .content(GSON.toJson(resource))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

    MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
    assertThat(response.getStatus()).isEqualTo(404);
    assertThat(response.getErrorMessage()).isEqualTo(String.format("student with id %d not found", studentId));
  }

  @Test
  void testDeleteStudent_whenStudentExists_shouldDeleteAndReturnSC204() throws Exception {
    long studentId = 1L;
    when(studentRepository.existsById(studentId)).thenReturn(true);

    RequestBuilder request =
        MockMvcRequestBuilders
            .delete("/students/{id}", studentId)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

    MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
    assertThat(response.getStatus()).isEqualTo(204);
    assertThat(response.getContentLength()).isEqualTo(0);
  }

  @Test
  void testDeleteStudent_whenStudentDoesNotExist_shouldSendMessageAndReturnSC404() throws Exception {
    long studentId = 1L;
    when(studentRepository.existsById(studentId)).thenReturn(false);

    RequestBuilder request =
        MockMvcRequestBuilders
            .delete("/students/{id}", studentId)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

    MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
    assertThat(response.getStatus()).isEqualTo(404);
    assertThat(response.getErrorMessage()).isEqualTo(String.format("student with id %d not found", studentId));
  }

}
