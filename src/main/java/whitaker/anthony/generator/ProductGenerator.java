package whitaker.anthony.generator;

import whitaker.anthony.model.Category;
import whitaker.anthony.model.Product;
import whitaker.anthony.model.ProductCandidate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.time.temporal.ChronoUnit.DAYS;

/**
 * Given a list of potential products, this class is capable of generating complete Product objects.
 */
public class ProductGenerator {

	private ArrayList<ProductCandidate> productCandidates = new ArrayList<>();

	/**
	 * Constructs a ProductGenerator.
	 *
	 * @param productCandidates List of potential products. If null, defaults to empty list.
	 */
	public ProductGenerator(Collection<ProductCandidate> productCandidates) {
		if(productCandidates != null) {
			this.productCandidates.addAll(productCandidates);
		}
	}

	/**
	 * Creates a product with a random product number, random expiration date and random set of category & name from available product candidates.
	 *
	 * @param productCandidates List of potential products from which to derive the random product.
	 *                          <b>Warning: This list will undergo destructive modification.</b>
	 * @param expirationDateMin Lower bound of random expiration date.
	 * @param expirationDateMax Upper bound of random expiration date.
	 * @return A product with a random product number, random expiration date and random set of category & name from available product candidates.
	 * @throws IllegalArgumentException If start date does not precede end date.
	 * @throws IllegalStateException    If there are no product candidates available.
	 */
	public static Product createRandomProduct(ArrayList<ProductCandidate> productCandidates, LocalDate expirationDateMin, LocalDate expirationDateMax) {
		if(productCandidates.isEmpty())
			throw new IllegalStateException("No product candidates available for product creation.");

		String number = generateProductNumber();
		ProductCandidate productCandidate = productCandidates.remove(getRandomIndex(productCandidates));
		Category category = productCandidate.getCategory();
		String name = productCandidate.getName();
		LocalDate expirationDate = getRandomDate(expirationDateMin, expirationDateMax);

		return new Product(number, name, category, expirationDate);
	}

	/**
	 * Generates a dataset of the given size by generating random products based on the product previously set product candidates, random expiration dates within given bounds and random product numbers.
	 *
	 * @param productCandidates List of potential products from which to derive the random product.
	 *                          <b>Warning: This list will undergo destructive modification.</b>
	 * @param size              Number of products to create. May not exceed size of product candidate list.
	 * @param expirationDateMin Lower bound for expiration date.
	 * @param expirationDateMax Upper bound for expiration date.
	 * @return A dataset of the given size by generating random products based on the product previously set product candidates, random expiration dates within given bounds and random product numbers.
	 */
	public static Collection<Product> createRandomProducts(ArrayList<ProductCandidate> productCandidates, int size, LocalDate expirationDateMin, LocalDate expirationDateMax) {
		if(size < 0 || size > productCandidates.size())
			throw new IllegalArgumentException("Invalid size.");
		return IntStream.range(0, size).mapToObj(i -> createRandomProduct(productCandidates, expirationDateMin, expirationDateMax)).collect(Collectors.toList());
	}

	/**
	 * Generates a unique number for use in a new Product's number field.
	 *
	 * @return A unique number.
	 */
	static String generateProductNumber() {
		return UUID.randomUUID().toString();
	}

	/**
	 * Creates a random LocalDate between start date and end date.
	 *
	 * @param startDate Lower bound of random date.
	 * @param endDate   Upper bound of random date.
	 * @return A random LocalDate between start date and end date.
	 * @throws IllegalArgumentException If start date does not precede end date.
	 */
	static LocalDate getRandomDate(LocalDate startDate, LocalDate endDate) {
		if(!startDate.isBefore(endDate)) throw new IllegalArgumentException("Start date must precede end date.");
		long daysBetween = DAYS.between(startDate, endDate);
		return startDate.plusDays(ThreadLocalRandom.current().nextLong(daysBetween + 1));
	}

	/**
	 * Generates a random index within the bounds of the given collection's size.
	 *
	 * @param collection collection to derive index from.
	 * @return A random index within the bounds of the given collection's size.
	 */
	static int getRandomIndex(Collection collection) {
		return ThreadLocalRandom.current().nextInt(collection.size());
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
	public static ArrayList<ProductCandidate> parseProductCandidateFile(String filename, String delimiter) {
		try(Stream<String> stream = Files.lines(Paths.get(filename))) {
			return parseProductCandidates(stream, delimiter);
		} catch(IOException | IllegalArgumentException e) {
			throw new IllegalArgumentException("Unable to parse \"" + filename + "\" with delimiter \"" + delimiter + "\"", e);
		}
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
	static ArrayList<ProductCandidate> parseProductCandidates(Stream<String> stream, String delimiter) {
		return stream.map(s -> s.split(delimiter))
				.map(strings -> {
					if(strings.length != 2)
						throw new IllegalArgumentException("Parsing with delimiter \"" + delimiter + "\" produced wrong amount of fields.");
					return new ProductCandidate(Category.forText(strings[0]), strings[1]);
				})
				.collect(Collectors.toCollection(ArrayList::new));
	}

	/**
	 * Creates a product with a random product number, random expiration date and random set of category & name from available product candidates.
	 *
	 * @param expirationDateMin Lower bound of random expiration date.
	 * @param expirationDateMax Upper bound of random expiration date.
	 * @return A product with a random product number, random expiration date and random set of category & name from available product candidates.
	 * @throws IllegalArgumentException If start date does not precede end date.
	 * @throws IllegalStateException    If there are no product candidates available.
	 */
	public Product createRandomProduct(LocalDate expirationDateMin, LocalDate expirationDateMax) {
		return ProductGenerator.createRandomProduct(productCandidates, expirationDateMin, expirationDateMax);
	}

	/**
	 * Generates a dataset of the given size by generating random products based on the product previously set product candidates, random expiration dates within given bounds and random product numbers.
	 *
	 * @param size              Number of products to create. May not exceed number of remaining product candidates.
	 * @param expirationDateMin Lower bound for expiration date.
	 * @param expirationDateMax Upper bound for expiration date.
	 * @return A dataset of the given size by generating random products based on the product previously set product candidates, random expiration dates within given bounds and random product numbers.
	 */
	public Collection<Product> createRandomProducts(int size, LocalDate expirationDateMin, LocalDate expirationDateMax) {
		return createRandomProducts(productCandidates, size, expirationDateMin, expirationDateMax);
	}

	public int getNumberOfRemainingCandidates() { return productCandidates.size(); }

}
