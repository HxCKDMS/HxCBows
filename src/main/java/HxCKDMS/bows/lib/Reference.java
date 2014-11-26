package HxCKDMS.bows.lib;

import net.minecraft.util.ResourceLocation;

public class Reference {
    public static final String MOD_ID = "HxCBows";
    public static final String MOD_NAME = "HxC-Bows";
    public static final String VERSION = "Alpha 0.1";
    public static final String DEPENDENCIES = "required-after:Forge@[10.13.2.1230,);required-after:HxCCore";
    public static final String CHANNEL_NAME = Reference.MOD_ID;
    public static final String RESOURCE_LOCATION = Reference.MOD_ID.toLowerCase() + ":";
    
    public static final ResourceLocation particleTextures = new ResourceLocation("textures/particle/particles.png");
}
