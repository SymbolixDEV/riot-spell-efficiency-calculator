package test;

import constant.Region;
import constant.staticdata.ChampData;
import constant.staticdata.SpellData;
import main.java.riotapi.RiotApi;
import main.java.riotapi.RiotApiException;
 
public class TestingRiotAPI {
 
    public static void main(String[] args) throws RiotApiException {
    	
        RiotApi api = new RiotApi("3aa2e59d-60e0-4179-a19c-671267d1854f", Region.NA);
        
        String annieEffectBurn = api.getDataChampion(1, null, null, ChampData.SPELLS).getSpells().get(0).getEffectBurn().get(1);
        
        System.out.print(Integer.parseInt(annieEffectBurn.substring(annieEffectBurn.lastIndexOf("/") + 1)));
        
    }
 
}