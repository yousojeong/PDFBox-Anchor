package org.anchor;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class RowTest {

    @Test
    void testRowHeightCalculation() throws IOException {
        // Arrange
        float startY = 600;
        Row row = new Row(0, startY, 500); // Y: 600, Width: 500
        TextContent textContent = TextContent.builder()
                .text("Hello, World!")
                .font(PDType1Font.HELVETICA)
                .fontSize(12)
                .build();

        // Act
        row.addCol(12, textContent);

        // Assert
        assertEquals(startY - textContent.getFontSize() * textContent.getLINE_HEIGHT(), row.getRowNextY(), "The row's Y position should remain unchanged.");
    }

    @Test
    void testRowRendering() throws IOException {
        // Arrange
        Row row = new Row(0, 600, 500);

        TextContent textContent = TextContent.builder()
                .text("Render Test")
                .font(PDType1Font.HELVETICA_BOLD)
                .fontSize(16)
                .build();

        row.addCol(12, textContent);

        // Mock content stream
        PDDocument doc = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        doc.addPage(page);

        try (PDPageContentStream contentStream = new PDPageContentStream(doc, page)) {
            // Act and Assert
            assertDoesNotThrow(() -> row.render(contentStream), "Row rendering should not throw an exception.");
        }
    }
}
