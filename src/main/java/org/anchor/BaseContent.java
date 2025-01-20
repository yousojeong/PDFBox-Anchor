package org.anchor;

import org.apache.pdfbox.pdmodel.common.PDRectangle;

import java.util.ArrayList;
import java.util.List;

public class BaseContent {

    protected static final float MARGIN = 50; // 기본 페이지 여백
    protected static final float PAGE_HEIGHT = PDRectangle.A4.getHeight(); // A4용지 높이
    public static final float CONTENT_MAX_WIDTH = PDRectangle.A4.getWidth() - MARGIN * 2; // 가용 범위: A4용지 너비 - 기본 여백
    protected static final float CONTENT_MAX_HEIGHT = PAGE_HEIGHT - 2 * 70; // 가용 범위: A4용지 높이 - 기본 여백
    public static final float START_X = 0 + MARGIN; // 기준이 될 시작 X
    public static final float START_Y = PAGE_HEIGHT - 70; // 기준이 될 시작 Y
    protected static float rowHeightSum = 0; // 기준 높이
    protected static final float ITEM_MAX_HEIGHT = 150f; // 초과 시 새 페이지 생성
    protected static final List<Object> lines = new ArrayList<>(); // 여러 줄 입력 값
    protected static List<List<Object>> dataToMove = new ArrayList<>();

    protected static boolean checkAddPage(float maxHeight, float rowHeight) {
        rowHeightSum += rowHeight;
        return rowHeightSum > maxHeight;
    }

    protected static List<List<Object>> getDataToMove() {
        return new ArrayList<>(dataToMove);
    }
}
