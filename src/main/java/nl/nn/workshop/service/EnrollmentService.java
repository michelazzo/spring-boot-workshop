package nl.nn.workshop.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import ma.glasnost.orika.MapperFacade;
import nl.nn.workshop.model.Enrollment;
import nl.nn.workshop.model.EnrollmentPk;
import nl.nn.workshop.repository.CourseRepository;
import nl.nn.workshop.repository.EnrollmentRepository;
import nl.nn.workshop.repository.StudentRepository;
import nl.nn.workshop.resource.EnrollmentResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class EnrollmentService {

  private final EnrollmentRepository enrollmentRepository;
  private final StudentRepository studentRepository;
  private final CourseRepository courseRepository;
  private final MapperFacade mapperFacade;

  public EnrollmentService(
      EnrollmentRepository enrollmentRepository,
      StudentRepository studentRepository,
      CourseRepository courseRepository,
      MapperFacade mapperFacade) {
    this.enrollmentRepository = enrollmentRepository;
    this.studentRepository = studentRepository;
    this.courseRepository = courseRepository;
    this.mapperFacade = mapperFacade;
  }

  public EnrollmentResource create(long studentId, long courseId) {
    if (!studentRepository.existsById(studentId)) {
      throw new ResponseStatusException(
          HttpStatus.NOT_FOUND, String.format("student with id %d not found", studentId));
    }
    if (!courseRepository.existsById(courseId)) {
      throw new ResponseStatusException(
          HttpStatus.NOT_FOUND, String.format("course with id %d not found", courseId));
    }
    Enrollment enrollment = new Enrollment();
    enrollment.setEnrollmentDate(LocalDateTime.now());
    enrollment.setStudentId(studentId);
    enrollment.setCourseId(courseId);
    return mapperFacade.map(enrollmentRepository.save(enrollment), EnrollmentResource.class);
  }

  public EnrollmentResource findById(long studentId, long courseId) {
    return enrollmentRepository
        .findById(new EnrollmentPk(studentId, courseId))
        .map(found -> mapperFacade.map(found, EnrollmentResource.class))
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
            String.format("enrollment for user with id %d and course with id %d not found", studentId, courseId)));
  }

  public List<EnrollmentResource> findAll() {
    return StreamSupport.stream(enrollmentRepository.findAll().spliterator(), false)
        .map(course -> mapperFacade.map(course, EnrollmentResource.class))
        .collect(Collectors.toList());
  }

  public void delete(long studentId, long courseId) {
    Enrollment found = enrollmentRepository
        .findById(new EnrollmentPk(studentId, courseId))
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
            String.format("enrollment for user with id %d and course with id %d not found", studentId, courseId)));
    enrollmentRepository.delete(found);
  }

}
