package nl.nn.workshop.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import ma.glasnost.orika.MapperFacade;
import nl.nn.workshop.model.Student;
import nl.nn.workshop.repository.StudentRepository;
import nl.nn.workshop.resource.CreateStudentRequestResource;
import nl.nn.workshop.resource.StudentResource;
import nl.nn.workshop.resource.UpdateStudentRequestResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class StudentService {

  private final StudentRepository studentRepository;
  private final MapperFacade mapperFacade;

  public StudentService(StudentRepository studentRepository, MapperFacade mapperFacade) {
    this.studentRepository = studentRepository;
    this.mapperFacade = mapperFacade;
  }

  public StudentResource create(CreateStudentRequestResource resource) {
    Student saved = studentRepository.save(mapperFacade.map(resource, Student.class));
    return mapperFacade.map(saved, StudentResource.class);
  }

  public StudentResource update(long id, UpdateStudentRequestResource resource) {
    return studentRepository.findById(id)
        .map(found -> {
          found.setName(resource.getName());
          found.setBirthday(resource.getBirthday());
          Student saved = studentRepository.save(found);
          return mapperFacade.map(saved, StudentResource.class);
        })
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND, String.format("student with id %d not found", id)));
  }

  public StudentResource findById(long id) {
    return studentRepository.findById(id)
        .map(found -> mapperFacade.map(found, StudentResource.class))
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND, String.format("student with id %d not found", id)));
  }

  public List<StudentResource> findAll() {
    return StreamSupport.stream(studentRepository.findAll().spliterator(), false)
        .map(student -> mapperFacade.map(student, StudentResource.class))
        .collect(Collectors.toList());
  }

  public void delete(long id) {
    if (!studentRepository.existsById(id)) {
      throw new ResponseStatusException(
          HttpStatus.NOT_FOUND, String.format("student with id %d not found", id));
    }
    studentRepository.deleteById(id);
  }

}
