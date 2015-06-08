import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;


public class RestrictionClass2 {
	
	public static void main(String[] args) {
		String base = "http://onotology.com/";
		
		OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM_RULE_INF);
		InfModel infModel = ModelFactory.createRDFSModel(ontModel);
		
		OntClass food = ontModel.createClass(base + "Food");
		food.addProperty(RDF.type, OWL.Class);
		OntClass person = ontModel.createClass(base + "Person");
		person.addProperty(RDF.type, OWL.Class);
		
		OntProperty eats = ontModel.createOntProperty(base + "eats");
		eats.addRange(food);
		eats.addDomain(person);
		
		Individual maverick = person.createIndividual(base + "Maverick");
		Individual steak = food.createIndividual(base + "Steak");
		maverick.addProperty(eats, steak);
		
		OntClass vegetarian = ontModel.createClass(base + "Vegetarian");
		vegetarian.addProperty(RDF.type, OWL.Class);
		person.addSubClass(vegetarian);
		
		OntClass vegetarianFood = ontModel.createClass(base + "VegetarianFood");
		vegetarianFood.addProperty(RDF.type, OWL.Class);
		
		Individual jen = vegetarian.createIndividual(base + "Jen");
		Individual marzipan = vegetarianFood.createIndividual(base + "Marzipan");
		jen.addProperty(eats, marzipan);
		
		vegetarian.addSubClass(ontModel.createAllValuesFromRestriction(base + "veggieFoodRestriction", eats, vegetarianFood));
		
		infModel.setNsPrefix("owl", OWL.getURI());
		infModel.setNsPrefix("rdf", RDF.getURI());
		infModel.setNsPrefix("rdfs", RDFS.getURI());
		infModel.setNsPrefix("", base);
		infModel.write(System.out, "TURTLE");
		
		
	}

}
