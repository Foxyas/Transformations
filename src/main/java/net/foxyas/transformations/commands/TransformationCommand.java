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
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TransformationCommand {

    public static final DynamicCommandExceptionType UNKNOWN_TRANSFORMATION =
            new DynamicCommandExceptionType(name ->
                    Component.literal("Unknown transformation: " + name));

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("transformations")
                .requires(source -> source.hasPermission(2)) // permission level 2+
                .executes(context -> list(context.getSource()))
                .then(Commands.literal("get")
                        .executes(context -> get(context.getSource(), null))
                        .then(Commands.argument("target", EntityArgument.player())
                                .executes(context -> get(context.getSource(), EntityArgument.getPlayer(context, "target")))
                        )
                )
                .then(Commands.literal("set")
                        .then(Commands.argument("transformation", ResourceKeyArgument.key(Transformations.TRANSFORMATION_REGISTRY))
                                .suggests(SUGGEST_TRANSFORMATIONS)
                                .executes(context -> set(context.getSource(), ResourceKeyArgument.getRegistryKey(context, "transformation", Transformations.TRANSFORMATION_REGISTRY, UNKNOWN_TRANSFORMATION), null))
                                .then(Commands.argument("target", EntityArgument.player())
                                        .executes(context -> set(context.getSource(), ResourceKeyArgument.getRegistryKey(context, "transformation", Transformations.TRANSFORMATION_REGISTRY, UNKNOWN_TRANSFORMATION), EntityArgument.getPlayer(context, "target")))
                                )
                        )
                )
                .then(Commands.literal("clear")
                        .executes(context -> clear(context.getSource(), null))
                        .then(Commands.argument("target", EntityArgument.player())
                                .executes(context -> clear(context.getSource(), EntityArgument.getPlayer(context, "target")))
                        )
                )
        );
    }

    private static int get(CommandSourceStack stack, @Nullable Player target){
        Component comp;
        ResourceKey<Transformation> key;
        if(target != null){
            key = TransformationUtils.getTransformation(target);
            comp = key == null ? Component.literal("").append(target.getDisplayName()).append(" has no transformation")
                    : Component.literal("Transformation of ").append(target.getDisplayName()).append(" is " + key.location());
        } else if(stack.isPlayer()){
            key = TransformationUtils.getTransformation(stack.getPlayer());
            comp = key == null ? Component.literal("You have no transformation")
                    : Component.literal("Your transformation is " + key.location());
        } else return 0;

        stack.sendSuccess(() -> comp, true);
        return 1;
    }

    private static int set(CommandSourceStack stack, ResourceKey<Transformation> transform, @Nullable Player target){
        Component comp;
        if(target != null){
            TransformationUtils.setTransformation(target, transform);
            comp = Component.literal("Set transformation of ").append(target.getDisplayName()).append(" to " + transform.location());
        } else if(stack.isPlayer()){
            TransformationUtils.setTransformation(stack.getPlayer(), transform);
            comp = Component.literal("Set own transformation to " + transform.location());
        } else return 0;

        stack.sendSuccess(() -> comp, true);
        return 1;
    }

    private static int clear(CommandSourceStack stack, @Nullable Player target){
        Component comp;
        if(target != null){
            TransformationUtils.setTransformation(target, null);
            comp = Component.literal("Cleared transformation of ").append(target.getDisplayName());
        } else if(stack.isPlayer()){
            TransformationUtils.setTransformation(stack.getPlayer(), null);
            comp = Component.literal("Cleared own transformation");
        } else return 0;

        stack.sendSuccess(() -> comp, true);
        return 1;
    }

    private static final List<String> list = new ArrayList<>();
    private static int list(CommandSourceStack stack){
        list.clear();
        stack.getServer().registryAccess()
                .lookupOrThrow(Transformations.TRANSFORMATION_REGISTRY)
                .listElements()
                .forEach(holder -> list.add(holder.getDelegate().getRegisteredName()));

        if(list.isEmpty()){
            stack.sendFailure(Component.literal("No transformations loaded!"));
            return 0;
        }

        MutableComponent comp = Component.literal("Transformations Available:");

        list.forEach(loc -> comp.append(" " + loc));

        stack.sendSuccess(() -> comp, true);
        return 1;
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
