package de.hshannover.f4.trust.visitmeta.dataservice.factories;

import static de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JPropertyConstants.KEY_META_CARDINALITY;
import static de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JPropertyConstants.KEY_RAW_DATA;
import static de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JPropertyConstants.KEY_TIMESTAMP_DELETE;
import static de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JPropertyConstants.KEY_TIMESTAMP_PUBLISH;
import static de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JPropertyConstants.KEY_HASH;
import static de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JPropertyConstants.KEY_TYPE_NAME;
import static de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JPropertyConstants.VALUE_META_CARDINALITY_MULTI;
import static de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JPropertyConstants.VALUE_META_CARDINALITY_SINGLE;

import java.util.HashMap;
import java.util.Iterator;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseSettings;
import org.neo4j.graphdb.index.ReadableIndex;
import org.neo4j.test.TestGraphDatabaseFactory;
import org.neo4j.tooling.GlobalGraphOperations;

import de.hshannover.f4.trust.visitmeta.persistence.neo4j.LinkTypes;
import de.hshannover.f4.trust.visitmeta.persistence.neo4j.Neo4JTypeLabels;

public class Neo4JTestDatabaseFactory {

	@SuppressWarnings("unchecked")
	public static GraphDatabaseService createGraphDB(HashMap<String, Object> map) {
		GraphDatabaseService db = createGraphDB();
		
		for (String key : map.keySet()) {
			createLink((HashMap<String, Object>) map.get(key), db);
		}
		
		return db;
	}
	
	
	public GraphDatabaseService void createGraphDB() {
		GraphDatabaseService db = new TestGraphDatabaseFactory().newImpermanentDatabaseBuilder().
				setConfig(GraphDatabaseSettings.node_keys_indexable, KEY_HASH).
				setConfig(GraphDatabaseSettings.node_auto_indexing, "true").
				newGraphDatabase();

		return db;
	}
	
	
	public static void printDB(GraphDatabaseService db) {
		GlobalGraphOperations p = GlobalGraphOperations.at(db);
		Iterator<Node> i = p.getAllNodes().iterator();
		
		while(i.hasNext()) {
			Node tmp = i.next();
			if(tmp.hasLabel(Neo4JTypeLabels.LINK)) {
				System.out.println("LINK-------------------------");
				System.out.println("ID: " + tmp.getId());
				for(String key : tmp.getPropertyKeys()) {
					System.out.println(key + ": " + tmp.getProperty(key));
				}
				System.out.println("/LINK------------------------");
			}
			else if(tmp.hasLabel(Neo4JTypeLabels.IDENTIFIER)) {
				System.out.println("IDENTIFIER-------------------");
				System.out.println("ID: " + tmp.getId());
				for(String key : tmp.getPropertyKeys()) {
					System.out.println(key + ": " + tmp.getProperty(key));
				}
				System.out.println("/IDENTIFIER------------------");
			} else if(tmp.hasLabel(Neo4JTypeLabels.METADATA)) {
				System.out.println("METADATA---------------------");
				System.out.println("ID: " + tmp.getId());
				for(String key : tmp.getPropertyKeys()) {	
					System.out.println(key + ": " + tmp.getProperty(key));
				}
				System.out.println("/METADATA--------------------");
			} else {
				System.out.println("unknown node");
			}
		}	
	}

	/**
	 * 
	 * @param map
	 *            Containing test data. Looks like: 
	 *            (link1)
	 *            	first
	 *            		(identifier data)
	 *            	second 
	 *            		(identifier data) 
	 *            	metadata 
	 *            		(meta1)
	 *            			(metadata data)
	 *            		(meta2)
	 *            			(metadata data)
	 *            (link2)
	 *            	first
	 *            		(identifier data)
	 *            	metadata
	 *            		meta1
	 *            			(metadata data)
	 * @param db
	 *            The Graphdatabase which should be filled with test data.
	 * @return Node of the link (or identifier).
	 */
	@SuppressWarnings("unchecked")
	private static Node createLink(HashMap<String, Object> map, GraphDatabaseService db) {
		Node link = null;
		try(Transaction tx = db.beginTx()) {
			if (map.containsKey("second")) {
				link = db.createNode();
				link.addLabel(Neo4JTypeLabels.LINK);
				Node id1 = searchOrCreateIdentifier((HashMap<String, Object>) map.get("first"), db);
				Node id2 = searchOrCreateIdentifier((HashMap<String, Object>) map.get("second"), db);
	
				id1.createRelationshipTo(link, LinkTypes.Link);
				id2.createRelationshipTo(link, LinkTypes.Link);
	
			} else {
				link = searchOrCreateIdentifier((HashMap<String, Object>) map.get("first"), db);
			}
			HashMap<String, Object> metadata = (HashMap<String, Object>) map.get("metadata");
			for (String key : metadata.keySet()) {
				link.createRelationshipTo(createMetadata((HashMap<String, Object>) metadata.get(key), db), LinkTypes.Meta);
			}
			tx.success();
		}

		return link;
	}

	/**
	 * 
	 * @param map
	 * 			  Containing test data. Looks like:
	 * 			  type
	 * 			  rawData
	 * 			  properties
	 * 			  	(properties map)
	 * @param db
	 *            The Graphdatabase which should be filled with test data.
	 * @return Node of the identifier.
	 */
	@SuppressWarnings("unchecked")
	private static Node searchOrCreateIdentifier(HashMap<String, Object> map, GraphDatabaseService db) {
		Node id = null;

		ReadableIndex<Node> ri = db.index().getNodeAutoIndexer().getAutoIndex();
		id = ri.get(KEY_HASH, map.get("rawData")).getSingle();

		if (id == null) {
			try(Transaction tx = db.beginTx()) {
				id = db.createNode();
				id.addLabel(Neo4JTypeLabels.IDENTIFIER);
				id.setProperty(KEY_TYPE_NAME, map.get("type"));
				id.setProperty(KEY_RAW_DATA, map.get("rawData"));
				HashMap<String, Object> properties = (HashMap<String, Object>) map.get("properties");
				for (String key : properties.keySet()) {
					id.setProperty(key, properties.get(key));
				}
				id.setProperty(KEY_HASH, map.get("rawData"));
				tx.success();
			}
		}

		return id;
	}

	/**
	 * 
	 * @param map
	 * 			  Containing test data. Looks like:
	 * 			  type
	 * 			  singleValue
	 * 			  pubStamp
	 *			  delStamp
	 * 			  rawData
	 * 			  properties
	 * 			  	(properties map)
	 * @param db
	 *            The Graphdatabase which should be filled with test data.
	 * @return Node of the metadata.
	 */
	@SuppressWarnings("unchecked")
	private static Node createMetadata(HashMap<String, Object> map, GraphDatabaseService db) {
		Node meta = null;
		try(Transaction tx = db.beginTx()) {
			meta = db.createNode();
			meta.addLabel(Neo4JTypeLabels.METADATA);
			meta.setProperty(KEY_TYPE_NAME, map.get("type"));
			meta.setProperty(KEY_META_CARDINALITY, ((boolean) map.get("singleValue")) ? VALUE_META_CARDINALITY_SINGLE
					: VALUE_META_CARDINALITY_MULTI);
			meta.setProperty(KEY_TIMESTAMP_PUBLISH, map.get("pubStamp"));
			meta.setProperty(KEY_TIMESTAMP_DELETE, map.get("delStamp"));
			meta.setProperty(KEY_RAW_DATA, map.get("rawData"));
			HashMap<String, Object> properties = (HashMap<String, Object>) map.get("properties");
			for (String key : properties.keySet()) {
				meta.setProperty(key, properties.get(key));
			}
			tx.success();
		}
		return meta;
	}
}
