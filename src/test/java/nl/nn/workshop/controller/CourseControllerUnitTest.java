package nl.nn.workshop.controller;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

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

}