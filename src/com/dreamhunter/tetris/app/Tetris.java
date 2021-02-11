package com.dreamhunter.tetris.app;

import com.dreamhunter.tetris.component.Background;
import com.dreamhunter.tetris.component.Block;
import com.dreamhunter.tetris.listener.BlockKeyListener;
import com.dreamhunter.tetris.util.Constant;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static com.dreamhunter.tetris.util.Constant.*;

public class Tetris extends Frame {

    private static final long serialVersionUID = 1L; // 保持版本的兼容性

    // 方块数组
    private List<Integer> blockList = new ArrayList<>(Constant.INIT_BLOCK_LIST);
    // 分数
    private int score = 0;

    // 游戏状态
    public int gameState;
    // 游戏未开始
    public static final int GAME_READY = 0;
    // 游戏开始
    public static final int GAME_START = 1;
    // 游戏结束
    public static final int GAME_OVER = 2;
    // 游戏暂停
    public static final int GAME_PAUSE = 3;

    private Background background; // 游戏背景对象
    public Block block; // 游戏方块对象

    // 在构造器中初始化
    public Tetris() {
        initFrame(); // 初始化游戏窗口
        setVisible(true); // 窗口默认为不可见，设置为可见
        initGame(); // 初始化游戏对象
    }

    // 初始化游戏窗口
    private void initFrame() {
        setSize(FRAME_WIDTH, FRAME_HEIGHT); // 设置窗口大小
        setTitle(GAME_TITLE); // 设置窗口标题
        setLocation(FRAME_X, FRAME_Y); // 窗口初始位置
        setResizable(false); // 设置窗口大小不可变
        // 添加关闭窗口事件（监听窗口发生的事件，派发给参数对象，参数对象调用对应的方法）
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0); // 结束程序
            }
        });
        addKeyListener(new BlockKeyListener(this)); // 添加按键监听
    }

    // 初始化游戏中的各个对象
    private void initGame() {
        background = new Background();
        block = new Block();
        setGameState(GAME_READY);

        // 启动用于刷新窗口的线程
        new Thread(() -> {
            while (true) {
                repaint(); // 通过调用repaint(),让JVM调用update()
                try {
                    Thread.sleep(FPS);
                } catch (InterruptedException | IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // 项目中存在两个线程：系统线程，自定义的线程：调用repaint()。
    // 系统线程：屏幕内容的绘制，窗口事件的监听与处理
    // 两个线程会抢夺系统资源，可能会出现一次刷新周期所绘制的内容，并没有在一次刷新周期内完成
    // （双缓冲）单独定义一张图片，将需要绘制的内容绘制到这张图片，再一次性地将图片绘制到窗口
    private final BufferedImage bufImg = new BufferedImage(FRAME_WIDTH, FRAME_HEIGHT, BufferedImage.TYPE_4BYTE_ABGR);

    /**
     * 绘制游戏内容 当repaint()方法被调用时，JVM会调用update()，参数g是系统提供的画笔，由系统进行实例化
     * 单独启动一个线程，不断地快速调用repaint()，让系统对整个窗口进行重绘
     */
    public void update(Graphics g) {
        Graphics bufG = bufImg.getGraphics(); // 获得图片画笔
        // 使用图片画笔将需要绘制的内容绘制到图片
        // 背景层
        background.draw(bufG);
        if (gameState == GAME_READY) {
            // 游戏未开始
            background.drawStart(bufG);
        } else if (gameState == GAME_START) {
            // 方块自动下落
            boolean res = block.blockAutoDown(blockList);
            if (!res) {
                // 当无法下落时计算分数并新建方块
                mergeBlockList();
                newBlock();
            }
            // 绘制所有方块
            background.drawBlockList(bufG, blockList, block.blockList);
            // 绘制分数
            background.drawScore(bufG, score);
        } else if (gameState == GAME_PAUSE) {
            // 绘制所有方块
            background.drawBlockList(bufG, blockList, block.blockList);
            // 绘制分数
            background.drawScore(bufG, score);
            // 绘制暂停页面
            background.drawPause(bufG);
        } else {
            // 游戏结束
            background.drawOver(bufG, score);
        }
        // 一次性将图片绘制到屏幕上
        g.drawImage(bufImg, 0, 0, null);
    }

    // 更新游戏状态
    public void setGameState(int gameState) {
        this.gameState = gameState;
    }

    // 方块下落完成时进行消除和记分
    private void mergeBlockList() {
        // 将下落后的方块添加进新的方块数组
        List<Integer> newBlockList = block.mergeBlockList(blockList);
        // 消除的行数即为分数
        int scoreRow = Constant.BLOCK_ROW_SIZE - newBlockList.size();
        // 补全空行
        for (int i = 0; i < scoreRow; i++) {
            newBlockList.add(Constant.BLANK_BLOCK_ROW);
        }
        // 更新分数和方块数组
        score += scoreRow;
        blockList = newBlockList;
    }

    // 新建一个方块
    public void newBlock() {
        boolean res = block.newBlock(blockList);
        if (!res) {
            // 新建方块失败，游戏结束
            setGameState(GAME_OVER);
        }
    }

    // 重新开始游戏
    public void resetGame() {
        setGameState(Tetris.GAME_READY);
        // 清空方块数组
        blockList = new ArrayList<>(Constant.INIT_BLOCK_LIST);
        // 清空计分
        score = 0;
    }

    // 方块快速下落
    public void moveBlockDownFast() {
        block.moveBlockDownFast(blockList);
        mergeBlockList();
        newBlock();
    }

    // 旋转方块
    public void rotateBlock() {
        block.rotateBlock(blockList);
    }

    // 左移方块
    public void moveBlockLeft() {
        block.moveBlockLeft(1, blockList);
    }

    // 右移方块
    public void moveBlockRight() {
        block.moveBlockRight(1, blockList);
    }
}
