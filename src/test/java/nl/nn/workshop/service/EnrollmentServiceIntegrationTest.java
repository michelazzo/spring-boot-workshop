package nl.nn.workshop.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import nl.nn.workshop.AbstractIntegrationTest;
import nl.nn.workshop.model.Course;
import nl.nn.workshop.model.Enrollment;
import nl.nn.workshop.model.EnrollmentPk;
import nl.nn.workshop.model.Student;
import nl.nn.workshop.repository.CourseRepository;
import nl.nn.workshop.repository.EnrollmentRepository;
import nl.nn.workshop.repository.StudentRepository;
import nl.nn.workshop.resource.EnrollmentResource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class EnrollmentServiceIntegrationTest extends AbstractIntegrationTest {

  @Autowired
  private StudentRepository studentRepository;

  @Autowired
  private CourseRepository courseRepository;

  @Autowired
  private EnrollmentRepository enrollmentRepository;

  @Autowired
  private EnrollmentService enrollmentService;

  @Test
  public void testCreateEnrollment_whenStudentAndCourseExist_shouldCreate() {
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

    EnrollmentResource created = enrollmentService.create(savedStudent.getId(), savedCourse.getId());

    assertThat(created.getEnrollmentDate()).isNotNull();
    assertThat(created.getStudentId()).isEqualTo(savedStudent.getId());
    assertThat(created.getCourseId()).isEqualTo(savedCourse.getId());
  }

  @Test
  public void testCreateEnrollment_whenStudentDoesNotExist_shouldThrowsNotFoundException() {
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

    ResponseStatusException exception = catchThrowableOfType(
        () -> enrollmentService.create(savedStudent.getId() + 1L, savedCourse.getId()), ResponseStatusException.class);
    assertThat(exception).hasMessage(String.format("404 NOT_FOUND \"student with id %d not found\"", savedStudent.getId() + 1));
    assertThat(exception.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  public void testCreateEnrollment_whenCourseDoesNotExist_shouldThrowsNotFoundException() {
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

    ResponseStatusException exception = catchThrowableOfType(
        () -> enrollmentService.create(savedStudent.getId(), savedCourse.getId() + 1), ResponseStatusException.class);
    assertThat(exception).hasMessage(String.format("404 NOT_FOUND \"course with id %d not found\"", savedCourse.getId() + 1));
    assertThat(exception.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  public void testCreateEnrollment_whenEnrollmentAlreadyExists_shouldThrowsNotFoundException() {
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
    enrollment.setStudentId(savedStudent.getId());
    enrollment.setCourseId(savedCourse.getId());
    enrollment.setEnrollmentDate(LocalDateTime.now());
    enrollmentRepository.save(enrollment);

    ResponseStatusException exception = catchThrowableOfType(
        () -> enrollmentService.create(savedStudent.getId(), savedCourse.getId()), ResponseStatusException.class);
    assertThat(exception).hasMessage(
        String.format("409 CONFLICT \"student %d is already enrolled in the course %d\"", savedStudent.getId(), savedCourse.getId()));
    assertThat(exception.getStatus()).isEqualTo(HttpStatus.CONFLICT);
  }

  @Test
  void testFindEnrollmentById_whenEnrollmentExists_shouldFind() {
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

    EnrollmentResource found = enrollmentService.findById(savedStudent.getId(), savedCourse.getId());

    assertThat(found.getEnrollmentDate()).isNotNull();
    assertThat(found.getStudentId()).isEqualTo(savedStudent.getId());
    assertThat(found.getCourseId()).isEqualTo(savedCourse.getId());
  }

  @Test
  void testFindEnrollmentById_whenEnrollmentDoesNotExists_shouldThrowsNotFoundException() {
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

    ResponseStatusException exception = catchThrowableOfType(
        () -> enrollmentService.findById(savedStudent.getId() + 1, savedCourse.getId() + 1), ResponseStatusException.class);
    assertThat(exception).hasMessage(
        String.format("404 NOT_FOUND \"enrollment for user with id %d and course with id %d not found\"", savedStudent.getId() + 1, savedCourse.getId() + 1));
    assertThat(exception.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void testFindAllEnrollments_whenEnrollmentsExist_shouldFind() {
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

    List<EnrollmentResource> fromResponse = enrollmentService.findAll();
    assertThat(fromResponse.size()).isEqualTo(1);

    EnrollmentResource enrollmentResource = new EnrollmentResource();
    enrollmentResource.setEnrollmentDate(fromResponse.get(0).getEnrollmentDate()); // hacky
    enrollmentResource.setStudentId(savedStudent.getId());
    enrollmentResource.setCourseId(savedCourse.getId());
    assertThat(fromResponse).containsExactlyInAnyOrder(enrollmentResource);
  }

  @Test
  public void testDeleteEnrollment_whenStudentAndCourseExist_shouldDelete() {
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

    assertThat(enrollmentRepository.existsById(
        new EnrollmentPk(enrollment.getStudentId(), enrollment.getCourseId()))).isTrue();

    enrollmentService.delete(enrollment.getStudentId(), enrollment.getCourseId());

    assertThat(enrollmentRepository.existsById(
        new EnrollmentPk(enrollment.getStudentId(), enrollment.getCourseId()))).isFalse();
  }

  @Test
  public void testDeleteEnrollment_whenEnrollmentDoesNotExist_shouldThrowsNotFoundException() {
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

    ResponseStatusException exception = catchThrowableOfType(
        () -> enrollmentService.delete(savedStudent.getId() + 1, savedCourse.getId() + 1), ResponseStatusException.class);
    assertThat(exception).hasMessage(
        String.format("404 NOT_FOUND \"enrollment for user with id %d and course with id %d not found\"", savedStudent.getId() + 1, savedCourse.getId() + 1));
    assertThat(exception.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
  }

}
