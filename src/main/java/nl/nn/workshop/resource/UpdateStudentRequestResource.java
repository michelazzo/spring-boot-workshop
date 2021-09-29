package nl.nn.workshop.resource;

import java.time.LocalDate;
import java.util.Objects;

public class UpdateStudentRequestResource {

  private String name;
  private LocalDate birthday;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public LocalDate getBirthday() {
    return birthday;
  }

  public void setBirthday(LocalDate birthday) {
    this.birthday = birthday;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UpdateStudentRequestResource that = (UpdateStudentRequestResource) o;
    return Objects.equals(name, that.name) && Objects.equals(birthday, that.birthday);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, birthday);
  }

}
