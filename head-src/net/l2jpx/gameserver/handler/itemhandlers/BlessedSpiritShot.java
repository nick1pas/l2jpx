package net.l2jpx.gameserver.handler.itemhandlers;

import net.l2jpx.Config;
import net.l2jpx.gameserver.handler.IItemHandler;
import net.l2jpx.gameserver.model.actor.instance.L2ItemInstance;
import net.l2jpx.gameserver.model.actor.instance.L2PcInstance;
import net.l2jpx.gameserver.model.actor.instance.L2PlayableInstance;
import net.l2jpx.gameserver.network.SystemMessageId;
import net.l2jpx.gameserver.network.serverpackets.ExAutoSoulShot;
import net.l2jpx.gameserver.network.serverpackets.MagicSkillUser;
import net.l2jpx.gameserver.network.serverpackets.SystemMessage;
import net.l2jpx.gameserver.templates.L2Item;
import net.l2jpx.gameserver.templates.L2Weapon;
import net.l2jpx.gameserver.util.Broadcast;

/**
 * @author programmos
 * @author scoria dev
 * @author ReynalDev
 */

public class BlessedSpiritShot implements IItemHandler
{
	// all the items ids that this handler knowns
	private static final int[] ITEM_IDS =
	{
		3947,
		3948,
		3949,
		3950,
		3951,
		3952
	};
	private static final int[] SKILL_IDS =
	{
		2061,
		2160,
		2161,
		2162,
		2163,
		2164
	};
	
	@Override
	public void useItem(final L2PlayableInstance playable, final L2ItemInstance item)
	{
		if (!(playable instanceof L2PcInstance))
		{
			return;
		}
		
		L2PcInstance activeChar = (L2PcInstance) playable;
		L2ItemInstance weaponInst = activeChar.getActiveWeaponInstance();
		L2Weapon weaponItem = activeChar.getActiveWeaponItem();
		
		final int itemId = item.getItemId();
		
		if (activeChar.isInOlympiadMode())
		{
			SystemMessage sm = new SystemMessage(SystemMessageId.THIS_ITEM_IS_NOT_AVAILABLE_FOR_THE_OLYMPIAD_EVENT);
			sm.addString(item.getItemName());
			activeChar.sendPacket(sm);
			sm = null;
			
			return;
		}
		
		// Check if Blessed Spiritshot can be used
		if (weaponInst == null || weaponItem.getSpiritShotCount() == 0)
		{
			if (!activeChar.getAutoSoulShot().contains(itemId))
			{
				activeChar.sendPacket(new SystemMessage(SystemMessageId.YOU_MAY_NOT_USE_SPIRITSHOTS));
			}
			
			return;
		}
		
		// Check if Blessed Spiritshot is already active (it can be charged over Spiritshot)
		if (weaponInst.getChargedSpiritshot() != L2ItemInstance.CHARGED_NONE)
		{
			return;
		}
		
		// Check for correct grade
		final int weaponGrade = weaponItem.getCrystalType();
		if (weaponGrade == L2Item.CRYSTAL_NONE && itemId != 3947 || weaponGrade == L2Item.CRYSTAL_D && itemId != 3948 || weaponGrade == L2Item.CRYSTAL_C && itemId != 3949 || weaponGrade == L2Item.CRYSTAL_B && itemId != 3950 || weaponGrade == L2Item.CRYSTAL_A && itemId != 3951 || weaponGrade == L2Item.CRYSTAL_S && itemId != 3952)
		{
			if (!activeChar.getAutoSoulShot().contains(itemId))
			{
				activeChar.sendPacket(new SystemMessage(SystemMessageId.THE_SPIRITSHOT_DOES_NOT_MATCH_THE_WEAPONS_GRADE));
			}
			return;
		}
		
		// Consume Blessed Spiritshot if player has enough of them
		// TODO: test ss
		if (!Config.DONT_DESTROY_SS)
		{
			if (!activeChar.destroyItemWithoutTrace("Consume", item.getObjectId(), weaponItem.getSpiritShotCount(), null, false))
			{
				if (activeChar.getAutoSoulShot().contains(itemId))
				{
					activeChar.removeAutoSoulShot(itemId);
					activeChar.sendPacket(new ExAutoSoulShot(itemId, 0));
					SystemMessage sm = new SystemMessage(SystemMessageId.AUTO_USE_OF_S1_CANCELLED);
					sm.addString(item.getItem().getName());
					activeChar.sendPacket(sm);
					sm = null;
					
					return;
				}
				
				activeChar.sendPacket(new SystemMessage(SystemMessageId.YOU_DO_NOT_HAVE_ENOUGH_SPIRITSHOTS_FOR_THAT));
				return;
			}
		}
		
		// Charge Blessed Spiritshot
		weaponInst.setChargedSpiritshot(L2ItemInstance.CHARGED_BLESSED_SPIRITSHOT);
		
		// Send message to client
		activeChar.sendPacket(new SystemMessage(SystemMessageId.POWER_OF_MANA_ENABLED));
		if (!activeChar.isSSDisabled())
			Broadcast.toSelfAndKnownPlayersInRadius(activeChar, new MagicSkillUser(activeChar, activeChar, SKILL_IDS[weaponGrade], 1, 0, 0), 360000/* 600 */);
		
	}
	
	@Override
	public int[] getItemIds()
	{
		return ITEM_IDS;
	}
}
