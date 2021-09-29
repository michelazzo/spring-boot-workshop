package nl.nn.workshop.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.gson.reflect.TypeToken;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import nl.nn.workshop.AbstractIntegrationTest;
import nl.nn.workshop.model.Course;
import nl.nn.workshop.model.Enrollment;
import nl.nn.workshop.model.Student;
import nl.nn.workshop.repository.CourseRepository;
import nl.nn.workshop.repository.EnrollmentRepository;
import nl.nn.workshop.repository.StudentRepository;
import nl.nn.workshop.resource.EnrollmentResource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@AutoConfigureMockMvc
public class EnrollmentControllerIntegrationTest extends AbstractIntegrationTest {

  @Autowired
  private StudentRepository studentRepository;

  @Autowired
  private CourseRepository courseRepository;

  @Autowired
  private EnrollmentRepository enrollmentRepository;

  @Test
  public void testCreateEnrollment_whenStudentAndCourseExist_shouldCreateAndReturnSC200() throws Exception {
    LocalDate birthday = LocalDate.of(1643, 1, 4);
    String studentName = "Isaac Newton";

    Student student = new Student();
    student.setBirthday(birthday);
    student.setName(studentName);

    Student savedStudent = studentRepository.save(student);

    String courseName = "Physics";

    Course course = new Course();
    course.setName(courseName);
    course.setAvailable(true);

    Course savedCourse = courseRepository.save(course);

    RequestBuilder request =
        MockMvcRequestBuilders
            .post("/enrollments/student/{studentId}/course/{courseId}", student.getId(), course.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

    MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
    assertThat(response.getStatus()).isEqualTo(200);

    EnrollmentResource fromResponse = GSON.fromJson(response.getContentAsString(), EnrollmentResource.class);
    assertThat(fromResponse.getEnrollmentDate()).isNotNull();
    assertThat(fromResponse.getStudentId()).isEqualTo(savedStudent.getId());
    assertThat(fromResponse.getCourseId()).isEqualTo(savedCourse.getId());
  }

  @Test
  public void testCreateEnrollment_whenStudentDoesNotExist_shouldFailAndReturnSC404() throws Exception {
    LocalDate birthday = LocalDate.of(1643, 1, 4);
    String studentName = "Isaac Newton";

    Student student = new Student();
    student.setBirthday(birthday);
    student.setName(studentName);

    Student savedStudent = studentRepository.save(student);

    String courseName = "Physics";

    Course course = new Course();
    course.setName(courseName);
    course.setAvailable(true);

    Course savedCourse = courseRepository.save(course);

    RequestBuilder request =
        MockMvcRequestBuilders
            .post("/enrollments/student/{studentId}/course/{courseId}", savedStudent.getId() + 1, savedCourse.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

    MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
    assertThat(response.getStatus()).isEqualTo(404);
    assertThat(response.getContentLength()).isEqualTo(0);
    assertThat(response.getErrorMessage()).isEqualTo(String.format("student with id %d not found", (savedStudent.getId() + 1)));
  }

  @Test
  public void testCreateEnrollment_whenCourseDoesNotExist_shouldFailAndReturnSC404() throws Exception {
    LocalDate birthday = LocalDate.of(1643, 1, 4);
    String studentName = "Isaac Newton";

    Student student = new Student();
    student.setBirthday(birthday);
    student.setName(studentName);

    Student savedStudent = studentRepository.save(student);

    String courseName = "Physics";

    Course course = new Course();
    course.setName(courseName);
    course.setAvailable(true);

    Course savedCourse = courseRepository.save(course);

    RequestBuilder request =
        MockMvcRequestBuilders
            .post("/enrollments/student/{studentId}/course/{courseId}", savedStudent.getId(), savedCourse.getId() + 1)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

    MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
    assertThat(response.getStatus()).isEqualTo(404);
    assertThat(response.getContentLength()).isEqualTo(0);
    assertThat(response.getErrorMessage()).isEqualTo(String.format("course with id %d not found", (savedCourse.getId() + 1)));
  }

  @Test
  public void testCreateEnrollment_whenEnrollmentAlreadyExists_shouldFailAndReturnSC409() throws Exception {
    LocalDate birthday = LocalDate.of(1643, 1, 4);
    String studentName = "Isaac Newton";

    Student student = new Student();
    student.setBirthday(birthday);
    student.setName(studentName);

    Student savedStudent = studentRepository.save(student);

    String courseName = "Physics";

    Course course = new Course();
    course.setName(courseName);
    course.setAvailable(true);

    Course savedCourse = courseRepository.save(course);

    Enrollment enrollment = new Enrollment();
    enrollment.setEnrollmentDate(LocalDateTime.now());
    enrollment.setStudentId(savedStudent.getId());
    enrollment.setCourseId(savedCourse.getId());

    enrollmentRepository.save(enrollment);

    RequestBuilder request =
        MockMvcRequestBuilders
            .post("/enrollments/student/{studentId}/course/{courseId}", savedStudent.getId(), savedCourse.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

    MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
    assertThat(response.getStatus()).isEqualTo(409);
    assertThat(response.getContentLength()).isEqualTo(0);
    assertThat(response.getErrorMessage()).isEqualTo(
        String.format("student %d is already enrolled in the course %d", savedStudent.getId(), savedCourse.getId()));
  }

  @Test
  void testGetEnrollment_whenEnrollmentExists_shouldGetAndReturnSC200() throws Exception {
    LocalDate birthday = LocalDate.of(1643, 1, 4);
    String studentName = "Isaac Newton";

    Student student = new Student();
    student.setBirthday(birthday);
    student.setName(studentName);

    Student savedStudent = studentRepository.save(student);

    String courseName = "Physics";

    Course course = new Course();
    course.setName(courseName);
    course.setAvailable(true);

    Course savedCourse = courseRepository.save(course);

    Enrollment enrollment = new Enrollment();
    enrollment.setEnrollmentDate(LocalDateTime.now());
    enrollment.setStudentId(savedStudent.getId());
    enrollment.setCourseId(savedCourse.getId());

    enrollmentRepository.save(enrollment);

    RequestBuilder request =
        MockMvcRequestBuilders
            .get("/enrollments/student/{studentId}/course/{courseId}", savedStudent.getId(), savedCourse.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

    MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
    assertThat(response.getStatus()).isEqualTo(200);

    EnrollmentResource fromResponse = GSON.fromJson(response.getContentAsString(), EnrollmentResource.class);
    assertThat(fromResponse.getEnrollmentDate()).isNotNull();
    assertThat(fromResponse.getStudentId()).isEqualTo(savedStudent.getId());
    assertThat(fromResponse.getCourseId()).isEqualTo(savedCourse.getId());
  }

  @Test
  void testGetEnrollment_whenEnrollmentDoesNotExists_shouldGetAndReturnSC404() throws Exception {
    LocalDate birthday = LocalDate.of(1643, 1, 4);
    String studentName = "Isaac Newton";

    Student student = new Student();
    student.setBirthday(birthday);
    student.setName(studentName);

    Student savedStudent = studentRepository.save(student);

    String courseName = "Physics";

    Course course = new Course();
    course.setName(courseName);
    course.setAvailable(true);

    Course savedCourse = courseRepository.save(course);

    Enrollment enrollment = new Enrollment();
    enrollment.setEnrollmentDate(LocalDateTime.now());
    enrollment.setStudentId(savedStudent.getId());
    enrollment.setCourseId(savedCourse.getId());

    enrollmentRepository.save(enrollment);

    RequestBuilder request =
        MockMvcRequestBuilders
            .get("/enrollments/student/{studentId}/course/{courseId}",
                enrollment.getStudentId() + 1, enrollment.getCourseId() + 1)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

    MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
    assertThat(response.getStatus()).isEqualTo(404);
    assertThat(response.getErrorMessage()).isEqualTo(
        String.format("enrollment for user with id %d and course with id %d not found",
            enrollment.getStudentId() + 1, enrollment.getCourseId() + 1));
  }

  @Test
  void testGetEnrollments_whenEnrollmentsExist_shouldGetListAndReturnSC200() throws Exception {
    LocalDate birthday = LocalDate.of(1643, 1, 4);
    String studentName = "Isaac Newton";

    Student student = new Student();
    student.setBirthday(birthday);
    student.setName(studentName);

    Student savedStudent = studentRepository.save(student);

    String courseName = "Physics";

    Course course = new Course();
    course.setName(courseName);
    course.setAvailable(true);

    Course savedCourse = courseRepository.save(course);

    Enrollment enrollment = new Enrollment();
    enrollment.setEnrollmentDate(LocalDateTime.now());
    enrollment.setStudentId(savedStudent.getId());
    enrollment.setCourseId(savedCourse.getId());

    enrollmentRepository.save(enrollment);

    RequestBuilder request =
        MockMvcRequestBuilders
            .get("/enrollments")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

    MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
    assertThat(response.getStatus()).isEqualTo(200);

    ArrayList<EnrollmentResource> fromResponse = GSON.fromJson(response.getContentAsString(),
        new TypeToken<ArrayList<EnrollmentResource>>() {}.getType());
    assertThat(fromResponse.size()).isEqualTo(1);

    EnrollmentResource enrollmentResource = new EnrollmentResource();
    enrollmentResource.setEnrollmentDate(fromResponse.get(0).getEnrollmentDate()); // hacky
    enrollmentResource.setStudentId(savedStudent.getId());
    enrollmentResource.setCourseId(savedCourse.getId());
    assertThat(fromResponse).containsExactlyInAnyOrder(enrollmentResource);
  }

  @Test
  public void testDeleteEnrollment_whenStudentAndCourseExist_shouldCreateAndReturnSC200() throws Exception {
    LocalDate birthday = LocalDate.of(1643, 1, 4);
    String studentName = "Isaac Newton";

    Student student = new Student();
    student.setBirthday(birthday);
    student.setName(studentName);

    Student savedStudent = studentRepository.save(student);

    String courseName = "Physics";

    Course course = new Course();
    course.setName(courseName);
    course.setAvailable(true);

    Course savedCourse = courseRepository.save(course);

    Enrollment enrollment = new Enrollment();
    enrollment.setEnrollmentDate(LocalDateTime.now());
    enrollment.setStudentId(savedStudent.getId());
    enrollment.setCourseId(savedCourse.getId());

    enrollmentRepository.save(enrollment);

    RequestBuilder request =
        MockMvcRequestBuilders
            .delete("/enrollments/student/{studentId}/course/{courseId}", savedStudent.getId(), savedCourse.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

    MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
    assertThat(response.getStatus()).isEqualTo(200);
    assertThat(response.getContentAsString()).isEmpty();
  }

  @Test
  public void testDeleteEnrollment_whenEnrollmentDoesNotExist_shouldFailAndReturnSC404() throws Exception {
    LocalDate birthday = LocalDate.of(1643, 1, 4);
    String studentName = "Isaac Newton";

    Student student = new Student();
    student.setBirthday(birthday);
    student.setName(studentName);

    Student savedStudent = studentRepository.save(student);

    String courseName = "Physics";

    Course course = new Course();
    course.setName(courseName);
    course.setAvailable(true);

    Course savedCourse = courseRepository.save(course);

    Enrollment enrollment = new Enrollment();
    enrollment.setEnrollmentDate(LocalDateTime.now());
    enrollment.setStudentId(savedStudent.getId());
    enrollment.setCourseId(savedCourse.getId());

    enrollmentRepository.save(enrollment);

    RequestBuilder request =
        MockMvcRequestBuilders
            .delete("/enrollments/student/{studentId}/course/{courseId}", savedStudent.getId() + 1L, savedCourse.getId() + 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

    MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
    assertThat(response.getStatus()).isEqualTo(404);
    assertThat(response.getContentAsString()).isEmpty();
  }

}
