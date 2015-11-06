package calculator;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import calculator.SpellEfficiencyCalculator;
import exception.NegativeNumberException;
import main.java.riotapi.RiotApiException;

/**
 * JUnit tests used to test the SpellEfficiencyCalculator class
 * @author Leonard
 */
public class SpellEfficiencyCalculatorTest {
	
	/** The instance of the SpellEfficiencyCalculator object to be tested */
	private SpellEfficiencyCalculator sec;
	
	/** The acceptable diffeerence in the values of doubles in this test */
	private final Double DELTA = 0.0001;
	
	/**
	 * Instantiates the SpellEfficiencyCalculator object.
	 * @throws RiotApiException
	 * @throws NegativeNumberException
	 */
	@Before
	public void setUp() throws RiotApiException, NegativeNumberException{
		sec = new SpellEfficiencyCalculator();
	}
		
	/**
	 * Tests the calculateEfficiency method
	 */
	@Test
	public void testCalculateEfficiency(){
		try {
			sec.loadSpells();
		} catch (RiotApiException e) {
			fail("C'mon man. It threw a RiotApiException!");
			e.printStackTrace();
		}
		assertTrue(!sec.getDamageSpells().isEmpty());
		
		try{
			sec.setAbilityPower(50.0);
			sec.setAttackPower(50.0);
		} catch (NegativeNumberException e){
			fail("Negative number detected when it wasn't present");
			e.printStackTrace();
		}
		sec.setCoolDownReduction(30.0);
		assertEquals(sec.getAbilityPower(), 50.0, DELTA);
		assertEquals(sec.getAttackPower(), 50.0, DELTA);
		assertEquals(sec.getCoolDownReduction(), 30.0, DELTA);
		
		assertEquals(sec.calculateEfficiency().getName(), "Shadow Dance");
		
		try {
			sec.reset();
		} catch (NegativeNumberException e) {
			fail("Negative number detected when it wasn't present");
			e.printStackTrace();
		}
		

		assertEquals(sec.getAbilityPower(), 0.0, DELTA);
		assertEquals(sec.getAttackPower(), 0.0, DELTA);
		assertEquals(sec.getCoolDownReduction(), 0.0, DELTA);
	}
	
	/**
	 * Tests the calculateEfficiency method
	 */
	@Test
	public void testSetAbilityPower(){
		try{
			sec.setAbilityPower(-1.0);
			fail();
		} catch(NegativeNumberException e){
			assertEquals(e.getMessage(), "Ability power must be a positive, real number.");
		}
	}
	
	/**
	 * Tests the setAttackPower method
	 */
	@Test
	public void testSetAttackPower(){
		try{
			sec.setAttackPower(-1.0);
			fail();
		} catch(NegativeNumberException e){
			assertEquals(e.getMessage(), "Attack power must be a positive, real number.");
		}
	}
	
	/**
	 * Tests the setCoolDownReduction method
	 */
	@Test
	public void testSetCoolDownReduction(){
		try{
			sec.setCoolDownReduction(-1.0);
			fail();
		} catch(IllegalArgumentException e){
			assertEquals(e.getMessage(), "Cooldown reduction must be between 0-40%.");
		}
		
		try{
			sec.setCoolDownReduction(41.0);
			fail();
		} catch(IllegalArgumentException e){
			assertEquals(e.getMessage(), "Cooldown reduction must be between 0-40%.");
		}
	}
}
