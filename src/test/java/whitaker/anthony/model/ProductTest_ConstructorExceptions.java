package whitaker.anthony.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(value = Parameterized.class)
public class ProductTest_ConstructorExceptions {

  private Category category;
  private String expected;
  private LocalDate expirationDate;
  private String name;
  private String number;

  public ProductTest_ConstructorExceptions(String expected, String number, String name, Category category, LocalDate expirationDate) {
    this.expected = expected;
    this.number = number;
    this.name = name;
    this.category = category;
    this.expirationDate = expirationDate;
  }

  @Parameterized.Parameters(name = "{index}: testProduct_ConstructorExeptions({0})")
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][]{
      {"cannot be null.", null, "Lemons", Category.PRODUCE, LocalDate.of(2017, 10, 8)},
      {"cannot be null.", "08f7751c-c495-469d-b4ca-5b1f118c91a7", null, Category.PRODUCE, LocalDate.of(2017, 10, 8)},
      {"cannot be null.", "08f7751c-c495-469d-b4ca-5b1f118c91a7", "Lemons", null, LocalDate.of(2017, 10, 8)},
      {"cannot be null.", "08f7751c-c495-469d-b4ca-5b1f118c91a7", "Lemons", Category.PRODUCE, null}
    });
  }

  @Test
  public void testProduct_ConstructorExceptions() {
    try {
      new Product(number, name, category, expirationDate);
      fail();
    } catch(IllegalArgumentException e) {
      assertTrue(e.getMessage().endsWith(expected));
    }
  }
}
