package org.anchor;

import lombok.Getter;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.io.IOException;

@Getter
public class ColContentWrapper {
    private float x; // Col의 시작 X 좌표
    private float y; // Col의 시작 Y 좌표
    private float width; // Col의 폭
    private float height; // Col의 높이
    private ColContent content; // Col 내부에 들어갈 콘텐츠

    public ColContentWrapper(float x, float y, float width, float height, ColContent content) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.content = content;
    }

    public void render(PDPageContentStream contentStream) throws IOException {
        content.render(contentStream, x, y, width, height);
    }

    public void setHeight(float height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return "ColContentWrapper{" +
                "x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                ", content=" + content +
                '}';
    }
}
