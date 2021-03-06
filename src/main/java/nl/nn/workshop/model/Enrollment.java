package nl.nn.workshop.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "enrollment")
@IdClass(EnrollmentPk.class)
public class Enrollment implements Serializable {

  @Id
  @Column(name = "student_id")
  private long studentId;

  @Id
  @Column(name = "course_id")
  private long courseId;

  @Column(name = "enrollment_date")
  private LocalDateTime enrollmentDate;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Enrollment that = (Enrollment) o;
    return studentId == that.studentId && courseId == that.courseId && Objects.equals(enrollmentDate,
        that.enrollmentDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(studentId, courseId, enrollmentDate);
  }

}
