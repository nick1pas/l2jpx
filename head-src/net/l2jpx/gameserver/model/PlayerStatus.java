package net.l2jpx.gameserver.model;

import net.l2jpx.gameserver.model.actor.instance.L2PcInstance;

/**
 * @author Shyla
 */
public class PlayerStatus
{
	
	public int level = -1;
	public int EXP = -1;
	public int STR = -1;
	public int DEX = -1;
	public int CON = -1;
	public int INT = -1;
	public int WIT = -1;
	public int MEN = -1;
	
	public int CUR_HP = -1;
	public int MAX_HP = -1;
	public int CUR_MP = -1;
	public int MAX_MP = -1;
	public int SP = -1;
	public int CUR_LOAD = -1;
	public int MAX_LOAD = -1;
	
	public int P_ATK = -1;
	public int ATK_SPD = -1;
	public int P_DEF = -1;
	public int EVASION = -1;
	public int ACCURACY = -1;
	public int CRITICAL = -1;
	public int M_ATK = -1;
	
	public int CAST_SPD = -1;
	public int M_DEF = -1;
	public int PVP_FLAG = -1;
	public int KARMA = -1;
	public int CUR_CP = -1;
	public int MAX_CP = -1;
	
	public PlayerStatus(final L2PcInstance actor)
	{
		
		level = actor.getLevel();
		EXP = (int) actor.getExp();
		STR = actor.getSTR();
		DEX = actor.getDEX();
		CON = actor.getCON();
		INT = actor.getINT();
		WIT = actor.getWIT();
		MEN = actor.getMEN();
		
		CUR_HP = (int) actor.getCurrentHp();
		MAX_HP = actor.getMaxHp();
		CUR_MP = (int) actor.getCurrentMp();
		MAX_MP = actor.getMaxMp();
		SP = actor.getSp();
		CUR_LOAD = actor.getCurrentLoad();
		MAX_LOAD = actor.getMaxLoad();
		
		P_ATK = actor.getPAtk(null);
		ATK_SPD = actor.getPAtkSpd();
		P_DEF = actor.getPDef(null);
		EVASION = actor.getEvasionRate(null);
		ACCURACY = actor.getAccuracy();
		CRITICAL = actor.getCriticalHit(null, null);
		M_ATK = actor.getMAtk(null, null);
		
		CAST_SPD = actor.getMAtkSpd();
		M_DEF = actor.getMDef(null, null);
		PVP_FLAG = actor.getPvpFlag();
		KARMA = actor.getKarma();
		CUR_CP = (int) actor.getCurrentCp();
		MAX_CP = actor.getMaxCp();
		
	}
	
}
