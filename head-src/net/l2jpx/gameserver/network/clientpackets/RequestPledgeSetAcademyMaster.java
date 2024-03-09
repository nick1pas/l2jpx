package net.l2jpx.gameserver.network.clientpackets;

import net.l2jpx.gameserver.model.L2Clan;
import net.l2jpx.gameserver.model.L2ClanMember;
import net.l2jpx.gameserver.model.actor.instance.L2PcInstance;
import net.l2jpx.gameserver.network.SystemMessageId;
import net.l2jpx.gameserver.network.serverpackets.SystemMessage;

/**
 * Format: (ch) dSS
 * @author -Wooden-
 */
public final class RequestPledgeSetAcademyMaster extends L2GameClientPacket
{
	private String currPlayerName;
	private int set; // 1 set, 0 delete
	private String targetPlayerName;
	
	@Override
	protected void readImpl()
	{
		set = readD();
		currPlayerName = readS();
		targetPlayerName = readS();
	}
	
	@Override
	protected void runImpl()
	{
		final L2PcInstance activeChar = getClient().getActiveChar();
		final L2Clan clan = activeChar.getClan();
		
		if (clan == null)
		{
			return;
		}
		
		if ((activeChar.getClanPrivileges() & L2Clan.CP_CL_MASTER_RIGHTS) != L2Clan.CP_CL_MASTER_RIGHTS)
		{
			activeChar.sendPacket(new SystemMessage(SystemMessageId.YOU_DO_NOT_HAVE_THE_RIGHT_TO_DISMISS_AN_APPRENTICE));
			return;
		}
		
		final L2ClanMember currentMember = clan.getClanMember(currPlayerName);
		final L2ClanMember targetMember = clan.getClanMember(targetPlayerName);
		
		if (currentMember == null || targetMember == null)
		{
			return;
		}
		
		L2ClanMember apprenticeMember, sponsorMember;
		
		if (currentMember.getPledgeType() == L2Clan.SUBUNIT_ACADEMY)
		{
			apprenticeMember = currentMember;
			sponsorMember = targetMember;
		}
		else
		{
			apprenticeMember = targetMember;
			sponsorMember = currentMember;
		}
		
		final L2PcInstance apprentice = apprenticeMember.getPlayerInstance();
		final L2PcInstance sponsor = sponsorMember.getPlayerInstance();
		
		SystemMessage sm = null;
		
		if (set == 0)
		{
			// test: do we get the current sponsor & apprentice from this packet or no?
			if (apprentice != null)
			{
				apprentice.setSponsor(0);
			}
			else
			{
				// offline
				apprenticeMember.initApprenticeAndSponsor(0, 0);
			}
			
			if (sponsor != null)
			{
				sponsor.setApprentice(0);
			}
			else
			{
				// offline
				sponsorMember.initApprenticeAndSponsor(0, 0);
			}
			
			apprenticeMember.saveApprenticeAndSponsor(0, 0);
			sponsorMember.saveApprenticeAndSponsor(0, 0);
			
			sm = new SystemMessage(SystemMessageId.S2_CLAN_MEMBER_S1_S_APPRENTICE_HAS_BEEN_REMOVED);
		}
		else
		{
			if (apprenticeMember.getSponsor() != 0 || sponsorMember.getApprentice() != 0 || apprenticeMember.getApprentice() != 0 || sponsorMember.getSponsor() != 0)
			{
				activeChar.sendMessage("Remove previous connections first.");
				return;
			}
			
			if (apprentice != null)
			{
				apprentice.setSponsor(sponsorMember.getObjectId());
			}
			else
			{
				// offline
				apprenticeMember.initApprenticeAndSponsor(0, sponsorMember.getObjectId());
			}
			
			if (sponsor != null)
			{
				sponsor.setApprentice(apprenticeMember.getObjectId());
			}
			else
			{
				// offline
				sponsorMember.initApprenticeAndSponsor(apprenticeMember.getObjectId(), 0);
			}
			
			// saving to database even if online, since both must match
			apprenticeMember.saveApprenticeAndSponsor(0, sponsorMember.getObjectId());
			sponsorMember.saveApprenticeAndSponsor(apprenticeMember.getObjectId(), 0);
			
			sm = new SystemMessage(SystemMessageId.S2_HAS_BEEN_DESIGNATED_AS_APPRENTICE_OF_CLAN_MEMBER_S1);
		}
		sm.addString(sponsorMember.getName());
		sm.addString(apprenticeMember.getName());
		
		if (sponsor != activeChar && sponsor != apprentice)
		{
			activeChar.sendPacket(sm);
		}
		
		if (sponsor != null)
		{
			sponsor.sendPacket(sm);
		}
		
		if (apprentice != null)
		{
			apprentice.sendPacket(sm);
		}
	}
	
	@Override
	public String getType()
	{
		return "[C] D0:19 RequestPledgeSetAcademyMaster";
	}
	
}