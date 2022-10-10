package builtin.functions;

import com.sksamuel.scrimage.ImmutableImage;
import com.sksamuel.scrimage.color.Colors;
import com.sksamuel.scrimage.metadata.ImageMetadata;
import core.Scope;
import core.checkers.ArgumentChecker;
import core.expressions.ExpressionVisitor;
import core.values.*;
import core.values.Image;

import java.awt.*;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import static com.sksamuel.scrimage.ImmutableImage.wrapAwt;

public class Write extends AbstractFunction {
    public final static String ACTUAL_NAME = "Write";

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

        TextLayout textLayout = new TextLayout(text, g2.getFont(), g2.getFontRenderContext());
        double textHeight = textLayout.getBounds().getHeight();
        double textWidth = textLayout.getBounds().getWidth();

        g2.drawString(text, width / 2 - ((int) (textWidth / 2)), height / 2 - ((int) textHeight / 2));
        g2.dispose();

        return new Image(img);
    }

    @Override
    public void checkArgs(Scope scope) {
        Map<String, String> params = new HashMap<>() {{
            put("$target", StringValue.NAME);
            put("width", IntegerValue.NAME);
            put("height", IntegerValue.NAME);
            put("font", StringValue.NAME);
            put("size", IntegerValue.NAME);
            put("style", StringValue.NAME);
        }};
        ArgumentChecker.check(scope, params, ACTUAL_NAME);
    }

    @Override
    public <C, T> T accept(C ctx, ExpressionVisitor<C, T> v) {
        return v.visit(ctx, this);
    }

}
