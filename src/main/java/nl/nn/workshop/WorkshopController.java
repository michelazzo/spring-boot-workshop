package nl.nn.workshop;

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

  @PostMapping(value = "/students")
  public ResponseEntity<Student> create(@RequestBody Student student) {
    return ResponseEntity.ok(studentRepository.save(student));
  }

  @PutMapping(value = "/students/{id}")
  public ResponseEntity<Student> update(
      @PathVariable(value = "id") Long id,
      @RequestBody Student student) {
    student.setId(id);
    return ResponseEntity.ok(studentRepository.save(student));
  }

  @GetMapping(value = "/students/{id}")
  public ResponseEntity<Student> getById(@PathVariable(value = "id") Long id) {
    return ResponseEntity.ok(studentRepository.findById(id).get());
  }

  @GetMapping(value = "/students")
  public ResponseEntity<Iterable<Student>> getAllStudents() {
    return ResponseEntity.ok(studentRepository.findAll());
  }

  @DeleteMapping(value = "/students/{id}")
  public ResponseEntity<Void> deleteById(@PathVariable(value = "id") Long id) {
    studentRepository.deleteById(id);
    return ResponseEntity.ok().build();
  }

}
