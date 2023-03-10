package builtin.functions;

import com.sksamuel.scrimage.ImmutableImage;
import com.sksamuel.scrimage.color.Colors;
import core.Scope;
import core.values.AbstractFunction;
import core.values.Image;
import core.values.IntegerValue;
import core.values.StringValue;

import java.awt.*;
import java.awt.font.TextLayout;
import java.util.HashMap;
import java.util.Map;

public class Write extends AbstractFunction {
    public final static String ACTUAL_NAME = "Write";

    @Override
    public Image call(Scope scope) {
        String text = scope.getLocalVar(AbstractFunction.PARAM_TARGET).asString().get();

        int width = scope.getLocalVar("width").asInteger().get();
        int height = scope.getLocalVar("height").asInteger().get();

        String font = scope.getLocalVar("font").asString().get();
        int size = scope.getLocalVar("size").asInteger().get();
        // style: plain, bold, italic, OTHERWISE Plain
        String style = scope.getLocalVar("style").asString().get();
        int styleType;
        if (style.equalsIgnoreCase("italic")) {
            styleType = Font.ITALIC;
        } else if (style.equalsIgnoreCase("bold")) {
            styleType = Font.BOLD;
        } else {
            styleType = Font.PLAIN;
        }

        Font fontObj = new Font(font, styleType, size);

        ImmutableImage img = ImmutableImage.filled(width, height, Colors.Transparent.toAWT());

        Graphics2D g2 = (Graphics2D) img.awt().getGraphics();
        g2.setFont(fontObj);

        TextLayout textLayout = new TextLayout(text, g2.getFont(), g2.getFontRenderContext());
        double textHeight = textLayout.getBounds().getHeight();
        double textWidth = textLayout.getBounds().getWidth();

        g2.drawString(text, width / 2 - ((int) (textWidth / 2)), height / 2 - ((int) textHeight / 2));
        g2.dispose();

        return new Image(img);
    }

    @Override
    public String getFunctionName() {
        return ACTUAL_NAME;
    }

    @Override
    public Map<String, String> getParams() {
        return new HashMap<>() {{
            put(AbstractFunction.PARAM_TARGET, StringValue.NAME);
            put("width", IntegerValue.NAME);
            put("height", IntegerValue.NAME);
            put("font", StringValue.NAME);
            put("size", IntegerValue.NAME);
            put("style", StringValue.NAME);
        }};
    }

    @Override
    public Image checkReturn() {
        return new Image(null);
    }
}
