package org.anchor;

import lombok.Builder;
import lombok.Getter;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
public class TextContent implements ColContent {

    private final float LINE_HEIGHT = 1.5f;
    @Builder.Default
    private String text = "Default Text";
    @Builder.Default
    private PDFont font = PDType1Font.HELVETICA;
    @Builder.Default
    private float fontSize = 12;
    @Builder.Default
    private Alignment alignment = Alignment.LEFT;

    @Override
    public float calculateHeight(float width) throws IOException {
        String[] lines = wrapText(text, font, fontSize, width);
        return lines.length * fontSize * LINE_HEIGHT; // 1.2f 줄간격 높이
    }

    @Override
    public void render(PDPageContentStream contentStream, float x, float y, float width, float height) throws IOException {
        String[] lines = wrapText(text, font, fontSize, width);
        float lineHeight = fontSize * LINE_HEIGHT;
        float currentY = y;

        for (String line : lines) {
            contentStream.beginText();
            contentStream.setFont(font, fontSize);

            float textWidth = font.getStringWidth(line) / 1000 * fontSize;
            float textX = x;

            switch (alignment) {
                case CENTER:
                    textX = x + (width - textWidth) / 2;
                    break;
                case RIGHT:
                    textX = x + width - textWidth;
                    break;
                case LEFT:
                    textX = x;
                    break;
                default:
                    throw new IllegalStateException("USE IN (CENTER, RIGHT, LEFT) : " + alignment);
            }
            contentStream.newLineAtOffset(textX, currentY);
            contentStream.showText(line);
            contentStream.endText();
            currentY -= lineHeight;
        }
    }

    // 단어 단위
    private String[] wrapText(String text, PDFont font, float fontSize, float maxWidth) throws IOException {
        List<String> lines = new ArrayList<>();
        for (String paragraph : text.split("\n")) {
            StringBuilder currentLine = new StringBuilder();
            for (String word : paragraph.split(" ")) {
                String testLine = currentLine.length() == 0 ? word : currentLine + " " + word;
                float textWidth = font.getStringWidth(testLine.replace(" ", "\u00A0")) / 1000 * fontSize; // 공백을 non-breaking space로 대체
                if (textWidth > maxWidth) {
                    lines.add(currentLine.toString().replace(" ", "\u00A0")); // 공백 유지
                    currentLine = new StringBuilder(word);
                } else {
                    currentLine.append(word).append(" "); // 공백 유지
                }
            }
            if (currentLine.length() > 0) {
                lines.add(currentLine.toString().replace(" ", "\u00A0")); // 마지막 라인 추가
            }
        }
        return lines.toArray(new String[0]);
    }
}
