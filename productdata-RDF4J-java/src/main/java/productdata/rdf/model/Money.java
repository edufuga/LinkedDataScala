package productdata.rdf.model;

import productdata.global.util.GLOBAL;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
* Class Diners 
*/
@SuppressWarnings("serial")
public class Money implements IMoney{

	IRI newInstance;
	public Money(String namespace, String instanceId) {		super();
		newInstance = GLOBAL.factory.createIRI(namespace, instanceId);
		GLOBAL.model.add(this, RDF.TYPE, GLOBAL.factory.createIRI("https://github.com/edufuga/LinkedDataScala/2024/3/linkeddata#Money"));
	}

	public IRI iri()
	{
		return newInstance;
	}

	
	public void setMonetaryValue(float param)
	{
	 GLOBAL.model.add(this, GLOBAL.factory.createIRI("https://github.com/edufuga/LinkedDataScala/2024/3/linkeddata#hasMonetaryValue"), GLOBAL.factory.createLiteral(param));
	}
	
	public String getMonetaryValue(){
		return (GLOBAL.model.filter(this, GLOBAL.factory.createIRI("https://github.com/edufuga/LinkedDataScala/2024/3/linkeddata#hasMonetaryValue"), null).objects().iterator().next()).stringValue();	
	}
	
	public void setCurrency(String param)
	{
	 GLOBAL.model.add(this, GLOBAL.factory.createIRI("https://github.com/edufuga/LinkedDataScala/2024/3/linkeddata#hasCurrency"), GLOBAL.factory.createLiteral(param));
	}
	
	public String getCurrency(){
		return (GLOBAL.model.filter(this, GLOBAL.factory.createIRI("https://github.com/edufuga/LinkedDataScala/2024/3/linkeddata#hasCurrency"), null).objects().iterator().next()).stringValue();	
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
	
	public static Set<IMoney> getAllMoneysObjectsCreated(){
		Set<IMoney> objects = new HashSet<IMoney>();
		objects = GLOBAL.model.filter(null, RDF.TYPE, GLOBAL.factory.createIRI("https://github.com/edufuga/LinkedDataScala/2024/3/linkeddata#Money")).subjects().stream().map(mapper->(IMoney)mapper).collect(Collectors.toSet());
				
		return objects;	
	}
}
