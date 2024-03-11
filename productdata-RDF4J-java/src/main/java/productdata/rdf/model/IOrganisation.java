package productdata.rdf.model;

import org.eclipse.rdf4j.model.IRI;
import java.util.Set;
/**
* Class Organisation 
*/
public interface IOrganisation extends IRI{

	public IRI iri();		
   
    /**
	* function addDepartments
	* This property is meant for relating a given entity to a list of several departments.* * Notice that OWL has no construct for a list of entities. Instead, we use a non-functional property for it. The "list" is an outcome of the graph structure, i.e. it isn't explicitly represented by a data structure in OWL nor in the resulting graph.
	*/     	
    public void addDepartments (IDepartment parameter);
	
	public Set<IDepartment> getDepartments();
    
}
