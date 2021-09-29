package nl.nn.workshop.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.google.gson.reflect.TypeToken;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;
import nl.nn.workshop.AbstractUnitTest;
import nl.nn.workshop.model.Enrollment;
import nl.nn.workshop.model.EnrollmentPk;
import nl.nn.workshop.repository.CourseRepository;
import nl.nn.workshop.repository.EnrollmentRepository;
import nl.nn.workshop.repository.StudentRepository;
import nl.nn.workshop.resource.EnrollmentResource;
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
    long studentId = 1L;
    long courseId = 1L;

    when(studentRepository.existsById(studentId)).thenReturn(true);
    when(courseRepository.existsById(courseId)).thenReturn(true);
    when(enrollmentRepository.save(any(Enrollment.class))).then(returnsFirstArg());

    RequestBuilder request =
        MockMvcRequestBuilders
            .post("/enrollments/student/{studentId}/course/{courseId}", studentId, courseId)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

    MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
    assertThat(response.getStatus()).isEqualTo(200);

    EnrollmentResource fromResponse = GSON.fromJson(response.getContentAsString(), EnrollmentResource.class);
    assertThat(fromResponse.getEnrollmentDate()).isNotNull();
    assertThat(fromResponse.getStudentId()).isEqualTo(studentId);
    assertThat(fromResponse.getCourseId()).isEqualTo(courseId);
  }

  @Test
  public void testCreateEnrollment_whenStudentDoesNotExist_shouldFailAndReturnSC404() throws Exception {
    long studentId = 1L;
    long courseId = 1L;

    when(studentRepository.existsById(studentId)).thenReturn(false);
    when(courseRepository.existsById(courseId)).thenReturn(true);

    RequestBuilder request =
        MockMvcRequestBuilders
            .post("/enrollments/student/{studentId}/course/{courseId}", studentId, courseId)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

    MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
    assertThat(response.getStatus()).isEqualTo(404);
    assertThat(response.getContentLength()).isEqualTo(0);
    assertThat(response.getErrorMessage()).isEqualTo("student with id 1 not found");
  }

  @Test
  public void testCreateEnrollment_whenCourseDoesNotExist_shouldFailAndReturnSC404() throws Exception {
    long studentId = 1L;
    long courseId = 1L;

    when(studentRepository.existsById(studentId)).thenReturn(true);
    when(courseRepository.existsById(courseId)).thenReturn(false);

    RequestBuilder request =
        MockMvcRequestBuilders
            .post("/enrollments/student/{studentId}/course/{courseId}", studentId, courseId)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

    MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
    assertThat(response.getStatus()).isEqualTo(404);
    assertThat(response.getContentLength()).isEqualTo(0);
    assertThat(response.getErrorMessage()).isEqualTo("course with id 1 not found");
  }

  @Test
  void testGetEnrollment_whenEnrollmentExists_shouldGetAndReturnSC200() throws Exception {
    long courseId = 1L;
    long studentId = 1L;

    Enrollment enrollment = new Enrollment();
    enrollment.setEnrollmentDate(LocalDateTime.now());
    enrollment.setStudentId(studentId);
    enrollment.setCourseId(courseId);

    EnrollmentPk pk = new EnrollmentPk(studentId, courseId);
    when(enrollmentRepository.findById(pk)).thenReturn(Optional.of(enrollment));

    RequestBuilder request =
        MockMvcRequestBuilders
            .get("/enrollments/student/{studentId}/course/{courseId}", studentId, courseId)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

    MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
    assertThat(response.getStatus()).isEqualTo(200);

    EnrollmentResource fromResponse = GSON.fromJson(response.getContentAsString(), EnrollmentResource.class);
    assertThat(fromResponse.getEnrollmentDate()).isNotNull();
    assertThat(fromResponse.getStudentId()).isEqualTo(studentId);
    assertThat(fromResponse.getCourseId()).isEqualTo(courseId);
  }

  @Test
  void testGetEnrollment_whenEnrollmentDoesNotExists_shouldGetAndReturnSC404() throws Exception {
    long courseId = 1L;
    long studentId = 1L;

    EnrollmentPk pk = new EnrollmentPk(studentId, courseId);
    when(enrollmentRepository.findById(pk)).thenReturn(Optional.empty());

    RequestBuilder request =
        MockMvcRequestBuilders
            .get("/enrollments/student/{studentId}/course/{courseId}", studentId, courseId)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

    MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
    assertThat(response.getStatus()).isEqualTo(404);
    assertThat(response.getErrorMessage()).isEqualTo(
        String.format("enrollment for user with id %d and course with id %d not found", studentId, courseId));
  }

  @Test
  void testGetEnrollments_whenEnrollmentsExist_shouldGetListAndReturnSC200() throws Exception {
    long courseId = 1L;
    long studentId = 1L;

    LocalDateTime now = LocalDateTime.now();

    Enrollment enrollment = new Enrollment();
    enrollment.setEnrollmentDate(now);
    enrollment.setStudentId(studentId);
    enrollment.setCourseId(courseId);

    EnrollmentResource enrollmentResource = new EnrollmentResource();
    enrollmentResource.setEnrollmentDate(now);
    enrollmentResource.setStudentId(studentId);
    enrollmentResource.setCourseId(courseId);

    when(enrollmentRepository.findAll()).thenReturn(Set.of(enrollment));

    RequestBuilder request =
        MockMvcRequestBuilders
            .get("/enrollments")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

    MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
    assertThat(response.getStatus()).isEqualTo(200);

    ArrayList<EnrollmentResource> fromResponse = GSON.fromJson(
        response.getContentAsString(), new TypeToken<ArrayList<EnrollmentResource>>() {}.getType());
    assertThat(fromResponse.size()).isEqualTo(1);

    assertThat(fromResponse).containsExactlyInAnyOrder(enrollmentResource);
  }

  @Test
  public void testDeleteEnrollment_whenStudentAndCourseExist_shouldCreateAndReturnSC200() throws Exception {
    long studentId = 1L;
    long courseId = 1L;

    Enrollment enrollment = new Enrollment();
    enrollment.setEnrollmentDate(LocalDateTime.now());
    enrollment.setStudentId(studentId);
    enrollment.setCourseId(courseId);

    when(enrollmentRepository.findById(any())).thenReturn(Optional.of(enrollment));

    RequestBuilder request =
        MockMvcRequestBuilders
            .delete("/enrollments/student/{studentId}/course/{courseId}", studentId, courseId)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

    MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
    assertThat(response.getStatus()).isEqualTo(200);
    assertThat(response.getContentAsString()).isEmpty();
  }

  @Test
  public void testDeleteEnrollment_whenEnrollmentDoesNotExist_shouldFailAndReturnSC404() throws Exception {
    long studentId = 1L;
    long courseId = 1L;

    when(enrollmentRepository.findById(any())).thenReturn(Optional.empty());

    RequestBuilder request =
        MockMvcRequestBuilders
            .delete("/enrollments/student/{studentId}/course/{courseId}", studentId, courseId)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

    MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
    assertThat(response.getStatus()).isEqualTo(404);
    assertThat(response.getContentAsString()).isEmpty();
  }

}
