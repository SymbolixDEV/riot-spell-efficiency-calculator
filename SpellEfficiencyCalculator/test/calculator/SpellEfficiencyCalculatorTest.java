package calculator;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import calculator.SpellEfficiencyCalculator;
import dto.Static.ChampionSpell;
import main.java.riotapi.RiotApiException;

public class SpellEfficiencyCalculatorTest {
	
	
	private SpellEfficiencyCalculator sec;
	
	
	@Before
	public void setUp() throws RiotApiException{
		sec = new SpellEfficiencyCalculator();
	}
	
	@Test
	public void testLoadSpells(){
		try{
			sec.loadSpells();
		} catch (RiotApiException e) {
			fail("C'mon man. It threw a RiotApiException!");
		}
		assertTrue(!sec.getDamageSpells().isEmpty());
		
						
		sec.setAbilityPower(9999999.0);
		sec.setAttackPower(0.0);
		sec.setCoolDownReduction(40.0);
		sec.calculateEfficiency();
		
		
	}
	
}
