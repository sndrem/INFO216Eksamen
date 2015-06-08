import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.hp.hpl.jena.update.UpdateAction;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.hp.hpl.jena.vocabulary.VCARD;


public class SemanticModel {

	public SemanticModel() {
		// TODO Auto-generated constructor stub
	}
	
	 public static void main(String[] args) {

	     //Declare the base of our ontology
		 String base = "http://ourOntology.com/";
		 
		 // Declare a model which will hold our data / triples
		 // We declare it as an ontmodel so we can work with classes
		 // The OntModelSpec.OWL_DL_MEM_RULE_INF tells the modelFactory what type of reasoning we want
		 OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM_RULE_INF);
		 // We would also like to store our data somewhere safe, such as a dataset...
		 Dataset ds = TDBFactory.createDataset("developerData");
		 Model model = ds.getDefaultModel();
		 ontModel.add(model);
		 model.begin();
		 
		 // Add some developers to our model
		 // The developers all have some properties
		 
		 Resource einar = ontModel.createResource(base + "EinarJohansen");
		 Resource sindre = ontModel.createResource(base + "SindreMoldeklev");
		 Resource haakon = ontModel.createResource(base + "HaakonRoald");
		 
		 // Define some properties for our developers
		 Property devLanguage = ontModel.createProperty(base + "developerLanguage");
		 Property workStation = ontModel.createProperty(base + "workStation");
		 
		 // Add the properties to the resources
		 einar.addProperty(devLanguage, "Java");
		 einar.addProperty(workStation, "Windows");
		 
		 sindre.addProperty(devLanguage, "Java");
		 sindre.addProperty(workStation, "Mac");
		 
		 haakon.addProperty(devLanguage, "HTML / CSS");
		 haakon.addProperty(workStation, "Windows");
		 
		 // Now we want to say that Einar, Sindre and Haakon are of the class developers
		 // To work with classes, we have to change our model to become a owlModel instead
		 // We are also going to discard our resources, and create individuals instead
		 OntClass developerClass = ontModel.createClass(base + "Developer");
		 
		 // In fact, we can create individuals based on our resources, to connect them together. 
		 Individual einarInd = developerClass.createIndividual(einar.getURI());
		 Individual sindreInd = developerClass.createIndividual(sindre.getURI());
		 Individual haakonInd = developerClass.createIndividual(haakon.getURI());
		 
		 // Now we want to create a subclass of developer, called internDeveloper
		 OntClass internDeveloper = ontModel.createClass(base + "InternDeveloper");
		 developerClass.addSubClass(internDeveloper);
		 
		 // Lets create a new individual which will be an intern developer
		 Individual kjetil = internDeveloper.createIndividual(base + "KjetilJVillanger");
		 kjetil.addProperty(VCARD.FN, "Kjetil Jacobsen Villanger");
		 kjetil.addProperty(devLanguage, "C#");
		 kjetil.addProperty(workStation, "Mac and Windows");
		 
		 // Now we want to connect all our developers to a firm
		 // Lets first create our firm class
		 OntClass firmClass = ontModel.createClass(base + "Firm");
		 // We also want to print out a human readable comment and label for displaying of our firm
		 firmClass.addProperty(RDFS.comment, "This class represents the firm where developers work");
		 firmClass.addProperty(RDFS.label, "The most awesome firm in history");
		 
		 // Then we will create a property relating the developers with the firm
		 // We will call the property worksFor
		 Property worksFor = ontModel.createProperty(base + "worksFor");
		 
		 // Now we simply relate our resources and individuals with the worksFor-property
		 sindre.addProperty(worksFor, firmClass);
		 kjetil.addProperty(worksFor, firmClass);
		 einar.addProperty(worksFor, firmClass);
		 haakon.addProperty(worksFor, firmClass);
		 
		 // One day, a super hot girl walked in to the firm and was offered a job. Our newly employed developer Sigve said it was his wife. 
		 // There was just one catch, the model could not confirm that they were married. Why not?
		 
		 Individual sigve = developerClass.createIndividual(base + "SigveSolvaag");
		 sigve.addProperty(worksFor, firmClass);
		 sigve.addProperty(devLanguage, "Pascal");
		 sigve.addProperty(workStation, "Linux");
		 sigve.addProperty(FOAF.knows, kjetil);
		 kjetil.addProperty(FOAF.knows, sigve);
		 
		 
		 Individual hotGirl = internDeveloper.createIndividual(base + "MeganFox");
		 hotGirl.addProperty(VCARD.FN, "Megan Fox");
		 hotGirl.addProperty(devLanguage, "Javascript");
		 hotGirl.addProperty(workStation, "Windows and Linux");
		 
		 OntProperty developerName = ontModel.createOntProperty(base + "developerName");
		 developerName.addProperty(RDFS.subPropertyOf, RDFS.label);
		 
		 
		 // Now we create the marriedTo property. Note that if we just create the property, then it will say that Sigve is married to hotGirl, but not vice versa. 
		 // We have to make marriedTo a symmetric property. Try commenting on and off the convertTo-method while running the program to see what the question below prints out.
		 OntProperty marriedTo = ontModel.createOntProperty(base + "marriedTo");
		 marriedTo.convertToSymmetricProperty();
		 sigve.addProperty(marriedTo, hotGirl);
		 
		 // Lets see now, if hotGirl is married to Sigve
		 System.out.println("Is Megan Fox married to Sigve? " + hotGirl.hasProperty(marriedTo));
		 
		 
		 // If we want to add a new developer to the firm, we can do it by creating a individual and then adding properties, or we can do it as a Sparql update
		 // like this
		 InfModel infModel = ModelFactory.createRDFSModel(ontModel);
		 UpdateAction.parseExecute(""
		 		+ "PREFIX dev: <http://ourOntology.com/> "
		 		+ ""
		 		+ "INSERT DATA { "
		 		+ "dev:Alexander a dev:Developer; "
		 		+ "dev:workStation 'Windows Surface'; "
		 		+ "dev:developerLanguage 'Java' . }", infModel);
		 
		 // lets insert one more developer
		 String insertString = ""
		 		+ "PREFIX dev: <http://ourOntology.com/> "
		 		+ ""
		 		+ "INSERT DATA { "
		 		+ "dev:Espen a dev:InternDeveloper; "
		 		+ "dev:worksFor dev:Firm; "
		 		+ "dev:developerLanguage 'Javascript & HTML5' . }";
		 UpdateAction.parseExecute(insertString, infModel);
	
		 
		 // One day, a person walked into the firm. The boss thought something was very familiar and indeed, the person had been working at the firm earlier
		 // The boss, an avid semantic entuisast knew they had a record on him earlier. His data looked like this
		 Resource oldDeveloper = ontModel.createResource(base + "RetiredDeveloper");
		 Property isRetired = ontModel.createProperty(base + "isRetired");
		 oldDeveloper.addLiteral(isRetired, true);
		 
		 // The assistant had already created a new record with information about this new/old guy. It looked like this
		 Individual newDev = developerClass.createIndividual(base + "NewDeveloper");
		 newDev.addProperty(VCARD.FN, "New Developer");
		 
		 // The boss told the assitant to hook up the old data with the new one like so
		 oldDeveloper.addProperty(OWL.sameAs, newDev);
		 System.out.println("Is the new developer the same guy as the old developer? " + newDev.hasProperty(OWL.sameAs));
		 
		 
		 // At last, a newspaper wanted to know about the firm and the people who worked there. They decided to do the 
		 // interview using only sparql queries. 
		 Query query1 = QueryFactory.create(""
		 		+ "PREFIX dev: <http://ourOntology.com/> "
		 		+ "SELECT ?developer ?language  "
		 		+ "WHERE { ?developer a dev:Developer; "
		 		+ " dev:developerLanguage ?language .}");
		 
		 QueryExecution qx = QueryExecutionFactory.create(query1, ontModel);
		 
		 ResultSet rs = qx.execSelect();
		 System.out.println("Answers from the interview");
		 while(rs.hasNext()) {
			 QuerySolution qs = rs.nextSolution();
			 System.out.println(qs.toString());
		 }
		 //Just for line break in the console
		 System.out.println();
		 
		 Statement statement = ontModel.createStatement(oldDeveloper, devLanguage, "Java, CSS & HTML");
		 ontModel.add(statement);
		 
		 
		 // Set prefixes when printing model
		 ontModel.setNsPrefix("dev", base);
		 
		 // Lets create file with out triples in the turtle format
		 try {
			ontModel.getBaseModel().write(new FileOutputStream("developers.ttl"), "TURTLE");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 // Lets also read some external data we had from a file
		 try {
			ontModel.getBaseModel().read(new FileInputStream("externalData.ttl"),null, "TURTLE");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 // We can print out all the classes that a individual belongs to
		 // Note that Kjetil is part of both InternDeveloper and Developer
		 // That is because InternDeveloper is a subclass of Developer.
		 System.out.println("Kjetil is part of these classes...");
		 for(OntClass cls : kjetil.listOntClasses(false).toList()) {
			 System.out.println(cls.toString());
		 }
		 System.out.println();
		 
		 // Sindre on the other hand, is not part of InternDeveloper
		 System.out.println("Sindre is part of these classes...");
		 for(OntClass cls : sindreInd.listOntClasses(false).toList()) {
			 System.out.println(cls.toString());
		 }
		 System.out.println();
		 
		 // Print out the model. To be able to print it we have to get the 
		 // base model of our ontology model.
//		 ontModel.getBaseModel().write(System.out, "TURTLE");
		 ontModel.write(System.out, "TURTLE");
		 model.close();
		 ontModel.close();
		 ds.close();
		 
	 }
}
