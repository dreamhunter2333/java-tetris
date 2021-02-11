package com.dreamhunter.tetris.listener;

import com.dreamhunter.tetris.app.Tetris;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

// 用于接收按键事件的对象的内部类
public class BlockKeyListener implements KeyListener {

    private final Tetris tetris;

    public BlockKeyListener(Tetris tetris) {
        this.tetris = tetris;
    }

    /**
     * Invoked when a key has been typed.
     * See the class description for {@link KeyEvent} for a definition of
     * a key typed event.
     *
     * @param e 按键事件
     */
    @Override
    public void keyTyped(KeyEvent e) {

    }

    /**
     * 按键按下，根据游戏当前的状态调用不同的方法
     * Invoked when a key has been pressed.
     * See the class description for {@link KeyEvent} for a definition of
     * a key pressed event.
     *
     * @param e 按键事件
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int keycode = e.getKeyCode();
        switch (tetris.gameState) {
            case Tetris.GAME_READY:
                if (keycode == KeyEvent.VK_SPACE) {
                    // 游戏开始
                    tetris.setGameState(Tetris.GAME_START);
                    tetris.newBlock();
                }
                break;
            case Tetris.GAME_START:
                if (keycode == KeyEvent.VK_SPACE) {
                    // 暂停
                    tetris.setGameState(Tetris.GAME_PAUSE);
                } else if (keycode == KeyEvent.VK_DOWN) {
                    // 快速下落
                    tetris.moveBlockDownFast();
                } else if (keycode == KeyEvent.VK_UP) {
                    // 旋转方块
                    tetris.rotateBlock();
                } else if (keycode == KeyEvent.VK_LEFT) {
                    // 左移方块
                    tetris.moveBlockLeft();
                } else if (keycode == KeyEvent.VK_RIGHT) {
                    // 右移方块
                    tetris.moveBlockRight();
                }
                break;
            case Tetris.GAME_OVER:
                if (keycode == KeyEvent.VK_SPACE) {
                    // 重新开始游戏
                    tetris.resetGame();
                }
                break;
            case Tetris.GAME_PAUSE:
                if (keycode == KeyEvent.VK_SPACE) {
                    // 游戏继续
                    tetris.setGameState(Tetris.GAME_START);
                }
                break;
        }
    }


    /**
     * Invoked when a key has been released.
     * See the class description for {@link KeyEvent} for a definition of
     * a key released event.
     *
     * @param e 按键事件
     */
    @Override
    public void keyReleased(KeyEvent e) {

    }
}
