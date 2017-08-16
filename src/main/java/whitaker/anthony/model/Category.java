package whitaker.anthony.model;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Document
public enum Category {
	BEVERAGES("Beverages"),
	CANNED_PACKAGED("Canned & Packaged Foods"),
	BAKERY_BREAKFAST_CEREAL("Bakery, Breakfast, Cereal"),
	FROZEN("Frozen Foods"),
	MISCELLANEOUS_KITCHEN("Miscellaneous Kitchen Items"),
	PRODUCE("Produce"),
	REFRIGERATED("Refrigerated Foods");

	private static final Map<String, Category> byText = new HashMap<>();
	private final String text;

	static {
		Arrays.stream(Category.values()).forEach(category -> byText.put(category.text, category));
	}

	Category(String name) {
		this.text = name;
	}

	public static Category forText(String name) {
		return byText.get(name);
	}

	@Override
	public String toString() {
		return text;
	}

	public String getText() {
		return text;
	}
}

