package nl.nn.workshop.controller;

import static org.assertj.core.api.Assertions.assertThat;

import nl.nn.workshop.AbstractIntegrationTest;
import nl.nn.workshop.model.Course;
import nl.nn.workshop.repository.CourseRepository;
import nl.nn.workshop.resource.CourseResource;
import nl.nn.workshop.resource.CreateCourseRequestResource;
import nl.nn.workshop.resource.UpdateCourseRequestResource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public class CourseControllerIntegrationTest extends AbstractIntegrationTest {

  @Autowired
  private CourseRepository courseRepository;

  @Test
  void testCreateCourse_whenCourseInfoIsProvided_shouldCreateAndReturnSC200() throws Exception {
    String courseName = "Physics";

    CreateCourseRequestResource course = new CreateCourseRequestResource();
    course.setName(courseName);
    course.setAvailable(true);

    RequestBuilder request =
        MockMvcRequestBuilders
            .post("/courses")
            .content(GSON.toJson(course))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

    MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
    assertThat(response.getStatus()).isEqualTo(200);

    CourseResource fromResponse = GSON.fromJson(response.getContentAsString(), CourseResource.class);
    assertThat(fromResponse.getName()).isEqualTo(courseName);
    assertThat(fromResponse.isAvailable()).isEqualTo(true);
  }

  @Test
  void testGetCourse_whenCourseExists_shouldGetAndReturnSC200() throws Exception {
    String courseName = "Physics";

    Course course = new Course();
    course.setName(courseName);
    course.setAvailable(true);

    Course saved = courseRepository.save(course);

    RequestBuilder request =
        MockMvcRequestBuilders
            .get("/courses/{id}", saved.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

    MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
    assertThat(response.getStatus()).isEqualTo(200);

    CourseResource fromResponse = GSON.fromJson(response.getContentAsString(), CourseResource.class);
    assertThat(fromResponse.getName()).isEqualTo(courseName);
    assertThat(fromResponse.isAvailable()).isEqualTo(true);
    assertThat(fromResponse.getId()).isEqualTo(saved.getId());
  }

  @Test
  void testGetCourse_whenCourseDoesNotExist_shouldSendMessageAndReturnSC404() throws Exception {
    String courseName = "Physics";

    Course course = new Course();
    course.setName(courseName);
    course.setAvailable(true);

    Course saved = courseRepository.save(course);

    RequestBuilder request =
        MockMvcRequestBuilders
            .get("/courses/{id}", saved.getId() + 1)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

    MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
    assertThat(response.getStatus()).isEqualTo(404);
    assertThat(response.getErrorMessage()).isEqualTo(String.format("course with id %d not found", saved.getId() + 1));
  }

  @Test
  void testPutCourse_whenCourseExists_shouldUpdateAndReturnSC200() throws Exception {
    String courseName = "Physics";
    String courseNewName = "Physics v1";

    Course course = new Course();
    course.setName(courseName);
    course.setAvailable(true);

    Course saved = courseRepository.save(course);

    UpdateCourseRequestResource courseUpdate = new UpdateCourseRequestResource();
    courseUpdate.setName(courseNewName);
    courseUpdate.setAvailable(false);

    RequestBuilder request =
        MockMvcRequestBuilders
            .put("/courses/{id}", saved.getId())
            .content(GSON.toJson(courseUpdate))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

    MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
    assertThat(response.getStatus()).isEqualTo(200);

    CourseResource fromResponse = GSON.fromJson(response.getContentAsString(), CourseResource.class);
    assertThat(fromResponse.getName()).isEqualTo(courseNewName);
    assertThat(fromResponse.isAvailable()).isEqualTo(false);
    assertThat(fromResponse.getId()).isEqualTo(saved.getId());
  }

  @Test
  void testPutCourse_whenCourseDoesNotExist_shouldSendMessageAndReturnSC404() throws Exception {
    String courseName = "Physics";

    Course course = new Course();
    course.setName(courseName);
    course.setAvailable(true);

    Course saved = courseRepository.save(course);

    UpdateCourseRequestResource update = new UpdateCourseRequestResource();
    update.setAvailable(true);
    update.setName("Other Course");

    RequestBuilder request =
        MockMvcRequestBuilders
            .put("/courses/{id}", saved.getId() + 1)
            .content(GSON.toJson(update))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

    MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
    assertThat(response.getStatus()).isEqualTo(404);
    assertThat(response.getErrorMessage()).isEqualTo(String.format("course with id %d not found", saved.getId() + 1));
  }

  @Test
  void testDeleteCourse_whenCourseExists_shouldDeleteAndReturnSC204() throws Exception {
    String courseName = "Physics";

    Course course = new Course();
    course.setName(courseName);
    course.setAvailable(true);

    Course saved = courseRepository.save(course);

    RequestBuilder request =
        MockMvcRequestBuilders
            .delete("/courses/{id}", saved.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

    MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
    assertThat(response.getStatus()).isEqualTo(204);
    assertThat(response.getContentLength()).isEqualTo(0);
  }

  @Test
  void testDeleteCourse_whenCourseDoesNotExist_shouldSendMessageAndReturnSC404() throws Exception {
    String courseName = "Physics";

    Course course = new Course();
    course.setName(courseName);
    course.setAvailable(true);

    Course saved = courseRepository.save(course);

    RequestBuilder request =
        MockMvcRequestBuilders
            .delete("/courses/{id}", saved.getId() + 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

    MockHttpServletResponse response = mvc.perform(request).andReturn().getResponse();
    assertThat(response.getStatus()).isEqualTo(404);
    assertThat(response.getErrorMessage()).isEqualTo(String.format("course with id %d not found", saved.getId() + 1L));
  }

}