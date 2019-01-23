package com.kamefrede.rpsideas.util;

import com.teamwizardry.librarianlib.features.animator.Animation;
import com.teamwizardry.librarianlib.features.animator.Animator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RPSAnimationHandler {
    private static final Animator ANIMATOR = new Animator();

    public static void animate(Animation<?> animation) {
        ANIMATOR.add(animation);
    }
}
