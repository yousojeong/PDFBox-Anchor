package org.anchor;

import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoxContentTest {

    @Test
    void testBoxContentInitialization() {
        // Arrange
        // Arrange
        List<Object> lines = new ArrayList<>();
        lines.add(new Line("Test Line 1", PDType1Font.HELVETICA, 12));
        lines.add(new Line("Test Line 2", PDType1Font.HELVETICA_BOLD, 14));


        // Act
        BoxContent boxContent = BoxContent.builder()
                .lines(lines)
                .fontSize(10)
                .padding(new float[]{5, 5, 5, 5})
                .borderSides("TRBL")
                .build();

        // Assert
        assertNotNull(boxContent);
        assertEquals(10, boxContent.getFontSize());
        assertEquals(4, boxContent.getPadding().length);
        assertEquals("TRBL", boxContent.getBorderSides());
    }
}
