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
		
		for(ChampionSpell spell : sec.getDamageSpells()){
			if(spell == null){
				System.out.print("NULL -- ");
			}
			System.out.println(spell.getName());
		}
		
		
		sec.setAbilityPower(0.0);
		sec.setAttackPower(0.0);
		sec.setCoolDownReduction(20.0);
		sec.calculateEfficiency();
		
		
	}
	
}
