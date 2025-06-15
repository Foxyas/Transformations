package net.foxyas.transformations.init;

import net.foxyas.transformations.commands.TransformationCommand;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@EventBusSubscriber
public class TransformationCommands {

    @SubscribeEvent
    public static void onCommandRegister(RegisterCommandsEvent event) {
        TransformationCommand.register(event.getDispatcher());
        TransformationCommand.registerGet(event.getDispatcher());
    }
}
