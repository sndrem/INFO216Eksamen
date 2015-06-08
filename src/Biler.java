import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;


public class Biler {
	
	public static void main(String[] args) {
		
		OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM_RULE_INF);
		InfModel infModel = ModelFactory.createRDFSModel(ontModel);
		
		
		try {
			ontModel.read(new FileInputStream("biler.ttl"), null, "TURTLE");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		infModel.write(System.out, "TURTLE");
		
	}

}
