package com.darkemerald78.lightrock.gui;

import com.darkemerald78.lightrock.LightRock;
import com.darkemerald78.lightrock.network.SetTagMessage;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class TagGUIWindow extends GuiScreen {

    private GuiTextField txtInput;

    public static final int WIDTH = 180;
    public static final int HEIGHT = 150;

    private GuiButton button;

    private int x,y,z;

    private int guiLeft, guiTop;


    public TagGUIWindow() {
        super();
    }

    public TagGUIWindow(int x, int y, int z) {
        super();
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void initGui() {

        this.labelList.clear();
        this.buttonList.clear();

        this.guiLeft = (this.width - WIDTH) / 2;
        this.guiTop = (this.height - HEIGHT) / 2;

        GuiLabel label = new GuiLabel(this.fontRenderer, 0, (int) (guiLeft * 1.25), (int) (guiTop * 1.25 - 30), 50, 20, 0xFFFFFF);
        label.addLine("Tag: ");
        this.labelList.add(label);


        txtInput = new GuiTextField(1, this.fontRenderer, (int) (guiLeft * 1.25), (int) (guiTop * 1.25), 100, 20);
        txtInput.setMaxStringLength(15);
        txtInput.setVisible(true);
        txtInput.setFocused(true);

        button = new GuiButton(2, (int) (guiLeft * 1.25), (int) (guiTop * 1.25 + 40), "Confirm");

        this.buttonList.add(button);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

        this.drawDefaultBackground();

        txtInput.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if(button.id == 2) {
            if(txtInput.getText() != null && txtInput.getText().trim().length() > 0) {
                LightRock.NETWORK.sendToServer(new SetTagMessage(txtInput.getText(), x, y, z));
                mc.player.closeScreen();
            }
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        txtInput.updateCursorCounter();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        txtInput.textboxKeyTyped(typedChar, keyCode);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }


}
