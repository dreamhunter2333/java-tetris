package com.dreamhunter.tetris.util;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.util.List;
import java.util.Random;

/**
 * 工具类，游戏中用到的工具都在此类
 */
public class TetrisUtil {

    private static final Random random = new Random();

    /**
     * 返回指定区间的一个随机整数
     * @param max 区间最大值，不包含
     * @return 该区间的随机数
     */
    public static int randomInt(int max) {
        return random.nextInt(max);
    }

    /**
     * 获得指定字符串在指定字体的宽
     */
    public static int getStringWidth(Font font, String str) {
        AffineTransform affinetransform = new AffineTransform();
        FontRenderContext frc = new FontRenderContext(affinetransform, true, true);
        return (int) (font.getStringBounds(str, frc).getWidth());
    }

    /**
     * 获得指定字符串在指定字体的高
     */
    public static int getStringHeight(Font font, String str) {
        AffineTransform affinetransform = new AffineTransform();
        FontRenderContext frc = new FontRenderContext(affinetransform, true, true);
        return (int) (font.getStringBounds(str, frc).getHeight());
    }

    /**
     * 判断两个数组的二进制数字是否有交集
     * 有交集时返回 false
     */
    public static boolean checkBlockMove(List<Integer> blockList1, List<Integer> blockList2) {
        for (int i = 0; i < Constant.BLOCK_ROW_SIZE; i++) {
            if ((blockList1.get(i) & blockList2.get(i)) != 0) {
                return false;
            }
        }
        return true;
    }
}
