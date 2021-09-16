package nl.nn.workshop;

import nl.nn.workshop.model.Course;
import nl.nn.workshop.model.Enrollment;
import nl.nn.workshop.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
    student.setId(id);
    return ResponseEntity.ok(studentRepository.save(student));
  }

  @GetMapping(value = "/students/{id}")
  public ResponseEntity<Student> getStudentById(@PathVariable(value = "id") Long id) {
    return ResponseEntity.ok(studentRepository.findById(id).get());
  }

  @GetMapping(value = "/students")
  public ResponseEntity<Iterable<Student>> getAllStudents() {
    return ResponseEntity.ok(studentRepository.findAll());
  }

  @DeleteMapping(value = "/students/{id}")
  public ResponseEntity<Void> deleteStudentById(@PathVariable(value = "id") Long id) {
    studentRepository.deleteById(id);
    return ResponseEntity.ok().build();
  }

  // COURSES
  @PostMapping(value = "/courses")
  public ResponseEntity<Course> createCourse(@RequestBody Course course) {
    return ResponseEntity.ok(courseRepository.save(course));
  }

  @PutMapping(value = "/courses/{id}")
  public ResponseEntity<Course> updateCourse(@PathVariable(value = "id") Long id, @RequestBody Course course) {
    course.setId(id);
    return ResponseEntity.ok(courseRepository.save(course));
  }

  @GetMapping(value = "/courses/{id}")
  public ResponseEntity<Course> getCourseById(@PathVariable(value = "id") Long id) {
    return ResponseEntity.ok(courseRepository.findById(id).get());
  }

  @GetMapping(value = "/courses")
  public ResponseEntity<Iterable<Course>> getAllCourses() {
    return ResponseEntity.ok(courseRepository.findAll());
  }

  @DeleteMapping(value = "/courses/{id}")
  public ResponseEntity<Void> deleteCourseById(@PathVariable(value = "id") Long id) {
    courseRepository.deleteById(id);
    return ResponseEntity.ok().build();
  }


  // ENROLLMENTS
  @PostMapping(value = "/enrollments")
  public ResponseEntity<Enrollment> createEnrollment(@RequestBody Enrollment enrollment) {
    return ResponseEntity.ok(enrollmentRepository.save(enrollment));
  }

  @PutMapping(value = "/enrollments/{id}")
  public ResponseEntity<Enrollment> updateEnrollment(@PathVariable(value = "id") Long id, @RequestBody Enrollment enrollment) {
    enrollment.setId(id);
    return ResponseEntity.ok(enrollmentRepository.save(enrollment));
  }

  @GetMapping(value = "/enrollments/{id}")
  public ResponseEntity<Enrollment> getEnrollmentById(@PathVariable(value = "id") Long id) {
    return ResponseEntity.ok(enrollmentRepository.findById(id).get());
  }

  @GetMapping(value = "/enrollments")
  public ResponseEntity<Iterable<Enrollment>> getAllEnrollments() {
    return ResponseEntity.ok(enrollmentRepository.findAll());
  }

  @DeleteMapping(value = "/enrollments/{id}")
  public ResponseEntity<Void> deleteEnrollmentById(@PathVariable(value = "id") Long id) {
    enrollmentRepository.deleteById(id);
    return ResponseEntity.ok().build();
  }

}
