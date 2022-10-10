package builtin.functions;

import com.sksamuel.scrimage.ImmutableImage;
import com.sksamuel.scrimage.color.Colors;
import core.Scope;
import core.values.AbstractFunction;
import core.values.Image;
import core.values.Value;

import java.awt.*;

public class Write extends AbstractFunction {

    @Override
    public Value call(Scope scope) {
        String text = scope.getVar("$target").asString().get();

        int width = scope.getVar("width").asInteger().get();
        int height = scope.getVar("height").asInteger().get();

        String font = scope.getVar("font").asString().get();
        int size = scope.getVar("size").asInteger().get();
        // style: plain, bold, italic, OTHERWISE Plain
        String style = scope.getVar("style").asString().get();
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
        g2.drawString(text, 0, 0);

        return new Image(img);
    }

}
