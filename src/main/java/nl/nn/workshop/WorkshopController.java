package nl.nn.workshop;

import nl.nn.workshop.model.Course;
import nl.nn.workshop.model.Enrollment;
import nl.nn.workshop.model.EnrollmentPk;
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
    //FIXME throw not found exception when doesn't exist
    student.setId(id);
    return ResponseEntity.ok(studentRepository.save(student));
  }

  @GetMapping(value = "/students/{id}")
  public ResponseEntity<Student> getStudentById(@PathVariable(value = "id") Long id) {
    //FIXME throw not found exception when doesn't exist
    return ResponseEntity.ok(studentRepository.findById(id).get());
  }

  @GetMapping(value = "/students")
  public ResponseEntity<Iterable<Student>> getAllStudents() {
    return ResponseEntity.ok(studentRepository.findAll());
  }

  @DeleteMapping(value = "/students/{id}")
  public ResponseEntity<Void> deleteStudentById(@PathVariable(value = "id") Long id) {
    //FIXME throw not found exception when doesn't exist
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
    //FIXME throw not found exception when doesn't exist
    course.setId(id);
    return ResponseEntity.ok(courseRepository.save(course));
  }

  @GetMapping(value = "/courses/{id}")
  public ResponseEntity<Course> getCourseById(@PathVariable(value = "id") Long id) {
    //FIXME throw not found exception when doesn't exist
    return ResponseEntity.ok(courseRepository.findById(id).get());
  }

  @GetMapping(value = "/courses")
  public ResponseEntity<Iterable<Course>> getAllCourses() {
    return ResponseEntity.ok(courseRepository.findAll());
  }

  @DeleteMapping(value = "/courses/{id}")
  public ResponseEntity<Void> deleteCourseById(@PathVariable(value = "id") Long id) {
    //FIXME throw not found exception when doesn't exist
    courseRepository.deleteById(id);
    return ResponseEntity.ok().build();
  }


  // ENROLLMENTS
  @PostMapping(value = "/enrollments")
  public ResponseEntity<Enrollment> createEnrollment(@RequestBody Enrollment enrollment) {
    //FIXME throw not found exception when student or course doesn't exist
    return ResponseEntity.ok(enrollmentRepository.save(enrollment));
  }

  @PutMapping(value = "/enrollments")
  public ResponseEntity<Enrollment> updateEnrollment(@RequestBody Enrollment enrollment) {
    //FIXME throw not found exception when doesn't exist
    return ResponseEntity.ok(enrollmentRepository.save(enrollment));
  }

  @GetMapping(value = "/enrollments/student/{studentId}/course/{courseId}")
  public ResponseEntity<Enrollment> getEnrollmentById(
      @PathVariable(value = "studentId") Long studentId,
      @PathVariable(value = "courseId") Long courseId) {
    //FIXME throw not found exception when doesn't exist
    return ResponseEntity.ok(enrollmentRepository.findById(new EnrollmentPk(studentId, courseId)).get());
  }

  @GetMapping(value = "/enrollments")
  public ResponseEntity<Iterable<Enrollment>> getAllEnrollments() {
    return ResponseEntity.ok(enrollmentRepository.findAll());
  }

  @DeleteMapping(value = "/enrollments/student/{studentId}/course/{courseId}")
  public ResponseEntity<Void> deleteEnrollmentById(
      @PathVariable(value = "studentId") Long studentId,
      @PathVariable(value = "courseId") Long courseId) {
    //FIXME throw not found exception when doesn't exist
    enrollmentRepository.deleteById(new EnrollmentPk(studentId, courseId));
    return ResponseEntity.ok().build();
  }

}
