package whitaker.anthony.model;

/**
 * Represents a potential product. Contains category & name.
 * Number will be auto-generated and expiration date is entered at product creation.
 */
public class ProductCandidate {
	private final Category category;
	private final String name;

	public ProductCandidate(Category category, String name) {
		this.category = category;
		this.name = name;
	}

	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;

		ProductCandidate that = (ProductCandidate)o;

		if(!category.equals(that.category)) return false;
		return name.equals(that.name);
	}

	@Override
	public int hashCode() {
		int result = category.hashCode();
		result = 31 * result + name.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return "ProductCandidate{" +
				"category='" + category + '\'' +
				", name='" + name + '\'' +
				'}';
	}

	public Category getCategory() {
		return category;
	}

	public String getName() {
		return name;
	}
}
