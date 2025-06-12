package net.foxyas.transformations.client.cmrs;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class Keybindings {

    private static final String CATEGORY = name(".keyCategory");

    public static final KeyMapping MODEL_MANAGER = new KeyMapping(name(".model_manager"), KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, InputConstants.KEY_I, CATEGORY);

    @Contract(pure = true)
    private static @NotNull String name(String str){
        return "key." + CMRS.MODID + str;
    }
}
