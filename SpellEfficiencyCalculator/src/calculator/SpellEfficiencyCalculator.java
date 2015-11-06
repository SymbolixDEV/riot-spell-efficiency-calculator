package calculator;

import java.util.AbstractSequentialList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Stack;

import constant.Region;
import constant.staticdata.ChampData;
import dto.Static.Champion;
import dto.Static.ChampionList;
import dto.Static.ChampionSpell;
import dto.Static.SpellVars;
import main.java.riotapi.RiotApi;
import main.java.riotapi.RiotApiException;

/**
 * An object that is useful for calculating the most efficient spell in League of Legends. In this
 * case, efficiency is defined strictly by damage per second. Factors that are considered include
 * ability power, attack power, and cooldown reduction. Only max rank spells are considered when
 * running this program. Also, assumes that the target has no magic resistance or armor, and does
 * not take into account the health of the champion or target. Only calculates efficiency based
 * on the initial hit, not over time or returning damage.
 * @author Leonard Kerr
 */
public class SpellEfficiencyCalculator {
	
	/** A list containing all of the spells in League of Legends with the label "Damage" */
	private AbstractSequentialList<ChampionSpell> damageSpells;
	
	/** An object which communicates with the RiotApi */
	private RiotApi api = new RiotApi("29ead916-ab5c-4fac-83d9-1a24450dd81a", Region.NA);
	
	/** The amount of ability power to be used */
	private Double abilityPower;
	
	/** The amount of attack power to be used */
	private Double attackPower;
	
	/** The amount of cooldown reduction to be used */
	private Double coolDownReduction;
	
	/** The link that denotes that a coefficient is meant for AP */
	public static final String SPELL_DAMAGE = "spelldamage";
	
	/** The link that denotes that a coefficient is meant for AD */
	public static final String ATTACK_DAMAGE = "attackdamage";
		
	/**
	 * Creates a new SpellEfficiencyCalculator object, with an empty stack and ability power,
	 * attack power, and cooldown reduction all set to zero.
	 */
	public SpellEfficiencyCalculator(){
		damageSpells = new LinkedList<ChampionSpell>();
		setAbilityPower(0.0);
		setAttackPower(0.0);
		setCoolDownReduction(0.0);
	}
	
	public void reset() throws RiotApiException{
		setAbilityPower(0.0);
		setAttackPower(0.0);
		setCoolDownReduction(0.0);
	}

	public Double getAbilityPower() {
		return abilityPower;
	}

	public void setAbilityPower(Double abilityPower) {
		this.abilityPower = abilityPower;
	}

	public Double getAttackPower() {
		return attackPower;
	}

	public void setAttackPower(Double attackPower) {
		this.attackPower = attackPower;
	}

	public Double getCoolDownReduction() {
		return coolDownReduction;
	}

	public void setCoolDownReduction(Double coolDownReduction) {
		this.coolDownReduction = coolDownReduction;
	}

	public void loadSpells() throws RiotApiException{
		ChampionList championList = api.getDataChampionList(Region.NA, null, null, false, ChampData.SPELLS);
		
		for(Champion champion : championList.getData().values()){
			for(ChampionSpell spell : champion.getSpells()){
				for(String label : spell.getLeveltip().getLabel()){
					if(label.equals("Damage")){
						damageSpells.add(spell);
					}
				}
			}
		}
	}
	
	/**
	 * Uses the ability power, attack power, and cooldown reduction to calculate the most efficient
	 * spell in terms of dps. 
	 * @return The most efficient spell given AP, AD, and CDR
	 */
	public ChampionSpell calculateEfficiency(){
		ChampionSpell mostEfficient = null;
		Double DPS = 0.0;
		Double mostDPS = 0.0;
		for(ChampionSpell spell : damageSpells){
			DPS = getDPS(spell);
			if(DPS > mostDPS){
				mostDPS = DPS;
				mostEfficient = spell;
			}
		}
		System.out.println(mostEfficient.getName() + " is most efficient with a DPS of " + mostDPS);
		return mostEfficient;
	}
	
	private Double getDPS(ChampionSpell spell){
		return ((getBaseDamage(spell) + getMagicDamage(spell)) + getPhysicalDamage(spell) /getCoolDown(spell));
	}
	
	private Double getBaseDamage(ChampionSpell spell) {
		Double damage = 0.0;
		int idxOfEffect = spell.getLeveltip().getLabel().indexOf("Damage") + 1;
		String str = spell.getEffectBurn().get(idxOfEffect);
		String[] effects = str.split("/");
		damage += Double.parseDouble(effects[effects.length - 1]);
		return damage;
	}

	private Double getPhysicalDamage(ChampionSpell spell) {
		Double damage = 0.0;
		int key = getDamageKey(spell);
		System.out.println(spell.getName());
		for(int i = 0; i < spell.getVars().size(); i++){
			if(spell.getVars().get(i) == null){
				System.out.println("This is stupid.");
			}
		}
		/*
		for(SpellVars vars : spell.getVars()){
			if(key == Integer.parseInt(vars.getKey().substring(1))){
				if(vars.getLink().contains(ATTACK_DAMAGE)){
					damage += (vars.getCoeff().get(0) * abilityPower);
				}
			}
		}
		*/
		return damage;
	}

	private Double getMagicDamage(ChampionSpell spell) {
		Double damage = 0.0;
		int key = getDamageKey(spell);
		for(SpellVars vars : spell.getVars()){
			if(key == Integer.parseInt(vars.getKey().substring(1))){
				if(vars.getLink().equals(SPELL_DAMAGE)){
					damage += (vars.getCoeff().get(0) * abilityPower);
				}
			}
		}
		return damage;
	}
	
	private Double getCoolDown(ChampionSpell spell) {
		return spell.getCooldown().get(spell.getCooldown().size() - 1) * (1 - (coolDownReduction/100));
	}

	private int getDamageKey(ChampionSpell spell){
		int key = 0;
		int idxOfEffect = spell.getLeveltip().getLabel().indexOf("Damage");
		key = Integer.parseInt(Arrays.asList(spell.getLeveltip().getEffect().get(idxOfEffect).replaceAll("[^0-9]+", " ").trim().split(" ")).get(0));
		return key;
	}
	
	public AbstractSequentialList<ChampionSpell> getDamageSpells() {
		return damageSpells;
	}
}
