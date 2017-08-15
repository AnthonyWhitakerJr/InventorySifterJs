package whitaker.anthony.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Locale;

@Document
public class Product {

  public static final Comparator<Product> BY_CATEGORY = Comparator.comparing(Product::getCategory);
  public static final Comparator<Product> BY_EXPIRATION_DATE = Comparator.comparing(Product::getExpirationDate);
  public static final Comparator<Product> BY_NAME = Comparator.comparing(Product::getName);
  public static final Comparator<Product> BY_NUMBER = Comparator.comparing(Product::getNumber);

  /** Product category. */
  @Indexed
  private final String category;
  /** Product expiration date. */
  @Indexed
  private final LocalDate expirationDate;
  /** Product name. */
  @Indexed
  private final String name;
  /** Unique generated product number. */
  @Indexed(unique = true)
  private final String number;
  @Id
  private String id;

  /**
   * Constructs a Product based on given values.
   *
   * @param number         Unique generated product number.
   * @param name           Product name.
   * @param category       Product category.
   * @param expirationDate Product expiration date.
   * @throws IllegalArgumentException If any parameters are {@code null}.
   */
  @PersistenceConstructor
  public Product(String number, String name, String category, LocalDate expirationDate) {
    if(number == null || name == null || category == null || expirationDate == null)
      throw new IllegalArgumentException("Parameters to Product constructor cannot be null.");
    this.number = number;
    this.name = name;
    this.category = category;
    this.expirationDate = expirationDate;
  }

  /**
   * Parses {@code productString} as a Product.
   *
   * @param productString        Delimited string containing product representation to be parsed.
   * @param delimiter            Delimiter for {@code productString}.
   * @param expirationDateFormat Date format of expiration date.
   * @param expirationDateLocale Locale of expiration date.
   * @return Product represented in given string.
   * @throws IllegalArgumentException                If any parameters are null,
   *                                                 or if any fields are unable to be parsed from {@code productString} with the given delimiter
   *                                                 or if {@code expirationDateFormat} is invalid.
   * @throws java.time.format.DateTimeParseException If expiration date cannot be parsed.
   */
  public static Product parseProduct(String productString, String delimiter, String expirationDateFormat, Locale expirationDateLocale) {
    if(productString == null || delimiter == null || expirationDateFormat == null || expirationDateLocale == null)
      throw new IllegalArgumentException("Parameters to parseProduct method cannot be null.");

    String[] productParts = productString.split(delimiter);

    if(productParts.length != 4)
      throw new IllegalArgumentException("Unable to parse \"" + productString + "\" with delimiter \"" + delimiter + "\"");

    String number = productParts[0];
    String name = productParts[1];
    String category = productParts[2];
    String expirationDateString = productParts[3];
    return new Product(number, name, category, parseStringToDate(expirationDateString, expirationDateFormat, expirationDateLocale));
  }

  /**
   * Obtains an instance of LocalDate from a text string using a formatter based on given format and locale.
   *
   * @param dateString String representation of date to parse.
   * @param dateFormat Date format of {@code dateString}.
   * @param dateLocale Locale of date.
   * @return The parsed local date, not null.
   * @throws java.time.format.DateTimeParseException If {@code dateString} cannot be parsed.
   */
  private static LocalDate parseStringToDate(String dateString, String dateFormat, Locale dateLocale) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat).withLocale(dateLocale);
    return LocalDate.parse(dateString, formatter);
  }

  /**
   * Returns a String representing this Product's fields delimited by given delimiter.
   * <p>
   * i.e. when delimited by ';': number;name;category;expirationDate
   *
   * @param delimiter            String used to separate fields. Recommend using an expression that does not appear in Product dataset, not null.
   * @param expirationDateFormat Date format of expiration date, not null.
   * @param expirationDateLocale Locale of expiration date, not null.
   * @return String representing this Product's fields delimited by given delimiter.
   * @throws IllegalArgumentException    If any parameters are null or if the date format pattern is invalid.
   * @throws java.time.DateTimeException If an error occurs during printing expiration date.
   */
  public String toDelimitedString(String delimiter, String expirationDateFormat, Locale expirationDateLocale) {
    if(delimiter == null || expirationDateFormat == null || expirationDateLocale == null)
      throw new IllegalArgumentException("Parameters for toDelimitedString method cannot be null.");

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(expirationDateFormat).withLocale(expirationDateLocale);
    String expirationDateString = expirationDate.format(formatter);

    return number + delimiter +
      name + delimiter +
      category + delimiter +
      expirationDateString;

  }

  @Override
  public String toString() {
    return "Product{" +
      "number='" + number + '\'' +
      ", name='" + name + '\'' +
      ", category='" + category + '\'' +
      ", expirationDate=" + expirationDate +
      '}';
  }


  public String getCategory() {
    return category;
  }

  public LocalDate getExpirationDate() {
    return expirationDate;
  }

  public String getName() {
    return name;
  }

  public String getNumber() {
    return number;
  }

}
