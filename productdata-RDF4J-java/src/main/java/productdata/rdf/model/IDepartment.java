package productdata.rdf.model;

import org.eclipse.rdf4j.model.IRI;
import java.util.Set;
/**
* Class Abteilung 
*/
public interface IDepartment extends IRI{

	public IRI iri();		
     	
    public void addManagers (IManager parameter);
	
	public Set<IManager> getManagers();
   
    /**
	* function addEmployees
	* This property is meant for relating a given entity to a list of several employees.* * Notice that OWL has no construct for a list of entities. Instead, we use a non-functional property for it. The "list" is an outcome of the graph structure, i.e. it isn't explicitly represented by a data structure in OWL nor in the resulting graph.
	*/     	
    public void addEmployees (IEmployee parameter);
	
	public Set<IEmployee> getEmployees();
    
    /** 
    * This property is meant for relating a given entity to a list of several services. Instead of having the full Service in the range of the function (property), we have only their identifiers. This weak linking is enough, but an alternative could be having an _object_ property with Service  in the range.
    * 
    * Notice that OWL has no construct for a list of entities. Instead, we use a non-functional property for it. The "list" is an outcome of the graph structure, i.e. it isn't explicitly represented by a data structure in OWL nor in the resulting graph.
	*/
	public void setServiceId (String parameter);
	
	public String getServiceId ();

	public void setName (String parameter);
	
	public String getName ();

	public void setId (String parameter);
	
	public String getId ();
    /** 
    * This property is meant for relating a given entity to a list of several products. Instead of having the full Product in the range of the function (property), we have only their identifiers. This weak linking is enough, but an alternative could be having an _object_ property with Product in the range.
    * 
    * Notice that OWL has no construct for a list of entities. Instead, we use a non-functional property for it. The "list" is an outcome of the graph structure, i.e. it isn't explicitly represented by a data structure in OWL nor in the resulting graph.
	*/
	public void setProductId (String parameter);
	
	public String getProductId ();
}
