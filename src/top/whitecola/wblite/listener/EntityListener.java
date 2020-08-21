package top.whitecola.wblite.listener;

import org.bukkit.Location;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ExplosionPrimeEvent;

import top.whitecola.wblite.WBLite;
import top.whitecola.wlbot.util.BotUtil;
import top.whitecola.wlbot.util.GroupUtil;

public class EntityListener implements Listener{
	@EventHandler(priority=EventPriority.MONITOR,ignoreCancelled=false)
	public void onCreeperExplosionPrime(ExplosionPrimeEvent e) {
		if((!WBLite.instance.config.getConfig().serverEventSentToGroup) || (!WBLite.instance.config.getConfig().creeperEventMsg)) {
			return;
		}
		if(e.getEntity()==null) {
			return;
		}
		if(e.getEntityType()==EntityType.CREEPER) {
			if(e.getEntity() instanceof Creeper) {
				Creeper cr = (Creeper)e.getEntity();
				Location loc = cr.getLocation();
				BotUtil.sendEventMessageToGroup("�����±�ը�� "+loc.getWorld().getName()+"���� "+loc.getBlockX()+" "+loc.getBlockY()+" "+loc.getBlockZ());
				Player tp = null;
				if(cr.getTarget() instanceof Player) {
					tp = (Player)cr.getTarget();
				}
				if(tp!=null) {
					GroupUtil.sendMsgToGroupAuto("�����±�ըĿ�����Ϊ "+tp.getName());
				}else {
					GroupUtil.sendMsgToGroupAuto("û���ҵ������±�ըĿ�����,����Ŀ����ҿ��������������� ���߸ÿ������Ǳ����ʯ����");
				}
			}
		}
	}
}
