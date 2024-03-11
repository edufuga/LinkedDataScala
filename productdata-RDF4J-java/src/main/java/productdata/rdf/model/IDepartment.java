package productdata.rdf.model;

import org.eclipse.rdf4j.model.IRI;
import java.util.Set;
/**
* Class Department 
*/
public interface IDepartment extends IRI{

	public IRI iri();		
   
    /**
	* function addProducts
	* This property is meant for relating a given entity to a list of several products.* * Notice that OWL has no construct for a list of entities. Instead, we use a non-functional property for it. The "list" is an outcome of the graph structure, i.e. it isn't explicitly represented by a data structure in OWL nor in the resulting graph.
	*/     	
    public void addProducts (IProduct parameter);
	
	public Set<IProduct> getProducts();
   
    /**
	* function addServices
	* This property is meant for relating a given entity to a list of several services.* * Notice that OWL has no construct for a list of entities. Instead, we use a non-functional property for it. The "list" is an outcome of the graph structure, i.e. it isn't explicitly represented by a data structure in OWL nor in the resulting graph.
	*/     	
    public void addServices (IService parameter);
	
	public Set<IService> getServices();
     	
    public void addManager (IManager parameter);
	
	public Set<IManager> getManager();
   
    /**
	* function addEmployees
	* This property is meant for relating a given entity to a list of several employees.* * Notice that OWL has no construct for a list of entities. Instead, we use a non-functional property for it. The "list" is an outcome of the graph structure, i.e. it isn't explicitly represented by a data structure in OWL nor in the resulting graph.
	*/     	
    public void addEmployees (IEmployee parameter);
	
	public Set<IEmployee> getEmployees();
    

	public void setId (String parameter);
	
	public String getId ();

	public void setName (String parameter);
	
	public String getName ();
    /** 
    * This property is meant for relating a given entity to a list of several services. Instead of having the full Service in the range of the function (property), we have only their identifiers. This weak linking is enough, but an alternative could be having an _object_ property with Service  in the range.
    * 
    * Notice that OWL has no construct for a list of entities. Instead, we use a non-functional property for it. The "list" is an outcome of the graph structure, i.e. it isn't explicitly represented by a data structure in OWL nor in the resulting graph.
	*/
	public void setServiceId (String parameter);
	
	public String getServiceId ();
    /** 
    * This property is meant for relating a given entity to a list of several products. Instead of having the full Product in the range of the function (property), we have only their identifiers. This weak linking is enough, but an alternative could be having an _object_ property with Product in the range.
    * 
    * Notice that OWL has no construct for a list of entities. Instead, we use a non-functional property for it. The "list" is an outcome of the graph structure, i.e. it isn't explicitly represented by a data structure in OWL nor in the resulting graph.
	*/
	public void setProductId (String parameter);
	
	public String getProductId ();
}
