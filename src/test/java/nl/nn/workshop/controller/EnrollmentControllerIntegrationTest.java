package nl.nn.workshop.controller;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import nl.nn.workshop.AbstractIntegrationTest;
import nl.nn.workshop.model.Course;
import nl.nn.workshop.model.Enrollment;
import nl.nn.workshop.model.Student;
import nl.nn.workshop.repository.CourseRepository;
import nl.nn.workshop.repository.EnrollmentRepository;
import nl.nn.workshop.repository.StudentRepository;
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

    LocalDateTime now = LocalDateTime.now();

    Enrollment enrollment = new Enrollment();
    enrollment.setStudentId(savedStudent.getId());
    enrollment.setCourseId(savedCourse.getId());
    enrollment.setEnrollmentDate(now);

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

    LocalDateTime now = LocalDateTime.now();

    Enrollment enrollment = new Enrollment();
    enrollment.setStudentId(savedStudent.getId() + 1);
    enrollment.setCourseId(savedCourse.getId());
    enrollment.setEnrollmentDate(now);

    RequestBuilder request =
        MockMvcRequestBuilders
            .post("/enrollments")
            .content(GSON.toJson(enrollment))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

    MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
    assertThat(response.getStatus()).isEqualTo(404);
    assertThat(response.getContentLength()).isEqualTo(0);
    assertThat(response.getErrorMessage()).isEqualTo(String.format("student with id %d not found", (student.getId() + 1)));
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

    LocalDateTime now = LocalDateTime.now();

    Enrollment enrollment = new Enrollment();
    enrollment.setStudentId(savedStudent.getId());
    enrollment.setCourseId(savedCourse.getId() + 1);
    enrollment.setEnrollmentDate(now);

    RequestBuilder request =
        MockMvcRequestBuilders
            .post("/enrollments")
            .content(GSON.toJson(enrollment))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

    MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
    assertThat(response.getStatus()).isEqualTo(404);
    assertThat(response.getContentLength()).isEqualTo(0);
    assertThat(response.getErrorMessage()).isEqualTo(String.format("course with id %d not found", (course.getId() + 1)));
  }

}