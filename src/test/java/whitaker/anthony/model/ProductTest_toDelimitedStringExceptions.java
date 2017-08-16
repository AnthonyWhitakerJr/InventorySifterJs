package whitaker.anthony.model;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(value = Parameterized.class)
public class ProductTest_toDelimitedStringExceptions {

  private String delimiter;
  private String expected;
  private String expirationDateFormat;
  private Locale expirationDateLocale;
  private Product product;

  public ProductTest_toDelimitedStringExceptions(String expected, String delimiter, String expirationDateFormat, Locale expirationDateLocale) {
    this.expected = expected;
    this.delimiter = delimiter;
    this.expirationDateFormat = expirationDateFormat;
    this.expirationDateLocale = expirationDateLocale;
  }

  @Parameterized.Parameters(name = "{index}: testToDelimitedString({0})")
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][]{
      {"cannot be null.", null, "yyyy-MM-dd", Locale.US},
      {"cannot be null.", ";", null, Locale.US},
      {"cannot be null.", ";", "yyyy-MM-dd", null},

      {"Unknown pattern", ";", "bbbb-MM-dd", Locale.US}
    });
  }

  @Before
  public void setup() {
    product = new Product("08f7751c-c495-469d-b4ca-5b1f118c91a7", "Lemons", Category.PRODUCE, LocalDate.of(2017, 10, 8));
  }


  @Test
  public void testToDelimitedString() {
    try {
      product.toDelimitedString(delimiter, expirationDateFormat, expirationDateLocale);
      fail();
    } catch(IllegalArgumentException e) {
      assertTrue(e.getMessage().startsWith(expected) || e.getMessage().endsWith(expected));
    }
  }
}
