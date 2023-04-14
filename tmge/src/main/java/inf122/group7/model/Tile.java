package inf122.group7.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;

public abstract class Tile {
    private String id; // a unique identifier for each tile
    private String name; // how this tile should be represented as text
    private boolean canSelect;
    protected boolean isSelected;

    // Controller Properties
    private StringProperty textValue; // how this tile will ultimately be shown

    private ChangeListener<String> activeListener; // needed to remove the listener later

    public Tile(String id, String name, boolean canSelect, boolean isSelected) {
        this(id, name);
        this.canSelect = canSelect;
        this.isSelected = isSelected;
        updateTextValue();
    }

    public Tile(String id, String name) {
        this.id = id;
        this.name = name;
        this.textValue = new SimpleStringProperty(name);
    }

    public String getId() {
        return this.id;
    }

    public void setName(String name) {
        this.name = name;
        updateTextValue();
    }

    public String getName() {
        return this.name;
    }

    public String getTextValue() {
        return textValue.get();
    }

    private void updateTextValue() {
        String value = name;
        if (isSelected) {
            value = "*" + value + "*";
        }
        this.textValue.set(value);
    }

    public void setValueChangeListener(ChangeListener<String> listener) {
        if (activeListener == null) {
            activeListener = listener;
        } else {
            clearValueChangeListener();
            activeListener = listener;
        }

        textValue.addListener(listener); // THIS DOES NOT REPLACE THE PREVIOUS LISTENER, HENCE THE ABOVE CODE
    }

    public void clearValueChangeListener() {
        if (activeListener != null) {
            textValue.removeListener(activeListener);
            activeListener = null;
        }
    }

    public void setCanSelect(boolean canSelect) {
        this.canSelect = canSelect;
    }

    public boolean getCanSelect() {
        return this.canSelect;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
        updateTextValue();
    }

    public boolean getIsSelected() {
        return this.isSelected;
    }

    public boolean matches(Tile otherTile) {
        return this.id.equalsIgnoreCase(otherTile.id);
    }

    public abstract String getImage(); // should return string of the path to the image resource
}