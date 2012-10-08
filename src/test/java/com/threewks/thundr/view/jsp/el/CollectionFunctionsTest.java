package com.threewks.thundr.view.jsp.el;

import static com.atomicleopard.expressive.Expressive.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class CollectionFunctionsTest {

	@Test
	public void shouldReturnTrueForContainsWhenCollectionContainsSingleValue() {
		assertThat(CollectionFunctions.contains(list("A", "B", "C"), "A"), is(true));
		assertThat(CollectionFunctions.contains(list("A", "B", "C"), "B"), is(true));
		assertThat(CollectionFunctions.contains(list("A", "B", "C"), "C"), is(true));
		assertThat(CollectionFunctions.contains(list("A", "B", "C"), "D"), is(false));
		assertThat(CollectionFunctions.contains(list("A", "B", "C"), null), is(false));
		assertThat(CollectionFunctions.contains(list("A", "B", "C", null), null), is(true));
	}

	@Test
	public void shouldReturnTrueForContainsWhenArrayContainsSingleValue() {
		assertThat(CollectionFunctions.contains(array("A", "B", "C"), "A"), is(true));
		assertThat(CollectionFunctions.contains(array("A", "B", "C"), "B"), is(true));
		assertThat(CollectionFunctions.contains(array("A", "B", "C"), "C"), is(true));
		assertThat(CollectionFunctions.contains(array("A", "B", "C"), "D"), is(false));
		assertThat(CollectionFunctions.contains(array("A", "B", "C"), null), is(false));
		assertThat(CollectionFunctions.contains(array("A", "B", "C", null), null), is(true));
	}

	@Test
	public void shouldReturnTrueForContainsWhenCollectionContainsCollectionValue() {
		assertThat(CollectionFunctions.contains(list("A", "B", "C"), list("A")), is(true));
		assertThat(CollectionFunctions.contains(list("A", "B", "C"), list("B")), is(true));
		assertThat(CollectionFunctions.contains(list("A", "B", "C"), list("C")), is(true));
		assertThat(CollectionFunctions.contains(list("A", "B", "C"), list("D")), is(false));
		assertThat(CollectionFunctions.contains(list("A", "B", "C"), null), is(false));
		assertThat(CollectionFunctions.contains(list("A", "B", "C", null), list((Object) null)), is(true));
		assertThat(CollectionFunctions.contains(list("A", "B", "C"), list()), is(true));

		assertThat(CollectionFunctions.contains(list("A", "B", "C"), list("A", "B")), is(true));
		assertThat(CollectionFunctions.contains(list("A", "B", "C"), list("B", "A")), is(true));
		assertThat(CollectionFunctions.contains(list("A", "B", "C"), list("C", "D")), is(false));
		assertThat(CollectionFunctions.contains(list("A", "B", "C"), list("A", "A")), is(true));
		assertThat(CollectionFunctions.contains(list("A", "B", "C", null), list((Object) null, "A")), is(true));
	}

	@Test
	public void shouldReturnTrueForContainsWhenArrayContainsCollectionValue() {
		assertThat(CollectionFunctions.contains(array("A", "B", "C"), list("A")), is(true));
		assertThat(CollectionFunctions.contains(array("A", "B", "C"), list("B")), is(true));
		assertThat(CollectionFunctions.contains(array("A", "B", "C"), list("C")), is(true));
		assertThat(CollectionFunctions.contains(array("A", "B", "C"), list("D")), is(false));
		assertThat(CollectionFunctions.contains(array("A", "B", "C"), null), is(false));
		assertThat(CollectionFunctions.contains(array("A", "B", "C", null), list((Object) null)), is(true));
		assertThat(CollectionFunctions.contains(array("A", "B", "C"), list()), is(true));

		assertThat(CollectionFunctions.contains(array("A", "B", "C"), list("A", "B")), is(true));
		assertThat(CollectionFunctions.contains(array("A", "B", "C"), list("B", "A")), is(true));
		assertThat(CollectionFunctions.contains(array("A", "B", "C"), list("C", "D")), is(false));
		assertThat(CollectionFunctions.contains(array("A", "B", "C"), list("A", "A")), is(true));
		assertThat(CollectionFunctions.contains(array("A", "B", "C", null), list((Object) null, "A")), is(true));
	}

	@Test
	public void shouldReturnTrueForContainsWhenCollectionContainsArrayValue() {
		assertThat(CollectionFunctions.contains(list("A", "B", "C"), array("A")), is(true));
		assertThat(CollectionFunctions.contains(list("A", "B", "C"), array("B")), is(true));
		assertThat(CollectionFunctions.contains(list("A", "B", "C"), array("C")), is(true));
		assertThat(CollectionFunctions.contains(list("A", "B", "C"), array("D")), is(false));
		assertThat(CollectionFunctions.contains(list("A", "B", "C"), null), is(false));
		assertThat(CollectionFunctions.contains(list("A", "B", "C", null), array((Object) null)), is(true));
		assertThat(CollectionFunctions.contains(list("A", "B", "C"), array()), is(true));

		assertThat(CollectionFunctions.contains(list("A", "B", "C"), array("A", "B")), is(true));
		assertThat(CollectionFunctions.contains(list("A", "B", "C"), array("B", "A")), is(true));
		assertThat(CollectionFunctions.contains(list("A", "B", "C"), array("C", "D")), is(false));
		assertThat(CollectionFunctions.contains(list("A", "B", "C"), array("A", "A")), is(true));
		assertThat(CollectionFunctions.contains(list("A", "B", "C", null), array((Object) null, "A")), is(true));
	}

	@Test
	public void shouldReturnTrueForContainsWhenArrayContainsArrayValue() {
		assertThat(CollectionFunctions.contains(array("A", "B", "C"), array("A")), is(true));
		assertThat(CollectionFunctions.contains(array("A", "B", "C"), array("B")), is(true));
		assertThat(CollectionFunctions.contains(array("A", "B", "C"), array("C")), is(true));
		assertThat(CollectionFunctions.contains(array("A", "B", "C"), array("D")), is(false));
		assertThat(CollectionFunctions.contains(array("A", "B", "C"), null), is(false));
		assertThat(CollectionFunctions.contains(array("A", "B", "C", null), array((Object) null)), is(true));
		assertThat(CollectionFunctions.contains(array("A", "B", "C"), array()), is(true));

		assertThat(CollectionFunctions.contains(array("A", "B", "C"), array("A", "B")), is(true));
		assertThat(CollectionFunctions.contains(array("A", "B", "C"), array("B", "A")), is(true));
		assertThat(CollectionFunctions.contains(array("A", "B", "C"), array("C", "D")), is(false));
		assertThat(CollectionFunctions.contains(array("A", "B", "C"), array("A", "A")), is(true));
		assertThat(CollectionFunctions.contains(array("A", "B", "C", null), array((Object) null, "A")), is(true));
	}

	@Test
	public void shouldReturnFalseForContainsWhenArrayIsNull() {
		assertThat(CollectionFunctions.contains(null, list("A", "B", "C")), is(false));
		assertThat(CollectionFunctions.contains(null, array((Object) "A", "B", "C")), is(false));
	}

	@Test
	public void shouldReturnTrueForContainsAnyWhenCollectionContainsSingleValue() {
		assertThat(CollectionFunctions.containsAny(list("A", "B", "C"), "A"), is(true));
		assertThat(CollectionFunctions.containsAny(list("A", "B", "C"), "B"), is(true));
		assertThat(CollectionFunctions.containsAny(list("A", "B", "C"), "C"), is(true));
		assertThat(CollectionFunctions.containsAny(list("A", "B", "C"), "D"), is(false));
		assertThat(CollectionFunctions.containsAny(list("A", "B", "C"), null), is(false));
		assertThat(CollectionFunctions.containsAny(list("A", "B", "C", null), null), is(true));
	}

	@Test
	public void shouldReturnTrueForContainsAnyWhenArrayContainsSingleValue() {
		assertThat(CollectionFunctions.containsAny(array("A", "B", "C"), "A"), is(true));
		assertThat(CollectionFunctions.containsAny(array("A", "B", "C"), "B"), is(true));
		assertThat(CollectionFunctions.containsAny(array("A", "B", "C"), "C"), is(true));
		assertThat(CollectionFunctions.containsAny(array("A", "B", "C"), "D"), is(false));
		assertThat(CollectionFunctions.containsAny(array("A", "B", "C"), null), is(false));
		assertThat(CollectionFunctions.containsAny(array("A", "B", "C", null), null), is(true));
	}

	@Test
	public void shouldReturnTrueForContainsAnyWhenCollectionContainsCollectionValue() {
		assertThat(CollectionFunctions.containsAny(list("A", "B", "C"), list("A")), is(true));
		assertThat(CollectionFunctions.containsAny(list("A", "B", "C"), list("B")), is(true));
		assertThat(CollectionFunctions.containsAny(list("A", "B", "C"), list("C")), is(true));
		assertThat(CollectionFunctions.containsAny(list("A", "B", "C"), list("D")), is(false));
		assertThat(CollectionFunctions.containsAny(list("A", "B", "C"), null), is(false));
		assertThat(CollectionFunctions.containsAny(list("A", "B", "C", null), list((Object) null)), is(true));
		assertThat(CollectionFunctions.containsAny(list("A", "B", "C"), list()), is(true));

		assertThat(CollectionFunctions.containsAny(list("A", "B", "C"), list("A", "B")), is(true));
		assertThat(CollectionFunctions.containsAny(list("A", "B", "C"), list("B", "A")), is(true));
		assertThat(CollectionFunctions.containsAny(list("A", "B", "C"), list("C", "D")), is(true));
		assertThat(CollectionFunctions.containsAny(list("A", "B", "C"), list("A", "A")), is(true));
		assertThat(CollectionFunctions.containsAny(list("A", "B", "C"), list("D", "D")), is(false));
		assertThat(CollectionFunctions.containsAny(list("A", "B", "C", null), list((Object) null, "A")), is(true));
	}

	@Test
	public void shouldReturnTrueForContainsAnyWhenArrayContainsCollectionValue() {
		assertThat(CollectionFunctions.containsAny(array("A", "B", "C"), list("A")), is(true));
		assertThat(CollectionFunctions.containsAny(array("A", "B", "C"), list("B")), is(true));
		assertThat(CollectionFunctions.containsAny(array("A", "B", "C"), list("C")), is(true));
		assertThat(CollectionFunctions.containsAny(array("A", "B", "C"), list("D")), is(false));
		assertThat(CollectionFunctions.containsAny(array("A", "B", "C"), null), is(false));
		assertThat(CollectionFunctions.containsAny(array("A", "B", "C", null), list((Object) null)), is(true));
		assertThat(CollectionFunctions.containsAny(array("A", "B", "C"), list()), is(true));

		assertThat(CollectionFunctions.containsAny(array("A", "B", "C"), list("A", "B")), is(true));
		assertThat(CollectionFunctions.containsAny(array("A", "B", "C"), list("B", "A")), is(true));
		assertThat(CollectionFunctions.containsAny(array("A", "B", "C"), list("C", "D")), is(true));
		assertThat(CollectionFunctions.containsAny(array("A", "B", "C"), list("A", "A")), is(true));
		assertThat(CollectionFunctions.containsAny(array("A", "B", "C"), list("D", "D")), is(false));
		assertThat(CollectionFunctions.containsAny(array("A", "B", "C", null), list((Object) null, "A")), is(true));
	}

	@Test
	public void shouldReturnTrueForContainsAnyWhenCollectionContainsArrayValue() {
		assertThat(CollectionFunctions.containsAny(list("A", "B", "C"), array("A")), is(true));
		assertThat(CollectionFunctions.containsAny(list("A", "B", "C"), array("B")), is(true));
		assertThat(CollectionFunctions.containsAny(list("A", "B", "C"), array("C")), is(true));
		assertThat(CollectionFunctions.containsAny(list("A", "B", "C"), array("D")), is(false));
		assertThat(CollectionFunctions.containsAny(list("A", "B", "C"), null), is(false));
		assertThat(CollectionFunctions.containsAny(list("A", "B", "C", null), array((Object) null)), is(true));
		assertThat(CollectionFunctions.containsAny(list("A", "B", "C"), array()), is(true));

		assertThat(CollectionFunctions.containsAny(list("A", "B", "C"), array("A", "B")), is(true));
		assertThat(CollectionFunctions.containsAny(list("A", "B", "C"), array("B", "A")), is(true));
		assertThat(CollectionFunctions.containsAny(list("A", "B", "C"), array("C", "D")), is(true));
		assertThat(CollectionFunctions.containsAny(list("A", "B", "C"), array("A", "A")), is(true));
		assertThat(CollectionFunctions.containsAny(list("A", "B", "C"), array("D", "D")), is(false));
		assertThat(CollectionFunctions.containsAny(list("A", "B", "C", null), array((Object) null, "A")), is(true));
	}

	@Test
	public void shouldReturnTrueForContainsAnyWhenArrayContainsArrayValue() {
		assertThat(CollectionFunctions.containsAny(array("A", "B", "C"), array("A")), is(true));
		assertThat(CollectionFunctions.containsAny(array("A", "B", "C"), array("B")), is(true));
		assertThat(CollectionFunctions.containsAny(array("A", "B", "C"), array("C")), is(true));
		assertThat(CollectionFunctions.containsAny(array("A", "B", "C"), array("D")), is(false));
		assertThat(CollectionFunctions.containsAny(array("A", "B", "C"), null), is(false));
		assertThat(CollectionFunctions.containsAny(array("A", "B", "C", null), array((Object) null)), is(true));
		assertThat(CollectionFunctions.containsAny(array("A", "B", "C"), array()), is(true));

		assertThat(CollectionFunctions.containsAny(array("A", "B", "C"), array("A", "B")), is(true));
		assertThat(CollectionFunctions.containsAny(array("A", "B", "C"), array("B", "A")), is(true));
		assertThat(CollectionFunctions.containsAny(array("A", "B", "C"), array("C", "D")), is(true));
		assertThat(CollectionFunctions.containsAny(array("A", "B", "C"), array("A", "A")), is(true));
		assertThat(CollectionFunctions.containsAny(array("A", "B", "C"), array("D", "D")), is(false));
		assertThat(CollectionFunctions.containsAny(array("A", "B", "C", null), array((Object) null, "A")), is(true));
	}

}
