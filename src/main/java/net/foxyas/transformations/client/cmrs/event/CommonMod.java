package net.foxyas.transformations.client.cmrs.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.foxyas.transformations.client.cmrs.CMRS;
import net.foxyas.transformations.client.cmrs.network.ClientPacketHandler;
import net.foxyas.transformations.client.cmrs.network.ClientboundRemovePlayerModelPacket;
import net.foxyas.transformations.client.cmrs.network.ClientboundSetBuiltInPlayerModelPacket;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class CommonMod {

    @SubscribeEvent
    public static void onRegisterPayload(@NotNull RegisterPayloadHandlersEvent event){
        PayloadRegistrar registrar = event.registrar(CMRS.MODID);

        //Lambda SHOULDN'T be replaced with method reference on handleClient! -> server will crash

        registrar.playToClient(ClientboundSetBuiltInPlayerModelPacket.TYPE, ClientboundSetBuiltInPlayerModelPacket.CODEC,
                ((payload, context) -> ClientPacketHandler.INSTANCE.handleSetBuiltInPlayerModelPacket(payload, context)));

        registrar.playToClient(ClientboundRemovePlayerModelPacket.TYPE, ClientboundRemovePlayerModelPacket.CODEC,
                (payload, context) -> ClientPacketHandler.INSTANCE.handleRemovePlayerModelPacket(payload, context));
    }
}
