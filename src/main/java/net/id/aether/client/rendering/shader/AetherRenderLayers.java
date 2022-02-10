package net.id.aether.client.rendering.shader;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.aether.mixin.client.render.RenderLayerAccessor;
import net.id.aether.mixin.client.render.RenderPhaseAccessor;
import net.id.aether.util.Config;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;

import java.util.List;

import static net.id.aether.client.rendering.shader.AetherShaders.locate;

@Environment(EnvType.CLIENT)
public final class AetherRenderLayers{
    private AetherRenderLayers(){}
    
    public static final RenderLayer AURAL;
    public static final RenderLayer AURAL_CUTOUT_MIPPED;
    
    static{
        if(Config.SODIUM_WORKAROUND){
            AURAL = RenderLayer.getSolid();
            AURAL_CUTOUT_MIPPED = RenderLayer.getCutoutMipped();
        }else{
            AURAL = RenderLayerAccessor.invokeOf(locate("aural"), VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS, 0x20000, true, false, RenderLayer.MultiPhaseParameters.builder().lightmap(RenderPhaseAccessor.getEnableLightmap()).shader(AetherRenderPhases.AURAL).texture(RenderPhaseAccessor.getMipmapBlockAtlasTexture()).build(true));
            AURAL_CUTOUT_MIPPED = RenderLayerAccessor.invokeOf(locate("aural_cutout_mipped"), VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS, 0x20000, true, false, RenderLayer.MultiPhaseParameters.builder().lightmap(RenderPhaseAccessor.getEnableLightmap()).shader(AetherRenderPhases.AURAL_CUTOUT_MIPPED).texture(RenderPhaseAccessor.getMipmapBlockAtlasTexture()).build(true));
        }
    }
    
    static void init(){}
    
    public static List<RenderLayer> getBlockLayers(){
        if(Config.SODIUM_WORKAROUND){
            return List.of();
        }
        return List.of(AURAL, AURAL_CUTOUT_MIPPED);
    }
}
