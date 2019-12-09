import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class Customer extends User {
	
	private Date startWorkingDate;
	private ArrayList<Order> orders;

	public Customer(String ID, String username, String password, String firstName, String lastName, 
					 Address address,String phoneNumber, String email, Date date)  
							 throws InvalidUserName, IlegalPassword {
	
		super(ID, username, password, firstName, lastName, address, phoneNumber, email);
		setStartWorkingDate(date);
		orders = new ArrayList<>();
	
	}

	public Date getStartWorkingDate() {
		return startWorkingDate;
	}
	
	public ArrayList<Order> getOrders() {
		return orders;
	}

	
	public void setStartWorkingDate(Date startWorkingDate) {
		this.startWorkingDate = startWorkingDate;
	}
	
	public void addOrder(Order order) {
		this.orders.add(order);
	}

	
	@Override
	public String toString() {
		
		return this.getClass().getSimpleName() + super.toString() + "startWorkingDate=" + startWorkingDate + "]";
	
	}
	
	
	

}
