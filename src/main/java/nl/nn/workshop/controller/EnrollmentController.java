package nl.nn.workshop.controller;

import nl.nn.workshop.model.Enrollment;
import nl.nn.workshop.model.EnrollmentPk;
import nl.nn.workshop.repository.CourseRepository;
import nl.nn.workshop.repository.EnrollmentRepository;
import nl.nn.workshop.repository.StudentRepository;
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
public class EnrollmentController {

  private final StudentRepository studentRepository;
  private final CourseRepository courseRepository;
  private final EnrollmentRepository enrollmentRepository;

  public EnrollmentController(
      StudentRepository studentRepository,
      CourseRepository courseRepository,
      EnrollmentRepository enrollmentRepository) {
    this.studentRepository = studentRepository;
    this.courseRepository = courseRepository;
    this.enrollmentRepository = enrollmentRepository;
  }

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
