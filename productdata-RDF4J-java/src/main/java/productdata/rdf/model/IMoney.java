package productdata.rdf.model;

import org.eclipse.rdf4j.model.IRI;
/**
* Class Money 
*/
public interface IMoney extends IRI{

	public IRI iri();		
    

	public void setCurrency (String parameter);
	
	public String getCurrency ();

	public void setMonetaryValue (float parameter);
	
	public String getMonetaryValue ();
}
