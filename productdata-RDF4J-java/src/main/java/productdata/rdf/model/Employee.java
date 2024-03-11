package productdata.rdf.model;

import productdata.global.util.GLOBAL;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
* Class Employee 
*/
@SuppressWarnings("serial")
public class Employee implements IEmployee{

	IRI newInstance;
	public Employee(String namespace, String instanceId) {		super();
		newInstance = GLOBAL.factory.createIRI(namespace, instanceId);
		GLOBAL.model.add(this, RDF.TYPE, GLOBAL.factory.createIRI("https://github.com/edufuga/LinkedDataScala/2024/3/linkeddata#Employee"));
	}

	public IRI iri()
	{
		return newInstance;
	}

	
	public void setProductExpertFor(String param)
	{
	 GLOBAL.model.add(this, GLOBAL.factory.createIRI("https://github.com/edufuga/LinkedDataScala/2024/3/linkeddata#isProductExpertFor"), GLOBAL.factory.createLiteral(param));
	}
	
	public String getProductExpertFor(){
		return (GLOBAL.model.filter(this, GLOBAL.factory.createIRI("https://github.com/edufuga/LinkedDataScala/2024/3/linkeddata#isProductExpertFor"), null).objects().iterator().next()).stringValue();	
	}
	
	public void setName(String param)
	{
	 GLOBAL.model.add(this, GLOBAL.factory.createIRI("https://github.com/edufuga/LinkedDataScala/2024/3/linkeddata#hasName"), GLOBAL.factory.createLiteral(param));
	}
	
	public String getName(){
		return (GLOBAL.model.filter(this, GLOBAL.factory.createIRI("https://github.com/edufuga/LinkedDataScala/2024/3/linkeddata#hasName"), null).objects().iterator().next()).stringValue();	
	}
	
	public void setAddress(String param)
	{
	 GLOBAL.model.add(this, GLOBAL.factory.createIRI("https://github.com/edufuga/LinkedDataScala/2024/3/linkeddata#hasAddress"), GLOBAL.factory.createLiteral(param));
	}
	
	public String getAddress(){
		return (GLOBAL.model.filter(this, GLOBAL.factory.createIRI("https://github.com/edufuga/LinkedDataScala/2024/3/linkeddata#hasAddress"), null).objects().iterator().next()).stringValue();	
	}
	
	public void setPhone(String param)
	{
	 GLOBAL.model.add(this, GLOBAL.factory.createIRI("https://github.com/edufuga/LinkedDataScala/2024/3/linkeddata#hasPhone"), GLOBAL.factory.createLiteral(param));
	}
	
	public String getPhone(){
		return (GLOBAL.model.filter(this, GLOBAL.factory.createIRI("https://github.com/edufuga/LinkedDataScala/2024/3/linkeddata#hasPhone"), null).objects().iterator().next()).stringValue();	
	}
	
	public void setEmail(String param)
	{
	 GLOBAL.model.add(this, GLOBAL.factory.createIRI("https://github.com/edufuga/LinkedDataScala/2024/3/linkeddata#hasEmail"), GLOBAL.factory.createLiteral(param));
	}
	
	public String getEmail(){
		return (GLOBAL.model.filter(this, GLOBAL.factory.createIRI("https://github.com/edufuga/LinkedDataScala/2024/3/linkeddata#hasEmail"), null).objects().iterator().next()).stringValue();	
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
	
	public static Set<IEmployee> getAllEmployeesObjectsCreated(){
		Set<IEmployee> objects = new HashSet<IEmployee>();
		objects = GLOBAL.model.filter(null, RDF.TYPE, GLOBAL.factory.createIRI("https://github.com/edufuga/LinkedDataScala/2024/3/linkeddata#Employee")).subjects().stream().map(mapper->(IEmployee)mapper).collect(Collectors.toSet());
				
		return objects;	
	}
}
