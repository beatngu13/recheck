package de.retest.recheck.report;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.retest.recheck.meta.global.TimeMetadataProvider;
import de.retest.recheck.ui.diff.meta.MetadataDifference;
import de.retest.recheck.ui.diff.meta.MetadataElementDifference;

class MetadataDifferenceFilterTest {

	MetadataDifferenceFilter cut;

	@BeforeEach
	void setUp() throws Exception {
		cut = new MetadataDifferenceFilter();
	}

	@Test
	void should_filter_ignored_differences() {
		final MetadataElementDifference elementDiff =
				new MetadataElementDifference( TimeMetadataProvider.TIME, "expected", "actual" );
		final Set<MetadataElementDifference> diffs = new HashSet<>( Arrays.asList( elementDiff ) );
		final MetadataDifference diff = MetadataDifference.of( diffs );
		assertThat( cut.filter( diff ) ).isEmpty();
	}

	@Test
	void should_not_filter_unignored_differences() {
		final MetadataElementDifference elementDiff = new MetadataElementDifference( "key", "expected", "actual" );
		final Set<MetadataElementDifference> diffs = new HashSet<>( Arrays.asList( elementDiff ) );
		final MetadataDifference diff = MetadataDifference.of( diffs );
		assertThat( cut.filter( diff ) ).containsExactly( elementDiff );
	}

}
