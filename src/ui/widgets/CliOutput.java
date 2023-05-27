package ui.widgets;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.widget.HighlightTextArea;
import com.kotcrab.vis.ui.widget.ScrollableTextArea;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import utils.MiscUtils;

import java.util.LinkedList;
import java.util.Queue;

public class CliOutput extends Table implements com.github.ykrasik.jaci.cli.output.CliOutput {

    int numBufferEntries;
    private final Queue<Label> bufferEntries;
    HighlightTextArea textArea;
    ScrollPane scrollPane;

    public CliOutput(int numBufferEntries){
        textArea = new HighlightTextArea("");
        textArea.setHighlighter(MiscUtils.createHighlighter());
        scrollPane = textArea.createCompatibleScrollPane();
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setupOverscroll(0, 0, 0);
        scrollPane.setFillParent(true);
        this.bufferEntries = new LinkedList<>();

        add(scrollPane).expand().fill().row();
        textArea.setReadOnly(true);



    }

    @Override
    public void println(String text) {
        updateScroll();
        textArea.appendText(text + "\n");
        scrollPane.setForceScroll(false, true);
        scrollPane.layout();
        scrollPane.scrollTo(0, 0, 0, 0);

    }

    private void updateScroll() {
        // Set the scroll to the bottom of the pane.
        scrollPane.layout();
        scrollPane.setScrollPercentY(100);
        scrollPane.updateVisualScroll();
    }
}
