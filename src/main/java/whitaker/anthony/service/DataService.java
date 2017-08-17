package whitaker.anthony.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import whitaker.anthony.generator.ProductGenerator;
import whitaker.anthony.model.ProductCandidate;
import whitaker.anthony.repository.ProductRepository;

import java.util.ArrayList;

@Service
public class DataService {

	private static final String DELIMITER = ";";
	private static final String PRODUCT_CANDIDATE_FILENAME = "src/main/resources/ProductCandidates.txt";
	private final ProductGenerator productGenerator;
	private final ProductRepository productRepository;

	enum ProductField {
		CATEGORY("Category"),
		EXPIRATION_DATE("Expiration Date"),
		NAME("Name"),
		NUMBER("Number");

		private final String text;

		ProductField(String text) {
			this.text = text;
		}

		public String getText() {
			return text;
		}
	}

	@Autowired
	private DataService(ProductRepository productRepository) {
		this.productRepository = productRepository;

		//TODO: Add product generator as dependency.
		ArrayList<ProductCandidate> productCandidates = ProductGenerator.parseProductCandidateFile(PRODUCT_CANDIDATE_FILENAME, DELIMITER);
		this.productGenerator = new ProductGenerator(productCandidates);
	}

//	public void deleteBy(ProductField field, ) {}

//	List<Product> deleteByCategory(Category category);

//	List<Product> deleteByExpirationDate(LocalDate expirationDate);

//	List<Product> deleteByName(String name);

//	Product deleteByNumber(String name);

	public void clearProducts() {
		productRepository.deleteAll();
	}
}
