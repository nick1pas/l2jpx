package net.l2jpx.gameserver.datatables.xml;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import net.l2jpx.gameserver.datatables.sql.ItemTable;
import net.l2jpx.gameserver.model.actor.instance.L2ItemInstance;
import net.l2jpx.gameserver.model.actor.instance.L2PcInstance;
import net.l2jpx.gameserver.model.multisell.MultiSellEntry;
import net.l2jpx.gameserver.model.multisell.MultiSellIngredient;
import net.l2jpx.gameserver.model.multisell.MultiSellListContainer;
import net.l2jpx.gameserver.network.serverpackets.MultiSellList;
import net.l2jpx.gameserver.templates.L2Armor;
import net.l2jpx.gameserver.templates.L2Item;
import net.l2jpx.gameserver.templates.L2Weapon;

/**
 * @author programmos
 * @author ReynalDev
 */
public class L2Multisell
{
	private static final Logger LOGGER = Logger.getLogger(L2Multisell.class);
	private static L2Multisell instance;
	private final List<MultiSellListContainer> entries = new ArrayList<>();
	
	public MultiSellListContainer getList(final int id)
	{
		synchronized (entries)
		{
			for (final MultiSellListContainer list : entries)
			{
				if (list.getListId() == id)
				{
					return list;
				}
			}
		}
		
		LOGGER.error("[L2Multisell] Can not find list with id: " + id);
		return null;
	}
	
	private L2Multisell()
	{
		parseData();
	}
	
	public void reload()
	{
		parseData();
	}
	
	public static L2Multisell getInstance()
	{
		if (instance == null)
		{
			instance = new L2Multisell();
		}
		return instance;
	}
	
	private void parseData()
	{
		entries.clear();
		parse();
	}
	
	/**
	 * This will generate the multisell list for the items. There exist various parameters in multisells that affect the way they will appear: 1) inventory only: * if true, only show items of the multisell for which the "primary" ingredients are already in the player's inventory. By "primary"
	 * ingredients we mean weapon and armor. * if false, show the entire list. 2) maintain enchantment: presumably, only lists with "inventory only" set to true should sometimes have this as true. This makes no sense otherwise... * If true, then the product will match the enchantment level of the
	 * ingredient. if the player has multiple items that match the ingredient list but the enchantment levels differ, then the entries need to be duplicated to show the products and ingredients for each enchantment level. For example: If the player has a crystal staff +1 and a crystal staff +3 and goes
	 * to exchange it at the mammon, the list should have all exchange possibilities for the +1 staff, followed by all possibilities for the +3 staff. * If false, then any level ingredient will be considered equal and product will always be at +0 3) apply taxes: Uses the "taxIngredient" entry in order
	 * to add a certain amount of adena to the ingredients
	 * @param  listId
	 * @param  inventoryOnly
	 * @param  player
	 * @param  taxRate
	 * @return
	 */
	private MultiSellListContainer generateMultiSell(final int listId, final boolean inventoryOnly, final L2PcInstance player, final double taxRate)
	{
		MultiSellListContainer listTemplate = L2Multisell.getInstance().getList(listId);
		MultiSellListContainer list = new MultiSellListContainer();
		
		if (listTemplate == null)
		{
			return list;
		}
		
		list = new MultiSellListContainer();
		list.setListId(listId);
		
		if (inventoryOnly)
		{
			if (player == null)
			{
				return list;
			}
			
			L2ItemInstance[] items;
			
			if (listTemplate.getMaintainEnchantment())
			{
				items = player.getInventory().getUniqueItemsByEnchantLevel(false, false, false, true);
			}
			else
			{
				items = player.getInventory().getUniqueItems(false, false, false, true);
			}
			
			int enchantLevel;
			for (final L2ItemInstance item : items)
			{
				// only do the matchup on equipable items that are not currently equipped
				// so for each appropriate item, produce a set of entries for the multisell list.
				if (!item.isWear() && (item.getItem() instanceof L2Armor || item.getItem() instanceof L2Weapon))
				{
					enchantLevel = listTemplate.getMaintainEnchantment() ? item.getEnchantLevel() : 0;
					// loop through the entries to see which ones we wish to include
					for (final MultiSellEntry ent : listTemplate.getEntries())
					{
						boolean doInclude = false;
						
						// check ingredients of this entry to see if it's an entry we'd like to include.
						for (final MultiSellIngredient ing : ent.getIngredients())
						{
							if (item.getItemId() == ing.getItemId())
							{
								doInclude = true;
								break;
							}
						}
						
						// manipulate the ingredients of the template entry for this particular instance shown
						// i.e: Assign enchant levels and/or apply taxes as needed.
						if (doInclude)
						{
							list.addEntry(prepareEntry(ent, listTemplate.getApplyTaxes(), listTemplate.getMaintainEnchantment(), enchantLevel, taxRate));
						}
					}
				}
			} // end for each inventory item.
			
			items = null;
		} // end if "inventory-only"
		else
		// this is a list-all type
		{
			// if no taxes are applied, no modifications are needed
			for (final MultiSellEntry ent : listTemplate.getEntries())
			{
				list.addEntry(prepareEntry(ent, listTemplate.getApplyTaxes(), false, 0, taxRate));
			}
		}
		
		listTemplate = null;
		
		return list;
	}
	
	// Regarding taxation, the following is the case:
	// a) The taxes come out purely from the adena TaxIngredient
	// b) If the entry has no adena ingredients other than the taxIngredient, the resulting
	// amount of adena is appended to the entry
	// c) If the entry already has adena as an entry, the taxIngredient is used in order to increase
	// the count for the existing adena ingredient
	private MultiSellEntry prepareEntry(final MultiSellEntry templateEntry, final boolean applyTaxes, final boolean maintainEnchantment, final int enchantLevel, final double taxRate)
	{
		final MultiSellEntry newEntry = new MultiSellEntry();
		newEntry.setEntryId(templateEntry.getEntryId() * 100000 + enchantLevel);
		
		int adenaAmount = 0;
		
		for (final MultiSellIngredient ing : templateEntry.getIngredients())
		{
			// load the ingredient from the template
			MultiSellIngredient newIngredient = new MultiSellIngredient(ing);
			
			// if taxes are to be applied, modify/add the adena count based on the template adena/ancient adena count
			if (ing.getItemId() == 57 && ing.isTaxIngredient())
			{
				if (applyTaxes)
				{
					adenaAmount += (int) Math.round(ing.getItemCount() * taxRate);
				}
				continue; // do not adena yet, as non-taxIngredient adena entries might occur next (order not guaranteed)
			}
			else if (ing.getItemId() == 57) // && !ing.isTaxIngredient()
			{
				adenaAmount += ing.getItemCount();
				continue; // do not adena yet, as taxIngredient adena entries might occur next (order not guaranteed)
			}
			// if it is an armor/weapon, modify the enchantment level appropriately, if necessary
			else if (maintainEnchantment)
			{
				L2Item tempItem = ItemTable.getInstance().createDummyItem(ing.getItemId()).getItem();
				if (tempItem instanceof L2Armor || tempItem instanceof L2Weapon)
				{
					newIngredient.setEnchantmentLevel(enchantLevel);
				}
				
				tempItem = null;
			}
			
			// finally, add this ingredient to the entry
			newEntry.addIngredient(newIngredient);
			newIngredient = null;
		}
		
		// now add the adena, if any.
		if (adenaAmount > 0)
		{
			newEntry.addIngredient(new MultiSellIngredient(57, adenaAmount, 0, false, false));
		}
		
		// Now modify the enchantment level of products, if necessary
		for (final MultiSellIngredient ing : templateEntry.getProducts())
		{
			// load the ingredient from the template
			MultiSellIngredient newIngredient = new MultiSellIngredient(ing);
			
			if (maintainEnchantment)
			{
				// if it is an armor/weapon, modify the enchantment level appropriately
				// (note, if maintain enchantment is "false" this modification will result to a +0)
				final L2Item tempItem = ItemTable.getInstance().createDummyItem(ing.getItemId()).getItem();
				
				if (tempItem instanceof L2Armor || tempItem instanceof L2Weapon)
				{
					newIngredient.setEnchantmentLevel(enchantLevel);
				}
			}
			
			newEntry.addProduct(newIngredient);
			newIngredient = null;
		}
		
		return newEntry;
	}
	
	public void separateAndSend(final int listId, final L2PcInstance player, final boolean inventoryOnly, final double taxRate)
	{
		MultiSellListContainer list = generateMultiSell(listId, inventoryOnly, player, taxRate);
		MultiSellListContainer temp = new MultiSellListContainer();
		
		int page = 1;
		
		temp.setListId(list.getListId());
		
		for (final MultiSellEntry e : list.getEntries())
		{
			if (temp.getEntries().size() == 40)
			{
				player.sendPacket(new MultiSellList(temp, page, 0));
				page++;
				temp = new MultiSellListContainer();
				temp.setListId(list.getListId());
			}
			
			temp.addEntry(e);
		}
		
		player.setMultiSellId(listId);
		
		player.sendPacket(new MultiSellList(temp, page, 1));
		
		if (player.isGM())
		{
			player.sendMessage("MULTISELL: " + listId + ".xml");
		}
	}
	
	private void hashFiles(String path, List<File> hash)
	{
		File dir = new File(path);
		
		if (!dir.exists())
		{
			LOGGER.warn("Dir " + dir.getAbsolutePath() + " not exists");
			return;
		}
		
		File[] files = dir.listFiles();
		
		for (File f : files)
		{
			if (f.getName().endsWith(".xml"))
			{
				hash.add(f);
			}
		}
	}
	
	private void parse()
	{
		Document doc = null;
		
		List<File> files = new ArrayList<>();
		hashFiles("data/xml/multisell", files);
		
		for (File f : files)
		{
			int id = Integer.parseInt(f.getName().replaceAll(".xml", ""));
			
			try
			{
				
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				factory.setValidating(false);
				factory.setIgnoringComments(true);
				doc = factory.newDocumentBuilder().parse(f);
				
				MultiSellListContainer list = parseDocument(doc);
				list.setListId(id);
				
				updateReferencePrice(list);
				
				entries.add(list);
			}
			catch (Exception e)
			{
				LOGGER.error("Error loading file " + f, e);
			}
		}
		
		LOGGER.info("Multisell : Loaded " + entries.size() + " multisell lists.");
	}
	
	protected MultiSellListContainer parseDocument(Document doc)
	{
		MultiSellListContainer list = new MultiSellListContainer();
		
		for (Node n = doc.getFirstChild(); n != null; n = n.getNextSibling())
		{
			if ("list".equalsIgnoreCase(n.getNodeName()))
			{
				Node attribute;
				attribute = n.getAttributes().getNamedItem("applyTaxes");
				
				if (attribute == null)
				{
					list.setApplyTaxes(false);
				}
				else
				{
					list.setApplyTaxes(Boolean.parseBoolean(attribute.getNodeValue()));
				}
				
				attribute = n.getAttributes().getNamedItem("maintainEnchantment");
				
				if (attribute == null)
				{
					list.setMaintainEnchantment(false);
				}
				else
				{
					list.setMaintainEnchantment(Boolean.parseBoolean(attribute.getNodeValue()));
				}
				
				for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling())
				{
					if ("item".equalsIgnoreCase(d.getNodeName()))
					{
						MultiSellEntry e = parseEntry(d);
						list.addEntry(e);
					}
				}
			}
		}
		
		return list;
	}
	
	protected MultiSellEntry parseEntry(Node n)
	{
		final int entryId = Integer.parseInt(n.getAttributes().getNamedItem("id").getNodeValue());
		
		Node first = n.getFirstChild();
		final MultiSellEntry entry = new MultiSellEntry();
		
		for (n = first; n != null; n = n.getNextSibling())
		{
			if ("ingredient".equalsIgnoreCase(n.getNodeName()))
			{
				Node attribute;
				
				final int id = Integer.parseInt(n.getAttributes().getNamedItem("id").getNodeValue());
				final int count = Integer.parseInt(n.getAttributes().getNamedItem("count").getNodeValue());
				
				boolean isTaxIngredient = false, mantainIngredient = false;
				
				attribute = n.getAttributes().getNamedItem("isTaxIngredient");
				
				if (attribute != null)
				{
					isTaxIngredient = Boolean.parseBoolean(attribute.getNodeValue());
				}
				
				attribute = n.getAttributes().getNamedItem("mantainIngredient");
				
				if (attribute != null)
				{
					mantainIngredient = Boolean.parseBoolean(attribute.getNodeValue());
				}
				
				MultiSellIngredient e = new MultiSellIngredient(id, count, isTaxIngredient, mantainIngredient);
				entry.addIngredient(e);
				e = null;
				attribute = null;
			}
			else if ("production".equalsIgnoreCase(n.getNodeName()))
			{
				final int id = Integer.parseInt(n.getAttributes().getNamedItem("id").getNodeValue());
				final int count = Integer.parseInt(n.getAttributes().getNamedItem("count").getNodeValue());
				int enchant = 0;
				// By Azagthtot support enchantment in multisell
				if (n.getAttributes().getNamedItem("enchant") != null)
				{
					enchant = Integer.parseInt(n.getAttributes().getNamedItem("enchant").getNodeValue());
				}
				MultiSellIngredient e = new MultiSellIngredient(id, count, enchant, false, false);
				entry.addProduct(e);
				e = null;
			}
		}
		
		entry.setEntryId(entryId);
		
		first = null;
		
		return entry;
	}
	
	/**
	 * This method checks and update the container to avoid possible items buy/sell duplication. Currently support ADENA check.
	 * @param container
	 */
	private void updateReferencePrice(final MultiSellListContainer container)
	{
		
		for (final MultiSellEntry entry : container.getEntries())
		{
			
			// if ingredient is just 1 and is adena
			if (entry.getIngredients().size() == 1 && entry.getIngredients().get(0).getItemId() == 57)
			{
				
				// the buy price must necessarily higher then total reference item price / 2 that is the default sell price
				
				int totalProductReferencePrice = 0;
				for (final MultiSellIngredient product : entry.getProducts())
				{
					
					totalProductReferencePrice += (ItemTable.getInstance().getTemplate(product.getItemId()).getReferencePrice() * product.getItemCount());
					
				}
				
				if (entry.getIngredients().get(0).getItemCount() < (totalProductReferencePrice / 2))
				{
					
					LOGGER.warn("Multisell " + container.getListId() + " entryId  " + entry.getEntryId() + " has an ADENA price less then total products reference price.. Automatically Updating it..");
					entry.getIngredients().get(0).setItemCount(totalProductReferencePrice);
					
				}
				
			}
			
		}
		
	}
	
}
