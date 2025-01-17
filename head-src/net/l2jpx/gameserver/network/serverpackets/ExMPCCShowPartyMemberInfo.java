package net.l2jpx.gameserver.network.serverpackets;

import net.l2jpx.gameserver.model.L2Party;
import net.l2jpx.gameserver.model.actor.instance.L2PcInstance;

/**
 * Format: ch d[Sdd]
 * @author KenM
 */
public class ExMPCCShowPartyMemberInfo extends L2GameServerPacket
{
	private final L2Party party;
	
	public ExMPCCShowPartyMemberInfo(final L2Party party)
	{
		this.party = party;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xfe);
		writeH(0x4a);
		
		writeD(party.getMemberCount());
		for (final L2PcInstance pc : party.getPartyMembers())
		{
			writeS(pc.getName());
			writeD(pc.getObjectId());
			writeD(pc.getClassId().getId());
		}
	}
	
	@Override
	public String getType()
	{
		return "[S] FE:4A ExMPCCShowPartyMemberInfo";
	}
}
