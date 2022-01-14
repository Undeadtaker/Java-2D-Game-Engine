package engine.GUI;

import imgui.ImGui;

public class ImGUI_Engine
{
    private boolean b_showText = false;

    public void draw_test_window() {
        ImGui.begin("Cool Window");

        if (ImGui.button("I am a button")) {b_showText = true;}

        if (b_showText)
        {
            ImGui.text("You clicked a button");
            ImGui.sameLine();

            if (ImGui.button("Stop showing text"))
            {
                b_showText = false;
            }
        }
        ImGui.end();
    }
}