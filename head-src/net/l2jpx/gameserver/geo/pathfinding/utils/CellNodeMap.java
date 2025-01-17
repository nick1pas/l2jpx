package net.l2jpx.gameserver.geo.pathfinding.utils;

import net.l2jpx.gameserver.geo.pathfinding.Node;
import net.l2jpx.gameserver.geo.util.L2FastSet;
import net.l2jpx.gameserver.geo.util.ObjectPool;

/**
 * @author Sami
 */
public final class CellNodeMap
{
	protected final L2FastSet<Node> cellIndex = new L2FastSet<>(4096);
	
	protected CellNodeMap()
	{
		
	}
	
	public void add(final Node n)
	{
		cellIndex.add(n);
	}
	
	public boolean contains(final Node n)
	{
		return cellIndex.contains(n);
	}
	
	public static CellNodeMap newInstance()
	{
		return POOL.get();
	}
	
	public static void recycle(final CellNodeMap map)
	{
		POOL.store(map);
	}
	
	private static final ObjectPool<CellNodeMap> POOL = new ObjectPool<>()
	{
		@Override
		protected void reset(final CellNodeMap map)
		{
			map.cellIndex.clear();
		}
		
		@Override
		protected CellNodeMap create()
		{
			return new CellNodeMap();
		}
	};
}