package whitaker.anthony;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import whitaker.anthony.model.Product;
import whitaker.anthony.repository.ProductRepository;

@SpringBootApplication
public class InventorySifterApplication implements CommandLineRunner {

	@Autowired
	private ProductRepository productRepository;

	public static void main(String[] args) {
		SpringApplication.run(InventorySifterApplication.class, args);
	}

	@Override
	public void run(String... strings) throws Exception {
//    productRepository.save(new Product("12346567", "Apple, Fuji", "Produce", ProductGenerator.getRandomDate(LocalDate.now(), LocalDate.now().plusMonths(2))));

		System.out.println("Products found with findAll():");
		System.out.println("-------------------------------");
		for(Product product : productRepository.findAll()) {
			System.out.println(product);
		}
	}
}
