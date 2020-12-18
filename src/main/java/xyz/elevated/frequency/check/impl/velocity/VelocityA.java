package xyz.elevated.frequency.check.impl.velocity;

import org.bukkit.Bukkit;
import xyz.elevated.frequency.check.CheckData;
import xyz.elevated.frequency.check.type.PositionCheck;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.data.impl.VelocityManager;
import xyz.elevated.frequency.update.PositionUpdate;


@CheckData(name = "Velocity (A)")
public class VelocityA extends PositionCheck {
    public VelocityA(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void process(PositionUpdate positionUpdate) {
        super.process(positionUpdate);
        if (playerData.getVelocityManager().isTakingVelocity()) {
            final VelocityManager man = playerData.getVelocityManager();

            man.setTicksSinceVel(playerData.getVelocityManager().getTicksSinceVel() + 1);

            if (man.getTicksSinceVel() <= man.getMaxVelTicks()/* && NmsUtil.getBlock(new Location(playerData.getBukkitPlayer().getWorld(), playerData.getBoundingBox().get().getCenterX(), playerData.getBoundingBox().get().getMaxY() + 0.5f, playerData.getBoundingBox().get().getCenterZ())) == null*/) {
                Bukkit.broadcastMessage("xddd");
            }
        }
    }
}
