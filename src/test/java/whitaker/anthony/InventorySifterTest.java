package whitaker.anthony;

import org.junit.Ignore;
import org.junit.Test;
import whitaker.anthony.generator.DataGenerator;
import whitaker.anthony.model.Category;
import whitaker.anthony.model.Product;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static whitaker.anthony.generator.DataGenerator.DEFAULT_EXPIRATION_DATE_FORMAT;
import static whitaker.anthony.generator.DataGenerator.DEFAULT_EXPIRATION_DATE_LOCALE;
import static whitaker.anthony.generator.DataGeneratorTest.DELIMITER;

public class InventorySifterTest {

  private static final String FILENAME_DATASET_1 = "src/test/resources/dataset1.txt";
  private static final String FILENAME_DATASET_2 = "src/test/resources/dataset2.txt";
  private static final String FILENAME_DATASET_3 = "src/test/resources/dataset3.txt";

  @Test
  @Ignore("MANUAL RUN ONLY. USE TO GENERATE DATA FOR TESTING.")
  public void generateTestData() {
    DataGenerator generator = new DataGenerator("src/main/resources/ProductCandidates.txt", DELIMITER, null, null);

    int numberOfProductsInSet = 25;
    Collection<Product> dataset = generator.generateDataSet(numberOfProductsInSet, LocalDate.now(), LocalDate.now().plusYears(1));
    assertEquals(numberOfProductsInSet, dataset.size());
    generator.writeDataSetToFile(dataset, FILENAME_DATASET_1, DELIMITER);
    assertTrue("File not created correctly.", Files.isRegularFile(Paths.get(FILENAME_DATASET_1)));

    numberOfProductsInSet = 25; // This line intentionally duplicated to allow for easy set modification.
    dataset = generator.generateDataSet(numberOfProductsInSet, LocalDate.now().plusMonths(2), LocalDate.now().plusMonths(14));
    assertEquals(numberOfProductsInSet, dataset.size());
    generator.writeDataSetToFile(dataset, FILENAME_DATASET_2, DELIMITER);
    assertTrue("File not created correctly.", Files.isRegularFile(Paths.get(FILENAME_DATASET_2)));

    numberOfProductsInSet = 25;
    dataset = generator.generateDataSet(numberOfProductsInSet, LocalDate.now().plusMonths(6), LocalDate.now().plusMonths(18));
    assertEquals(numberOfProductsInSet, dataset.size());
    generator.writeDataSetToFile(dataset, FILENAME_DATASET_3, DELIMITER);
    assertTrue("File not created correctly.", Files.isRegularFile(Paths.get(FILENAME_DATASET_3)));
  }

  @Test
  public void testInventorySifter() {
    List<Product> products = new ArrayList<>();

    // Add first data set.
    products.addAll(DataGenerator.parseProductsFromFile(FILENAME_DATASET_1, DELIMITER, DEFAULT_EXPIRATION_DATE_FORMAT, DEFAULT_EXPIRATION_DATE_LOCALE));
    assertEquals(25, products.size());

    // Return products sorted by name.
    System.out.println("--------------------------------PRODUCTS BY NAME--------------------------------");
    products.stream().sorted(Product.BY_NAME).forEach(System.out::println);

    // Add the next data set.
    products.addAll(DataGenerator.parseProductsFromFile(FILENAME_DATASET_2, DELIMITER, DEFAULT_EXPIRATION_DATE_FORMAT, DEFAULT_EXPIRATION_DATE_LOCALE));
    assertEquals(50, products.size());

    // Return products sorted by category.
    System.out.println("\n\n--------------------------------PRODUCTS BY CATEGORY--------------------------------");
    products.stream().sorted(Product.BY_CATEGORY).forEach(System.out::println);

    // Add the next data set.
    products.addAll(DataGenerator.parseProductsFromFile(FILENAME_DATASET_3, DELIMITER, DEFAULT_EXPIRATION_DATE_FORMAT, DEFAULT_EXPIRATION_DATE_LOCALE));
    assertEquals(75, products.size());

    // Return products sorted by expiration date.
    System.out.println("\n\n--------------------------------PRODUCTS BY EXPIRATION DATE--------------------------------");
    products.stream().sorted(Product.BY_EXPIRATION_DATE).forEach(System.out::println);

    // Delete all products of a specific category.
    products = products.stream().filter(product -> !Category.CANNED_PACKAGED.equals(product.getCategory())).collect(Collectors.toList());
    assertNotEquals(75, products.size());
    assertTrue(75 > products.size());
    assertTrue(products.stream().filter(product -> Category.CANNED_PACKAGED.equals(product.getCategory())).collect(Collectors.toList()).isEmpty());

    // Return products sorted by product number.
    System.out.println("\n\n--------------------------------PRODUCTS BY PRODUCT NUMBER--------------------------------");
    products.stream().sorted(Product.BY_NUMBER).forEach(System.out::println);
  }

}
