package org.gateinnavigation.view.util;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.SceneBuilder;
import javafx.scene.control.Button;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextBuilder;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MessageDialog extends Stage {
	private static MessageDialog dialog;

	private Text txtMessage;
	private Button btnClose;

	static {
		dialog = new MessageDialog();
	}

	private MessageDialog() {
		super(StageStyle.TRANSPARENT);
		setWidth(250);
		txtMessage = TextBuilder.create().textAlignment(TextAlignment.CENTER)
				.build();

		btnClose = new Button("X");
		btnClose.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				((Button) e.getSource()).getScene().getWindow().hide();
			}
		});
		initModality(Modality.APPLICATION_MODAL);
		setScene(SceneBuilder
				.create().stylesheets("main.css")
				.fill(Color.DARKGREY)
				.root(VBoxBuilder.create().children(txtMessage, btnClose).padding(new Insets(5))
						.alignment(Pos.CENTER).spacing(20).build()).build());
	}

	public static void showMessage(String title, String message,
			MessageType type) {
		dialog.showMsg(title, message, type);
	}

	private void showMsg(String title, String message, MessageType type) {
		setTitle(title == null ? "Message" : title);
		txtMessage.setText(message);
		switch (type) {
		case ERROR:
			txtMessage.setFill(Color.RED);
			break;
		case WARNING:
			txtMessage.setFill(Color.YELLOW);
			break;
		case INFO:
			txtMessage.setFill(Color.BLACK);
			break;
		}
		sizeToScene();
		show();
	}

	public static enum MessageType {
		ERROR, WARNING, INFO;
	}

}
