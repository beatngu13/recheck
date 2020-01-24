package de.retest.recheck.printer;

import java.util.stream.Collectors;

import de.retest.recheck.report.SuiteReplayResult;

public class SuiteReplayResultPrinter implements Printer<SuiteReplayResult> {

	private final TestReplayResultPrinter delegate;

	public SuiteReplayResultPrinter( final DefaultValueFinderProvider provider ) {
		delegate = new TestReplayResultPrinter( provider );
	}

	@Override
	public String toString( final SuiteReplayResult difference, final String indent ) {
		return indent + createDescription( difference ) + "\n" + createDifferences( difference, indent + "\t" );
	}

	private String createDescription( final SuiteReplayResult difference ) {
		final String name = difference.getName();
		final int differences = difference.getDifferencesCount();
		final long deleted = difference.getDeletedCount();
		final long created = difference.getCreatedCount();
		final long maintained = difference.getMaintainedCount();
		final int states = difference.getTestReplayResults().size();
		return String.format( "Suite '%s' has %d difference(s) (%d deleted, %d created, %d maintained) in %d test(s):",
				name, differences, deleted, created, maintained, states );
	}

	private String createDifferences( final SuiteReplayResult difference, final String indent ) {
		return difference.getTestReplayResults().stream() //
				.filter( testReplayResult -> !testReplayResult.isEmpty() ) //
				.map( d -> delegate.toString( d, indent ) ) //
				.collect( Collectors.joining( "\n" ) );
	}

}
