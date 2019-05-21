package de.retest.recheck.ignore;

import static de.retest.recheck.configuration.ProjectConfiguration.FILTER_FOLDER;
import static de.retest.recheck.configuration.ProjectConfiguration.RETEST_PROJECT_CONFIG_FOLDER;
import static de.retest.recheck.configuration.ProjectConfiguration.RETEST_PROJECT_ROOT;
import static de.retest.recheck.ignore.SearchFilterFiles.FILTER_EXTENSION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import de.retest.recheck.util.junit.jupiter.SystemProperty;

class SearchFilterFilesTest {

	private Path filterFolder;

	@BeforeEach
	void setUp( @TempDir final Path temp ) throws IOException {
		final Path configFolder = temp.resolve( RETEST_PROJECT_ROOT );
		Files.createDirectory( configFolder );
		final Path retestFolder = configFolder.resolve( RETEST_PROJECT_CONFIG_FOLDER );
		Files.createDirectory( retestFolder );
		filterFolder = retestFolder.resolve( FILTER_FOLDER );
		Files.createDirectory( filterFolder );
	}

	@Test
	void getDefaultFilterFiles_should_get_all_filter_files_from_classpath() {
		final List<Pair<String, FilterLoader>> defaultFilterFiles = SearchFilterFiles.getDefaultFilterFiles();
		assertThat( defaultFilterFiles.stream().map( Pair::getLeft ) ).contains( "positioning.filter",
				"visibility.filter" );
	}

	@Test
	@SystemProperty( key = RETEST_PROJECT_ROOT )
	void getProjectFilterFiles_should_only_get_filter_files() throws IOException {
		Files.createTempFile( filterFolder, "random", ".ignore" ).toFile();
		final File colorFilter = Files.createTempFile( filterFolder, "color", FILTER_EXTENSION ).toFile();
		final File webFontFilter = Files.createTempFile( filterFolder, "web-font", FILTER_EXTENSION ).toFile();
		System.setProperty( RETEST_PROJECT_ROOT, filterFolder.toString() );

		final List<Pair<String, FilterLoader>> projectFilterFiles = SearchFilterFiles.getProjectFilterFiles();
		assertThat( projectFilterFiles ).allMatch( pair -> pair.getLeft().endsWith( FILTER_EXTENSION ) );
		assertThat( projectFilterFiles.stream().map( Pair::getLeft ) ).contains(
				colorFilter.toPath().getFileName().toString(), webFontFilter.toPath().getFileName().toString() );
	}

	@Test
	@SystemProperty( key = RETEST_PROJECT_ROOT )
	void filter_mapping_should_prioritize_user_defined_filters() throws Exception {
		final Pair<String, FilterLoader> pair = Pair.of( "some.filter", () -> mock( Filter.class ) );
		final List<Pair<String, FilterLoader>> paths = Arrays.asList( pair, pair );
		SearchFilterFiles.toFileNameFilterMapping( paths );
	}
}
