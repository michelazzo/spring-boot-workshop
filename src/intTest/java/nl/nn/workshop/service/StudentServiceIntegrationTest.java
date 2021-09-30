package nl.nn.workshop.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

import java.time.LocalDate;
import nl.nn.workshop.AbstractIntegrationTest;
import nl.nn.workshop.model.Student;
import nl.nn.workshop.repository.StudentRepository;
import nl.nn.workshop.resource.CreateStudentRequestResource;
import nl.nn.workshop.resource.StudentResource;
import nl.nn.workshop.resource.UpdateStudentRequestResource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class StudentServiceIntegrationTest extends AbstractIntegrationTest {

  @Autowired
  private StudentRepository studentRepository;

  @Autowired
  private StudentService studentService;

  @Test
  void testCreateStudent_whenStudentInfoIsProvided_shouldCreate() {
    LocalDate birthday = LocalDate.of(1643, 1, 4);
    String studentName = "Isaac Newton";

    CreateStudentRequestResource student = new CreateStudentRequestResource();
    student.setBirthday(birthday);
    student.setName(studentName);

    StudentResource saved = studentService.create(student);

    assertThat(saved.getId()).isNotNull();
    assertThat(saved.getBirthday()).isEqualTo(birthday);
    assertThat(saved.getName()).isEqualTo(studentName);
  }

  @Test
  void testFindByIdStudent_whenStudentExists_shouldFind() {
    LocalDate birthday = LocalDate.of(1643, 1, 4);
    String studentName = "Isaac Newton";

    Student student = new Student();
    student.setBirthday(birthday);
    student.setName(studentName);

    Student saved = studentRepository.save(student);

    StudentResource found = studentService.findById(saved.getId());

    assertThat(found.getName()).isEqualTo(studentName);
    assertThat(found.getBirthday()).isEqualTo(birthday);
    assertThat(found.getId()).isEqualTo(saved.getId());
  }

  @Test
  void testFindByIdStudent_whenStudentDoesNotExist_shouldThrowsNotFoundException() {
    LocalDate birthday = LocalDate.of(1643, 1, 4);
    String studentName = "Isaac Newton";

    Student student = new Student();
    student.setBirthday(birthday);
    student.setName(studentName);

    Student saved = studentRepository.save(student);

    ResponseStatusException exception = catchThrowableOfType(
        () -> studentService.findById(saved.getId() + 1), ResponseStatusException.class);
    assertThat(exception).hasMessage(String.format("404 NOT_FOUND \"student with id %d not found\"", saved.getId() + 1));
    assertThat(exception.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void testUpdateStudent_whenStudentExists_shouldUpdate() {
    LocalDate birthday = LocalDate.of(1643, 2, 4);
    String name = "Isaac Newton";

    Student student = new Student();
    student.setBirthday(birthday);
    student.setName(name);

    Student saved = studentRepository.save(student);

    String updatedName = "Sir Isaac Newton";
    LocalDate updatedBirthday = LocalDate.of(1643, 1, 4);

    UpdateStudentRequestResource studentUpdate = new UpdateStudentRequestResource();
    studentUpdate.setName(updatedName);
    studentUpdate.setBirthday(updatedBirthday);

    StudentResource updated = studentService.update(saved.getId(), studentUpdate);

    assertThat(updated.getName()).isEqualTo(updatedName);
    assertThat(updated.getBirthday()).isEqualTo(updatedBirthday);
    assertThat(updated.getId()).isEqualTo(saved.getId());
  }

  @Test
  void testUpdateStudent_whenStudentDoesNotExist_shouldThrowsNotFoundException() {
    LocalDate birthday = LocalDate.of(1643, 2, 4);
    String name = "Isaac Newton";

    Student student = new Student();
    student.setBirthday(birthday);
    student.setName(name);

    Student saved = studentRepository.save(student);

    String updatedName = "Sir Isaac Newton";
    LocalDate updatedBirthday = LocalDate.of(1643, 1, 4);

    UpdateStudentRequestResource studentUpdate = new UpdateStudentRequestResource();
    studentUpdate.setName(updatedName);
    studentUpdate.setBirthday(updatedBirthday);

    ResponseStatusException exception = catchThrowableOfType(
        () -> studentService.update(saved.getId() + 1, studentUpdate), ResponseStatusException.class);
    assertThat(exception).hasMessage(String.format("404 NOT_FOUND \"student with id %d not found\"", saved.getId() + 1));
    assertThat(exception.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void testDeleteStudent_whenStudentExists_shouldDelete() {
    LocalDate birthday = LocalDate.of(1643, 1, 4);
    String studentName = "Isaac Newton";

    Student student = new Student();
    student.setBirthday(birthday);
    student.setName(studentName);

    Student saved = studentRepository.save(student);

    assertThat(studentRepository.existsById(saved.getId())).isTrue();

    studentService.delete(saved.getId());

    assertThat(studentRepository.existsById(saved.getId())).isFalse();
  }

  @Test
  void testDeleteStudent_whenStudentDoesNotExist_shouldThrowsNotFoundException() {
    LocalDate birthday = LocalDate.of(1643, 1, 4);
    String studentName = "Isaac Newton";

    Student student = new Student();
    student.setBirthday(birthday);
    student.setName(studentName);

    Student saved = studentRepository.save(student);

    assertThat(studentRepository.existsById(saved.getId())).isTrue();

    ResponseStatusException exception = catchThrowableOfType(
        () -> studentService.delete(saved.getId() + 1), ResponseStatusException.class);
    assertThat(exception).hasMessage(String.format("404 NOT_FOUND \"student with id %d not found\"", saved.getId() + 1));
    assertThat(exception.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
  }

}
