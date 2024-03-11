package productdata.rdf.model;

import productdata.global.util.GLOBAL;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
* Class Organització 
*/
@SuppressWarnings("serial")
public class Organisation implements IOrganisation{

	IRI newInstance;
	public Organisation(String namespace, String instanceId) {		super();
		newInstance = GLOBAL.factory.createIRI(namespace, instanceId);
		GLOBAL.model.add(this, RDF.TYPE, GLOBAL.factory.createIRI("https://github.com/edufuga/LinkedDataScala/2024/3/linkeddata#Organisation"));
	}

	public IRI iri()
	{
		return newInstance;
	}

    /**
    * This property is meant for relating a given entity to a list of several departments.

Notice that OWL has no construct for a list of entities. Instead, we use a non-functional property for it. The "list" is an outcome of the graph structure, i.e. it isn't explicitly represented by a data structure in OWL nor in the resulting graph.
	*/
    public void addDepartments (IDepartment parameter)
	{
		GLOBAL.model.add(this, GLOBAL.factory.createIRI("https://github.com/edufuga/LinkedDataScala/2024/3/linkeddata#hasDepartment"), parameter);
	}
	
	public Set<IDepartment> getDepartments (){
		Set<IDepartment> Departments = new HashSet<IDepartment>();
		GLOBAL.model.filter(this, GLOBAL.factory.createIRI("https://github.com/edufuga/LinkedDataScala/2024/3/linkeddata#hasDepartment"), null).objects().forEach(action->{
			if(action instanceof Department) {
				Departments.add((Department)action);			
			}
		});
		return Departments;	
	}
	
	@Override
	public String stringValue() {
		return this.newInstance.getLocalName();
	}

	@Override
	public String getNamespace() {
		return this.newInstance.getNamespace();
	}

	@Override
	public String getLocalName() {
		return this.newInstance.getLocalName();
	}
	
	@Override
	public String toString() {
		return this.newInstance.toString();
	}
	
	public static Set<IOrganisation> getAllOrganisationsObjectsCreated(){
		Set<IOrganisation> objects = new HashSet<IOrganisation>();
		objects = GLOBAL.model.filter(null, RDF.TYPE, GLOBAL.factory.createIRI("https://github.com/edufuga/LinkedDataScala/2024/3/linkeddata#Organisation")).subjects().stream().map(mapper->(IOrganisation)mapper).collect(Collectors.toSet());
				
		return objects;	
	}
}
