package whitaker.anthony.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(value = Parameterized.class)
public class ProductTest_ParseProductExceptions {

  private String delimiter;
  private String expected;
  private String expirationDateFormat;
  private Locale expirationDateLocale;
  private String productString;

  public ProductTest_ParseProductExceptions(String expected, String productString, String delimiter, String expirationDateFormat, Locale expirationDateLocale) {
    this.expected = expected;
    this.productString = productString;
    this.delimiter = delimiter;
    this.expirationDateFormat = expirationDateFormat;
    this.expirationDateLocale = expirationDateLocale;
  }

  @Parameterized.Parameters(name = "{index}: testParseProduct_testExceptions({0})")
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][]{
      {"cannot be null.", null, ";", "yyyy-MM-dd", Locale.US},
      {"cannot be null.", "08f7751c-c495-469d-b4ca-5b1f118c91a7;Lemons;Produce;2017-10-08", null, "yyyy-MM-dd", Locale.US},
      {"cannot be null.", "08f7751c-c495-469d-b4ca-5b1f118c91a7;Lemons;Produce;2017-10-08", ";", null, Locale.US},
      {"cannot be null.", "08f7751c-c495-469d-b4ca-5b1f118c91a7;Lemons;Produce;2017-10-08", ";", "yyyy-MM-dd", null},

      {"Unable to parse", "08f7751c-c495-469d-b4ca-5b1f118c91a7;Produce;2017-10-08", ";", "yyyy-MM-dd", Locale.US},
      {"Unable to parse", "08f7751c-c495-469d-b4ca-5b1f118c91a7;Lemons;Lemons;Produce;2017-10-08", ";", "yyyy-MM-dd", Locale.US},
      {"Unable to parse", "08f7751c-c495-469d-b4ca-5b1f118c91a7|Lemons|Produce|2017-10-08", "|", "yyyy-MM-dd", Locale.US},

      {"Unknown pattern", "08f7751c-c495-469d-b4ca-5b1f118c91a7;Lemons;Produce;2017-10-08", ";", "bbbb-MM-dd", Locale.US},
      {"could not be parsed at index 8", "08f7751c-c495-469d-b4ca-5b1f118c91a7;Lemons;Produce;2017-10-", ";", "yyyy-MM-dd", Locale.US},
      {"could not be parsed at index 0", "08f7751c-c495-469d-b4ca-5b1f118c91a7;Lemons;Produce;201-10-08", ";", "yyyy-MM-dd", Locale.US}
    });
  }

  @Test
  public void testParseProduct_testExceptions() {
    try {
      Product.parseProduct(productString, delimiter, expirationDateFormat, expirationDateLocale);
      fail();
    } catch(IllegalArgumentException e) {
      assertTrue(e.getMessage().startsWith(expected) || e.getMessage().endsWith(expected));
    } catch(java.time.format.DateTimeParseException e) {
      assertTrue(e.getMessage().endsWith(expected));
    }
  }

}
