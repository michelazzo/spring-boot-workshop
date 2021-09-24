package nl.nn.workshop.resource;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Enrollment implements Serializable {

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

}
