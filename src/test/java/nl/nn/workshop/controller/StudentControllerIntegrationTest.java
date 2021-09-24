package nl.nn.workshop.controller;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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

}