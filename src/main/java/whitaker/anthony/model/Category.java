package whitaker.anthony.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Category implements Comparable<Category> {

  @Indexed(unique = true)
  private final String name;
  @Id
  private String id;

  @PersistenceConstructor
  public Category(String name) {
    this.name = name;
  }

  @Override
  public int compareTo(Category o) {
    return this.name.compareTo(o.name);
  }

  @Override
  public String toString() {
    return name;
  }

  public String getName() {
    return name;
  }
}
