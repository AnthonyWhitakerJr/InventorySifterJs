package whitaker.anthony.repository;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import whitaker.anthony.generator.DataGenerator;
import whitaker.anthony.model.Product;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static whitaker.anthony.generator.DataGenerator.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductRepositoryTest {

	private static final String FILENAME_DATASET_1 = "src/test/resources/dataset1.txt";

	@Autowired
	private ProductRepository productRepository;

	@Test
	@Ignore(value = "Proof of concept. Unfinished.")
	public void testCreateRead() {
		List<Product> products = new ArrayList<>();

		// Add first data set.
		products.addAll(DataGenerator.parseProductsFromFile(FILENAME_DATASET_1, DEFAULT_DELIMITER, DEFAULT_EXPIRATION_DATE_FORMAT, DEFAULT_EXPIRATION_DATE_LOCALE));
		assertEquals(25, products.size());

		productRepository.save(products);
		System.out.println("Products found with findAll():");
		System.out.println("-------------------------------");
		for(Product product : productRepository.findAll()) {
			System.out.println(product);
		}

	}

}