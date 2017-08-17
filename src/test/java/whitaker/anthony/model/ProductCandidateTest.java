package whitaker.anthony.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class ProductCandidateTest {
	@Test
	public void testEquals_SanityCheck() {
		ProductCandidate candidate = new ProductCandidate(Category.BEVERAGES, "Bar");
		assertEquals(candidate, candidate);
		assertNotEquals(new Object(), candidate);
		assertNotEquals(null, candidate);
	}

	@Test
	public void testHashCode_SanityCheck() {
		ProductCandidate candidate1 = new ProductCandidate(Category.BEVERAGES, "Bar");
		ProductCandidate candidate2 = new ProductCandidate(Category.BAKERY_BREAKFAST_CEREAL, "Else");
		assertNotEquals(candidate1.hashCode(), candidate2.hashCode());
	}

	@Test
	public void testToString_SanityCheck() {
		ProductCandidate candidate = new ProductCandidate(Category.BEVERAGES, "Bar");
		assertTrue(candidate.toString().contains(Category.BEVERAGES.toString()));
		assertTrue(candidate.toString().contains("Bar"));
	}

}
