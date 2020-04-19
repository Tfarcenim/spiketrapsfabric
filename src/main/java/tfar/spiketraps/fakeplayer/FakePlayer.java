package tfar.spiketraps.fakeplayer;

/*
 * Minecraft Forge
 * Copyright (c) 2016-2019.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation version 2.1
 * of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */


import com.mojang.authlib.GameProfile;
import net.minecraft.container.NameableContainerProvider;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.MessageType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.network.packet.ClientSettingsC2SPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stat;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

import java.util.OptionalInt;

//Preliminary, simple Fake Player class
public class FakePlayer extends ServerPlayerEntity
{
	public FakePlayer(ServerWorld world, GameProfile name)
	{
		super(world.getServer(), world, name, new ServerPlayerInteractionManager(world));
	}

	@Override public Vec3d getPosVector(){ return new Vec3d(0, 0, 0); }
	@Override public void sendChatMessage(Text text, MessageType messageType) {}
	@Override public void sendMessage(Text component) {}
	@Override public void increaseStat(Stat par1StatBase, int par2){}
	@Override public OptionalInt openContainer(NameableContainerProvider nameableContainerProvider) {
		return OptionalInt.empty(); }

	@Override public boolean isInvulnerableTo(DamageSource source){ return true; }
	@Override public boolean isAffectedBySplashPotions() {return false;}
	@Override public boolean shouldDamagePlayer(PlayerEntity player) {return false;}
	@Override public void onDeath(DamageSource source){}
	@Override public void tick(){}
	@Override public void setClientSettings(ClientSettingsC2SPacket clientSettingsC2SPacket) {}
}