package nl.nn.workshop.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

import nl.nn.workshop.AbstractIntegrationTest;
import nl.nn.workshop.model.Course;
import nl.nn.workshop.repository.CourseRepository;
import nl.nn.workshop.resource.CourseResource;
import nl.nn.workshop.resource.CreateCourseRequestResource;
import nl.nn.workshop.resource.UpdateCourseRequestResource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CourseServiceIntegrationTest extends AbstractIntegrationTest {

  @Autowired
  private CourseRepository courseRepository;

  @Autowired
  private CourseService courseService;

  @Test
  void testCreateCourse_whenCourseInfoIsProvided_shouldCreate() {
    String courseName = "Physics";

    CreateCourseRequestResource resource = new CreateCourseRequestResource();
    resource.setName(courseName);
    resource.setAvailable(true);

    CourseResource created = courseService.create(resource);

    assertThat(created.getId()).isNotNull();
    assertThat(created.getName()).isEqualTo(courseName);
    assertThat(created.isAvailable()).isEqualTo(true);
  }

  @Test
  void testFindCourseById_whenCourseExists_shouldFind() {
    String courseName = "Physics";

    CreateCourseRequestResource course = new CreateCourseRequestResource();
    course.setName(courseName);
    course.setAvailable(true);

    CourseResource saved = courseService.create(course);

    CourseResource found = courseService.findById(saved.getId());

    assertThat(found.getId()).isEqualTo(saved.getId());
    assertThat(found.getName()).isEqualTo(courseName);
    assertThat(found.isAvailable()).isEqualTo(true);
  }

  @Test
  void testFindCourseById_whenCourseDoesNotExist_shouldThrowsNotFoundException() {
    String courseName = "Physics";

    Course course = new Course();
    course.setName(courseName);
    course.setAvailable(true);

    Course saved = courseRepository.save(course);

    ResponseStatusException exception = catchThrowableOfType(
        () -> courseService.findById(saved.getId() + 1), ResponseStatusException.class);
    assertThat(exception).hasMessage(String.format("404 NOT_FOUND \"course with id %d not found\"", saved.getId() + 1));
    assertThat(exception.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void testUpdateCourse_whenCourseExists_shouldUpdate() {
    String courseName = "Physics";
    String courseNewName = "Physics v1";

    Course course = new Course();
    course.setName(courseName);
    course.setAvailable(true);

    Course saved = courseRepository.save(course);

    UpdateCourseRequestResource courseUpdate = new UpdateCourseRequestResource();
    courseUpdate.setName(courseNewName);
    courseUpdate.setAvailable(false);

    CourseResource updated = courseService.update(saved.getId(), courseUpdate);

    assertThat(updated.getName()).isEqualTo(courseNewName);
    assertThat(updated.isAvailable()).isEqualTo(false);
    assertThat(updated.getId()).isEqualTo(saved.getId());
  }

  @Test
  void testUpdateCourse_whenCourseDoesNotExist_shouldThrowsNotFoundException() {
    String courseName = "Physics";

    Course course = new Course();
    course.setName(courseName);
    course.setAvailable(true);

    Course saved = courseRepository.save(course);

    UpdateCourseRequestResource update = new UpdateCourseRequestResource();
    update.setAvailable(true);
    update.setName("Other Course");

    ResponseStatusException exception = catchThrowableOfType(
        () -> courseService.update(saved.getId() + 1, update), ResponseStatusException.class);
    assertThat(exception).hasMessage(String.format("404 NOT_FOUND \"course with id %d not found\"", saved.getId() + 1));
    assertThat(exception.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void testDeleteCourse_whenCourseExists_shouldDelete() {
    String courseName = "Physics";

    Course course = new Course();
    course.setName(courseName);
    course.setAvailable(true);

    Course saved = courseRepository.save(course);

    assertThat(courseRepository.existsById(saved.getId())).isTrue();

    courseService.delete(saved.getId());

    assertThat(courseRepository.existsById(saved.getId())).isFalse();
  }

  @Test
  void testDeleteCourse_whenCourseDoesNotExist_shouldThrowsNotFoundException() {
    String courseName = "Physics";

    Course course = new Course();
    course.setName(courseName);
    course.setAvailable(true);

    Course saved = courseRepository.save(course);

    assertThat(courseRepository.existsById(saved.getId())).isTrue();

    ResponseStatusException exception = catchThrowableOfType(
        () -> courseService.delete(saved.getId() + 1), ResponseStatusException.class);
    assertThat(exception).hasMessage(String.format("404 NOT_FOUND \"course with id %d not found\"", saved.getId() + 1));
    assertThat(exception.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
  }

}