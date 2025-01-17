package net.l2jpx.gameserver.network.clientpackets;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import net.l2jpx.Config;
import net.l2jpx.gameserver.ai.CtrlIntention;
import net.l2jpx.gameserver.controllers.GameTimeController;
import net.l2jpx.gameserver.datatables.SkillTable;
import net.l2jpx.gameserver.managers.CastleManager;
import net.l2jpx.gameserver.model.Inventory;
import net.l2jpx.gameserver.model.L2Character;
import net.l2jpx.gameserver.model.L2ManufactureList;
import net.l2jpx.gameserver.model.L2Object;
import net.l2jpx.gameserver.model.L2Skill;
import net.l2jpx.gameserver.model.L2Summon;
import net.l2jpx.gameserver.model.actor.instance.L2DoorInstance;
import net.l2jpx.gameserver.model.actor.instance.L2PcInstance;
import net.l2jpx.gameserver.model.actor.instance.L2PetInstance;
import net.l2jpx.gameserver.model.actor.instance.L2SiegeSummonInstance;
import net.l2jpx.gameserver.model.actor.instance.L2StaticObjectInstance;
import net.l2jpx.gameserver.model.actor.instance.L2SummonInstance;
import net.l2jpx.gameserver.model.actor.position.L2CharPosition;
import net.l2jpx.gameserver.network.SystemMessageId;
import net.l2jpx.gameserver.network.serverpackets.ActionFailed;
import net.l2jpx.gameserver.network.serverpackets.ChairSit;
import net.l2jpx.gameserver.network.serverpackets.RecipeShopManageList;
import net.l2jpx.gameserver.network.serverpackets.Ride;
import net.l2jpx.gameserver.network.serverpackets.SystemMessage;

public final class RequestActionUse extends L2GameClientPacket
{
	private static final Logger LOGGER = Logger.getLogger(RequestActionUse.class);
	
	private int actionId;
	private boolean ctrlPressed;
	private boolean shiftPressed;
	
	// List of Pet Actions
	private static List<Integer> petActions = Arrays.asList(15, 16, 17, 21, 22, 23, 52, 53, 54);
	
	@Override
	protected void readImpl()
	{
		actionId = readD();
		ctrlPressed = readD() == 1;
		shiftPressed = readC() == 1;
	}
	
	@Override
	protected void runImpl()
	{
		final L2PcInstance activeChar = getClient().getActiveChar();
		
		if (activeChar == null)
		{
			return;
		}
		
		if (Config.DEBUG)
		{
			LOGGER.debug(activeChar.getName() + " request Action use: id " + actionId + " 2:" + ctrlPressed + " 3:" + shiftPressed);
		}
		
		// dont do anything if player is dead
		if (actionId != 0 && activeChar.isAlikeDead())
		{
			getClient().sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		// don't do anything if player is confused
		if (activeChar.isOutOfControl())
		{
			getClient().sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		// don't do anything if player is casting and the action is not a Pet one (skills too)
		if ((petActions.contains(actionId) || actionId >= 1000))
		{
			if (Config.DEBUG)
			{
				LOGGER.debug(activeChar.getName() + " request Pet Action use: id " + actionId + " ctrl:" + ctrlPressed + " shift:" + shiftPressed);
			}
		}
		else if (activeChar.isCastingNow())
		{
			getClient().sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		final L2Summon pet = activeChar.getPet();
		final L2Object target = activeChar.getTarget();
		
		if (Config.DEBUG)
		{
			LOGGER.info("Requested Action ID: " + String.valueOf(actionId));
		}
		
		switch (actionId)
		{
			case 0:
				if (activeChar.getMountType() != 0)
				{
					break;
				}
				
				if (target != null && !activeChar.isSitting() && target instanceof L2StaticObjectInstance && ((L2StaticObjectInstance) target).getType() == 1 && CastleManager.getInstance().getCastle(target) != null && activeChar.isInsideRadius(target, L2StaticObjectInstance.INTERACTION_DISTANCE, false, false))
				{
					final ChairSit cs = new ChairSit(activeChar, ((L2StaticObjectInstance) target).getStaticObjectId());
					activeChar.sendPacket(cs);
					activeChar.sitDown();
					activeChar.broadcastPacket(cs);
					break;
				}
				
				if (activeChar.isSitting() || activeChar.isFakeDeath())
				{
					activeChar.standUp();
				}
				else
				{
					activeChar.sitDown();
				}
				
				if (Config.DEBUG)
				{
					LOGGER.debug("new wait type: " + (activeChar.isSitting() ? "SITTING" : "STANDING"));
				}
				
				break;
			case 1:
				if (activeChar.isRunning())
				{
					activeChar.setWalking();
				}
				else
				{
					activeChar.setRunning();
				}
				
				if (Config.DEBUG)
				{
					LOGGER.debug("new move type: " + (activeChar.isRunning() ? "RUNNING" : "WALKIN"));
				}
				break;
			case 15:
			case 21: // pet follow/stop
				if (pet != null && !pet.isMovementDisabled() && !activeChar.isBetrayed())
				{
					pet.setFollowStatus(!pet.getFollowStatus());
				}
				
				break;
			case 16:
			case 22: // pet attack
				if (target != null && pet != null && pet != target && !activeChar.isBetrayed())
				{
					if (pet.isAttackingDisabled())
					{
						if (pet.getAttackEndTime() > GameTimeController.getGameTicks())
						{
							pet.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, target);
						}
						else
						{
							return;
						}
					}
					
					if (activeChar.isInOlympiadMode() && !activeChar.isInOlympiadFight())
					{
						// if L2PcInstance is in Olympia and the match isn't already start, send a Server->Client packet ActionFailed
						activeChar.sendPacket(ActionFailed.STATIC_PACKET);
						return;
					}
					
					if (target instanceof L2PcInstance && !activeChar.getAccessLevel().allowPeaceAttack() && L2Character.isInsidePeaceZone(pet, target))
					{
						activeChar.sendPacket(SystemMessageId.YOU_MAY_NOT_ATTACK_THIS_TARGET_IN_PEACEFUL_ZONE);
						return;
					}
					
					if (target.isAutoAttackable(activeChar) || ctrlPressed)
					{
						if (target instanceof L2DoorInstance)
						{
							if (((L2DoorInstance) target).isAttackable(activeChar) && pet.getNpcId() != L2SiegeSummonInstance.SWOOP_CANNON_ID)
							{
								pet.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, target);
							}
						}
						// siege golem AI doesn't support attacking other than doors at the moment
						else if (pet.getNpcId() != L2SiegeSummonInstance.SIEGE_GOLEM_ID)
						{
							pet.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, target);
						}
					}
				}
				break;
			case 17:
			case 23: // pet - cancel action
				if (pet != null && !pet.isMovementDisabled() && !activeChar.isBetrayed())
				{
					pet.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE, null);
				}
				
				break;
			case 19: // pet unsummon
				if (pet != null && !activeChar.isBetrayed())
				{
					// returns pet to control item
					if (pet.isDead())
					{
						activeChar.sendPacket(new SystemMessage(SystemMessageId.A_DEAD_PET_CANNOT_SENT_BACK));
					}
					else if (pet.isAttackingNow() || pet.isRooted())
					{
						activeChar.sendMessage("You cannot despawn a summon during combat."); // Message like L2OFF
					}
					else if (pet.isInCombat() || activeChar.isInCombat())
					{
						activeChar.sendMessage("You cannot despawn a summon during combat."); // Message like L2OFF
					}
					else
					{
						// if it is a pet and not a summon
						if (pet instanceof L2PetInstance)
						{
							final L2PetInstance petInst = (L2PetInstance) pet;
							
							// if the pet is more than 40% fed
							if (petInst.getCurrentFed() > petInst.getMaxFed() * 0.40)
							{
								pet.unSummon(activeChar);
							}
							else
							{
								activeChar.sendPacket(new SystemMessage(SystemMessageId.YOU_MAY_NOT_RESTORE_A_HUNGRY_PET));
							}
						}
					}
				}
				break;
			case 38: // pet mount
				// mount
				if (pet != null && pet.isMountable() && !activeChar.isMounted() && !activeChar.isBetrayed())
				{
					if (activeChar.isDead())
					{
						// A strider cannot be ridden when dead
						final SystemMessage msg = new SystemMessage(SystemMessageId.A_STRIDER_CANNOT_BE_RIDDEN_WHEN_DEAD);
						activeChar.sendPacket(msg);
					}
					else if (pet.isDead())
					{
						// A dead strider cannot be ridden.
						final SystemMessage msg = new SystemMessage(SystemMessageId.A_DEAD_STRIDER_CANNOT_BE_RIDDEN);
						activeChar.sendPacket(msg);
					}
					else if (pet.isInCombat() || pet.isRooted())
					{
						// A strider in battle cannot be ridden
						final SystemMessage msg = new SystemMessage(SystemMessageId.STRIDER_IN_BATLLE_CANT_BE_RIDDEN);
						activeChar.sendPacket(msg);
					}
					else if (activeChar.isInCombat())
					{
						// A strider cannot be ridden while in battle
						final SystemMessage msg = new SystemMessage(SystemMessageId.STRIDER_CANT_BE_RIDDEN_WHILE_IN_BATTLE);
						activeChar.sendPacket(msg);
					}
					else if (activeChar.isSitting()) // Like L2OFF you can mount also during movement
					{
						// A strider can be ridden only when standing
						final SystemMessage msg = new SystemMessage(SystemMessageId.STRIDER_CAN_BE_RIDDEN_ONLY_WHILE_STANDING);
						activeChar.sendPacket(msg);
					}
					else if (activeChar.isFishing())
					{
						// You can't mount, dismount, break and drop items while fishing
						final SystemMessage msg = new SystemMessage(SystemMessageId.CANNOT_DO_WHILE_FISHING_2);
						activeChar.sendPacket(msg);
					}
					else if (activeChar.isCursedWeaponEquiped())
					{
						// You can't mount, dismount, break and drop items while weilding a cursed weapon
						final SystemMessage msg = new SystemMessage(SystemMessageId.STRIDER_CANT_BE_RIDDEN_WHILE_IN_BATTLE);
						activeChar.sendPacket(msg);
					}
					else if (!pet.isDead() && !activeChar.isMounted())
					{
						if (!activeChar.disarmWeapons())
						{
							return;
						}
						
						if (!activeChar.getFloodProtectors().getItemPetSummon().tryPerformAction("mount"))
						{
							return;
						}
						
						final Ride mount = new Ride(activeChar.getObjectId(), Ride.ACTION_MOUNT, pet.getTemplate().npcId);
						activeChar.broadcastPacket(mount);
						activeChar.setMountType(mount.getMountType());
						activeChar.setMountObjectID(pet.getControlItemId());
						pet.unSummon(activeChar);
						
						if (activeChar.getInventory().getPaperdollItem(Inventory.PAPERDOLL_RHAND) != null || activeChar.getInventory().getPaperdollItem(Inventory.PAPERDOLL_LRHAND) != null)
						{
							if (activeChar.isFlying())
							{
								// Remove skill Wyvern Breath
								activeChar.removeSkill(SkillTable.getInstance().getInfo(4289, 1));
								activeChar.sendSkillList();
							}
							
							if (activeChar.setMountType(0))
							{
								final Ride dismount = new Ride(activeChar.getObjectId(), Ride.ACTION_DISMOUNT, 0);
								activeChar.broadcastPacket(dismount);
								activeChar.setMountObjectID(0);
							}
						}
					}
				}
				else if (activeChar.isRentedPet())
				{
					activeChar.stopRentPet();
				}
				else if (activeChar.isMounted())
				{
					if (activeChar.isFlying())
					{
						// Remove skill Wyvern Breath
						activeChar.removeSkill(SkillTable.getInstance().getInfo(4289, 1));
						activeChar.sendSkillList();
					}
					
					if (activeChar.setMountType(0))
					{
						final Ride dismount = new Ride(activeChar.getObjectId(), Ride.ACTION_DISMOUNT, 0);
						activeChar.broadcastPacket(dismount);
						activeChar.setMountObjectID(0);
						
						// Update status after unmount to avoid visual bug
						activeChar.broadcastStatusUpdate();
						activeChar.broadcastUserInfo();
					}
				}
				break;
			case 32: // Wild Hog Cannon - Mode Change
				useSkill(4230);
				break;
			case 36: // Soulless - Toxic Smoke
				useSkill(4259);
				break;
			case 37:
				
				if (activeChar.isAlikeDead())
				{
					getClient().sendPacket(ActionFailed.STATIC_PACKET);
					return;
				}
				
				// Like L2OFF - You can't open Manufacture when you are in private store
				if (activeChar.getPrivateStoreType() == L2PcInstance.STORE_PRIVATE_BUY || activeChar.getPrivateStoreType() == L2PcInstance.STORE_PRIVATE_SELL)
				{
					getClient().sendPacket(ActionFailed.STATIC_PACKET);
					return;
				}
				
				// Like L2OFF - You can't open Manufacture when you are sitting
				if (activeChar.isSitting() && activeChar.getPrivateStoreType() != L2PcInstance.STORE_PRIVATE_MANUFACTURE)
				{
					getClient().sendPacket(ActionFailed.STATIC_PACKET);
					return;
				}
				
				// You can't open Manufacture when the task is launched
				if (activeChar.isSittingTaskLaunched())
				{
					sendPacket(ActionFailed.STATIC_PACKET);
					return;
				}
				
				if (activeChar.getPrivateStoreType() == L2PcInstance.STORE_PRIVATE_MANUFACTURE)
				{
					activeChar.setPrivateStoreType(L2PcInstance.STORE_PRIVATE_NONE);
					
					if (activeChar.isSitting())
					{
						activeChar.standUp();
					}
				}
				
				if (activeChar.getCreateList() == null)
				{
					activeChar.setCreateList(new L2ManufactureList());
				}
				
				activeChar.sendPacket(new RecipeShopManageList(activeChar, true));
				break;
			case 39: // Soulless - Parasite Burst
				useSkill(4138);
				break;
			case 41: // Wild Hog Cannon - Attack
				useSkill(4230);
				break;
			case 42: // Kai the Cat - Self Damage Shield
				useSkill(4378, activeChar);
				break;
			case 43: // Unicorn Merrow - Hydro Screw
				useSkill(4137);
				break;
			case 44: // Big Boom - Boom Attack
				useSkill(4139);
				break;
			case 45: // Unicorn Boxer - Master Recharge
				useSkill(4025, activeChar);
				break;
			case 46: // Mew the Cat - Mega Storm Strike
				useSkill(4261);
				break;
			case 47: // Silhouette - Steal Blood
				useSkill(4260);
				break;
			case 48: // Mechanic Golem - Mech. Cannon
				useSkill(4068);
				break;
			case 51:
				
				// Player shouldn't be able to set stores if he/she is alike dead (dead or fake death)
				if (activeChar.isAlikeDead())
				{
					getClient().sendPacket(ActionFailed.STATIC_PACKET);
					return;
				}
				
				// Like L2OFF - You can't open Manufacture when you are in private store
				if (activeChar.getPrivateStoreType() == L2PcInstance.STORE_PRIVATE_BUY || activeChar.getPrivateStoreType() == L2PcInstance.STORE_PRIVATE_SELL)
				{
					getClient().sendPacket(ActionFailed.STATIC_PACKET);
					return;
				}
				
				// Like L2OFF - You can't open Manufacture when you are sitting
				if (activeChar.isSitting() && activeChar.getPrivateStoreType() != L2PcInstance.STORE_PRIVATE_MANUFACTURE)
				{
					getClient().sendPacket(ActionFailed.STATIC_PACKET);
					return;
				}
				
				// You can't open Manufacture when the task is launched
				if (activeChar.isSittingTaskLaunched())
				{
					sendPacket(ActionFailed.STATIC_PACKET);
					return;
				}
				
				if (activeChar.getPrivateStoreType() == L2PcInstance.STORE_PRIVATE_MANUFACTURE)
				{
					activeChar.setPrivateStoreType(L2PcInstance.STORE_PRIVATE_NONE);
					
					if (activeChar.isSitting())
					{
						activeChar.standUp();
					}
				}
				
				if (activeChar.getCreateList() == null)
				{
					activeChar.setCreateList(new L2ManufactureList());
				}
				
				activeChar.sendPacket(new RecipeShopManageList(activeChar, false));
				break;
			case 52: // unsummon
				if (pet != null && pet instanceof L2SummonInstance)
				{
					if (pet.isInCombat() || activeChar.isInCombat())
					{
						activeChar.sendMessage("You cannot despawn a summon during combat."); // Message like L2OFF
					}
					else if (pet.isAttackingNow() || pet.isRooted())
					{
						activeChar.sendMessage("You cannot despawn a summon during combat."); // Message like L2OFF
					}
					else
					{
						pet.unSummon(activeChar);
					}
				}
				break;
			case 53: // move to target
				if (target != null && pet != null && pet != target && !pet.isMovementDisabled())
				{
					pet.getAI().setIntention(CtrlIntention.AI_INTENTION_MOVE_TO, new L2CharPosition(target.getX(), target.getY(), target.getZ(), 0));
				}
				break;
			case 54: // move to target hatch/strider
				if (target != null && pet != null && pet != target && !pet.isMovementDisabled())
				{
					pet.getAI().setIntention(CtrlIntention.AI_INTENTION_MOVE_TO, new L2CharPosition(target.getX(), target.getY(), target.getZ(), 0));
				}
				break;
			case 96: // Quit Party Command Channel
				LOGGER.info("98 Accessed");
				break;
			case 97: // Request Party Command Channel Info
				// if (!PartyCommandManager.getInstance().isPlayerInChannel(activeChar))
				// return;
				LOGGER.info("97 Accessed");
				// PartyCommandManager.getInstance().getActiveChannelInfo(activeChar);
				break;
			case 1000: // Siege Golem - Siege Hammer
				if (target instanceof L2DoorInstance)
				{
					useSkill(4079);
				}
				break;
			case 1001:
				break;
			case 1003: // Wind Hatchling/Strider - Wild Stun
				useSkill(4710); // TODO use correct skill lvl based on pet lvl
				break;
			case 1004: // Wind Hatchling/Strider - Wild Defense
				useSkill(4711, activeChar); // TODO use correct skill lvl based on pet lvl
				break;
			case 1005: // Star Hatchling/Strider - Bright Burst
				useSkill(4712); // TODO use correct skill lvl based on pet lvl
				break;
			case 1006: // Star Hatchling/Strider - Bright Heal
				useSkill(4713, activeChar); // TODO use correct skill lvl based on pet lvl
				break;
			case 1007: // Cat Queen - Blessing of Queen
				useSkill(4699, activeChar);
				break;
			case 1008: // Cat Queen - Gift of Queen
				useSkill(4700, activeChar);
				break;
			case 1009: // Cat Queen - Cure of Queen
				useSkill(4701);
				break;
			case 1010: // Unicorn Seraphim - Blessing of Seraphim
				useSkill(4702, activeChar);
				break;
			case 1011: // Unicorn Seraphim - Gift of Seraphim
				useSkill(4703, activeChar);
				break;
			case 1012: // Unicorn Seraphim - Cure of Seraphim
				useSkill(4704);
				break;
			case 1013: // Nightshade - Curse of Shade
				useSkill(4705);
				break;
			case 1014: // Nightshade - Mass Curse of Shade
				useSkill(4706, activeChar);
				break;
			case 1015: // Nightshade - Shade Sacrifice
				useSkill(4707);
				break;
			case 1016: // Cursed Man - Cursed Blow
				useSkill(4709);
				break;
			case 1017: // Cursed Man - Cursed Strike/Stun
				useSkill(4708);
				break;
			case 1031: // Feline King - Slash
				useSkill(5135);
				break;
			case 1032: // Feline King - Spinning Slash
				useSkill(5136);
				break;
			case 1033: // Feline King - Grip of the Cat
				useSkill(5137);
				break;
			case 1034: // Magnus the Unicorn - Whiplash
				useSkill(5138);
				break;
			case 1035: // Magnus the Unicorn - Tridal Wave
				useSkill(5139);
				break;
			case 1036: // Spectral Lord - Corpse Kaboom
				useSkill(5142);
				break;
			case 1037: // Spectral Lord - Dicing Death
				useSkill(5141);
				break;
			case 1038: // Spectral Lord - Force Curse
				useSkill(5140);
				break;
			case 1039: // Swoop Cannon - Cannon Fodder
				if (!(target instanceof L2DoorInstance))
				{
					useSkill(5110);
				}
				break;
			case 1040: // Swoop Cannon - Big Bang
				if (!(target instanceof L2DoorInstance))
				{
					useSkill(5111);
				}
				break;
			default:
				LOGGER.warn("Player " + activeChar + " sent unhandled action type to server, action id: " + actionId + " will be kicked.");
				activeChar.kick();
		}
	}
	
	/*
	 * Cast a skill for active pet/servitor. Target is specified as a parameter but can be overwrited or ignored depending on skill type.
	 */
	private void useSkill(final int skillId, final L2Object target)
	{
		final L2PcInstance activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		
		final L2Summon activeSummon = activeChar.getPet();
		
		if (activeChar.getPrivateStoreType() != 0)
		{
			activeChar.sendMessage("Cannot use skills while trading");
			return;
		}
		
		if (activeSummon != null && !activeChar.isBetrayed())
		{
			final Map<Integer, L2Skill> skills = activeSummon.getTemplate().getSkills();
			
			if (skills.size() == 0)
			{
				activeChar.sendPacket(new SystemMessage(SystemMessageId.S1_IS_NOT_AVAILABLE_AT_THIS_TIME_BEING_PREPARED_FOR_REUSE));
				return;
			}
			
			final L2Skill skill = skills.get(skillId);
			
			if (skill == null)
			{
				if (Config.DEBUG)
				{
					LOGGER.warn("Skill " + skillId + " missing from npcskills.sql for a summon id " + activeSummon.getNpcId());
				}
				return;
			}
			
			activeSummon.setTarget(target);
			
			boolean force = ctrlPressed;
			
			if (target instanceof L2Character)
			{
				if (activeSummon.isInsideZone(L2Character.ZONE_PVP) && ((L2Character) target).isInsideZone(L2Character.ZONE_PVP))
				{
					force = true;
				}
			}
			
			activeSummon.useMagic(skill, force, shiftPressed);
		}
	}
	
	/*
	 * Cast a skill for active pet/servitor. Target is retrieved from owner' target, then validated by overloaded method useSkill(int, L2Character).
	 */
	private void useSkill(final int skillId)
	{
		final L2PcInstance activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		
		useSkill(skillId, activeChar.getTarget());
	}
	
	@Override
	public String getType()
	{
		return "[C] 45 RequestActionUse";
	}
}
