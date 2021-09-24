package nl.nn.workshop.controller;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

import java.util.Optional;
import nl.nn.workshop.AbstractUnitTest;
import nl.nn.workshop.model.Course;
import nl.nn.workshop.repository.CourseRepository;
import nl.nn.workshop.repository.EnrollmentRepository;
import nl.nn.workshop.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@MockBean(StudentRepository.class)
@MockBean(EnrollmentRepository.class)
public class CourseControllerUnitTest extends AbstractUnitTest {

  @MockBean
  private CourseRepository courseRepository;

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
  void testGetCourse_whenCourseExists_shouldGetAndReturnSC200() throws Exception {
    String courseName = "Physics";
    long courseId = 1L;

    Course course = new Course();
    course.setName(courseName);
    course.setAvailable(true);
    course.setId(courseId);

    when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

    RequestBuilder request =
        MockMvcRequestBuilders
            .get("/courses/{id}", courseId)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

    MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
    assertThat(response.getStatus()).isEqualTo(200);

    Course fromResponse = GSON.fromJson(response.getContentAsString(), Course.class);
    assertThat(fromResponse.getName()).isEqualTo(courseName);
    assertThat(fromResponse.isAvailable()).isEqualTo(true);
    assertThat(fromResponse.getId()).isEqualTo(courseId);
  }

  @Test
  void testGetCourse_whenCourseDoesNotExist_shouldSendMessageAndReturnSC404() throws Exception {
    long courseId = 1L;
    when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

    RequestBuilder request =
        MockMvcRequestBuilders
            .get("/courses/{id}", courseId)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

    MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
    assertThat(response.getStatus()).isEqualTo(404);
    assertThat(response.getErrorMessage()).isEqualTo("course with id 1 not found");
  }

  @Test
  void testPutCourse_whenCourseExists_shouldUpdateAndReturnSC200() throws Exception {
    String courseName = "Physics";
    String courseNewName = "Physics v1";
    long courseId = 1L;

    Course course = new Course();
    course.setName(courseName);
    course.setAvailable(true);
    course.setId(courseId);

    Course courseUpdate = new Course();
    courseUpdate.setName(courseNewName);
    courseUpdate.setAvailable(false);
    courseUpdate.setId(courseId);

    when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
    when(courseRepository.save(course)).thenReturn(courseUpdate);

    RequestBuilder request =
        MockMvcRequestBuilders
            .put("/courses/{id}", courseId)
            .content(GSON.toJson(courseUpdate))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

    MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
    assertThat(response.getStatus()).isEqualTo(200);

    Course fromResponse = GSON.fromJson(response.getContentAsString(), Course.class);
    assertThat(fromResponse.getName()).isEqualTo(courseNewName);
    assertThat(fromResponse.isAvailable()).isEqualTo(false);
    assertThat(fromResponse.getId()).isEqualTo(courseId);
  }

  @Test
  void testPutCourse_whenCourseDoesNotExist_shouldSendMessageAndReturnSC404() throws Exception {
    String courseName = "Physics";
    long courseId = 1L;

    Course course = new Course();
    course.setName(courseName);
    course.setAvailable(true);
    course.setId(courseId);

    when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

    RequestBuilder request =
        MockMvcRequestBuilders
            .put("/courses/{id}", courseId)
            .content(GSON.toJson(course))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

    MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
    assertThat(response.getStatus()).isEqualTo(404);
    assertThat(response.getErrorMessage()).isEqualTo("course with id 1 not found");
  }



  @Test
  void testDeleteCourse_whenCourseExists_shouldDeleteAndReturnSC204() throws Exception {
    long courseId = 1L;
    when(courseRepository.existsById(courseId)).thenReturn(true);

    RequestBuilder request =
        MockMvcRequestBuilders
            .delete("/courses/{id}", courseId)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

    MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
    assertThat(response.getStatus()).isEqualTo(204);
    assertThat(response.getContentLength()).isEqualTo(0);
  }

  @Test
  void testDeleteCourse_whenCourseDoesNotExist_shouldSendMessageAndReturnSC404() throws Exception {
    long courseId = 1L;
    when(courseRepository.existsById(courseId)).thenReturn(false);

    RequestBuilder request =
        MockMvcRequestBuilders
            .delete("/courses/{id}", courseId)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

    MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
    assertThat(response.getStatus()).isEqualTo(404);
    assertThat(response.getErrorMessage()).isEqualTo("course with id 1 not found");
  }

}