package whitaker.anthony.generator;

import whitaker.anthony.model.Product;
import whitaker.anthony.model.ProductCandidate;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class DataGenerator {
	public static final String DEFAULT_EXPIRATION_DATE_FORMAT = "yyyy-MM-dd";
	public static final Locale DEFAULT_EXPIRATION_DATE_LOCALE = Locale.US;

	private final ProductGenerator productGenerator;
	private String expirationDateFormat;
	private Locale expirationDateLocale;

	/**
	 * Constructs a DataGenerator
	 *
	 * @param filename             File name for product candidates, not null.
	 * @param delimiter            Delimiter between product candidate category & name, not null.
	 * @param expirationDateFormat Date format of expiration date, not null.
	 * @param expirationDateLocale Locale of expiration date, not null.
	 * @throws IllegalArgumentException If unable to parse product candidate file properly with given delimiter.
	 */
	public DataGenerator(String filename, String delimiter, String expirationDateFormat, Locale expirationDateLocale) {
		ArrayList<ProductCandidate> productCandidates = ProductGenerator.parseProductCandidateFile(filename, delimiter);
		this.productGenerator = new ProductGenerator(productCandidates);
		this.setExpirationDateFormat(expirationDateFormat);
		this.setExpirationDateLocale(expirationDateLocale);
	}

	/**
	 * Parse stream containing product candidates into an ArrayList.
	 * Stream should contain lines with a product category & name separated by a delimiter.
	 *
	 * @param stream    Stream to parse.
	 * @param delimiter Delimiter between category & name in stream.
	 * @return Arraylist of product candidates based on file contents.
	 * @throws IllegalArgumentException If unable to parse file properly with given delimiter.
	 */
	public static List<Product> parseProducts(Stream<String> stream, String delimiter, String expirationDateFormat, Locale expirationDateLocale) {
		return stream.map(s -> Product.parseProduct(s, delimiter, expirationDateFormat, expirationDateLocale))
				.collect(Collectors.toList());
	}

	/**
	 * Parse file containing product candidates into an ArrayList.
	 * File should contain lines with a product category & name separated by a delimiter.
	 *
	 * @param filename  Name of file to parse
	 * @param delimiter Delimiter between category & name in file.
	 * @return Arraylist of product candidates based on file contents.
	 * @throws IllegalArgumentException If unable to parse file properly with given delimiter.
	 */
	public static List<Product> parseProductsFromFile(String filename, String delimiter, String expirationDateFormat, Locale expirationDateLocale) {
		try(Stream<String> stream = Files.lines(Paths.get(filename))) {
			return parseProducts(stream, delimiter, expirationDateFormat, expirationDateLocale);
		} catch(IOException | IllegalArgumentException e) {
			throw new IllegalArgumentException("Unable to parse \"" + filename + "\" with delimiter \"" + delimiter + "\"", e);
		}
	}

	/**
	 * Generates a dataset of the given size by generating random products based on the product previously set product candidates, random expiration dates within given bounds and random product numbers.
	 *
	 * @param size              Size of desired dataset.
	 * @param expirationDateMin lower bound for expiration date.
	 * @param expirationDateMax upper bound for expiration date.
	 * @return A dataset of the given size by generating random products based on the product previously set product candidates, random expiration dates within given bounds and random product numbers.
	 */
	public Collection<Product> generateDataSet(int size, LocalDate expirationDateMin, LocalDate expirationDateMax) {
		if(size < 1 || size > productGenerator.getProductCandidates().size())
			throw new IllegalArgumentException("Invalid size.");
		return IntStream.range(0, size).mapToObj(i -> productGenerator.createRandomProduct(expirationDateMin, expirationDateMax)).collect(Collectors.toList());
	}

	/**
	 * Writes given dataset to file. Product fields are delimited with given delimiter. Use same delimiter when parsing back into program.
	 *
	 * @param dataset   Dataset to write to file.
	 * @param filename  Name of file to create/overwrite.
	 * @param delimiter Delimiter to use between product fields.
	 */
	public void writeDataSetToFile(Collection<Product> dataset, String filename, String delimiter) {
		try(PrintWriter out = new PrintWriter(Files.newBufferedWriter(Paths.get(filename)))) {
			dataset.stream()
					.map(product -> product.toDelimitedString(delimiter, expirationDateFormat, expirationDateLocale))
					.forEach(out::println);
		} catch(IOException e) {
			throw new IllegalArgumentException("Unable to write to \"" + filename + "\"", e);
		}
	}

	public String getExpirationDateFormat() {
		return expirationDateFormat;
	}

	/**
	 * Sets expiration date format to given value. Defaults to {@link .DEFAULT_EXPIRATION_DATE_FORMAT} if null.
	 *
	 * @param expirationDateFormat expiration date format to set. Defaults to {@link .DEFAULT_EXPIRATION_DATE_FORMAT} if null.
	 */
	public void setExpirationDateFormat(String expirationDateFormat) {
		if(expirationDateFormat == null) {
			this.expirationDateFormat = DEFAULT_EXPIRATION_DATE_FORMAT;
		}
		else {
			this.expirationDateFormat = expirationDateFormat;
		}
	}

	public Locale getExpirationDateLocale() {
		return expirationDateLocale;
	}

	/**
	 * Sets expiration date locale to given value. Defaults to {@link .DEFAULT_EXPIRATION_DATE_LOCALE} if null.
	 *
	 * @param expirationDateLocale expiration date locale to set. Defaults to {@link .DEFAULT_EXPIRATION_DATE_LOCALE} if null.
	 */
	public void setExpirationDateLocale(Locale expirationDateLocale) {
		if(expirationDateLocale == null) {
			this.expirationDateLocale = DEFAULT_EXPIRATION_DATE_LOCALE;
		}
		else {
			this.expirationDateLocale = expirationDateLocale;
		}
	}
}
