package org.anchor;

import lombok.Getter;
import org.apache.pdfbox.pdmodel.font.PDFont;

import java.io.IOException;

@Getter
public class Col {
    private float x; // Col 시작 X 좌표
    private float y; // Col 시작 Y 좌표
    private float width; // Col 너비
    private String text; // Col에 표시될 텍스트
    private PDFont font; // 폰트
    private int fontSize; // 폰트 크기

    public Col(float x, float y, float width, String text, PDFont font, int fontSize) throws IOException {
        this.x = x;
        this.y = y;
        this.width = width;
        this.text = text;
        this.font = font;
        this.fontSize = fontSize;
    }

    @Override
    public String toString() {
        return "Col{" +
                "x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", text='" + text + '\'' +
                ", font=" + font +
                ", fontSize=" + fontSize +
                '}';
    }
}