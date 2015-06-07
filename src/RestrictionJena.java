import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.Restriction;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;


public class RestrictionJena {

	public RestrictionJena() {
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String[] args) {
		String base = "http://foodontology.com/";
		
		OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM_RULE_INF);
		InfModel infModel = ModelFactory.createRDFSModel(ontModel);
		
		OntClass personCls = ontModel.createClass(base + "Person");
		personCls.addProperty(RDF.type, OWL.Class);
		
		OntClass foodCls = ontModel.createClass(base + "Food");
		foodCls.addProperty(RDF.type, OWL.Class);
		
		OntProperty eats = ontModel.createOntProperty(base + "eats");
		
		eats.addDomain(personCls);
		eats.addRange(foodCls);
		
		OntClass vegetarianFoodCls = ontModel.createClass(base + "VegetarianFood");
//		foodCls.addSubClass(vegetarianFoodCls);
		
		OntProperty veggiesEatVeggieFoodRestriction = ontModel.createOntProperty(base + "veggieEatsVeggieFood");
		veggiesEatVeggieFoodRestriction.addProperty(RDF.type, OWL.Restriction)
										.addProperty(OWL.onProperty, eats)
										.addProperty(OWL.allValuesFrom, vegetarianFoodCls);
		
		DatatypeProperty veggieProp = ontModel.createDatatypeProperty(base + "isVegetarian");
		
		OntClass vegetarianClass = ontModel.createAllValuesFromRestriction(base + "Vegetarian", eats, vegetarianFoodCls);
	
		
		Individual sindre = ontModel.createIndividual(base + "Sindre", personCls);
		Individual einar = ontModel.createIndividual(base + "Einar", personCls);
		
		Individual steak = foodCls.createIndividual(base + "Steak");
		Individual beans = vegetarianFoodCls.createIndividual(base + "Beans");
		
		einar.addProperty(eats, steak);
		sindre.addProperty(eats, beans);
		
//		ExtendedIterator<Restriction> rsIterator = ontModel.listRestrictions();
//		while(rsIterator.hasNext()) {
//			Restriction rs = rsIterator.next();
//			System.out.println(rs);
//		}
		
		for(OntClass cls : sindre.listOntClasses(false).toList()) {
			System.out.println(cls.toString());
		}
		
		
		infModel.write(System.out, "TURTLE");
		System.out.println(sindre.hasOntClass(vegetarianClass));
		
	}

}
