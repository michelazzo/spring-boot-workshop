package nl.nn.workshop;

import nl.nn.workshop.model.Course;
import nl.nn.workshop.model.Enrollment;
import nl.nn.workshop.model.EnrollmentPk;
import nl.nn.workshop.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class WorkshopController {

  @Autowired
  private StudentRepository studentRepository;

  @Autowired
  private CourseRepository courseRepository;

  @Autowired
  private EnrollmentRepository enrollmentRepository;

  // STUDENTS
  @PostMapping(value = "/students")
  public ResponseEntity<Student> createStudent(@RequestBody Student student) {
    return ResponseEntity.ok(studentRepository.save(student));
  }

  @PutMapping(value = "/students/{id}")
  public ResponseEntity<Student> updateStudent(@PathVariable(value = "id") Long id, @RequestBody Student student) {
    Student found = studentRepository
        .findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    found.setName(student.getName());
    found.setBirthday(student.getBirthday());
    return ResponseEntity.ok(studentRepository.save(found));
  }

  @GetMapping(value = "/students/{id}")
  public ResponseEntity<Student> getStudentById(@PathVariable(value = "id") Long id) {
    return ResponseEntity.of(studentRepository.findById(id));
  }

  @GetMapping(value = "/students")
  public ResponseEntity<Iterable<Student>> getAllStudents() {
    return ResponseEntity.ok(studentRepository.findAll());
  }

  @DeleteMapping(value = "/students/{id}")
  public ResponseEntity<Void> deleteStudentById(@PathVariable(value = "id") Long id) {
    Student student = studentRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    studentRepository.delete(student);
    return ResponseEntity.ok().build();
  }

  // COURSES
  @PostMapping(value = "/courses")
  public ResponseEntity<Course> createCourse(@RequestBody Course course) {
    return ResponseEntity.ok(courseRepository.save(course));
  }

  @PutMapping(value = "/courses/{id}")
  public ResponseEntity<Course> updateCourse(@PathVariable(value = "id") Long id, @RequestBody Course course) {
    Course found = courseRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    found.setAvailable(course.isAvailable());
    found.setName(course.getName());
    return ResponseEntity.ok(courseRepository.save(found));
  }

  @GetMapping(value = "/courses/{id}")
  public ResponseEntity<Course> getCourseById(@PathVariable(value = "id") Long id) {
    return ResponseEntity.of(courseRepository.findById(id));
  }

  @GetMapping(value = "/courses")
  public ResponseEntity<Iterable<Course>> getAllCourses() {
    return ResponseEntity.ok(courseRepository.findAll());
  }

  @DeleteMapping(value = "/courses/{id}")
  public ResponseEntity<Void> deleteCourseById(@PathVariable(value = "id") Long id) {
    Course found = courseRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    courseRepository.delete(found);
    return ResponseEntity.ok().build();
  }

  // ENROLLMENTS
  @PostMapping(value = "/enrollments")
  public ResponseEntity<Enrollment> createEnrollment(@RequestBody Enrollment enrollment) {
    if (!studentRepository.existsById(enrollment.getStudentId())) {
      throw new ResponseStatusException(
          HttpStatus.NOT_FOUND, String.format("student with id %d not found", enrollment.getStudentId()));
    }
    if (!courseRepository.existsById(enrollment.getCourseId())) {
      throw new ResponseStatusException(
          HttpStatus.NOT_FOUND, String.format("course with id %d not found", enrollment.getCourseId()));
    }
    return ResponseEntity.ok(enrollmentRepository.save(enrollment));
  }

  @PutMapping(value = "/enrollments")
  public ResponseEntity<Enrollment> updateEnrollment(@RequestBody Enrollment enrollment) {
    Enrollment found = enrollmentRepository
        .findById(new EnrollmentPk(enrollment.getStudentId(), enrollment.getCourseId()))
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
            String.format("enrollment for user with id %d and course with id %d not found",
            enrollment.getStudentId(),
            enrollment.getCourseId())));
    return ResponseEntity.ok(enrollmentRepository.save(found));
  }

  @GetMapping(value = "/enrollments/student/{studentId}/course/{courseId}")
  public ResponseEntity<Enrollment> getEnrollmentById(
      @PathVariable(value = "studentId") Long studentId,
      @PathVariable(value = "courseId") Long courseId) {
    Enrollment found = enrollmentRepository
        .findById(new EnrollmentPk(studentId, courseId))
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
            String.format("enrollment for user with id %d and course with id %d not found", studentId, courseId)));
    return ResponseEntity.ok(found);
  }

  @GetMapping(value = "/enrollments")
  public ResponseEntity<Iterable<Enrollment>> getAllEnrollments() {
    return ResponseEntity.ok(enrollmentRepository.findAll());
  }

  @DeleteMapping(value = "/enrollments/student/{studentId}/course/{courseId}")
  public ResponseEntity<Void> deleteEnrollmentById(
      @PathVariable(value = "studentId") Long studentId,
      @PathVariable(value = "courseId") Long courseId) {
    Enrollment found = enrollmentRepository
        .findById(new EnrollmentPk(studentId, courseId))
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
            String.format("enrollment for user with id %d and course with id %d not found", studentId, courseId)));
    enrollmentRepository.delete(found);
    return ResponseEntity.ok().build();
  }

}
