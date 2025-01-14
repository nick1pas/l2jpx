package net.l2jpx.loginserver;

import java.nio.channels.SocketChannel;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import net.l2jpx.loginserver.network.serverpackets.Init;
import net.l2jpx.netcore.IAcceptFilter;
import net.l2jpx.netcore.IClientFactory;
import net.l2jpx.netcore.IMMOExecutor;
import net.l2jpx.netcore.MMOConnection;
import net.l2jpx.netcore.ReceivablePacket;
import net.l2jpx.util.IPv4Filter;

/**
 * @author ProGramMoS
 */
public class SelectorHelper implements IMMOExecutor<L2LoginClient>, IClientFactory<L2LoginClient>, IAcceptFilter
{
	private final ThreadPoolExecutor generalPacketsThreadPool;
	private final IPv4Filter ipv4filter;
	
	public SelectorHelper()
	{
		generalPacketsThreadPool = new ThreadPoolExecutor(4, 6, 15L, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
		ipv4filter = new IPv4Filter();
	}
	
	@Override
	public void execute(final ReceivablePacket<L2LoginClient> packet)
	{
		generalPacketsThreadPool.execute(packet);
	}
	
	@Override
	public L2LoginClient create(final MMOConnection<L2LoginClient> con)
	{
		final L2LoginClient client = new L2LoginClient(con);
		client.sendPacket(new Init(client));
		
		return client;
	}
	
	@Override
	public boolean accept(final SocketChannel sc)
	{
		// return !LoginController.getInstance().isBannedAddress(sc.socket().getInetAddress());
		
		return ipv4filter.accept(sc) && !LoginController.getInstance().isBannedAddress(sc.socket().getInetAddress());
	}
}
