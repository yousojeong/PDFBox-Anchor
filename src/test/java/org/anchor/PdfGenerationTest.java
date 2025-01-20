package org.anchor;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PdfGenerationTest {

    @Test
    void testPdfGeneration() throws IOException {
        // Arrange
        PDDocument doc = new PDDocument();
        PDPage page = new PDPage();
        doc.addPage(page);

        try (PDPageContentStream contentStream = new PDPageContentStream(doc, page)) {
            Row row = new Row(0, 600, 500);
            TextContent textContent = TextContent.builder()
                    .text("PDF Test")
                    .font(PDType1Font.HELVETICA)
                    .fontSize(12)
                    .build();
            row.addCol(12, textContent);
            row.render(contentStream);


            List<Object> lines = new ArrayList<>(); // 여러 줄 입력 값
            Row row2 = new Row(0, row.getRowNextY(), PDRectangle.A4.getWidth(), true);
            lines.add(new Line("test1", PDType1Font.HELVETICA_BOLD, 10));
            lines.add(new Line("test2", PDType1Font.HELVETICA_BOLD, 10));
            BoxContent boxContent = BoxContent.builder()
                    .lines(lines)
                    .fontSize(8)
                    .padding(new float[5])
                    .borderSides("TBL")
                    .build();
            row2.addCol(12, boxContent);
            row2.render(contentStream);
        }

        // Act
        String outputPath = "src/main/resources/test.pdf";
        doc.save(outputPath);
        doc.close();

        // Assert
        File outputFile = new File(outputPath);
        assertTrue(outputFile.exists(), "Generated PDF file should exist.");
        assertTrue(outputFile.length() > 0, "Generated PDF file should not be empty.");

        // Clean up
//        outputFile.delete();
    }
}
