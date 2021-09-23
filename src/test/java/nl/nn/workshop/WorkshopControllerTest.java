package nl.nn.workshop;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import nl.nn.workshop.model.Course;
import nl.nn.workshop.model.Enrollment;
import nl.nn.workshop.model.Student;
import nl.nn.workshop.utils.LocalDateDeserializer;
import nl.nn.workshop.utils.LocalDateSerializer;
import nl.nn.workshop.utils.LocalDateTimeDeserializer;
import nl.nn.workshop.utils.LocalDateTimeSerializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("unit-test")
class WorkshopControllerTest {

  private static final Gson GSON;

  static {
    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
    gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
    gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
    gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer());
    GSON = gsonBuilder.setPrettyPrinting().create();
  }

  @MockBean
  private StudentRepository studentRepository;

  @MockBean
  private CourseRepository courseRepository;

  @MockBean
  private EnrollmentRepository enrollmentRepository;

  @Autowired
  protected MockMvc mvc;

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

  @Test
  void testCreateCourse_whenCourseInfoIsProvided_shouldCreateAndReturnSC200() throws Exception {
    String courseName = "Physics";

    Course course = new Course();
    course.setName(courseName);
    course.setAvailable(true);

    Course saved = new Course();
    saved.setName(courseName);
    saved.setAvailable(true);
    saved.setId(1);

    when(courseRepository.save(course)).thenReturn(saved);

    RequestBuilder request =
        MockMvcRequestBuilders
            .post("/courses")
            .content(GSON.toJson(course))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

    MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
    assertThat(response.getStatus()).isEqualTo(200);

    Course fromResponse = GSON.fromJson(response.getContentAsString(), Course.class);
    assertThat(fromResponse.getName()).isEqualTo(courseName);
    assertThat(fromResponse.isAvailable()).isEqualTo(true);
    assertThat(fromResponse.getId()).isEqualTo(1L);
  }

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