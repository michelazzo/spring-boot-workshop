package nl.nn.workshop.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import ma.glasnost.orika.MapperFacade;
import nl.nn.workshop.model.Course;
import nl.nn.workshop.repository.CourseRepository;
import nl.nn.workshop.resource.CourseResource;
import nl.nn.workshop.resource.CreateCourseResponseResource;
import nl.nn.workshop.resource.UpdateCourseRequestResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CourseService {

  private final CourseRepository courseRepository;
  private final MapperFacade mapperFacade;

  public CourseService(CourseRepository courseRepository, MapperFacade mapperFacade) {
    this.courseRepository = courseRepository;
    this.mapperFacade = mapperFacade;
  }

  public CreateCourseResponseResource create(CreateCourseResponseResource resource) {
    Course saved = courseRepository.save(mapperFacade.map(resource, Course.class));
    return mapperFacade.map(saved, CreateCourseResponseResource.class);
  }

  public CourseResource update(long id, UpdateCourseRequestResource resource) {
    return courseRepository.findById(id)
        .map(found -> {
          found.setName(resource.getName());
          found.setAvailable(resource.isAvailable());
          Course saved = courseRepository.save(found);
          return mapperFacade.map(saved, CourseResource.class);
        })
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND, String.format("course with id %d not found", id)));
  }

  public CourseResource findById(long id) {
    return courseRepository.findById(id)
        .map(found -> mapperFacade.map(found, CourseResource.class))
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND, String.format("course with id %d not found", id)));
  }

  public List<CourseResource> findAll() {
    return StreamSupport.stream(courseRepository.findAll().spliterator(), false)
        .map(x -> mapperFacade.map(x, CourseResource.class))
        .collect(Collectors.toList());
  }

  public void delete(long id) {
    if (!courseRepository.existsById(id)) {
      throw new ResponseStatusException(
          HttpStatus.NOT_FOUND, String.format("course with id %d not found", id));
    }
    courseRepository.deleteById(id);
  }

}
