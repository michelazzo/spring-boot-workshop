package nl.nn.workshop.resource;

import java.time.LocalDate;

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

}
