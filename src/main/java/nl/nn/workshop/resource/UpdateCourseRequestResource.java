package nl.nn.workshop.resource;

import java.util.Objects;

public class UpdateCourseRequestResource {

  private String name;
  private boolean available;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isAvailable() {
    return available;
  }

  public void setAvailable(boolean available) {
    this.available = available;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UpdateCourseRequestResource resource = (UpdateCourseRequestResource) o;
    return available == resource.available && Objects.equals(name, resource.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, available);
  }

}
