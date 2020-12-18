package xyz.elevated.frequency.data.impl;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.PacketPlayOutTransaction;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import net.minecraft.server.v1_8_R3.PlayerConnectionUtils;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import xyz.elevated.frequency.Frequency;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.util.NmsUtil;
import xyz.elevated.frequency.wrapper.impl.client.WrappedPlayInTransaction;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;

public final class VelocityManager {
    private final PlayerData data;

    // This is where we will store all the velocity data
    @Getter
    private final List<VelocitySnapshot> velocities;

    public VelocityManager(final PlayerData data) {
        velocities = new ArrayList<>();
        this.data = data;
    }

    // Remove any old and unnecessary velocity occurrences
    private final Predicate<VelocitySnapshot> shouldRemoveVelocity = velocity -> velocity.getCreationTime() + 2000L < System.currentTimeMillis();

    // Add the player's velocity to the list
    public void addVelocityEntry(final double x, final double y, final double z) {
        velocities.add(new VelocitySnapshot(x, y, z, x * x + z * z, Math.abs(y)));
    }

    @Getter private double velocityX, velocityY, velocityZ, lastVelocityX, lastVelocityY, lastVelocityZ;
    @Getter private short velocityId;
    @Getter private boolean verifyingVelocity;
    @Getter private int maxVelTicks, velTicks;
    @Setter @Getter private int ticksSinceVel;

    public void handleVel(final double velocityX, final double velocityY, final double velocityZ) {
        ticksSinceVel = 0;

        lastVelocityX = this.velocityX;
        lastVelocityY = this.velocityY;
        lastVelocityZ = this.velocityZ;

        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.velocityZ = velocityZ;

        velocityId = (short) ThreadLocalRandom.current().nextInt(32767);
        verifyingVelocity = true;
        data.sendPacket(new PacketPlayOutTransaction(0, velocityId, false));
    }

    public void handleTrans(final WrappedPlayInTransaction p) {
        if(verifyingVelocity && p.getHash() == velocityId) {
            verifyingVelocity = false;

            velTicks = Frequency.INSTANCE.getTickProcessor().getTicks();

            maxVelTicks = (int) (((lastVelocityZ + lastVelocityX) / 2 + 2) * 15);
        }
    }

    // Get the highest horizontal velocity
    public double getMaxHorizontal() {
        try {
            return Math.sqrt(velocities.stream()
                    .mapToDouble(VelocitySnapshot::getHorizontal)
                    .max()
                    .orElse(0.0));
        } catch (Exception e) {
            return 1.0;
        }
    }

    // Get the highest vertical velocity
    public double getMaxVertical() {
        try {
            return Math.sqrt(velocities.stream()
                    .mapToDouble(VelocitySnapshot::getHorizontal)
                    .max()
                    .orElse(0.f));
        } catch (Exception e) {
            return 1.0;
        }
    }

    public boolean isTakingVelocity() {
        return Math.abs(Frequency.INSTANCE.getTickProcessor().getTicks() - velTicks) < maxVelTicks;
    }

    public void apply() {
        velocities.removeIf(shouldRemoveVelocity);
    }

    // We do not want to put a getter here as we don't want a getter for the entire class including a getter for the class itself
    private class VelocitySnapshot {
        @Getter
        private final double horizontal, vertical;
        @Getter
        private final double x, y, z;
        @Getter
        private final long creationTime = System.currentTimeMillis();

        public VelocitySnapshot(final double x, final double y, final double z, final double horizontal, final double vertical) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.horizontal = horizontal;
            this.vertical = vertical;
        }
    }
}
