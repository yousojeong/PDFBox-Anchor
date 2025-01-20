package org.anchor;

import lombok.Getter;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Row {
    private float x; // 시작 X 좌표
    private float y; // 시작 Y 좌표
    private float width; // Row의 전체 너비
    private float height; // Row의 높이
    private boolean useMaxHeight; // 최대 높이 사용 여부
    private List<ColContentWrapper> cols = new ArrayList<>();

    public Row(float x, float y, float width) {
        this(x, y, width, false);
    }

    public Row(float x, float y, float width, boolean useMaxHeight) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.useMaxHeight = useMaxHeight;
    }

    public void addCol(float colSpan, ColContent content) throws IOException {
        float colWidth = (width / 12) * colSpan; // 12등분 기준의 폭 계산
        float colHeight = content.calculateHeight(colWidth); // 콘텐츠의 높이 계산
        float currentX = (float) (x + cols.stream().mapToDouble(ColContentWrapper::getWidth).sum()); // 누적 X 좌표 계산

        ColContentWrapper colWrapper = new ColContentWrapper(currentX, y, colWidth, colHeight, content);
        cols.add(colWrapper);

        // Row의 높이를 Col들 중 가장 높은 값으로 설정
        if (useMaxHeight) {
            this.height = Math.max(this.height, colHeight);
        } else {
            this.height = colHeight;
        }
    }

    public void render(PDPageContentStream contentStream) throws IOException {
        for (ColContentWrapper col : cols) {
            if (useMaxHeight) {
                col.setHeight(this.height); // 각 Col의 높이를 Row의 높이로 설정
            }
            col.render(contentStream); // 각 Col 렌더링
        }
    }

    // false의 경우에는 마지막 col의 높이가 row 의 높이로 나오게 됨
    public float getRowHeight() {
        return this.height;
    }

    // 다음 Row의 Y 시작값
    public float updateCurrentY(float currentY) {
        return currentY - getRowHeight();
    }

    // Row의 다음 Y값
    public float getRowNextY() {
        return y - getRowHeight();
    }

    // 특정 Col의 시작 Y 좌표를 반환
    public float getColStartY(int index) {
        return cols.get(index).getY();
    }


    // 특정 Col의 다음 Y 좌표
    public float getColNextY(int index) {
        return cols.get(index).getY() - cols.get(index).getHeight();
    }

    @Override
    public String toString() {
        return "Row{" +
                "x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                ", useMaxHeight=" + useMaxHeight +
                ", cols=" + cols +
                '}';
    }
}
