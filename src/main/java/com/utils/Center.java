package com.utils;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

public class Center {

    public static void centerNode(Node node, AnchorPane parent) {

        node.layoutXProperty().bind(
                parent.widthProperty().subtract(node.layoutBoundsProperty().get().getWidth()).divide(2)
        );

        node.layoutYProperty().bind(
                parent.heightProperty().subtract(node.layoutBoundsProperty().get().getHeight()).divide(2)
        );
    }

    public static void centerNodes(AnchorPane parent, Node... nodes) {
        for (Node node : nodes) {
            centerNode(node, parent);
        }
    }
}
