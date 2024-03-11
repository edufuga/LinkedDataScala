package productdata.rdf.model;

import productdata.global.util.GLOBAL;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
* Class Service 
*/
@SuppressWarnings("serial")
public class Service implements IService{

	IRI newInstance;
	public Service(String namespace, String instanceId) {		super();
		newInstance = GLOBAL.factory.createIRI(namespace, instanceId);
		GLOBAL.model.add(this, RDF.TYPE, GLOBAL.factory.createIRI("https://github.com/edufuga/LinkedDataScala/2024/3/linkeddata#Service"));
	}

	public IRI iri()
	{
		return newInstance;
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
    * This is just an email address.
	*/	
	public void setProductManager(String param)
	{
	 GLOBAL.model.add(this, GLOBAL.factory.createIRI("https://github.com/edufuga/LinkedDataScala/2024/3/linkeddata#hasProductManager"), GLOBAL.factory.createLiteral(param));
	}
	
	public String getProductManager(){
		return (GLOBAL.model.filter(this, GLOBAL.factory.createIRI("https://github.com/edufuga/LinkedDataScala/2024/3/linkeddata#hasProductManager"), null).objects().iterator().next()).stringValue();	
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

    public void addPrice (IMoney parameter)
	{
		GLOBAL.model.add(this, GLOBAL.factory.createIRI("https://github.com/edufuga/LinkedDataScala/2024/3/linkeddata#hasPrice"), parameter);
	}
	
	public Set<IMoney> getPrice (){
		Set<IMoney> Price = new HashSet<IMoney>();
		GLOBAL.model.filter(this, GLOBAL.factory.createIRI("https://github.com/edufuga/LinkedDataScala/2024/3/linkeddata#hasPrice"), null).objects().forEach(action->{
			if(action instanceof Money) {
				Price.add((Money)action);			
			}
		});
		return Price;	
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
	
	public static Set<IService> getAllServicesObjectsCreated(){
		Set<IService> objects = new HashSet<IService>();
		objects = GLOBAL.model.filter(null, RDF.TYPE, GLOBAL.factory.createIRI("https://github.com/edufuga/LinkedDataScala/2024/3/linkeddata#Service")).subjects().stream().map(mapper->(IService)mapper).collect(Collectors.toSet());
				
		return objects;	
	}
}
