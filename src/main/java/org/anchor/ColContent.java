package org.anchor;

import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.io.IOException;

public interface ColContent {
    void render(PDPageContentStream contentStream, float x, float y, float width, float height) throws IOException; // 컨텐츠 렌더링

    float calculateHeight(float width) throws IOException; // 컨텐츠 높이 계산
}
