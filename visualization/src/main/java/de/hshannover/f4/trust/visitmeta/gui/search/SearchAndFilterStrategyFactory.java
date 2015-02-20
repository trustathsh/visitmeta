package de.hshannover.f4.trust.visitmeta.gui.search;


public class SearchAndFilterStrategyFactory {

	public static SeachAndFilterStrategy create(
			SearchAndFilterStrategyType type, Searchable searchableGraphPanel) {
		switch (type) {
			case SIMPLE_SEARCH_AND_NO_FILTER:
				return new SimpleSearchAndNoFilter(searchableGraphPanel);
			default:
				throw new IllegalArgumentException(
						"No strategy for given type '" + type + "'");
		}
	}

}
