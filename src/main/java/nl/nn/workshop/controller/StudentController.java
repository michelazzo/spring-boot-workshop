package nl.nn.workshop.controller;

import nl.nn.workshop.model.Student;
import nl.nn.workshop.repository.StudentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/students")
public class StudentController {

  private final StudentRepository studentRepository;

  public StudentController(StudentRepository studentRepository) {
    this.studentRepository = studentRepository;
  }

  @PostMapping
  public ResponseEntity<Student> createStudent(@RequestBody Student student) {
    return ResponseEntity.ok(studentRepository.save(student));
  }

  @PutMapping(value = "/{id}")
  public ResponseEntity<Student> updateStudent(@PathVariable(value = "id") Long id, @RequestBody Student student) {
    Student found = studentRepository
        .findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    found.setName(student.getName());
    found.setBirthday(student.getBirthday());
    return ResponseEntity.ok(studentRepository.save(found));
  }

  @GetMapping(value = "/{id}")
  public ResponseEntity<Student> getStudentById(@PathVariable(value = "id") Long id) {
    return ResponseEntity.of(studentRepository.findById(id));
  }

  @GetMapping
  public ResponseEntity<Iterable<Student>> getAllStudents() {
    return ResponseEntity.ok(studentRepository.findAll());
  }

  @DeleteMapping(value = "/{id}")
  public ResponseEntity<Void> deleteStudentById(@PathVariable(value = "id") Long id) {
    Student student = studentRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    studentRepository.delete(student);
    return ResponseEntity.ok().build();
  }

}
