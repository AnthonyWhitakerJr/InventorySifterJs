package whitaker.anthony.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static whitaker.anthony.generator.DataGenerator.DEFAULT_EXPIRATION_DATE_FORMAT;
import static whitaker.anthony.generator.DataGenerator.DEFAULT_EXPIRATION_DATE_LOCALE;
import static whitaker.anthony.generator.DataGeneratorTest.DELIMITER;

@RunWith(value = Parameterized.class)
public class ProductTest_toDelimitedString {

	private String expected;
	private Product product;

	public ProductTest_toDelimitedString(String expected, Product product) {
		this.expected = expected;
		this.product = product;
	}

	@Parameterized.Parameters(name = "{index}: testToDelimitedString({0})")
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][]{
				{"08f7751c-c495-469d-b4ca-5b1f118c91a7;Lemons;Produce;2017-10-08", new Product("08f7751c-c495-469d-b4ca-5b1f118c91a7", "Lemons", Category.PRODUCE, LocalDate.of(2017, 10, 8))},
				{"a6b65395-e757-4688-8bd3-5326c5193621;Tea, green, bags;Beverages;2018-04-24", new Product("a6b65395-e757-4688-8bd3-5326c5193621", "Tea, green, bags", Category.BEVERAGES, LocalDate.of(2018, 4, 24))},
				{"1d2bdbec-1375-45f7-a3ee-5d0a742e3fbb;Ice Cream, simple;Frozen Foods;2017-09-14", new Product("1d2bdbec-1375-45f7-a3ee-5d0a742e3fbb", "Ice Cream, simple", Category.FROZEN, LocalDate.of(2017, 9, 14))}
		});
	}

	@Test
	public void testToDelimitedString() {
		assertEquals(expected, product.toDelimitedString(DELIMITER, DEFAULT_EXPIRATION_DATE_FORMAT, DEFAULT_EXPIRATION_DATE_LOCALE));
	}
}
