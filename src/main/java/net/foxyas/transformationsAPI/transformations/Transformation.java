package net.foxyas.transformationsAPI.transformations;

import net.minecraft.resources.ResourceLocation;

public class Transformation {
    private final ResourceLocation name;

    public Transformation(ResourceLocation name) {
        this.name = name;
    }

    public ResourceLocation getName() {
        return name;
    }

    // Você pode adicionar métodos utilitários ou propriedades extras aqui
}
