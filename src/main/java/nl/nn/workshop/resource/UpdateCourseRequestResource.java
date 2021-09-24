package nl.nn.workshop.resource;

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

}
