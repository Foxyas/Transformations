package net.foxyas.transformations.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.foxyas.transformations.Transformation;
import net.foxyas.transformations.Transformations;
import net.foxyas.transformations.util.TransformationUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;

public class TransformationCommand {

    public static final DynamicCommandExceptionType UNKNOWN_TRANSFORMATION =
            new DynamicCommandExceptionType(name ->
                    Component.literal("Unknown transformation: " + name));

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("transformations")
                .requires(source -> source.hasPermission(2)) // permission level 2+
                .then(Commands.literal("set")
                        .then(Commands.argument("target", EntityArgument.player())
                                .then(Commands.argument("transformation", ResourceKeyArgument.key(Transformations.TRANSFORMATION_REGISTRY))
                                        .suggests(SUGGEST_TRANSFORMATIONS)
                                        .executes(context -> {
                                            ServerPlayer target = EntityArgument.getPlayer(context, "target");
                                            ResourceKey<Transformation> key = ResourceKeyArgument.getRegistryKey(context, "transformation", Transformations.TRANSFORMATION_REGISTRY, UNKNOWN_TRANSFORMATION);

                                            TransformationUtils.setTransformation(target, key, true);
                                            context.getSource().sendSuccess(() ->
                                                    Component.literal("Set transformation of " + target.getName().getString() + " to " + key.location()), true);
                                            return 1;
                                        })))));
    }


    public static void registerGet(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("transformations")
                .requires(source -> source.hasPermission(2)) // permission level 2+
                .then(Commands.literal("get").executes(context -> {
                    List<String> transformations = new ArrayList<>();
                    context.getSource().getServer().registryAccess()
                            .lookupOrThrow(Transformations.TRANSFORMATION_REGISTRY)
                            .listElements()
                            .forEach(holder -> transformations.add(holder.getDelegate().getRegisteredName()));

                    for (String transformation : transformations) {
                        context.getSource().sendSuccess(() ->
                                Component.literal("Transformations Available: " + transformation), true);
                    }
                    return 1;
                })));
    }

    private static final SuggestionProvider<CommandSourceStack> SUGGEST_TRANSFORMATIONS =
            (context, builder) -> {
                context.getSource().getServer().registryAccess()
                        .lookupOrThrow(Transformations.TRANSFORMATION_REGISTRY)
                        .listElements()
                        .forEach(holder -> builder.suggest(holder.getDelegate().getRegisteredName()));
                return builder.buildFuture();
            };

}
