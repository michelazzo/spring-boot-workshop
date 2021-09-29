package nl.nn.workshop.resource;

import java.time.LocalDate;
import java.util.Objects;

public class StudentResource {

  private long id;
  private String name;
  private LocalDate birthday;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

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
    StudentResource that = (StudentResource) o;
    return id == that.id && Objects.equals(name, that.name) && Objects.equals(birthday,
        that.birthday);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, birthday);
  }

}
