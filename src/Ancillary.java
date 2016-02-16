/**
 * Description:
 *      		Ancillary object contains additional information for the User.
 *				Users will be able to select from a variety of accomidations such as 
 *				Cars, Hotels, Food
 */

public class Ancillary {
	String type;
	String ancillaryID;
	String location;
	String date;
	String price;
	int reservationLength;
	Boolean available;

	//** Constructors TBD
    public Ancillary(){}
    
    //** Getter/Setter/Add funcions for Flight
    static void setType(){}
    static String getType(){ return ""; }
    static void setAncillaryID(){}
    static String getAncillaryID(){ return ""; }
    static void setLocation(){}
    static String getLocation(){ return ""; }
    static void setDate(){}
    static String getDate(){ return ""; }
    static void setPrice(){}
    static String getPrice(){ return ""; }
    static void setLength(){}
    static int getLength(){ return 0; }
    static void setAvailable(){}
    static Boolean getAvailable(){ return true; }
    
}