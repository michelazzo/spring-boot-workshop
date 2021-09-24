package nl.nn.workshop.controller;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import nl.nn.workshop.AbstractUnitTest;
import nl.nn.workshop.model.Student;
import nl.nn.workshop.repository.CourseRepository;
import nl.nn.workshop.repository.EnrollmentRepository;
import nl.nn.workshop.repository.StudentRepository;
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

    Student student = new Student();
    student.setBirthday(birthday);
    student.setName(studentName);

    Student saved = new Student();
    saved.setBirthday(birthday);
    saved.setName(studentName);
    saved.setId(1);

    when(studentRepository.save(student)).thenReturn(saved);

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
