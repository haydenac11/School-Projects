
/**
 * Exception is thrown when a structure is empty
 *
 * @author Jalal Kawash
 */
public class OverflowException extends RuntimeException
{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**E
     * Default constructor for objects of class OverflowException
     */
    public OverflowException()
    {
        super();
    }
    
    /**
     * Constructor for objects of class OverflowException
     */
    public OverflowException(String message)
    {
        super(message);
    }  
}