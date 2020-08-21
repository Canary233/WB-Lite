package top.whitecola.wblite.listener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;

import top.dsbbs2.whitelist.WhiteListPlugin;
import top.dsbbs2.whitelist.config.struct.WhiteListConfig;
import top.dsbbs2.whitelist.config.struct.WhiteListConfig.WLPlayer;
import top.dsbbs2.whitelist.util.CommandUtil;
import top.dsbbs2.whitelist.util.PlayerUtil;
import top.whitecola.wblite.WBLite;
import top.whitecola.wlbot.util.BotUtil;
import top.whitecola.wlbot.util.GroupUtil;

public class PlayerListener implements Listener {
	@EventHandler(priority=EventPriority.MONITOR)
	public void onPlayerLogin(PlayerLoginEvent e) {
		if (e.getResult()==Result.ALLOWED) {
			WBLite.instance.addTask(()->{
				if(WBLite.instance.config.getConfig().playerJoinServerGroupMsg) {
					sendJoinOrLeaveServerGroupMsg(1, e.getPlayer());
				}
				WLPlayer wlp = PlayerUtil.getWLPlayerByName(e.getPlayer().getName());
				if(wlp!=null&&wlp.QQ!=-1) {
					if(!GroupUtil.isQQInGroupAuto(wlp.QQ)) {
						Bukkit.getScheduler().runTask(WBLite.instance, ()->{
							CommandUtil.getCommand(WhiteListPlugin.instance.childCmds, "remove").onCommand(Bukkit.getConsoleSender(), WhiteListPlugin.instance.getCommand("wl"), "wl", new String[] {"remove",e.getPlayer().getName()});
						});
					}
				}
			});
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		if(WBLite.instance.config.getConfig().playerLeaveServerGroupMsg) {
			WBLite.instance.addTask(()->{
				sendJoinOrLeaveServerGroupMsg(2, e.getPlayer());
			});
		}
	}


	public void sendJoinOrLeaveServerGroupMsg(int type,Player p) {
		try {

		} catch (Exception e) {
			// TODO: handle exception
		}
		//type ==1 join
		WhiteListConfig.WLPlayer wlp=PlayerUtil.getWLPlayerByName(p.getName());
		if(wlp!=null)
		{
			if(wlp.QQ!=-1) {
				if(type==1) {
					GroupUtil.sendMsgToGroupAuto("��� "+wlp.name+"["+wlp.QQ+"] ��������Ϸ.");
					GroupUtil.sendMsgToGroupAuto("Ŀǰ����������: "+(p.getServer().getOnlinePlayers().size())+" ��.");
					return;
				}else {
					GroupUtil.sendMsgToGroupAuto("��� "+wlp.name+"["+wlp.QQ+"] �˳�����Ϸ.");
					GroupUtil.sendMsgToGroupAuto("Ŀǰ����������: "+(p.getServer().getOnlinePlayers().size())+" ��.");
					return;
				}
			}else {
				if(type==1) {
					GroupUtil.sendMsgToGroupAuto("��� "+p.getName()+" ��������Ϸ.");
					GroupUtil.sendMsgToGroupAuto("Ŀǰ����������: "+(p.getServer().getOnlinePlayers().size())+" ��.");
					return;
				}else {
					GroupUtil.sendMsgToGroupAuto("��� "+p.getName()+" �˳�����Ϸ.");
					GroupUtil.sendMsgToGroupAuto("Ŀǰ����������: "+(p.getServer().getOnlinePlayers().size())+" ��.");
					return;
				}

			}
		}else {
			if(type==1) {
				GroupUtil.sendMsgToGroupAuto("û�а���������� "+p.getName()+" ��������Ϸ.");
				GroupUtil.sendMsgToGroupAuto("Ŀǰ����������: "+(p.getServer().getOnlinePlayers().size()+1)+" ��.");
				return;
			}else {
				GroupUtil.sendMsgToGroupAuto("û�а���������� "+p.getName()+" �˳�����Ϸ.");
				GroupUtil.sendMsgToGroupAuto("Ŀǰ����������: "+(p.getServer().getOnlinePlayers().size()-1)+" ��.");
				return;
			}
		}




	}

	@EventHandler(priority=EventPriority.MONITOR,ignoreCancelled=false)
	public void onPlayerMsg(AsyncPlayerChatEvent e) {
		if(WBLite.instance.config.getConfig().synchronousMsgFunction) {
			WBLite.instance.addTask(()->{
				BotUtil.sendServerMsgToGroup(e.getMessage(),e.getPlayer());
			});

		}


	}

	@EventHandler(priority=EventPriority.MONITOR,ignoreCancelled=false)
	public void onPlayerDie(PlayerDeathEvent e) {
		if(!WBLite.instance.config.getConfig().sendDieMsgToServerWhenPlayerDie) {
			return;
		}
		
		WBLite.instance.addTask(()->{
			BotUtil.sendEventMessageToGroup("���"+e.getEntity().getPlayer().getName()+"������ , ԭ��Ϊ "+e.getDeathMessage());
			WLPlayer wlp = PlayerUtil.getWLPlayerByName(e.getEntity().getName());
			if(wlp!=null&&wlp.QQ!=-1) {
				GroupUtil.sendMsgToGroupAuto("���������Ѿ��ڷ������з���"+wlp.name+"["+wlp.QQ+"]");
			}
		});
		


		Location loc = e.getEntity().getLocation();
		e.getEntity().getPlayer().sendMessage("[WB]��e���ϴ��������� "+loc.getWorld().getName()+"���� "+loc.getBlockX()+" "+loc.getBlockY()+" "+loc.getBlockZ());
	}


}
