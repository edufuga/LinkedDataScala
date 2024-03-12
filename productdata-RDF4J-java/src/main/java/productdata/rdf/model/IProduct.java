package productdata.rdf.model;

import org.eclipse.rdf4j.model.IRI;
import java.util.Set;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
/**
* Class Product 
*/
public interface IProduct extends IRI{

	public IRI iri();		
     	
    public void addPrice (IMoney parameter);
	
	public Set<IMoney> getPrice();
    

	public void setName (String parameter);
	
	public String getName ();
    /** 
    * This is just an email address.
	*/
	public void setProductManager (String parameter);
	
	public String getProductManager ();

	public void setDepth (BigDecimal parameter);
	
	public String getDepth ();

	public void setHeight (BigDecimal parameter);
	
	public String getHeight ();

	public void setId (String parameter);
	
	public String getId ();

	public void setWeight (BigDecimal parameter);
	
	public String getWeight ();

	public void setWidth (BigDecimal parameter);
	
	public String getWidth ();
}
