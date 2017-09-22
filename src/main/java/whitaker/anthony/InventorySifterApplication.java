package whitaker.anthony;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import whitaker.anthony.generator.DataGenerator;
import whitaker.anthony.model.Product;
import whitaker.anthony.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;

import static whitaker.anthony.generator.DataGenerator.*;

@SpringBootApplication
public class InventorySifterApplication implements CommandLineRunner {

	private static final String FILENAME_DATASET_1 = "src/test/resources/dataset1.txt";

	@Autowired
	private ProductRepository productRepository;

	public static void main(String[] args) {
		SpringApplication.run(InventorySifterApplication.class, args);
	}

	@Override
	public void run(String... strings) throws Exception {
		productRepository.deleteAll();

		List<Product> products = new ArrayList<>();

		// Add first data set.
		products.addAll(DataGenerator.parseProductsFromFile(FILENAME_DATASET_1, DEFAULT_DELIMITER, DEFAULT_EXPIRATION_DATE_FORMAT, DEFAULT_EXPIRATION_DATE_LOCALE));

		productRepository.save(products);
		System.out.println("Products found with findAll():");
		System.out.println("-------------------------------");
		for(Product product : productRepository.findAll()) {
			System.out.println(product);
		}
	}
}
