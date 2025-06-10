package net.foxyas.transformationsAPI.transformations;

import net.minecraft.resources.ResourceLocation;

public class Transformation {
    private final ResourceLocation id;

    public Transformation(ResourceLocation id) {
        this.id = id;
    }

    public ResourceLocation getId() {
        return id;
    }

    // Você pode adicionar métodos utilitários ou propriedades extras aqui
}
