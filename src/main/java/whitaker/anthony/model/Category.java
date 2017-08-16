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

	/**
	 * Returns Category associated with given String, or null if given string is not associated with a Category.
	 *
	 * @param text Text associated with a Category.
	 * @return Category associated with given String, or null if given string is not associated with a Category.
	 */
	public static Category forText(String text) {
		return byText.get(text);
	}

	@Override
	public String toString() {
		return text;
	}

	public String getText() {
		return text;
	}
}

