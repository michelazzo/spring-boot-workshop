package nl.nn.workshop.controller;

import nl.nn.workshop.model.Course;
import nl.nn.workshop.repository.CourseRepository;
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
@RequestMapping("/courses")
public class CourseController {

  private final CourseRepository courseRepository;

  public CourseController(CourseRepository courseRepository) {
    this.courseRepository = courseRepository;
  }

  @PostMapping
  public ResponseEntity<Course> createCourse(@RequestBody Course course) {
    return ResponseEntity.ok(courseRepository.save(course));
  }

  @PutMapping(value = "/{id}")
  public ResponseEntity<Course> updateCourse(@PathVariable(value = "id") Long id, @RequestBody Course course) {
    Course found = courseRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    found.setAvailable(course.isAvailable());
    found.setName(course.getName());
    return ResponseEntity.ok(courseRepository.save(found));
  }

  @GetMapping(value = "/{id}")
  public ResponseEntity<Course> getCourseById(@PathVariable(value = "id") Long id) {
    return ResponseEntity.of(courseRepository.findById(id));
  }

  @GetMapping
  public ResponseEntity<Iterable<Course>> getAllCourses() {
    return ResponseEntity.ok(courseRepository.findAll());
  }

  @DeleteMapping(value = "/{id}")
  public ResponseEntity<Void> deleteCourseById(@PathVariable(value = "id") Long id) {
    Course found = courseRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    courseRepository.delete(found);
    return ResponseEntity.ok().build();
  }

}
