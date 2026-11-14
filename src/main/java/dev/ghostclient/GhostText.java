package dev.ghostclient;

import com.ibm.icu.text.ArabicShaping;
import com.ibm.icu.text.Bidi;

/**
 * Converts logical Persian/Arabic text into visual presentation order for
 * Minecraft's basic font renderer, which does not perform Arabic shaping.
 */
public final class GhostText {
    private static final ArabicShaping SHAPER =
            new ArabicShaping(ArabicShaping.LETTERS_SHAPE);

    private GhostText() {}

    public static String rtl(String logicalText) {
        if (logicalText == null || logicalText.isEmpty()) return logicalText;
        try {
            String shaped = SHAPER.shape(logicalText);
            Bidi bidi = new Bidi(shaped, Bidi.DIRECTION_RIGHT_TO_LEFT);
            return bidi.writeReordered(Bidi.DO_MIRRORING);
        } catch (ArabicShaping.ArabicShapingException ex) {
            return logicalText;
        }
    }
}
