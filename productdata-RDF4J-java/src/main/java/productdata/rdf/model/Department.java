package productdata.rdf.model;

import productdata.global.util.GLOBAL;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
* Class Department 
*/
@SuppressWarnings("serial")
public class Department implements IDepartment{

	IRI newInstance;
	public Department(String namespace, String instanceId) {		super();
		newInstance = GLOBAL.factory.createIRI(namespace, instanceId);
		GLOBAL.model.add(this, RDF.TYPE, GLOBAL.factory.createIRI("https://github.com/edufuga/LinkedDataScala/2024/3/linkeddata#Department"));
	}

	public IRI iri()
	{
		return newInstance;
	}

	/** 
    * This property is meant for relating a given entity to a list of several products. Instead of having the full Product in the range of the function (property), we have only their identifiers. This weak linking is enough, but an alternative could be having an _object_ property with Product in the range.
    * 
    * Notice that OWL has no construct for a list of entities. Instead, we use a non-functional property for it. The "list" is an outcome of the graph structure, i.e. it isn't explicitly represented by a data structure in OWL nor in the resulting graph.
	*/	
	public void setProductId(String param)
	{
	 GLOBAL.model.add(this, GLOBAL.factory.createIRI("https://github.com/edufuga/LinkedDataScala/2024/3/linkeddata#hasProductId"), GLOBAL.factory.createLiteral(param));
	}
	
	public String getProductId(){
		return (GLOBAL.model.filter(this, GLOBAL.factory.createIRI("https://github.com/edufuga/LinkedDataScala/2024/3/linkeddata#hasProductId"), null).objects().iterator().next()).stringValue();	
	}
	
	public void setName(String param)
	{
	 GLOBAL.model.add(this, GLOBAL.factory.createIRI("https://github.com/edufuga/LinkedDataScala/2024/3/linkeddata#hasName"), GLOBAL.factory.createLiteral(param));
	}
	
	public String getName(){
		return (GLOBAL.model.filter(this, GLOBAL.factory.createIRI("https://github.com/edufuga/LinkedDataScala/2024/3/linkeddata#hasName"), null).objects().iterator().next()).stringValue();	
	}
	
	public void setId(String param)
	{
	 GLOBAL.model.add(this, GLOBAL.factory.createIRI("https://github.com/edufuga/LinkedDataScala/2024/3/linkeddata#hasId"), GLOBAL.factory.createLiteral(param));
	}
	
	public String getId(){
		return (GLOBAL.model.filter(this, GLOBAL.factory.createIRI("https://github.com/edufuga/LinkedDataScala/2024/3/linkeddata#hasId"), null).objects().iterator().next()).stringValue();	
	}
	/** 
    * This property is meant for relating a given entity to a list of several services. Instead of having the full Service in the range of the function (property), we have only their identifiers. This weak linking is enough, but an alternative could be having an _object_ property with Service  in the range.
    * 
    * Notice that OWL has no construct for a list of entities. Instead, we use a non-functional property for it. The "list" is an outcome of the graph structure, i.e. it isn't explicitly represented by a data structure in OWL nor in the resulting graph.
	*/	
	public void setServiceId(String param)
	{
	 GLOBAL.model.add(this, GLOBAL.factory.createIRI("https://github.com/edufuga/LinkedDataScala/2024/3/linkeddata#hasServiceId"), GLOBAL.factory.createLiteral(param));
	}
	
	public String getServiceId(){
		return (GLOBAL.model.filter(this, GLOBAL.factory.createIRI("https://github.com/edufuga/LinkedDataScala/2024/3/linkeddata#hasServiceId"), null).objects().iterator().next()).stringValue();	
	}
    /**
    * This property is meant for relating a given entity to a list of several employees.

Notice that OWL has no construct for a list of entities. Instead, we use a non-functional property for it. The "list" is an outcome of the graph structure, i.e. it isn't explicitly represented by a data structure in OWL nor in the resulting graph.
	*/
    public void addEmployees (IEmployee parameter)
	{
		GLOBAL.model.add(this, GLOBAL.factory.createIRI("https://github.com/edufuga/LinkedDataScala/2024/3/linkeddata#hasEmployee"), parameter);
	}
	
	public Set<IEmployee> getEmployees (){
		Set<IEmployee> Employees = new HashSet<IEmployee>();
		GLOBAL.model.filter(this, GLOBAL.factory.createIRI("https://github.com/edufuga/LinkedDataScala/2024/3/linkeddata#hasEmployee"), null).objects().forEach(action->{
			if(action instanceof Employee) {
				Employees.add((Employee)action);			
			}
		});
		return Employees;	
	}
	

    public void addManager (IManager parameter)
	{
		GLOBAL.model.add(this, GLOBAL.factory.createIRI("https://github.com/edufuga/LinkedDataScala/2024/3/linkeddata#hasManager"), parameter);
	}
	
	public Set<IManager> getManager (){
		Set<IManager> Manager = new HashSet<IManager>();
		GLOBAL.model.filter(this, GLOBAL.factory.createIRI("https://github.com/edufuga/LinkedDataScala/2024/3/linkeddata#hasManager"), null).objects().forEach(action->{
			if(action instanceof Manager) {
				Manager.add((Manager)action);			
			}
		});
		return Manager;	
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
	
	public static Set<IDepartment> getAllDepartmentsObjectsCreated(){
		Set<IDepartment> objects = new HashSet<IDepartment>();
		objects = GLOBAL.model.filter(null, RDF.TYPE, GLOBAL.factory.createIRI("https://github.com/edufuga/LinkedDataScala/2024/3/linkeddata#Department")).subjects().stream().map(mapper->(IDepartment)mapper).collect(Collectors.toSet());
				
		return objects;	
	}
}
