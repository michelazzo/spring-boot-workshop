package nl.nn.workshop.model;

import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "enrollment")
public class Enrollment {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "student_id")
  private long studentId;

  @Column(name = "course_id")
  private long courseId;

  @Column(name = "enrollment_date")
  private LocalDateTime enrollmentDate;

  @Column(name = "active")
  private boolean active;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public long getStudentId() {
    return studentId;
  }

  public void setStudentId(long studentId) {
    this.studentId = studentId;
  }

  public long getCourseId() {
    return courseId;
  }

  public void setCourseId(long courseId) {
    this.courseId = courseId;
  }

  public LocalDateTime getEnrollmentDate() {
    return enrollmentDate;
  }

  public void setEnrollmentDate(LocalDateTime enrollmentDate) {
    this.enrollmentDate = enrollmentDate;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Enrollment that = (Enrollment) o;
    return id == that.id && studentId == that.studentId && courseId == that.courseId && active == that.active
        && Objects.equals(enrollmentDate, that.enrollmentDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, studentId, courseId, enrollmentDate, active);
  }

}
