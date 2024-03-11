package productdata.rdf.model;

import org.eclipse.rdf4j.model.IRI;
/**
* Class Manager 
*/
public interface IManager extends IRI{

	public IRI iri();		
    

	public void setPhone (String parameter);
	
	public String getPhone ();

	public void setName (String parameter);
	
	public String getName ();

	public void setEmail (String parameter);
	
	public String getEmail ();

	public void setAddress (String parameter);
	
	public String getAddress ();
}
