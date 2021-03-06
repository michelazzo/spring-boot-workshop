package nl.nn.workshop.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;

public class EnrollmentPk implements Serializable {

  @Column(name = "student_id")
  private long studentId;

  @Column(name = "course_id")
  private long courseId;

  public EnrollmentPk(long studentId, long courseId) {
    this.studentId = studentId;
    this.courseId = courseId;
  }

  public EnrollmentPk() {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EnrollmentPk that = (EnrollmentPk) o;
    return studentId == that.studentId && courseId == that.courseId;
  }

  @Override
  public int hashCode() {
    return Objects.hash(studentId, courseId);
  }

}
