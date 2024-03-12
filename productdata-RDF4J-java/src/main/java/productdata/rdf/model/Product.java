package productdata.rdf.model;

import productdata.global.util.GLOBAL;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;

/**
* Class Product 
*/
@SuppressWarnings("serial")
public class Product implements IProduct{

	IRI newInstance;
	public Product(String namespace, String instanceId) {		super();
		newInstance = GLOBAL.factory.createIRI(namespace, instanceId);
		GLOBAL.model.add(this, RDF.TYPE, GLOBAL.factory.createIRI("https://github.com/edufuga/LinkedDataScala/2024/3/linkeddata#Product"));
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
	
	public void setDepth(BigDecimal param)
	{
	 GLOBAL.model.add(this, GLOBAL.factory.createIRI("https://github.com/edufuga/LinkedDataScala/2024/3/linkeddata#hasDepth"), GLOBAL.factory.createLiteral(param));
	}
	
	public String getDepth(){
		return (GLOBAL.model.filter(this, GLOBAL.factory.createIRI("https://github.com/edufuga/LinkedDataScala/2024/3/linkeddata#hasDepth"), null).objects().iterator().next()).stringValue();	
	}
	
	public void setHeight(BigDecimal param)
	{
	 GLOBAL.model.add(this, GLOBAL.factory.createIRI("https://github.com/edufuga/LinkedDataScala/2024/3/linkeddata#hasHeight"), GLOBAL.factory.createLiteral(param));
	}
	
	public String getHeight(){
		return (GLOBAL.model.filter(this, GLOBAL.factory.createIRI("https://github.com/edufuga/LinkedDataScala/2024/3/linkeddata#hasHeight"), null).objects().iterator().next()).stringValue();	
	}
	
	public void setId(String param)
	{
	 GLOBAL.model.add(this, GLOBAL.factory.createIRI("https://github.com/edufuga/LinkedDataScala/2024/3/linkeddata#hasId"), GLOBAL.factory.createLiteral(param));
	}
	
	public String getId(){
		return (GLOBAL.model.filter(this, GLOBAL.factory.createIRI("https://github.com/edufuga/LinkedDataScala/2024/3/linkeddata#hasId"), null).objects().iterator().next()).stringValue();	
	}
	
	public void setWeight(BigDecimal param)
	{
	 GLOBAL.model.add(this, GLOBAL.factory.createIRI("https://github.com/edufuga/LinkedDataScala/2024/3/linkeddata#hasWeight"), GLOBAL.factory.createLiteral(param));
	}
	
	public String getWeight(){
		return (GLOBAL.model.filter(this, GLOBAL.factory.createIRI("https://github.com/edufuga/LinkedDataScala/2024/3/linkeddata#hasWeight"), null).objects().iterator().next()).stringValue();	
	}
	
	public void setWidth(BigDecimal param)
	{
	 GLOBAL.model.add(this, GLOBAL.factory.createIRI("https://github.com/edufuga/LinkedDataScala/2024/3/linkeddata#hasWidth"), GLOBAL.factory.createLiteral(param));
	}
	
	public String getWidth(){
		return (GLOBAL.model.filter(this, GLOBAL.factory.createIRI("https://github.com/edufuga/LinkedDataScala/2024/3/linkeddata#hasWidth"), null).objects().iterator().next()).stringValue();	
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
	
	public static Set<IProduct> getAllProductsObjectsCreated(){
		Set<IProduct> objects = new HashSet<IProduct>();
		objects = GLOBAL.model.filter(null, RDF.TYPE, GLOBAL.factory.createIRI("https://github.com/edufuga/LinkedDataScala/2024/3/linkeddata#Product")).subjects().stream().map(mapper->(IProduct)mapper).collect(Collectors.toSet());
				
		return objects;	
	}
}
