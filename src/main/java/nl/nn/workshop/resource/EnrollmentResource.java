package nl.nn.workshop.resource;

import java.time.LocalDateTime;
import java.util.Objects;

public class EnrollmentResource {

  private long studentId;
  private long courseId;
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
    EnrollmentResource that = (EnrollmentResource) o;
    return studentId == that.studentId && courseId == that.courseId && Objects.equals(enrollmentDate,
        that.enrollmentDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(studentId, courseId, enrollmentDate);
  }

}
