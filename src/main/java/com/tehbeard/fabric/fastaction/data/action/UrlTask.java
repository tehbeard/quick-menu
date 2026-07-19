package com.tehbeard.fabric.fastaction.data.action;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.util.Util;

public class UrlTask implements IActionTask {

    public static final MapCodec<UrlTask> CODEC = RecordCodecBuilder.mapCodec(inst ->
        inst.group(
            Codec.STRING.fieldOf("url").forGetter(UrlTask::getUrl)
        ).apply(inst, UrlTask::new)
    );

    public UrlTask(String url) {
        this.url = url;
    }

    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        if(!url.contains("://"))
        {
            this.url = "https://" + url;
        } else {
            this.url = url;
        }
    }

    @Override
    public String type() {
        return "url";
    }

    @Override
    public Component description() {
        return Component.literal("URL: " + url).setStyle(Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.DARK_GRAY)));
    }

    @Override
    public long run() {
        Util.getPlatform().openUri(url);
        return 0;
    }

    @Override
    public TaskType<?> getType() {
        return TaskType.URL_TASK;
    }
}
