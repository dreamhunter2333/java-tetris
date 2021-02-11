package com.dreamhunter.tetris.component;

import com.dreamhunter.tetris.util.Constant;
import com.dreamhunter.tetris.util.TetrisUtil;

import java.awt.*;
import java.util.List;

/**
 * 游戏图形绘制
 */
public class Background {

    // 绘制背景色
    public void draw(Graphics g) {
        g.setColor(Constant.BG_COLOR);
        g.fillRect(0, 0, Constant.FRAME_WIDTH, Constant.FRAME_HEIGHT);
    }

    // 绘制所有方块
    public void drawBlockList(Graphics g, List<Integer> blockList1, List<Integer> blockList2) {
        for (int i = 0; i < Constant.BLOCK_ROW_SIZE; i++) {
            // 正在下落的方块数组和已经下落的方块数组与操作，即可绘制所有方块
            int rowValue = blockList1.get(i) | blockList2.get(i);
            for (int j = 0; j < Constant.BLOCK_COLUMN_SIZE; j++) {
                int bitValue = (rowValue >> j) & 1;
                //  值为 1 的位置需要绘制方块
                if (bitValue == 0) continue;
                int x = Constant.BLOCK_SIZE * j;
                int y = Constant.FRAME_HEIGHT - Constant.BLOCK_SIZE * (i + 1);
                g.setColor(Constant.BLOCK_COLOR);
                g.fillRect(x, y, Constant.BLOCK_SIZE, Constant.BLOCK_SIZE);
                g.setColor(Constant.BLOCK_BORDER_COLOR);
                g.drawRect(x, y, Constant.BLOCK_SIZE, Constant.BLOCK_SIZE);
            }
        }
    }

    // 绘制开始页面
    public void drawStart(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(Constant.APP_FONT);
        int y = Constant.FRAME_HEIGHT / 3;
        int x = (Constant.FRAME_WIDTH - TetrisUtil.getStringWidth(Constant.APP_FONT, "空格键开始游戏")) >> 1;
        g.drawString("空格键开始游戏", x, y);
        y += TetrisUtil.getStringHeight(Constant.APP_FONT, "回车键开始游戏");
        x = (Constant.FRAME_WIDTH - TetrisUtil.getStringWidth(Constant.APP_FONT, "左右方向键移动")) >> 1;
        g.drawString("左右方向键移动", x, y);
        y += TetrisUtil.getStringHeight(Constant.APP_FONT, "左右方向键移动");
        x = (Constant.FRAME_WIDTH - TetrisUtil.getStringWidth(Constant.APP_FONT, "下方向键快速降落")) >> 1;
        g.drawString("下方向键快速降落", x, y);
        y += TetrisUtil.getStringHeight(Constant.APP_FONT, "下方向键快速降落");
        x = (Constant.FRAME_WIDTH - TetrisUtil.getStringWidth(Constant.APP_FONT, "上方向键旋转")) >> 1;
        g.drawString("上方向键旋转", x, y);
    }

    // 绘制结束页面
    public void drawOver(Graphics g, int score, int bestScore) {
        g.setColor(Color.BLACK);
        int x = (Constant.FRAME_WIDTH - TetrisUtil.getStringWidth(Constant.APP_FONT, "游戏结束，空格键以继续")) >> 1;
        int y = Constant.FRAME_HEIGHT / 2;
        g.setFont(Constant.APP_FONT);
        g.drawString("游戏结束，空格键以继续", x, y);
        y += TetrisUtil.getStringHeight(Constant.APP_FONT, "游戏结束，空格键以继续");
        String str = "分数：".concat(Long.toString(score));
        x = (Constant.FRAME_WIDTH - TetrisUtil.getStringWidth(Constant.APP_FONT, str)) >> 1;
        g.drawString(str, x, y);
        y += TetrisUtil.getStringHeight(Constant.APP_FONT, str);
        str = "分数：".concat(Long.toString(bestScore));
        x = (Constant.FRAME_WIDTH - TetrisUtil.getStringWidth(Constant.APP_FONT, str)) >> 1;
        g.drawString("最高分数：".concat(Long.toString(bestScore)), x, y);
    }

    // 绘制实时分数
    public void drawScore(Graphics g, int score) {
        g.setColor(Color.white);
        g.setFont(Constant.APP_FONT);
        String str = "分数：".concat(Long.toString(score));
        int x = Constant.FRAME_WIDTH - TetrisUtil.getStringWidth(Constant.APP_FONT, str) - 10;
        g.drawString(str, x, Constant.FRAME_HEIGHT / 10);
    }

    // 绘制暂停页面
    public void drawPause(Graphics g) {
        g.setColor(Color.white);
        g.setFont(Constant.APP_FONT);
        int x = (Constant.FRAME_WIDTH - TetrisUtil.getStringWidth(Constant.APP_FONT, "暂停中 空格键以继续")) >> 1;
        g.drawString("暂停中 空格键以继续", x, Constant.FRAME_HEIGHT / 2);
    }
}
