package fr.unice.polytech.isa.tcf;

import fr.unice.polytech.isa.tcf.entities.Customer;
import fr.unice.polytech.isa.tcf.exceptions.AlreadyExistingCustomerException;
import fr.unice.polytech.isa.tcf.utils.Database;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import javax.ejb.EJB;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class CustomerRegistryTest extends AbstractTCFTest {

	@EJB
	private Database memory;

	@EJB
	CustomerRegistry registry;

	@Before
	public void setUpContext() throws Exception { memory.flush(); }

	@Test
	public void unknownCustomer() {
		assertFalse(registry.findByName("John").isPresent());
	}

	@Test
	public void registerCustomer() throws Exception {
		String name = "John";
		String creditCard = "credit card number";
		registry.register(name, creditCard);
		Optional<Customer> customer = registry.findByName(name);
		assertTrue(customer.isPresent());
		Customer john = customer.get();
		assertEquals(name, john.getName());
		assertEquals(creditCard, john.getCreditCard());
	}


	@Test(expected = AlreadyExistingCustomerException.class)
	public void cannotRegisterTwice() throws Exception {
		String name = "John";
		String creditCard = "credit card number";
		registry.register(name, creditCard);
		registry.register(name, creditCard);
	}

}