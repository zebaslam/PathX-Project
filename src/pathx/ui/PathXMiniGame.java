package pathx.ui;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import pathx.data.PathXDataModel;
import mini_game.MiniGame;
import mini_game.MiniGameState;
import static pathx.PathXConstants.*;
import mini_game.Sprite;
import mini_game.SpriteType;
import mini_game.Viewport;
import properties_manager.PropertiesManager;
import pathx.PathXConstants;
import pathx.PathX.PathXPropertyType;
import pathx.file.PathXFileManager;
import pathx.ui.PathXCarState;
//import sorting_hat.file.PathXFileManager;
//import sorting_hat.data.PathXRecord;

/**
 *
 * @author zeb
 * @author Richard Mckenna
 */
public class PathXMiniGame extends MiniGame {

    private PathXErrorHandler errorHandler;
    private PathXEventHandler eventHandler;
    private PathXFileManager fileManager;
    // THE SCREEN CURRENTLY BEING PLAYED
    private String currentScreenState;
    static PathXMiniGame miniGame = new PathXMiniGame();
    private float background_x = 0;
    private float background_y = 0;
    //used for making sure the button doesnt move too much
    private int scrollNum = 0;
    private boolean scroll;

    public boolean getScroll() {
        return scroll;
    }

    public void setScroll(boolean TF) {
        scroll = TF;
    }

    public PathXErrorHandler getErrorHandler() {
        return errorHandler;
    }

    public PathXFileManager getFileManager() {
        return fileManager;
    }

    public void savePlayerRecord() {
        //TODO 
    }

    public boolean isCurrentScreenState(String testScreenState) {
        return testScreenState.equals(currentScreenState);
    }

    @Override
    public void initGUIControls() {
        BufferedImage img;
        float x, y;
        float ex, why;
        SpriteType sT;
        Sprite s;

        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty(PathXPropertyType.PATH_IMG);
        String windowIconFile = props.getProperty(PathXPropertyType.IMAGE_WINDOW_ICON);
        img = loadImage(imgPath + windowIconFile);
        window.setIconImage(img);

        // CONSTRUCT THE PANEL WHERE WE'LL DRAW EVERYTHING
        canvas = new PathXPanel(this, (PathXDataModel) data);

        // LOAD THE BACKGROUNDS, WHICH ARE GUI DECOR
        currentScreenState = MENU_SCREEN_STATE;
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_BACKGROUND_MENU));
        sT = new SpriteType(BACKGROUND_TYPE);
        sT.addState(MENU_SCREEN_STATE, img);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_BACKGROUND_GAME));
        sT.addState(GAME_SCREEN_STATE, img);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_BACKGROUND_HELP));
        sT.addState(HELP_SCREEN_STATE, img);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_BACKGROUND_LEVEL_SELECT));
        sT.addState(LEVEL_SELECT_SCREEN_STATE, img);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_BACKGROUND_SETTINGS));
        sT.addState(SETTINGS_SCREEN_STATE, img);
        s = new Sprite(sT, 0, 0, 0, 0, MENU_SCREEN_STATE);
        guiDecor.put(BACKGROUND_TYPE, s);

        String playButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_PLAY);
        sT = new SpriteType(PLAY_BUTTON_TYPE);
        img = loadImage(imgPath + playButton);
        sT.addState(VISIBLE_STATE.toString(), img);
        String newMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_PLAY_MOUSE_OVER);
        img = loadImage(imgPath + newMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, PLAY_BUTTON_X, PLAY_BUTTON_Y, 0, 0, VISIBLE_STATE.toString());
        guiButtons.put(PLAY_BUTTON_TYPE, s);

        String resetButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_RESET);
        sT = new SpriteType(RESET_BUTTON_TYPE);
        img = loadImage(imgPath + resetButton);
        sT.addState(VISIBLE_STATE.toString(), img);
        String resetMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_RESET_MOUSE_OVER);
        img = loadImage(imgPath + resetMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, RESET_X, RESET_Y, 0, 0, VISIBLE_STATE.toString());
        guiButtons.put(RESET_BUTTON_TYPE, s);

        String helpButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_HELP);
        sT = new SpriteType(HELP_BUTTON_TYPE);
        img = loadImage(imgPath + helpButton);
        sT.addState(VISIBLE_STATE.toString(), img);
        String helpMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_HELP_MOUSE_OVER);
        img = loadImage(imgPath + helpMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, HELP_X, HELP_Y, 0, 0, VISIBLE_STATE.toString());
        guiButtons.put(HELP_BUTTON_TYPE, s);

        String settingsButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_SETTINGS);
        sT = new SpriteType(SETTINGS_BUTTON_TYPE);
        img = loadImage(imgPath + settingsButton);
        sT.addState(VISIBLE_STATE.toString(), img);
        String settingsMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_SETTINGS_MOUSE_OVER);
        img = loadImage(imgPath + settingsMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, SETTINGS_X, SETTINGS_Y, 0, 0, VISIBLE_STATE.toString());
        guiButtons.put(SETTINGS_BUTTON_TYPE, s);

        String closeButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_CLOSE);
        sT = new SpriteType(CLOSE_BUTTON_TYPE);
        img = loadImage(imgPath + closeButton);
        sT.addState(VISIBLE_STATE.toString(), img);
        String closeMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_CLOSE_MOUSE_OVER);
        img = loadImage(imgPath + closeMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, EXIT_X, EXIT_Y, 0, 0, VISIBLE_STATE.toString());
        guiButtons.put(CLOSE_BUTTON_TYPE, s);

        String homeButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_HOME);
        sT = new SpriteType(HOME_BUTTON_TYPE);
        img = loadImage(imgPath + homeButton);
        sT.addState(VISIBLE_STATE.toString(), img);
        String homeMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_HOME_MOUSE_OVER);
        img = loadImage(imgPath + homeMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, HOME_X, HOME_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(HOME_BUTTON_TYPE, s);

        String upButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_UP);
        sT = new SpriteType(UP_BUTTON_TYPE);
        img = loadImage(imgPath + upButton);
        sT.addState(VISIBLE_STATE.toString(), img);
        String upMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_UP_MOUSE_OVER);
        img = loadImage(imgPath + upMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, UP_X, UP_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(UP_BUTTON_TYPE, s);

        String downButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_DOWN);
        sT = new SpriteType(DOWN_BUTTON_TYPE);
        img = loadImage(imgPath + downButton);
        sT.addState(VISIBLE_STATE.toString(), img);
        String downMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_DOWN_MOUSE_OVER);
        img = loadImage(imgPath + downMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, DOWN_X, DOWN_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(DOWN_BUTTON_TYPE, s);

        String leftButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_LEFT);
        sT = new SpriteType(LEFT_BUTTON_TYPE);
        img = loadImage(imgPath + leftButton);
        sT.addState(VISIBLE_STATE.toString(), img);
        String leftMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_LEFT_MOUSE_OVER);
        img = loadImage(imgPath + leftMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, LEFT_X, LEFT_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(LEFT_BUTTON_TYPE, s);

        String rightButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_RIGHT);
        sT = new SpriteType(RIGHT_BUTTON_TYPE);
        img = loadImage(imgPath + rightButton);
        sT.addState(VISIBLE_STATE.toString(), img);
        String rightMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_RIGHT_MOUSE_OVER);
        img = loadImage(imgPath + rightMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, RIGHT_X, RIGHT_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(RIGHT_BUTTON_TYPE, s);

        //level buttons?
        
        String level1Button = props.getProperty(PathXPropertyType.IMAGE_MAP_AVAILABLE);
        sT = new SpriteType(LEVEL_1_BUTTON_TYPE);
        img = loadImage(imgPath + level1Button);
        sT.addState(VISIBLE_STATE.toString(), img);
        String level1ButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_MAP_AVAILABLE_MOUSE_OVER);
        img = loadImage(imgPath + level1ButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, LEVEL_1_X, LEVEL_1_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_1_BUTTON_TYPE, s);
        
        String level2Button = props.getProperty(PathXPropertyType.IMAGE_MAP_LOCKED);
        sT = new SpriteType(LEVEL_2_BUTTON_TYPE);
        img = loadImage(imgPath + level2Button);
        sT.addState(VISIBLE_STATE.toString(), img);
        String level2ButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_MAP_LOCKED_MOUSE_OVER);
        img = loadImage(imgPath + level2ButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, LEVEL_2_X, LEVEL_2_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_2_BUTTON_TYPE, s);

        //Dialogs
        String settingsDialog = props.getProperty(PathXPropertyType.IMAGE_DIALOG_SETTINGS);
        sT = new SpriteType(SETTINGS_DIALOG_TYPE);
        img = loadImage(imgPath + settingsDialog);
        sT.addState(PathXCarState.VISIBLE_STATE.toString(), img);
        ex = (WINDOW_WIDTH / 2 - 200);
        why = (WINDOW_HEIGHT / 2 - 165);
        s = new Sprite(sT, ex, why, 0, 0, INVISIBLE_STATE.toString());
        guiDialogs.put(SETTINGS_DIALOG_TYPE, s);

        String levelInfoDialog = props.getProperty(PathXPropertyType.IMAGE_DIALOG_LEVEL_SELECT);
        sT = new SpriteType(LEVEL_INFO_DIALOG_TYPE);
        img = loadImage(imgPath + levelInfoDialog);
        sT.addState(PathXCarState.VISIBLE_STATE.toString(), img);
        //change around maybe
        ex = (WINDOW_WIDTH / 2 - 200);
        why = (WINDOW_HEIGHT / 2 - 200);
        s = new Sprite(sT, ex, why, 0, 0, INVISIBLE_STATE.toString());
        guiDialogs.put(LEVEL_INFO_DIALOG_TYPE, s);

        String closeLevelButton = props.getProperty(PathXPropertyType.LEVEL_CLOSE_BUTTON);
        sT = new SpriteType(CLOSE_DIALOG_BUTTON_TYPE);
        img = loadImage(imgPath + closeLevelButton);
        sT.addState(VISIBLE_STATE.toString(), img);
        String closeLevelButtonMouseOverButton = props.getProperty(PathXPropertyType.LEVEL_CLOSE_BUTTON_MOUSE_OVER);
        img = loadImage(imgPath + closeLevelButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, LEVEL_CLOSE_X, LEVEL_CLOSE_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(CLOSE_DIALOG_BUTTON_TYPE, s);

        x = guiDecor.get(BACKGROUND_TYPE).getX();
        y = guiDecor.get(BACKGROUND_TYPE).getY();
        setInitialValues(x, y);

    }

    public void switchToHelpScreen() {
        System.out.println("IN SWITCH TO HELP SCREEN");
        guiDecor.get(BACKGROUND_TYPE).setState(HELP_SCREEN_STATE);
        guiButtons.get(PLAY_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(PLAY_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(CLOSE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(CLOSE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(HELP_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(HELP_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(RESET_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(RESET_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SETTINGS_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(SETTINGS_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(HOME_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(HOME_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(UP_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(UP_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(DOWN_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(DOWN_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEFT_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEFT_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(RIGHT_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(RIGHT_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_1_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_1_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_2_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(CLOSE_DIALOG_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(CLOSE_DIALOG_BUTTON_TYPE).setEnabled(false);
        currentScreenState = HELP_SCREEN_STATE;

        audio.play(PathXPropertyType.SONG_CUE_MENU_SCREEN.toString(), true);
        audio.stop(PathXPropertyType.SONG_CUE_LEVEL_SCREEN.toString());
        audio.stop(PathXPropertyType.SONG_CUE_GAME_SCREEN.toString());

        guiDialogs.get(SETTINGS_DIALOG_TYPE).setState(INVISIBLE_STATE.toString());
        guiDialogs.get(LEVEL_INFO_DIALOG_TYPE).setState(INVISIBLE_STATE.toString());

        float x = getX();
        float y = getY();
        resetBackground(x, y);
        setScroll(false);
    }

    public void displaySettings() {
        guiDialogs.get(SETTINGS_DIALOG_TYPE).setState(PathXCarState.VISIBLE_STATE.toString());
    }

    public void switchToSettingsScreen() {
        guiDecor.get(BACKGROUND_TYPE).setState(SETTINGS_SCREEN_STATE);
        guiButtons.get(PLAY_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(PLAY_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(CLOSE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(CLOSE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(HELP_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(HELP_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(RESET_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(RESET_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SETTINGS_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(SETTINGS_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(HOME_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(HOME_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(UP_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(UP_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(DOWN_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(DOWN_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEFT_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEFT_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(RIGHT_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(RIGHT_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_1_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_1_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_2_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(CLOSE_DIALOG_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(CLOSE_DIALOG_BUTTON_TYPE).setEnabled(false);
        currentScreenState = SETTINGS_SCREEN_STATE;

        audio.play(PathXPropertyType.SONG_CUE_MENU_SCREEN.toString(), true);
        audio.stop(PathXPropertyType.SONG_CUE_LEVEL_SCREEN.toString());
        audio.stop(PathXPropertyType.SONG_CUE_GAME_SCREEN.toString());

        guiDialogs.get(SETTINGS_DIALOG_TYPE).setState(VISIBLE_STATE.toString());
        guiDialogs.get(LEVEL_INFO_DIALOG_TYPE).setState(INVISIBLE_STATE.toString());

        float x = getX();
        float y = getY();
        resetBackground(x, y);
        setScroll(false);
    }

    public void switchToGameScreen() {
        System.out.println("IN SWITCH TO GAME SCREEN");
        guiDecor.get(BACKGROUND_TYPE).setState(GAME_SCREEN_STATE);
        guiButtons.get(PLAY_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(PLAY_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(CLOSE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(CLOSE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(HELP_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(HELP_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(RESET_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(RESET_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SETTINGS_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(SETTINGS_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(HOME_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(HOME_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(UP_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(UP_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(DOWN_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(DOWN_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEFT_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEFT_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(RIGHT_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(RIGHT_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_1_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_1_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_2_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_BUTTON_TYPE).setEnabled(false);

        currentScreenState = GAME_SCREEN_STATE;

        audio.play(PathXPropertyType.SONG_CUE_GAME_SCREEN.toString(), true);
        audio.stop(PathXPropertyType.SONG_CUE_LEVEL_SCREEN.toString());
        audio.stop(PathXPropertyType.SONG_CUE_MENU_SCREEN.toString());

        guiDialogs.get(SETTINGS_DIALOG_TYPE).setState(INVISIBLE_STATE.toString());
        guiDialogs.get(LEVEL_INFO_DIALOG_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(CLOSE_DIALOG_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(CLOSE_DIALOG_BUTTON_TYPE).setEnabled(true);

        float x = getX();
        float y = getY();
        resetBackground(x, y);
        setScroll(false);
    }

    public void switchToLevelSelectScreen() {
        guiDecor.get(BACKGROUND_TYPE).setState(LEVEL_SELECT_SCREEN_STATE);
        guiButtons.get(PLAY_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(PLAY_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(CLOSE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(CLOSE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(HELP_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(HELP_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(RESET_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(RESET_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SETTINGS_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(SETTINGS_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(HOME_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(HOME_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(UP_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(UP_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(DOWN_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(DOWN_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEFT_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEFT_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(RIGHT_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(RIGHT_BUTTON_TYPE).setEnabled(true);

        guiButtons.get(LEVEL_1_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_1_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_2_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(CLOSE_DIALOG_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(CLOSE_DIALOG_BUTTON_TYPE).setEnabled(false);
        currentScreenState = LEVEL_SELECT_SCREEN_STATE;

        guiDialogs.get(SETTINGS_DIALOG_TYPE).setState(INVISIBLE_STATE.toString());
        guiDialogs.get(LEVEL_INFO_DIALOG_TYPE).setState(INVISIBLE_STATE.toString());

        // PLAY THE GAMEPLAY SCREEN SONG
        audio.stop(PathXPropertyType.SONG_CUE_MENU_SCREEN.toString());
        audio.stop(PathXPropertyType.SONG_CUE_GAME_SCREEN.toString());
        audio.play(PathXPropertyType.SONG_CUE_LEVEL_SCREEN.toString(), true);
        setScroll(true);
    }

    public void switchToSplashScreen() {
     //System.out.println("IN SWITCH TO SPLASH SCREEN");
        //Determines which buttons to display on splash screen
        guiDecor.get(BACKGROUND_TYPE).setState(MENU_SCREEN_STATE);
        guiButtons.get(PLAY_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(PLAY_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(CLOSE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(CLOSE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(HELP_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(HELP_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(RESET_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(RESET_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(SETTINGS_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(SETTINGS_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(HOME_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(HOME_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(UP_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(UP_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(DOWN_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(DOWN_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEFT_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEFT_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(RIGHT_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(RIGHT_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_1_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_1_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_2_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(CLOSE_DIALOG_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(CLOSE_DIALOG_BUTTON_TYPE).setEnabled(false);

        currentScreenState = MENU_SCREEN_STATE;
        data.setGameState(MiniGameState.NOT_STARTED);

        audio.play(PathXPropertyType.SONG_CUE_MENU_SCREEN.toString(), true);
        audio.stop(PathXPropertyType.SONG_CUE_LEVEL_SCREEN.toString());
        audio.stop(PathXPropertyType.SONG_CUE_GAME_SCREEN.toString());

        guiDialogs.get(SETTINGS_DIALOG_TYPE).setState(INVISIBLE_STATE.toString());
        guiDialogs.get(LEVEL_INFO_DIALOG_TYPE).setState(INVISIBLE_STATE.toString());
        float x = getX();
        float y = getY();
        resetBackground(x, y);
        setScroll(false);
    }

    public void makeDialogInvisible() {
        guiButtons.get(CLOSE_DIALOG_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(CLOSE_DIALOG_BUTTON_TYPE).setEnabled(false);

        guiDialogs.get(LEVEL_INFO_DIALOG_TYPE).setState(INVISIBLE_STATE.toString());
    }

    @Override
    public void initGUIHandlers() {
        eventHandler = new PathXEventHandler(this);
        window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        window.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                eventHandler.respondToExitRequest();
            }
        });

        //Event handler for close button
        guiButtons.get(CLOSE_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToExitRequest();
            }
        });

        //Event Handler for home button
        guiButtons.get(HOME_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToHomeRequest();
            }

        });

        guiButtons.get(HELP_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToHelpRequest();
            }

        });
        guiButtons.get(PLAY_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToLevelSelect();
            }

        });
        guiButtons.get(SETTINGS_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToSettingsSelect();
            }

        });
        guiButtons.get(UP_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                ScrollUp();
                ScrollUp();
                ScrollUp();
            }

        });
        guiButtons.get(DOWN_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                ScrollDown();
                ScrollDown();
                ScrollDown();
            }

        });
        guiButtons.get(LEFT_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                ScrollLeft();
                ScrollLeft();
                ScrollLeft();
            }

        });
        guiButtons.get(LEVEL_1_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToGameScreenSelect();
            }

        });
        guiButtons.get(RIGHT_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                ScrollRight();
                ScrollRight();
                ScrollRight();
            }

        });
        guiButtons.get(CLOSE_DIALOG_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToCloseDialog();
            }

        });
        this.setKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ke) {
                eventHandler.respondToKeyPress(ke.getKeyCode());
            }
        });
    }

    @Override
    public void initData() {
        errorHandler = new PathXErrorHandler(window);
        fileManager = new PathXFileManager(this);
        data = new PathXDataModel(this);
    }

    @Override
    public void reset() {
        data.reset(this);
    }

    @Override
    public void updateGUI() {
        // GO THROUGH THE VISIBLE BUTTONS TO TRIGGER MOUSE OVERS
        Iterator<Sprite> buttonsIt = guiButtons.values().iterator();
        while (buttonsIt.hasNext()) {
            Sprite button = buttonsIt.next();

            // ARE WE ENTERING A BUTTON?
            if (button.getState().equals(PathXCarState.VISIBLE_STATE.toString())) {
                if (button.containsPoint(data.getLastMouseX(), data.getLastMouseY())) {
                    button.setState(PathXCarState.MOUSE_OVER_STATE.toString());
                }
            } // ARE WE EXITING A BUTTON?
            else if (button.getState().equals(PathXCarState.MOUSE_OVER_STATE.toString())) {
                if (!button.containsPoint(data.getLastMouseX(), data.getLastMouseY())) {
                    button.setState(PathXCarState.VISIBLE_STATE.toString());
                }
            }
        }
    }

    @Override
    public void initAudioContent() {
        try {
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            String audioPath = props.getProperty(PathXPropertyType.PATH_AUDIO);

            // LOAD ALL THE AUDIO
            loadAudioCue(PathXPropertyType.AUDIO_CUE_SELECT_TILE);
            loadAudioCue(PathXPropertyType.AUDIO_CUE_DESELECT_TILE);
            loadAudioCue(PathXPropertyType.AUDIO_CUE_GOOD_MOVE);
            loadAudioCue(PathXPropertyType.AUDIO_CUE_BAD_MOVE);
            loadAudioCue(PathXPropertyType.AUDIO_CUE_CHEAT);
            loadAudioCue(PathXPropertyType.AUDIO_CUE_UNDO);
            loadAudioCue(PathXPropertyType.AUDIO_CUE_WIN);
            loadAudioCue(PathXPropertyType.SONG_CUE_MENU_SCREEN);
            loadAudioCue(PathXPropertyType.SONG_CUE_GAME_SCREEN);
            loadAudioCue(PathXPropertyType.SONG_CUE_LEVEL_SCREEN);

            // PLAY THE WELCOME SCREEN SONG
            audio.play(PathXPropertyType.SONG_CUE_MENU_SCREEN.toString(), true);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException | InvalidMidiDataException | MidiUnavailableException e) {
            errorHandler.processError(PathXPropertyType.TEXT_ERROR_LOADING_AUDIO);
        }
    }

    private void loadAudioCue(PathXPropertyType audioCueType)
            throws UnsupportedAudioFileException, IOException, LineUnavailableException,
            InvalidMidiDataException, MidiUnavailableException {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String audioPath = props.getProperty(PathXPropertyType.PATH_AUDIO);
        String cue = props.getProperty(audioCueType.toString());
        audio.loadAudio(audioCueType.toString(), audioPath + cue);
    }

    public void ScrollUp() {
        scrollNum = 0;
        boolean scrollvalue = getScroll();
        if (scrollvalue == true) {
            float x = guiDecor.get(BACKGROUND_TYPE).getX();
            float y = guiDecor.get(BACKGROUND_TYPE).getY();
            if ((y) < 0) {
                guiDecor.get(BACKGROUND_TYPE).setX(x);
                guiDecor.get(BACKGROUND_TYPE).setY(y + 15);
            }

            System.out.println("y:" + y);
        }
        setLevelVisibility();
        updateGUI();
    }

    public void setLevelVisibility() {
        float x = guiDecor.get(BACKGROUND_TYPE).getX();
        float y = guiDecor.get(BACKGROUND_TYPE).getY();

        //level 1
        if (x < -45 || y < -285) {
            guiButtons.get(LEVEL_1_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
            guiButtons.get(LEVEL_1_BUTTON_TYPE).setEnabled(false);
        } else {
            guiButtons.get(LEVEL_1_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
            guiButtons.get(LEVEL_1_BUTTON_TYPE).setEnabled(true);
            
            
            guiButtons.get(LEVEL_1_BUTTON_TYPE).setX(LEVEL_1_X+ x);
            guiButtons.get(LEVEL_1_BUTTON_TYPE).setY(LEVEL_1_Y+ y);

        }
        if (x < -45 || y < -345) {
            guiButtons.get(LEVEL_2_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
            guiButtons.get(LEVEL_2_BUTTON_TYPE).setEnabled(false);
        } else {
            guiButtons.get(LEVEL_2_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
            guiButtons.get(LEVEL_2_BUTTON_TYPE).setEnabled(true);
            
            
            guiButtons.get(LEVEL_2_BUTTON_TYPE).setX(LEVEL_2_X+ x);
            guiButtons.get(LEVEL_2_BUTTON_TYPE).setY(LEVEL_2_Y+ y);

        }
        updateGUI();
    }

    public void ScrollDown() {
        scrollNum = 1;
        boolean scrollvalue = getScroll();
        if (scrollvalue == true) {
            float x = guiDecor.get(BACKGROUND_TYPE).getX();
            float y = guiDecor.get(BACKGROUND_TYPE).getY();
            if (y > -468) {
                guiDecor.get(BACKGROUND_TYPE).setX(x);
                guiDecor.get(BACKGROUND_TYPE).setY(y - 15);
            }
            System.out.println("y:" + y);
        }
        setLevelVisibility();

        updateGUI();
    }

    public void ScrollLeft() {
        scrollNum = 2;
        boolean scrollvalue = getScroll();
        if (scrollvalue == true) {
            float x = guiDecor.get(BACKGROUND_TYPE).getX();
            float y = guiDecor.get(BACKGROUND_TYPE).getY();
            if (Math.abs(x) > 0.0) {
                guiDecor.get(BACKGROUND_TYPE).setX(x + 15);
                guiDecor.get(BACKGROUND_TYPE).setY(y);
            }
            System.out.println("x:" + x);

        }
        setLevelVisibility();
        updateGUI();
    }

    public void ScrollRight() {
        scrollNum = 3;
        boolean scrollvalue = getScroll();
        if (scrollvalue == true) {
            float x = guiDecor.get(BACKGROUND_TYPE).getX();
            float y = guiDecor.get(BACKGROUND_TYPE).getY();
            if ((x) > -1245) {
                guiDecor.get(BACKGROUND_TYPE).setX(x - 15);
                guiDecor.get(BACKGROUND_TYPE).setY(y);
            }
            System.out.println("x:" + x);

        }
        setLevelVisibility();
        updateGUI();
    }

    /**
     * This method resets the background image to that of it's original
     * locations
     *
     * @param x x coordinate for background image
     * @param y y coordinate for background image
     */
    public void setInitialValues(float x, float y) {
        background_x = x;
        background_y = y;
    }

    /**
     * get the initial y coordinate
     *
     * @return
     */
    public float getY() {
        return background_y;
    }

    public float getX() {
        return background_x;
    }

    /**
     * This resets the background to its initial value
     *
     * @param x x coordinate
     * @param y y coordinate
     */
    public void resetBackground(float x, float y) {
        guiDecor.get(BACKGROUND_TYPE).setX(x);
        guiDecor.get(BACKGROUND_TYPE).setY(y);
        guiButtons.get(LEVEL_1_BUTTON_TYPE).setX(LEVEL_1_X);
        guiButtons.get(LEVEL_1_BUTTON_TYPE).setY(LEVEL_1_Y);
        guiButtons.get(LEVEL_2_BUTTON_TYPE).setX(LEVEL_2_X);
        guiButtons.get(LEVEL_2_BUTTON_TYPE).setY(LEVEL_2_Y);
        updateGUI();
    }

}
