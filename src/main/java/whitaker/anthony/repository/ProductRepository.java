package whitaker.anthony.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import whitaker.anthony.model.Product;

import java.time.LocalDate;
import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {
	List<Product> findByCategory(String category);

	List<Product> findByExpirationDate(LocalDate expirationDate);

	List<Product> findByName(String name);

	Product findByNumber(String number);
}
