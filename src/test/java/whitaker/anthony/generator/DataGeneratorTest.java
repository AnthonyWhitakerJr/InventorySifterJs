package whitaker.anthony.generator;

import org.junit.Before;
import org.junit.Test;
import whitaker.anthony.model.Product;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.*;
import static whitaker.anthony.generator.DataGenerator.DEFAULT_EXPIRATION_DATE_FORMAT;
import static whitaker.anthony.generator.DataGenerator.DEFAULT_EXPIRATION_DATE_LOCALE;

public class DataGeneratorTest {

  public static final String DELIMITER = ";";

  private DataGenerator generator;

  @Before
  public void setup() {
    generator = new DataGenerator("src/main/resources/ProductCandidates.txt", DELIMITER, null, null);
  }

  @Test
  public void testExpirationDateFormat_SanityCheck() {
    generator.setExpirationDateFormat("yyyy-MMM-dd");
    assertEquals("yyyy-MMM-dd", generator.getExpirationDateFormat());
  }

  @Test
  public void testExpirationDateLocale_SanityCheck() {
    generator.setExpirationDateLocale(Locale.CANADA);
    assertEquals(Locale.CANADA, generator.getExpirationDateLocale());
  }

  @Test
  public void testGenerateDataSet() {
    int numberOfProductsInSet = 25;
    Collection<Product> dataset = generator.generateDataSet(numberOfProductsInSet, LocalDate.now(), LocalDate.now().plusYears(1));
    assertEquals(numberOfProductsInSet, dataset.size());
    assertFalse(dataset.contains(null));
  }

  @Test
  public void testGenerateDataSet_InvalidSize1() {
    try {
      generator.generateDataSet(0, LocalDate.now(), LocalDate.now().plusYears(1));
      fail();
    } catch(IllegalArgumentException e) {
      assertEquals("Invalid size.", e.getMessage());
    }
  }

  @Test
  public void testGenerateDataSet_InvalidSize2() {
    try {
      generator.generateDataSet(200, LocalDate.now(), LocalDate.now().plusYears(1));
      fail();
    } catch(IllegalArgumentException e) {
      assertEquals("Invalid size.", e.getMessage());
    }
  }

  @Test
  public void testWriteDataSetToFile() {
    int numberOfProductsInSet = 25;
    Collection<Product> dataset = generator.generateDataSet(numberOfProductsInSet, LocalDate.now(), LocalDate.now().plusYears(1));
    assertEquals(numberOfProductsInSet, dataset.size());
    generator.writeDataSetToFile(dataset, "DELETE_ME.txt", DELIMITER);
    assertTrue("File not created correctly.", Files.isRegularFile(Paths.get("DELETE_ME.txt")));

    List<Product> products = DataGenerator.parseProductsFromFile("DELETE_ME.txt", DELIMITER, DEFAULT_EXPIRATION_DATE_FORMAT, DEFAULT_EXPIRATION_DATE_LOCALE);
    assertEquals(25, products.size());
    assertFalse(products.contains(null));

    try {
      Files.deleteIfExists(Paths.get("DELETE_ME.txt"));
    } catch(IOException e) {
      e.printStackTrace(); // Unable to delete file. Not a terrible issue.
    }
  }

}
