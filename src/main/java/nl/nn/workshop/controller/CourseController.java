package nl.nn.workshop.controller;

import java.util.List;
import nl.nn.workshop.resource.CourseResource;
import nl.nn.workshop.resource.CreateCourseRequestResource;
import nl.nn.workshop.resource.UpdateCourseRequestResource;
import nl.nn.workshop.service.CourseService;
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
@RequestMapping("/courses")
public class CourseController {

  private final CourseService courseService;

  public CourseController(CourseService courseService) {
    this.courseService = courseService;
  }

  @PostMapping
  public ResponseEntity<CourseResource> createCourse(@RequestBody CreateCourseRequestResource course) {
    return ResponseEntity.ok(courseService.create(course));
  }

  @PutMapping(value = "/{id}")
  public ResponseEntity<CourseResource> updateCourse(
      @PathVariable(value = "id") long id,
      @RequestBody UpdateCourseRequestResource course) {
    return ResponseEntity.ok(courseService.update(id, course));
  }

  @GetMapping(value = "/{id}")
  public ResponseEntity<CourseResource> getCourseById(@PathVariable(value = "id") long id) {
    return ResponseEntity.ok(courseService.findById(id));
  }

  @GetMapping
  public ResponseEntity<List<CourseResource>> getAllCourses() {
    return ResponseEntity.ok(courseService.findAll());
  }

  @DeleteMapping(value = "/{id}")
  public ResponseEntity<Void> deleteCourseById(@PathVariable(value = "id") Long id) {
    courseService.delete(id);
    return ResponseEntity.noContent().build();
  }

}
