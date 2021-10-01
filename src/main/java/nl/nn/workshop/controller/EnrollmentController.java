package nl.nn.workshop.controller;

import nl.nn.workshop.resource.EnrollmentResource;
import nl.nn.workshop.service.EnrollmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/enrollments")
public class EnrollmentController {

  private final EnrollmentService enrollmentService;

  public EnrollmentController(EnrollmentService enrollmentService) {
    this.enrollmentService = enrollmentService;
  }

  @PostMapping(value = "/student/{studentId}/course/{courseId}")
  public ResponseEntity<EnrollmentResource> createEnrollment(
      @PathVariable(value = "studentId") Long studentId,
      @PathVariable(value = "courseId") Long courseId) {
    return ResponseEntity.ok(enrollmentService.create(studentId, courseId));
  }

  @GetMapping(value = "/student/{studentId}/course/{courseId}")
  public ResponseEntity<EnrollmentResource> getEnrollmentById(
      @PathVariable(value = "studentId") Long studentId,
      @PathVariable(value = "courseId") Long courseId) {
    return ResponseEntity.ok(enrollmentService.findById(studentId, courseId));
  }

  @GetMapping
  public ResponseEntity<Iterable<EnrollmentResource>> getAllEnrollments() {
    return ResponseEntity.ok(enrollmentService.findAll());
  }

  @DeleteMapping(value = "/student/{studentId}/course/{courseId}")
  public ResponseEntity<Void> deleteEnrollmentById(
      @PathVariable(value = "studentId") Long studentId,
      @PathVariable(value = "courseId") Long courseId) {
    enrollmentService.delete(studentId, courseId);
    return ResponseEntity.ok().build();
  }

}
