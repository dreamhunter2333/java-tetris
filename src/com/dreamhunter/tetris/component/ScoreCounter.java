package com.dreamhunter.tetris.component;

import com.dreamhunter.tetris.util.Constant;

import java.io.*;

/**
 * 游戏计分器, 使用静态内部类实现了单例模式
 */
public class ScoreCounter {

    private static class ScoreCounterHolder {
        private static final ScoreCounter scoreCounter = new ScoreCounter();
    }

    public static ScoreCounter getInstance() {
        return ScoreCounterHolder.scoreCounter;
    }

    private int score = 0; // 分数
    private int bestScore; // 最高分数

    private ScoreCounter() {
        bestScore = -1;
        try {
            loadBestScore();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 装载最高纪录
    private void loadBestScore() throws Exception {
        File file = new File(Constant.SCORE_FILE_PATH);
        if (file.exists()) {
            DataInputStream dis = new DataInputStream(new FileInputStream(file));
            bestScore = dis.readInt();
            dis.close();
        }
    }

    public void saveScore() {
        bestScore = Math.max(bestScore, getCurrentScore());
        try {
            File file = new File(Constant.SCORE_FILE_PATH);
            DataOutputStream dos = new DataOutputStream(new FileOutputStream(file));
            dos.writeInt(bestScore);
            dos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void score(int addScore) {
        score += addScore;
    }

    public int getBestScore() {
        return bestScore;
    }

    public int getCurrentScore() {
        return score;
    }

    public void reset() {
        score = 0;
    }

}