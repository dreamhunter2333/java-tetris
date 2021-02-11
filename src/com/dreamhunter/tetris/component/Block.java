package com.dreamhunter.tetris.component;

import com.dreamhunter.tetris.util.Constant;
import com.dreamhunter.tetris.util.TetrisUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Block {

    public List<List<Integer>> block;
    public List<Integer> blockList = new ArrayList<>(Constant.INIT_BLOCK_LIST);
    private int blockState;
    private int moveROW = 0;
    private int moveDown = 0;
    private int moveDownCount = 0;

    public Block() {
    }

    // 新建方块
    public boolean newBlock(List<Integer> tetrisBlockList) {
        // 清空状态
        moveROW = moveDown = 0;
        blockState = 0;
        // 生成随机数，获取方块
        int nextBlockIndex = TetrisUtil.randomInt(Constant.ALL_BLOCK.size());
        block = Constant.ALL_BLOCK.get(nextBlockIndex);
        blockList = block.get(blockState);
        return TetrisUtil.checkBlockMove(blockList, tetrisBlockList);
    }

    // 合并正在下落的方块和已经下落的方块列表，并消除填满的行
    public List<Integer> mergeBlockList(List<Integer> tetrisBlockList) {
        List<Integer> newBlockList = new ArrayList<>(Constant.INIT_BLOCK_LIST);
        for (int i = 0; i < Constant.BLOCK_ROW_SIZE; i++) {
            int rowValue = blockList.get(i) | tetrisBlockList.get(i);
            newBlockList.set(i, rowValue);
        }
        return newBlockList.stream()
                .filter(item -> !item.equals(Constant.FULL_BLOCK_ROW))
                .collect(Collectors.toList());
    }

    // 方块旋转
    public void rotateBlock(List<Integer> tetrisBlockList) {
        // 方块不存在
        if (block == null) return;

        // 获取方块下一状态
        int newBlockState = blockState + 1;
        if (newBlockState >= block.size()) {
            newBlockState = 0;
        }
        List<Integer> rotateBlockList = block.get(newBlockState);

        // 垂直偏移量大于 0 时，判断是否可以移动过去
        if (moveDown > 0) {
            int bottomValue = rotateBlockList.get(moveDown - 1);
            if (bottomValue != 0) return;
        }

        // 计算移位之前的所有有方块的列
        int moveValue = 0;

        // 新建列表, 并将偏移量应用
        List<Integer> newBlockList = new ArrayList<>(Constant.INIT_BLOCK_LIST);
        for (int i = moveDown; i < blockList.size(); i++) {
            int rowValue;
            // 进行移位
            if (moveROW >= 0) {
                rowValue = rotateBlockList.get(i) >> moveROW;
            } else {
                rowValue = rotateBlockList.get(i) << Math.abs(moveROW);
            }
            moveValue |= rotateBlockList.get(i);
            newBlockList.set(i - moveDown, rowValue);
        }

        // 判断是否越界，moveValue 为 0 时不越界
        int tempValue = (int) Math.pow(2, Math.abs(moveROW)) - 1;
        if (moveROW >= 0) {
            moveValue &= tempValue;
        } else {
            moveValue = (moveValue >> (Constant.BLOCK_COLUMN_SIZE - Math.abs(moveROW))) & tempValue;
        }

        // 方块列表和方块状态
        if (moveValue == 0 && TetrisUtil.checkBlockMove(newBlockList, tetrisBlockList)) {
            blockList = newBlockList;
            blockState = newBlockState;
        }
    }

    // 方块左移
    public void moveBlockLeft(int length, List<Integer> tetrisBlockList) {
        // 新建数组
        List<Integer> newBlockList = new ArrayList<>(Constant.INIT_BLOCK_LIST);
        // 计算右移之前的所有有方块的列
        int rightValue = 0;
        for (int i = 0; i < blockList.size(); i++) {
            rightValue |= blockList.get(i);
            // 右移，即方块左移
            int rowValue = blockList.get(i) >> length;
            newBlockList.set(i, rowValue);
        }
        // 计算右移之后，是否越界，为 0 时不越界
        rightValue &= (int) Math.pow(2, length) - 1;

        if (rightValue == 0 && TetrisUtil.checkBlockMove(newBlockList, tetrisBlockList)) {
            // 更新偏移量和方块列表
            moveROW += length;
            blockList = newBlockList;
        }
    }

    // 方块右移
    public void moveBlockRight(int length, List<Integer> tetrisBlockList) {
        // 新建数组
        List<Integer> newBlockList = new ArrayList<>(Constant.INIT_BLOCK_LIST);
        // 计算左移之前的所有有方块的列
        int leftValue = 0;
        for (int i = 0; i < blockList.size(); i++) {
            leftValue |= blockList.get(i);
            // 左移，即方块右移
            int rowValue = blockList.get(i) << length;
            newBlockList.set(i, rowValue);
        }
        // 计算左移之后，是否越界，为 0 时不越界
        leftValue = (leftValue >> (Constant.BLOCK_COLUMN_SIZE - length)) & ((int) Math.pow(2, length) - 1);

        if (leftValue == 0 && TetrisUtil.checkBlockMove(newBlockList, tetrisBlockList)) {
            // 更新偏移量和方块列表
            moveROW -= length;
            blockList = newBlockList;
        }
    }

    // 方块下落
    public boolean moveBlockDown(int length, List<Integer> tetrisBlockList) {
        // 新建空数组
        List<Integer> newBlockList = new ArrayList<>(Constant.INIT_BLOCK_LIST);
        // 获取下落前，下落长度上一层的值
        int bottomValue = blockList.get(length - 1);
        // 不为 0 时，方块无法下落
        if (bottomValue != 0) return false;

        // 更新到新的数组
        for (int i = length; i < blockList.size(); i++) {
            newBlockList.set(i - length, blockList.get(i));
        }

        // 无交集时可以下落，并更新数据
        if (TetrisUtil.checkBlockMove(newBlockList, tetrisBlockList)) {
            moveDown += length;
            blockList = newBlockList;
            return true;
        }
        return false;
    }

    // 方块快速下落
    public void moveBlockDownFast(List<Integer> tetrisBlockList) {
        boolean res = true;
        while (res) {
            res = moveBlockDown(1, tetrisBlockList);
        }
    }

    // 方块自动下落
    public boolean blockAutoDown(List<Integer> tetrisBlockList) {
        moveDownCount++;
        if (moveDownCount == Constant.BLOCK_SPEED) {
            moveDownCount = 0;
            return moveBlockDown(1, tetrisBlockList);
        }
        return true;
    }
}
