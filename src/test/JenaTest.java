package test;

import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;

public class JenaTest {
	private String sourceURL = "http://www.arbi.com/ontologies/test";
	private String owlFile = "./owl/isro_service.owl";
	public String NS = sourceURL + "#";

	public static void main(String[] ar) {
		new JenaTest();
	}

	public JenaTest() {
		Model model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
		model.read(owlFile);
		
		Resource vcard = model.getResource("http://www.robot-arbi.kr/ontologies/isro_service.owl#MoveToDestination");
		
		StmtIterator iter = model.listStatements();
		Property prop = ResourceFactory.createProperty("http://www.robot-arbi.kr/ontologies/isro_service.owl#hasPlanPrecondition");
		Statement testR = vcard.getProperty(prop);
		System.out.println(testR.getResource().toString());
		
		
		prop = ResourceFactory.createProperty("http://www.robot-arbi.kr/ontologies/isro_service.owl#consistOfStatement");
		iter = testR.getResource().listProperties(prop);
		
		
		System.out.println(testR);
		
		
		//System.out.println(vcard.getProperty(prop).getString());
		
		
		
		
		while (iter.hasNext()) {
			Statement stmt = iter.nextStatement();
			Resource subject = stmt.getSubject(); // get the subject
			Property predicate = stmt.getPredicate(); // get the predicate
			RDFNode object = stmt.getObject(); // get the object
			
			System.out.println("statement start");
			System.out.println("s : " + subject.toString());
			System.out.println("p : " + predicate.toString() + " ");
			if (object instanceof Resource) {
				System.out.println("o : " + object.toString());
			} else {
				// object is a literal
				System.out.println("o : \"" + object.toString() + "\"");
			}

			System.out.println(" .");
		}
	
		// Resource name = vcard.getPropertyResourceValue("");
	}

}
