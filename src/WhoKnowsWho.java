import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import com.hp.hpl.jena.vocabulary.XSD;


public class WhoKnowsWho {

	public WhoKnowsWho() {
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String[] args) {
		OntModel ontModel = ModelFactory.createOntologyModel();
		String base = "http://ontology.com/";
		
		Resource sindre = ontModel.createResource(base + "Sindre");
		Resource einar = ontModel.createResource(base + "Einar");
		Resource haakon = ontModel.createResource(base + "Haakon");
		
		sindre.addProperty(FOAF.knows, einar);
		sindre.addProperty(FOAF.knows, haakon);
		sindre.addLiteral(FOAF.mbox, "sndrem@live.no");
		sindre.addLiteral(FOAF.homepage, "SindreMoldeklev.com");
		sindre.addLiteral(FOAF.name, "Sindre Moldeklev");
		
		einar.addLiteral(FOAF.nick, "Einar Beinhard");
		einar.addLiteral(FOAF.name, "Einar S. Johansen");
		einar.addProperty(FOAF.knows, sindre);
		einar.addProperty(FOAF.knows, haakon);
		
		haakon.addLiteral(FOAF.name, "Håkon Roald");
		haakon.addLiteral(FOAF.homepage, "hakondesign.no");
		haakon.addProperty(FOAF.knows, einar);
		haakon.addProperty(FOAF.knows, sindre);
		
		ontModel.setNsPrefix("", base);
		ontModel.setNsPrefix("xsd", XSD.getURI());
		ontModel.setNsPrefix("foaf", FOAF.getURI());
		ontModel.write(System.out, "TURTLE");
		
	}

}
