package se.jerka.ops.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import se.jerka.ops.ai.ArtificialIntelligence;
import se.jerka.ops.init.Initializer;
import se.jerka.ops.model.Creature;
import se.jerka.ops.model.Location;
import se.jerka.ops.model.Location.Direction;
import se.jerka.ops.model.Position;
import se.jerka.ops.model.Thing;
import se.jerka.ops.model.World;
import se.jerka.ops.usr.User;
import se.jerka.ops.usr.User.Mode;

public class Gui {

	private static final int LOCATION_GRID_HGAP = 3;
	private static final int LOCATION_GRID_VGAP = 4;
	
	private static Initializer gameInit = Initializer.getInstance();
	
	private World world;
	private User user;
	private ArtificialIntelligence ai;
	
	private BorderPane mainPanel;
	private Scene gui;
	private SplitPane top;
	private GridPane topRight;
	private GridPane navigation;
	private List<Node> navigationComponents;
	private Map<Direction, Button> navigationButtonMap;
	private Map<KeyCode, Direction> directionMap;
	private VBox worldBox;
	private GridPane worldGrid;
	private GridPane locationGrid;
	private VBox inventoryBox;
	private Label inventoryHeader;
	private ListView<Thing> inventoryView;
	private Label messages;
	private Map<Location, Button> worldMap;
	
	private Activity macro;
	private Activity micro;
	private Activity inventory;
	private Map<Activity, Mode> modeMap;
	private Activity currentActivity;
	
	private enum NavigationButton {
		DIR_NORTH("^"),
		DIR_WEST("<"),
		DIR_EAST(">"),
		DIR_SOUTH("v");
		
		private String arrow;
		
		NavigationButton(String text) {
			arrow = text;
		}
		
		private String arrow() {
		    return arrow;
		}
	}
	
	private enum NodeLink {
		NEXT,
		LAST;
	}
		
	private class Activity {
		
		private Node node;
		private Activity next;
		private Activity last;
		
		Activity(Node node, Activity next, Activity last) {
			this.node = node;
			this.next = next;
			this.last = last;
		}
		
		private Activity get(NodeLink link) {
			switch (link) {
			case NEXT:
				return next;
			case LAST:
				return last;
				default: throw new IllegalArgumentException(); 
			}
		}
		
		private void setLink(Activity activity, NodeLink link) {
			switch (link) {
			case NEXT:
				next = activity;
				break;
			case LAST:
				last = activity;
				break;
				default: throw new IllegalArgumentException();
			}
		}
		
		private Node node() {
			return node;
		}
	}
	
	private void initComponents() {		
		world = gameInit.initWorld();
		user = gameInit.initUser();
		ai = gameInit.initAI(user.currentLocation());
		
		mainPanel = new BorderPane();
		gui = new Scene(mainPanel);
		gui.setCursor(Cursor.NONE);
		top = new SplitPane();
		topRight = new GridPane();
		worldBox = new VBox();
		worldGrid = new GridPane();
		worldGrid.setFocusTraversable(true);
		locationGrid = new GridPane();
		locationGrid.setHgap(LOCATION_GRID_HGAP);
		locationGrid.setVgap(LOCATION_GRID_VGAP);
		locationGrid.setMouseTransparent(true);
		locationGrid.setFocusTraversable(false);
		inventoryBox = new VBox();
		inventoryView = new ListView<Thing>();
		inventoryHeader = new Label("inventory");
		
		updateInventory();
		
		inventoryView.setMouseTransparent(true);
		inventoryView.setFocusTraversable(false);
		messages = new Label(user.currentLocation().getName());
		navigation = new GridPane();
		navigationComponents = new ArrayList<Node>();
		List<Button> navigationButtons = new ArrayList<Button>();
		for (NavigationButton button : NavigationButton.values()) {
			navigationButtons.add(new Button(button.arrow()));
		}
		navigation.setMouseTransparent(true);
		navigation.setFocusTraversable(false);
		navigationButtonMap = new HashMap<Direction, Button>();
		int nbi = 0;
		for (Direction direction : Direction.values()) {
			navigationButtonMap.put(direction, navigationButtons.get(nbi++));
		}
		updateNavigationButtons();
		for (Button button : navigationButtons) {
			navigationComponents.add(new Label(" "));
			navigationComponents.add(button);
		}
		worldMap = new HashMap<Location, Button>();
		navigationComponents.add(new Label(" "));
		for (Location location : world.locations()) {
			worldMap.put(location, new Button(location.getName()));
		}
		updateWorldGrid();
		
		directionMap = new HashMap<KeyCode, Direction>();
		directionMap.put(KeyCode.W, Direction.NORTH);
		directionMap.put(KeyCode.A, Direction.WEST);
		directionMap.put(KeyCode.D, Direction.EAST);
		directionMap.put(KeyCode.S, Direction.SOUTH);
		macro = new Activity(worldGrid, null, null);
		micro = new Activity(locationGrid, null, null);
		inventory = new Activity(inventoryView, null, null);
		macro.setLink(micro, NodeLink.NEXT);
		micro.setLink(macro, NodeLink.LAST);
		micro.setLink(inventory, NodeLink.NEXT);
		inventory.setLink(micro, NodeLink.LAST);
		modeMap = new HashMap<Activity, Mode>();
		modeMap.put(macro, Mode.MACRO);		
		modeMap.put(micro, Mode.MICRO);
		modeMap.put(inventory, Mode.INVENTORY);
		
		currentActivity = macro;
	}

	private void updateNavigationButtons() {
		for (Direction direction : Direction.values()) {
			switch (user.currentMode()) {
			case MACRO:
				navigationButtonMap.get(direction).setDisable(!user.hasExitIn(direction));
				break;
			case MICRO:
				Position position = user.currentPosition().nextPosition(direction);
				navigationButtonMap.get(direction).setDisable(!position.isPossible());
				break;
			case INVENTORY:
				break;
				default: throw new IllegalArgumentException();
			}	
		}
	}
	
	private void updateInventory() {
		inventoryView.setItems(user.inventory());
	}
	
	public void updateWorldGrid() {
		for (Location location : world.locations()) {
			worldMap.get(location).setDisable(!user.currentLocation().equals(location));
		}
	}
	
	private void updateLocationGrid() {
		locationGrid.getChildren().clear();
		for (int row = 0; row < Location.POSITION_ROW_MAX; row++) {
			for (int col = 0; col < Location.POSITION_COL_MAX; col++) {
				boolean hasActivity = false;
				if (col == user.currentPosition().x() && row == user.currentPosition().y()) {
					locationGrid.add(new Label(User.VISUAL_REPRESENTATION), col, row);
					hasActivity = true;
				}
				if (user.currentLocation().getThings().size() > 0) {
					for (Thing thing : user.currentLocation().getThings()) {
						if (col == thing.getPosition().x() && row == thing.getPosition().y()) {
							locationGrid.add(new Label(thing.getName()), col, row);
							hasActivity = true;
						}
					}
				}
				if (ai.creatures(user.currentLocation()).size() > 0) {
					for (Creature creature : ai.creatures(user.currentLocation())) {
						if (col == creature.getPosition().x() && row == creature.getPosition().y()) {
							locationGrid.add(new Label(creature.getName()), col, row);
							hasActivity = true;
						}
					}
				}
				if (!hasActivity) {
					locationGrid.add(new Label(Position.EMPTY), col, row);
				}
			}
		}
	}
	
	private void setWorldGrid() {
		int locationIndex = 0;
		
		for (int row = 0; row < World.LOCATION_ROW_MAX; row++) {
			RowConstraints rc = new RowConstraints();
			rc.setVgrow(Priority.ALWAYS);
			rc.setFillHeight(true);
			worldGrid.getRowConstraints().add(rc);
			for (int col = 0; col < World.LOCATION_COL_MAX; col++) {
				ColumnConstraints cc = new ColumnConstraints();
				cc.setHgrow(Priority.ALWAYS);
				cc.setFillWidth(true);
				worldGrid.getColumnConstraints().add(cc);
				Location location = world.locations().get(locationIndex++);
				Button button = worldMap.get(location);
				button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
				worldGrid.add(button, col, row);
			}
		}
	}
	
	private void addListeners() {
		gui.setOnKeyPressed((event) -> {
			
			switch (event.getCode()) {
			case I:
				handleNewActivity(NodeLink.NEXT);
				break;
			case O:
				handleNewActivity(NodeLink.LAST);
				break;
			case W:
			case A:
			case D:
			case S:
				Direction direction = directionMap.get(event.getCode());
				if (!navigationButtonMap.get(direction).isDisabled()) {
					handleNavigation(direction);
				}
				break;
			case L:
				handleInventoryAction();
				updateLocationGrid();
				break;
			case Q:
				System.exit(Status.SUCCESS.code());
				break;
				default: break;
			}
			event.consume();
		});
		user.currentLocationProperty().addListener(
				(observable, oldValue, newValue) -> {
			updateNavigationButtons();
			updateWorldGrid();
		});	
		user.currentPositionProperty().addListener(
				(observable, oldValue, newValue) -> {
			updateNavigationButtons();
			updateLocationGrid();
		});
	}
	
	private void handleNewActivity(NodeLink link) {
		Activity activity = currentActivity.get(link);
		activity.node().setFocusTraversable(true);
		activity.node().requestFocus();
		
		user.setCurrentMode(modeMap.get(activity));
		
		switch (user.currentMode()) {
		case MACRO:
			worldBox.getChildren().remove(locationGrid);
			updateWorldGrid();
			updateNavigationButtons();
			break;
		case MICRO:
			updateLocationGrid();
			worldBox.getChildren().add(locationGrid);
			break;
		case INVENTORY:
			updateNavigationButtons();
			worldBox.getChildren().remove(locationGrid);
			break;
			default: throw new IllegalArgumentException();
		}
		currentActivity.node().setFocusTraversable(false);
		currentActivity = activity;

	}
	
	private void handleNavigation(Direction direction) {
		try {
			user.move(direction);
			if (user.currentMode().equals(Mode.MICRO)) {
				messages.setText(direction.getName() + " -> " + user.currentPosition().toString());
				inspectPosition(); // belongs in user class
			} else {
				messages.setText(direction.getName() + " -> " + user.currentLocation().getName());
			}
		} catch (NullPointerException npe) {
			System.err.println("move user: " + npe.getMessage());
			System.exit(1);
		}
	}
	
	private void inspectPosition() { // move to user class
		Thing thing = null;
		for (Thing t : user.currentLocation().getThings()) {
			if (t.getPosition().x() == user.currentPosition().x()
					&& t.getPosition().y() == user.currentPosition().y()) {
				thing = t;
			}
		}
		if (thing != null) {
			try {
				user.take(thing);
			} catch (IllegalArgumentException iae) {
				System.err.println("inadequate acquirement " + iae.getMessage());
				System.exit(1);
			} catch (IOException ioe) {
				System.err.println("unable to write data " + ioe.getMessage());
				System.exit(1);
			}
		}	
	}
	
	private void handleInventoryAction() {
		if (!inventoryIsEmpty()) {
			try {
				Thing thing = inventoryView.getSelectionModel().getSelectedItem();
				user.drop(thing);
				messages.setText(thing.getName() + " dropped");
				handleNewActivity(NodeLink.LAST);
			} catch (IOException ioe) {
				System.err.println("unable to write data " + ioe.getMessage());
				System.exit(1);
			}
		}
	}
		
	private boolean inventoryIsEmpty() {
		return inventoryView.getSelectionModel().getSelectedIndex() == -1;
	}
	
	private void layoutComponents() {
		final int SIZE = 3;
		int childIndex = 0;
		for (int row = 0; row < SIZE; row++) {
			for (int col = 0; col < SIZE; col++) {
				navigation.add(navigationComponents.get(childIndex++), col, row);
			}
		}
		top.getItems().add(navigation);
		top.getItems().add(topRight);
		inventoryBox.getChildren().addAll(inventoryHeader, inventoryView);
		
		setWorldGrid();
		
		worldGrid.requestFocus();
		worldBox.getChildren().add(worldGrid);
		mainPanel.setTop(top);
		mainPanel.setLeft(worldBox);
		mainPanel.setRight(inventoryBox);
		mainPanel.setBottom(messages);
	}
	
	public void createAndShow(Stage stage) {
		initComponents();
		addListeners();
		layoutComponents();
		
		stage.setScene(gui);
		stage.show();
	}
	
	private enum Status {
		SUCCESS(0),
		FAILURE(-1);
		
		private int code;
		
		Status(int num) {
			code = num;
		}
		
		private int code() {
			return code;
		}
	}
	
}
