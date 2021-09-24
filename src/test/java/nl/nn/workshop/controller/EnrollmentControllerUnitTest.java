package nl.nn.workshop.controller;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import nl.nn.workshop.AbstractUnitTest;
import nl.nn.workshop.model.Enrollment;
import nl.nn.workshop.repository.CourseRepository;
import nl.nn.workshop.repository.EnrollmentRepository;
import nl.nn.workshop.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public class EnrollmentControllerUnitTest extends AbstractUnitTest {

  @MockBean
  private StudentRepository studentRepository;

  @MockBean
  private CourseRepository courseRepository;

  @MockBean
  private EnrollmentRepository enrollmentRepository;

  @Test
  public void testCreateEnrollment_whenStudentAndCourseExist_shouldCreateAndReturnSC200() throws Exception {
    LocalDateTime now = LocalDateTime.now();

    when(studentRepository.existsById(1L)).thenReturn(true);
    when(courseRepository.existsById(1L)).thenReturn(true);

    Enrollment enrollment = new Enrollment();
    enrollment.setStudentId(1L);
    enrollment.setCourseId(1L);
    enrollment.setEnrollmentDate(now);

    when(enrollmentRepository.save(enrollment)).thenReturn(enrollment);

    RequestBuilder request =
        MockMvcRequestBuilders
            .post("/enrollments")
            .content(GSON.toJson(enrollment))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

    MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
    assertThat(response.getStatus()).isEqualTo(200);

    Enrollment fromResponse = GSON.fromJson(response.getContentAsString(), Enrollment.class);
    assertThat(fromResponse.getEnrollmentDate()).isEqualTo(now);
    assertThat(fromResponse.getStudentId()).isEqualTo(1L);
    assertThat(fromResponse.getCourseId()).isEqualTo(1L);
  }

  @Test
  public void testCreateEnrollment_whenStudentDoesNotExist_shouldFailAndReturnSC404() throws Exception {
    LocalDateTime now = LocalDateTime.now();

    when(studentRepository.existsById(1L)).thenReturn(false);
    when(courseRepository.existsById(1L)).thenReturn(true);

    Enrollment enrollment = new Enrollment();
    enrollment.setStudentId(1L);
    enrollment.setCourseId(1L);
    enrollment.setEnrollmentDate(now);

    when(enrollmentRepository.save(enrollment)).thenReturn(enrollment);

    RequestBuilder request =
        MockMvcRequestBuilders
            .post("/enrollments")
            .content(GSON.toJson(enrollment))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

    MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
    assertThat(response.getStatus()).isEqualTo(404);
    assertThat(response.getContentLength()).isEqualTo(0);
    assertThat(response.getErrorMessage()).isEqualTo("student with id 1 not found");
  }

  @Test
  public void testCreateEnrollment_whenCourseDoesNotExist_shouldFailAndReturnSC404() throws Exception {
    LocalDateTime now = LocalDateTime.now();

    when(studentRepository.existsById(1L)).thenReturn(true);
    when(courseRepository.existsById(1L)).thenReturn(false);

    Enrollment enrollment = new Enrollment();
    enrollment.setStudentId(1L);
    enrollment.setCourseId(1L);
    enrollment.setEnrollmentDate(now);

    when(enrollmentRepository.save(enrollment)).thenReturn(enrollment);

    RequestBuilder request =
        MockMvcRequestBuilders
            .post("/enrollments")
            .content(GSON.toJson(enrollment))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

    MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
    assertThat(response.getStatus()).isEqualTo(404);
    assertThat(response.getContentLength()).isEqualTo(0);
    assertThat(response.getErrorMessage()).isEqualTo("course with id 1 not found");
  }

}