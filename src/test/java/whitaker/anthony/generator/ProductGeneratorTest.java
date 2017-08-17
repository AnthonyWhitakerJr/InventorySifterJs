package whitaker.anthony.generator;

import org.junit.Test;
import whitaker.anthony.model.Category;
import whitaker.anthony.model.Product;
import whitaker.anthony.model.ProductCandidate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class ProductGeneratorTest {

	@Test
	public void testCreateRandomProduct() {
		final ArrayList<ProductCandidate> potentialCandidates = new ArrayList<>(Arrays.asList(
				new ProductCandidate(Category.BEVERAGES, "Coffee"),
				new ProductCandidate(Category.BEVERAGES, "Milk"),
				new ProductCandidate(Category.FROZEN, "Waffles, frozen"),
				new ProductCandidate(Category.MISCELLANEOUS_KITCHEN, "Aluminum Foil"),
				new ProductCandidate(Category.PRODUCE, "Tomatoes"),
				new ProductCandidate(Category.REFRIGERATED, "Bacon")));

		List<Product> products;
		ProductGenerator generator = new ProductGenerator(potentialCandidates);

		products = IntStream.range(0, potentialCandidates.size()).mapToObj(i -> generator.createRandomProduct(LocalDate.now(), LocalDate.now().plusMonths(3))).collect(Collectors.toList());
		assertEquals(potentialCandidates.size(), products.size());

		try {
			generator.createRandomProduct(LocalDate.now(), LocalDate.now().plusMonths(3));
			fail();
		} catch(IllegalStateException e) {
			assertTrue(e.getMessage().startsWith("No product candidates available"));
		}
	}

	@Test
	public void testGenerateNumber_UniqueResults() {
		String firstNumber = ProductGenerator.generateNumber();
		String secondNumber = ProductGenerator.generateNumber();
		String thirdNumber = ProductGenerator.generateNumber();

		assertNotEquals(firstNumber, secondNumber);
		assertNotEquals(firstNumber, thirdNumber);
		assertNotEquals(secondNumber, thirdNumber);
	}

	@Test
	public void testGetRandomDate() {
		LocalDate start = LocalDate.now();
		LocalDate end = LocalDate.now().plusWeeks(2);
		for(int i = 0; i < 5; i++) {
			LocalDate result = ProductGenerator.getRandomDate(start, end);
			assertTrue("End should be after result. Start: " + start + "; End: " + end + "; Result: " + result, end.plusDays(1).isAfter(result));
			assertTrue("Start should be before result. Start: " + start + "; End: " + end + "; Result: " + result, start.minusDays(1).isBefore(result));
		}
	}

	@Test
	public void testGetRandomDate_Exception() {
		LocalDate start = LocalDate.now().plusWeeks(2);
		LocalDate end = LocalDate.now();

		try {
			ProductGenerator.getRandomDate(start, end);
			fail();
		} catch(IllegalArgumentException e) {
			assertEquals("Start date must precede end date.", e.getMessage());
		}
	}

	@Test
	public void testGetRandomListIndex() {
		Collection collection = Arrays.asList("0", "1", "2", "3", "4");
		for(int i = 0; i < 5; i++) {
			int index = ProductGenerator.getRandomIndex(collection);
			assertTrue("Random index too small", 0 <= index);
			assertTrue("Random index too large", collection.size() > index);
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testParseProductCandidateFile_Exception() {
		ProductGenerator.parseProductCandidateFile("BogusFilename", ",");
	}

	@Test
	public void testParseProductCandidates() {
		String candidates = "Beverages;Coffee\n" +
				"Beverages;Milk\n" +
				"Frozen Foods;Waffles, frozen\n" +
				"Miscellaneous Kitchen Items;Aluminum Foil\n" +
				"Produce;Tomatoes\n" +
				"Refrigerated Foods;Bacon";

		final Collection<ProductCandidate> expected = new ArrayList<>(Arrays.asList(
				new ProductCandidate(Category.BEVERAGES, "Coffee"),
				new ProductCandidate(Category.BEVERAGES, "Milk"),
				new ProductCandidate(Category.FROZEN, "Waffles, frozen"),
				new ProductCandidate(Category.MISCELLANEOUS_KITCHEN, "Aluminum Foil"),
				new ProductCandidate(Category.PRODUCE, "Tomatoes"),
				new ProductCandidate(Category.REFRIGERATED, "Bacon")));

		ArrayList<ProductCandidate> productCandidates = ProductGenerator.parseProductCandidates(Arrays.stream(candidates.split("\n")), ";");

		assertTrue("Extra candidates created", expected.containsAll(productCandidates));
		assertTrue("Some candidates missing", productCandidates.containsAll(expected));
	}

	@Test
	public void testParseProductCandidates_Exception() {
		String candidates = "Beverages|Coffee\n" +
				"Refrigerated Foods|Bacon";

		try {
			ProductGenerator.parseProductCandidates(Arrays.stream(candidates.split("\n")), "|");
			fail();
		} catch(IllegalArgumentException e) {
			assertTrue(e.getMessage().endsWith("wrong amount of fields."));
		}
	}

	@Test
	public void testProductGenerator_SanityCheck() {
		ProductGenerator generator = new ProductGenerator(null);
		assertNotNull(generator.getProductCandidates());
		assertTrue(generator.getProductCandidates().isEmpty());
	}

}
