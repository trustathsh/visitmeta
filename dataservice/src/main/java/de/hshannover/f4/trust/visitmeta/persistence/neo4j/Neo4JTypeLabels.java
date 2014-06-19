package de.hshannover.f4.trust.visitmeta.persistence.neo4j;

import org.neo4j.graphdb.Label;

public enum Neo4JTypeLabels implements Label {
		LINK,
		HISTORY,
        IDENTIFIER,
        CHANGE,
        METADATA
}
