package nl.nn.workshop.controller;

import java.util.List;
import nl.nn.workshop.resource.CreateStudentRequestResource;
import nl.nn.workshop.resource.CreateStudentResponseResource;
import nl.nn.workshop.resource.StudentResource;
import nl.nn.workshop.resource.UpdateStudentRequestResource;
import nl.nn.workshop.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/students")
public class StudentController {

  private final StudentService studentService;

  public StudentController(StudentService studentService) {
    this.studentService = studentService;
  }

  @PostMapping
  public ResponseEntity<CreateStudentResponseResource> createStudent(@RequestBody CreateStudentRequestResource student) {
    return ResponseEntity.ok(studentService.create(student));
  }

  @PutMapping(value = "/{id}")
  public ResponseEntity<StudentResource> updateStudent(
      @PathVariable(value = "id") long id,
      @RequestBody UpdateStudentRequestResource student) {
    return ResponseEntity.ok(studentService.update(id, student));
  }

  @GetMapping(value = "/{id}")
  public ResponseEntity<StudentResource> getStudentById(@PathVariable(value = "id") long id) {
    return ResponseEntity.ok(studentService.findById(id));
  }

  @GetMapping
  public ResponseEntity<List<StudentResource>> getAllStudents() {
    return ResponseEntity.ok(studentService.findAll());
  }

  @DeleteMapping(value = "/{id}")
  public ResponseEntity<Void> deleteStudentById(@PathVariable(value = "id") long id) {
    studentService.delete(id);
    return ResponseEntity.noContent().build();
  }

}
