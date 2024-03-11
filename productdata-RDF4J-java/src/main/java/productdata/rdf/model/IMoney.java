package productdata.rdf.model;

import org.eclipse.rdf4j.model.IRI;
/**
* Class Diners 
*/
public interface IMoney extends IRI{

	public IRI iri();		
    

	public void setMonetaryValue (float parameter);
	
	public String getMonetaryValue ();

	public void setCurrency (String parameter);
	
	public String getCurrency ();
}
