package xyz.elevated.frequency.check.impl.aimassist;

import org.bukkit.Bukkit;
import xyz.elevated.frequency.check.CheckData;
import xyz.elevated.frequency.check.type.RotationCheck;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.update.RotationUpdate;

@CheckData(name = "AimAssist (F)")
public final class AimAssistF extends RotationCheck {
    public AimAssistF(PlayerData playerData) {
        super(playerData);
    }

    private double lastDeltaPitch, lastDeltaYaw, lastAccelYaw;

    @Override
    public void process(RotationUpdate rotationUpdate) {
        final double deltaYaw = rotationUpdate.getDeltaYaw();
        final double deltaPitch = rotationUpdate.getDeltaYaw();

        final double accelYaw = deltaYaw - lastDeltaYaw;

        Bukkit.broadcastMessage(accelYaw + "");
        fail();

        lastDeltaYaw = deltaYaw;
    }
}
