package calculator;

import java.util.AbstractSequentialList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import constant.Region;
import constant.staticdata.ChampData;
import dto.Static.Champion;
import dto.Static.ChampionList;
import dto.Static.ChampionSpell;
import dto.Static.SpellVars;
import main.java.riotapi.RiotApi;
import main.java.riotapi.RiotApiException;

import exception.NegativeNumberException;

/**
 * An object that is useful for calculating the most efficient spell in League of Legends. In this
 * case, efficiency is defined strictly by damage per second. Factors that are considered include
 * ability power, attack power, and cooldown reduction. Only max rank spells are considered when
 * running this program. Also, assumes that the target has no magic resistance or armor, and does
 * not take into account the health of the champion or target. Only calculates efficiency based
 * on the initial hit, not over time or returning damage.
 * 
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
	private Double cooldownReduction;
	
	/** An  integer representation of the highest cooldown reduction possible in League of Legends */
	private static final int MAX_CDR = 40;
	
	/** The link that denotes that a coefficient is meant for AP */
	private static final String SPELL_DAMAGE = "spelldamage";
	
	/** The link that denotes that a coefficient is meant for AD */
	private static final String ATTACK_DAMAGE = "attackdamage";
		
	/**
	 * Creates a new SpellEfficiencyCalculator object, with an empty spell list and ability power,
	 * attack power, and cooldown reduction all set to zero.
	 * @throws RiotApiException whenever information can not be properly retrieved from the Riot API
	 * @throws NegativeNumberException if the default constructor value for attack or ability power is negative
	 */
	public SpellEfficiencyCalculator() throws RiotApiException, NegativeNumberException{
		damageSpells = new LinkedList<ChampionSpell>();
		loadSpells();
		setAbilityPower(0.0);
		setAttackPower(0.0);
		setCoolDownReduction(0.0);
	}
	
	/**
	 * Resets each of the ability power, attack power, and cooldown reduction fields back to their
	 * default value of zero.
	 * @throws NegativeNumberException if the number to reset the fields to is negative
	 */
	public void reset() throws NegativeNumberException{
		setAbilityPower(0.0);
		setAttackPower(0.0);
		setCoolDownReduction(0.0);
	}
	
	/**
	 * Returns the current ability power.
	 * @return the current ability power
	 */
	public Double getAbilityPower() {
		return abilityPower;
	}
	
	/**
	 * Sets the ability power to the passed in value.
	 * @param abilityPower the ability power to be set
	 * @throws NegativeNumberException when the ability power passed in is negative
	 */
	public void setAbilityPower(Double abilityPower) throws NegativeNumberException {
		if (abilityPower < 0){
			throw new NegativeNumberException("Ability power must be a positive, real number.");
		}
		this.abilityPower = abilityPower;
	}

	/**
	 * Returns the current attack power.
	 * @return the current attack power
	 */
	public Double getAttackPower() {
		return attackPower;
	}

	/**
	 * Sets the attack power to the passed in value.
	 * @param attackPower the attack power to be set
	 * @throws NegativeNumberException when the attack power passed in is negative
	 */
	public void setAttackPower(Double attackPower) throws NegativeNumberException {
		if (attackPower < 0){
			throw new NegativeNumberException("Attack power must be a positive, real number.");
		}
		this.attackPower = attackPower;
	}
	
	/**
	 * Returns the current cooldown reduction.
	 * @return the current cooldown reduction
	 */
	public Double getCoolDownReduction() {
		return cooldownReduction;
	}
	
	/**
	 * Sets the cooldown reduction to the passed in value if it is between 0 and 40 inclusive.
	 * @param coolDownReduction the cooldown reduction to be set
	 * @throws IllegalArgumentException if cooldown reduction passed in exceeds 40 or is negative
	 */
	public void setCoolDownReduction(Double coolDownReduction) throws IllegalArgumentException {
		if(coolDownReduction > MAX_CDR || coolDownReduction < 0){
			throw new IllegalArgumentException("Cooldown reduction must be between 0-40%.");
		}
		this.cooldownReduction = coolDownReduction;
	}
	
	/**
	 * Loads all of the spells into the damageSpells linked list that contain the label "Damage".
	 * Information about the spells is obtained through the Riot API and as such an exception is
	 * thrown when access is unavailable.
	 * @throws RiotApiException whenever information can not be properly retrieved from the Riot API
	 */
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
		Double dps = 0.0;
		Double mostDPS = Double.MIN_VALUE;
		for(ChampionSpell spell : damageSpells){
			dps = getDPS(spell);
			if(dps > mostDPS){
				mostDPS = dps;
				mostEfficient = spell;
			}
		}
		return mostEfficient;
	}
	
	/**
	 * Returns the DPS of a spell passed in. Does not account for resistances or inefficiencies.
	 * For example, Akali's "Shadow Dance" spell is assumed to have an Essence of Shadow as soon
	 * as it is off cooldown.
	 * @param spell the spell to find the DPS of
	 * @return the DPS of the spell as a double
	 */
	public Double getDPS(ChampionSpell spell){
		return (((getBaseDamage(spell) + getMagicDamage(spell)) + getPhysicalDamage(spell)) /getCoolDown(spell));
	}
	
	/**
	 * Given a spell, returns the base damage of that spell based on the key value of the "Damage"
	 * label.
	 * @param spell the spell to find the base damage of
	 * @return the base damage of the spell
	 */
	private Double getBaseDamage(ChampionSpell spell) {
		Double damage = 0.0;
		if(spell.getEffectBurn() != null){
			String str = spell.getEffectBurn().get(getDamageKey(spell));
			if(!str.isEmpty()){
				damage += Double.parseDouble(str.substring(str.lastIndexOf("/") + 1));
			}
		}
		return damage;
	}

	/**
	 * Given a spell, returns the additional physical damage to be added to that spell.
	 * If there are no variables associated with a spell, simply returns zero.
	 * @param spell the spell to find the physical damage of
	 * @return the additional physical damage of the spell
	 */
	private Double getPhysicalDamage(ChampionSpell spell) {
		Double damage = 0.0;
		int key = getDamageKey(spell);
		List<SpellVars> varList = spell.getVars();
		if(varList != null){
			for(SpellVars vars : spell.getVars()){
				if(key == Integer.parseInt(vars.getKey().substring(1))){
					if(vars.getLink().contains(ATTACK_DAMAGE)){
						damage += (vars.getCoeff().get(0) * attackPower);
					}
				}
			}
		}
		return damage;
	}

	/**
	 * Given a spell, returns the additional magic damage to be added to that spell.
	 * If there are no variables associated with a spell, simply returns zero.
	 * @param spell the spell to find the magic damage of
	 * @return the additional magic damage of the spell
	 */
	private Double getMagicDamage(ChampionSpell spell) {
		Double damage = 0.0;
		int key = getDamageKey(spell);
		List<SpellVars> varList = spell.getVars();
		if(varList != null){
			for(SpellVars vars : spell.getVars()){
				if(key == Integer.parseInt(vars.getKey().substring(1))){
					if(vars.getLink().equals(SPELL_DAMAGE)){
						damage += (vars.getCoeff().get(0) * abilityPower);
					}
				}
			}
		}
		return damage;
	}
	
	/**
	 * Returns the cooldown of a spell that is passed in with regards to the damage a spell
	 * outputs. For example, Vorpal Spikes has a cooldown of 0.78 because it is based on 
	 * Cho'Gath's attack speed. When applicable, takes cooldown reduction into account. 
	 * @param spell the spell to find the cooldown of
	 * @return the cooldown of the spell
	 */
	private Double getCoolDown(ChampionSpell spell) {
		String spellName = spell.getName();
		Double coolDown = 0.0;
		switch(spellName){
			case "Poison Trail":
				coolDown = 1.0;
				break;
			case "Electro Harpoon":
				coolDown = 10 * (1 - (cooldownReduction/100));
				break;
			case "Force of Will":
				coolDown = 8 * (1 - (cooldownReduction/100));
				break;
			case "Rend":
				coolDown = 8 * (1 - (cooldownReduction/100));
				break;
			case "Battle Roar":
				coolDown = 12 * (1 - (cooldownReduction/100));
				break;
			case "Bola Strike":
				coolDown = 10 * (1 - (cooldownReduction/100));
				break;
			case "Sweeping Blade":
				coolDown =  6.0;
				break;
			case "Last Breath":
				coolDown =  30 * (1 - (cooldownReduction/100));
				break;
			case "Riposte":
				coolDown = 15 * (1 - (cooldownReduction/100));
				break;
			case "Vorpal Spikes":
				coolDown = 0.78;
				break;
			case "Excessive Force":
				coolDown = 4.0;
				break;
			default:
				coolDown = spell.getCooldown().get(spell.getCooldown().size() - 1) * (1 - (cooldownReduction/100));
		}
		return coolDown;
	}
	
	/**
	 * Returns the integer portion of the key associated with the "Damage" tag for a spell.
	 * @param spell the spell to find the "Damage" key of
	 * @return the integer portion of the "Damage" key
	 */
	private int getDamageKey(ChampionSpell spell){
		int key = 0;
		int idxOfEffect = spell.getLeveltip().getLabel().indexOf("Damage");
		String keyString = spell.getLeveltip().getEffect().get(idxOfEffect);
		Scanner lineScan = new Scanner(keyString);
		if(lineScan.useDelimiter("\\D+").hasNextInt()){
			key = lineScan.useDelimiter("\\D+").nextInt();
		}
		lineScan.close();
		return key;
	}
	
	/**
	 * Returns the entire list of damaging spells.
	 * @return the list of damaging spells
	 */
	public AbstractSequentialList<ChampionSpell> getDamageSpells() {
		return damageSpells;
	}
}
