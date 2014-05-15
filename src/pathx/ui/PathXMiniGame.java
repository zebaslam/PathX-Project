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
import pathx.data.Intersection;
import pathx.data.PathXGameGraph;
import pathx.data.Road;
import pathx.file.PathXFileManager;
import pathx.ui.PathXCarState;
//import mini_game.Viewport1;


/**
 *
 * @author zeb
 * @author Richard Mckenna
 */
public class PathXMiniGame extends MiniGame {

    private PathXErrorHandler errorHandler;
    private PathXEventHandler eventHandler;
    private PathXFileManager fileManager;
    private int level;
    private int lost;
    private int access=1;
    private int levelwon=0;
    // THE SCREEN CURRENTLY BEING PLAYED
    private String currentScreenState;
    static PathXMiniGame miniGame = new PathXMiniGame();
    
    private float background_x = 0;
    private float background_y = 0;
    
    private int scrollNum=0;
    private boolean enableSound=true;
    private boolean win;
    
    
    private boolean level1win=false;
    public int getLost(){
        return lost;
    }
    public boolean isWin() {
        return win;
    }

    public void setWin(boolean win) {
        this.win = win;
    }

    public boolean getEnableSound() {
        return enableSound;
    }

    public void setEnableSound(boolean enableSound) {
        this.enableSound = enableSound;
    }
    private boolean scroll;
    private boolean enableMusic=true;
    public boolean getEnableMusic() {
        return enableMusic;
    }

    public void setEnableMusic(boolean enableMusic) {
        this.enableMusic = enableMusic;
    }
    private PathXGameGraph graph;
    

    public int getLevel(){
        return level;
    }
    public void setLevel(int lev){
        level=lev;
    }
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

        String unselectedMusic = props.getProperty(PathXPropertyType.IMAGE_SETTINGS_MUSIC_UNSELECTED);
        sT = new SpriteType(MUSIC_UNSELECTED_BUTTON_TYPE);
        img = loadImage(imgPath + unselectedMusic);
        sT.addState(VISIBLE_STATE.toString(), img);
        String unselectedMusicMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_SETTINGS_MUSIC_UNSELECTED);
        img = loadImage(imgPath + unselectedMusicMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, MUSIC_SELECTED_X, MUSIC_SELECTED_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(MUSIC_UNSELECTED_BUTTON_TYPE, s);
        
        String selectedMusic = props.getProperty(PathXPropertyType.IMAGE_SETTINGS_MUSIC_SELECTED);
        sT = new SpriteType(MUSIC_SELECTED_BUTTON_TYPE);
        img = loadImage(imgPath + selectedMusic);
        sT.addState(VISIBLE_STATE.toString(), img);
        String selectedMusicMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_SETTINGS_MUSIC_SELECTED);
        img = loadImage(imgPath + selectedMusicMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, MUSIC_SELECTED_X, MUSIC_SELECTED_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(MUSIC_SELECTED_BUTTON_TYPE, s);
        
        
        String unselectedSound = props.getProperty(PathXPropertyType.IMAGE_SETTINGS_SOUND_UNSELECTED);
        sT = new SpriteType(SOUND_UNSELECTED_BUTTON_TYPE);
        img = loadImage(imgPath + unselectedSound);
        sT.addState(VISIBLE_STATE.toString(), img);
        String unselectedSoundMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_SETTINGS_SOUND_UNSELECTED);
        img = loadImage(imgPath + unselectedSoundMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, SOUND_SELECTED_X, SOUND_SELECTED_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(SOUND_UNSELECTED_BUTTON_TYPE, s);
        
        String selectedSound = props.getProperty(PathXPropertyType.IMAGE_SETTINGS_SOUND_SELECTED);
        sT = new SpriteType(SOUND_SELECTED_BUTTON_TYPE);
        img = loadImage(imgPath + selectedSound);
        sT.addState(VISIBLE_STATE.toString(), img);
        String selectedSoundMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_SETTINGS_SOUND_SELECTED);
        img = loadImage(imgPath + selectedSoundMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, SOUND_SELECTED_X, SOUND_SELECTED_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(SOUND_SELECTED_BUTTON_TYPE, s);
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
        
        String level1Buttoncompleted = props.getProperty(PathXPropertyType.IMAGE_MAP_COMPLETED);
        sT = new SpriteType(LEVEL_1_COMPLETE_BUTTON_TYPE);
        img = loadImage(imgPath + level1Buttoncompleted);
        sT.addState(VISIBLE_STATE.toString(), img);
        String level1completedButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_MAP_COMPLETED_MOUSE_OVER);
        img = loadImage(imgPath + level1completedButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, LEVEL_1_X, LEVEL_1_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_1_COMPLETE_BUTTON_TYPE, s);

        String level2Button = props.getProperty(PathXPropertyType.IMAGE_MAP_LOCKED);
        sT = new SpriteType(LEVEL_2_BUTTON_TYPE);
        img = loadImage(imgPath + level2Button);
        sT.addState(VISIBLE_STATE.toString(), img);
        String level2ButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_MAP_LOCKED_MOUSE_OVER);
        img = loadImage(imgPath + level2ButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, LEVEL_2_X, LEVEL_2_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_2_BUTTON_TYPE, s);

        String level2availbleButton = props.getProperty(PathXPropertyType.IMAGE_MAP_AVAILABLE);
        sT = new SpriteType(LEVEL_2_AVAILABLE_BUTTON_TYPE);
        img = loadImage(imgPath + level2availbleButton);
        sT.addState(VISIBLE_STATE.toString(), img);
        String level2availbleButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_MAP_AVAILABLE_MOUSE_OVER);
        img = loadImage(imgPath + level2availbleButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, LEVEL_2_X, LEVEL_2_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_2_AVAILABLE_BUTTON_TYPE, s);
        
       String level2completeButton = props.getProperty(PathXPropertyType.IMAGE_MAP_COMPLETED);
        sT = new SpriteType(LEVEL_2_COMPLETE_BUTTON_TYPE);
        img = loadImage(imgPath + level2completeButton);
        sT.addState(VISIBLE_STATE.toString(), img);
        String level2completeButtonButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_MAP_COMPLETED_MOUSE_OVER);
        img = loadImage(imgPath + level2completeButtonButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, LEVEL_2_X, LEVEL_2_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_2_COMPLETE_BUTTON_TYPE, s);
        
        String level3Button = props.getProperty(PathXPropertyType.IMAGE_MAP_LOCKED);
        sT = new SpriteType(LEVEL_3_BUTTON_TYPE);
        img = loadImage(imgPath + level3Button);
        sT.addState(VISIBLE_STATE.toString(), img);
        String level3ButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_MAP_LOCKED_MOUSE_OVER);
        img = loadImage(imgPath + level3ButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, LEVEL_3_X, LEVEL_3_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_3_BUTTON_TYPE, s);

        String level3availbleButton = props.getProperty(PathXPropertyType.IMAGE_MAP_AVAILABLE);
        sT = new SpriteType(LEVEL_3_AVAILABLE_BUTTON_TYPE);
        img = loadImage(imgPath + level3availbleButton);
        sT.addState(VISIBLE_STATE.toString(), img);
        String level3availbleButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_MAP_AVAILABLE_MOUSE_OVER);
        img = loadImage(imgPath + level3availbleButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, LEVEL_3_X, LEVEL_3_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_3_AVAILABLE_BUTTON_TYPE, s);
        
        String level3completeButton = props.getProperty(PathXPropertyType.IMAGE_MAP_COMPLETED);
        sT = new SpriteType(LEVEL_3_COMPLETE_BUTTON_TYPE);
        img = loadImage(imgPath + level3completeButton);
        sT.addState(VISIBLE_STATE.toString(), img);
        String level3completeButtonButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_MAP_COMPLETED_MOUSE_OVER);
        img = loadImage(imgPath + level3completeButtonButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, LEVEL_3_X, LEVEL_3_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_3_COMPLETE_BUTTON_TYPE, s);
        
        String level4Button = props.getProperty(PathXPropertyType.IMAGE_MAP_LOCKED);
        sT = new SpriteType(LEVEL_4_BUTTON_TYPE);
        img = loadImage(imgPath + level4Button);
        sT.addState(VISIBLE_STATE.toString(), img);
        String level4ButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_MAP_LOCKED_MOUSE_OVER);
        img = loadImage(imgPath + level4ButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, LEVEL_4_X, LEVEL_4_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_4_BUTTON_TYPE, s);

        String level4availbleButton = props.getProperty(PathXPropertyType.IMAGE_MAP_AVAILABLE);
        sT = new SpriteType(LEVEL_4_AVAILABLE_BUTTON_TYPE);
        img = loadImage(imgPath + level4availbleButton);
        sT.addState(VISIBLE_STATE.toString(), img);
        String level4availbleButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_MAP_AVAILABLE_MOUSE_OVER);
        img = loadImage(imgPath + level4availbleButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, LEVEL_4_X, LEVEL_4_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_4_AVAILABLE_BUTTON_TYPE, s);
        
        String level4completeButton = props.getProperty(PathXPropertyType.IMAGE_MAP_COMPLETED);
        sT = new SpriteType(LEVEL_4_COMPLETE_BUTTON_TYPE);
        img = loadImage(imgPath + level4completeButton);
        sT.addState(VISIBLE_STATE.toString(), img);
        String level4completeButtonButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_MAP_COMPLETED_MOUSE_OVER);
        img = loadImage(imgPath + level4completeButtonButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, LEVEL_4_X, LEVEL_4_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_4_COMPLETE_BUTTON_TYPE, s);
        
        String level5Button = props.getProperty(PathXPropertyType.IMAGE_MAP_LOCKED);
        sT = new SpriteType(LEVEL_5_BUTTON_TYPE);
        img = loadImage(imgPath + level5Button);
        sT.addState(VISIBLE_STATE.toString(), img);
        String level5ButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_MAP_LOCKED_MOUSE_OVER);
        img = loadImage(imgPath + level5ButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, LEVEL_5_X, LEVEL_5_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_5_BUTTON_TYPE, s);

        
        String level5availbleButton = props.getProperty(PathXPropertyType.IMAGE_MAP_AVAILABLE);
        sT = new SpriteType(LEVEL_5_AVAILABLE_BUTTON_TYPE);
        img = loadImage(imgPath + level5availbleButton);
        sT.addState(VISIBLE_STATE.toString(), img);
        String level5availbleButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_MAP_AVAILABLE_MOUSE_OVER);
        img = loadImage(imgPath + level5availbleButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, LEVEL_5_X, LEVEL_5_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_5_AVAILABLE_BUTTON_TYPE, s);
        
        String level5completeButton = props.getProperty(PathXPropertyType.IMAGE_MAP_COMPLETED);
        sT = new SpriteType(LEVEL_5_COMPLETE_BUTTON_TYPE);
        img = loadImage(imgPath + level5completeButton);
        sT.addState(VISIBLE_STATE.toString(), img);
        String level5completeButtonButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_MAP_COMPLETED_MOUSE_OVER);
        img = loadImage(imgPath + level5completeButtonButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, LEVEL_5_X, LEVEL_5_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_5_COMPLETE_BUTTON_TYPE, s);
        
        
        String level6Button = props.getProperty(PathXPropertyType.IMAGE_MAP_LOCKED);
        sT = new SpriteType(LEVEL_6_BUTTON_TYPE);
        img = loadImage(imgPath + level6Button);
        sT.addState(VISIBLE_STATE.toString(), img);
        String level6ButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_MAP_LOCKED_MOUSE_OVER);
        img = loadImage(imgPath + level6ButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, LEVEL_6_X, LEVEL_6_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_6_BUTTON_TYPE, s);
        
         String level6availbleButton = props.getProperty(PathXPropertyType.IMAGE_MAP_AVAILABLE);
        sT = new SpriteType(LEVEL_6_AVAILABLE_BUTTON_TYPE);
        img = loadImage(imgPath + level6availbleButton);
        sT.addState(VISIBLE_STATE.toString(), img);
        String level6availbleButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_MAP_AVAILABLE_MOUSE_OVER);
        img = loadImage(imgPath + level6availbleButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, LEVEL_6_X, LEVEL_6_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_6_AVAILABLE_BUTTON_TYPE, s);
        
        String level6completeButton = props.getProperty(PathXPropertyType.IMAGE_MAP_COMPLETED);
        sT = new SpriteType(LEVEL_6_COMPLETE_BUTTON_TYPE);
        img = loadImage(imgPath + level6completeButton);
        sT.addState(VISIBLE_STATE.toString(), img);
        String level6completeButtonButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_MAP_COMPLETED_MOUSE_OVER);
        img = loadImage(imgPath + level6completeButtonButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, LEVEL_6_X, LEVEL_6_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_6_COMPLETE_BUTTON_TYPE, s);
        
        String level7Button = props.getProperty(PathXPropertyType.IMAGE_MAP_LOCKED);
        sT = new SpriteType(LEVEL_7_BUTTON_TYPE);
        img = loadImage(imgPath + level7Button);
        sT.addState(VISIBLE_STATE.toString(), img);
        String level7ButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_MAP_LOCKED_MOUSE_OVER);
        img = loadImage(imgPath + level7ButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, LEVEL_7_X, LEVEL_7_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_7_BUTTON_TYPE, s);
        
        String level7availbleButton = props.getProperty(PathXPropertyType.IMAGE_MAP_AVAILABLE);
        sT = new SpriteType(LEVEL_7_AVAILABLE_BUTTON_TYPE);
        img = loadImage(imgPath + level7availbleButton);
        sT.addState(VISIBLE_STATE.toString(), img);
        String level7availbleButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_MAP_AVAILABLE_MOUSE_OVER);
        img = loadImage(imgPath + level7availbleButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, LEVEL_7_X, LEVEL_7_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_7_AVAILABLE_BUTTON_TYPE, s);
        
        String level7completeButton = props.getProperty(PathXPropertyType.IMAGE_MAP_COMPLETED);
        sT = new SpriteType(LEVEL_7_COMPLETE_BUTTON_TYPE);
        img = loadImage(imgPath + level7completeButton);
        sT.addState(VISIBLE_STATE.toString(), img);
        String level7completeButtonButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_MAP_COMPLETED_MOUSE_OVER);
        img = loadImage(imgPath + level7completeButtonButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, LEVEL_7_X, LEVEL_7_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_7_COMPLETE_BUTTON_TYPE, s);
        
        String level8Button = props.getProperty(PathXPropertyType.IMAGE_MAP_LOCKED);
        sT = new SpriteType(LEVEL_8_BUTTON_TYPE);
        img = loadImage(imgPath + level8Button);
        sT.addState(VISIBLE_STATE.toString(), img);
        String level8ButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_MAP_LOCKED_MOUSE_OVER);
        img = loadImage(imgPath + level8ButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, LEVEL_8_X, LEVEL_8_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_8_BUTTON_TYPE, s);

        String level8availbleButton = props.getProperty(PathXPropertyType.IMAGE_MAP_AVAILABLE);
        sT = new SpriteType(LEVEL_8_AVAILABLE_BUTTON_TYPE);
        img = loadImage(imgPath + level8availbleButton);
        sT.addState(VISIBLE_STATE.toString(), img);
        String level8availbleButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_MAP_AVAILABLE_MOUSE_OVER);
        img = loadImage(imgPath + level8availbleButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, LEVEL_8_X, LEVEL_8_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_8_AVAILABLE_BUTTON_TYPE, s);
        
        String level8completeButton = props.getProperty(PathXPropertyType.IMAGE_MAP_COMPLETED);
        sT = new SpriteType(LEVEL_8_COMPLETE_BUTTON_TYPE);
        img = loadImage(imgPath + level8completeButton);
        sT.addState(VISIBLE_STATE.toString(), img);
        String level8completeButtonButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_MAP_COMPLETED_MOUSE_OVER);
        img = loadImage(imgPath + level8completeButtonButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, LEVEL_8_X, LEVEL_8_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_8_COMPLETE_BUTTON_TYPE, s);
        
        String level9Button = props.getProperty(PathXPropertyType.IMAGE_MAP_LOCKED);
        sT = new SpriteType(LEVEL_9_BUTTON_TYPE);
        img = loadImage(imgPath + level9Button);
        sT.addState(VISIBLE_STATE.toString(), img);
        String level9ButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_MAP_LOCKED_MOUSE_OVER);
        img = loadImage(imgPath + level9ButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, LEVEL_9_X, LEVEL_9_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_9_BUTTON_TYPE, s);
        
        String level9availbleButton = props.getProperty(PathXPropertyType.IMAGE_MAP_AVAILABLE);
        sT = new SpriteType(LEVEL_9_AVAILABLE_BUTTON_TYPE);
        img = loadImage(imgPath + level9availbleButton);
        sT.addState(VISIBLE_STATE.toString(), img);
        String level9availbleButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_MAP_AVAILABLE_MOUSE_OVER);
        img = loadImage(imgPath + level9availbleButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, LEVEL_9_X, LEVEL_9_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_9_AVAILABLE_BUTTON_TYPE, s);
        
        String level9completeButton = props.getProperty(PathXPropertyType.IMAGE_MAP_COMPLETED);
        sT = new SpriteType(LEVEL_9_COMPLETE_BUTTON_TYPE);
        img = loadImage(imgPath + level9completeButton);
        sT.addState(VISIBLE_STATE.toString(), img);
        String level9completeButtonButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_MAP_COMPLETED_MOUSE_OVER);
        img = loadImage(imgPath + level9completeButtonButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, LEVEL_9_X, LEVEL_9_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_9_COMPLETE_BUTTON_TYPE, s);
        
        
        
        String level10Button = props.getProperty(PathXPropertyType.IMAGE_MAP_LOCKED);
        sT = new SpriteType(LEVEL_10_BUTTON_TYPE);
        img = loadImage(imgPath + level10Button);
        sT.addState(VISIBLE_STATE.toString(), img);
        String level10ButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_MAP_LOCKED_MOUSE_OVER);
        img = loadImage(imgPath + level10ButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, LEVEL_10_X, LEVEL_10_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_10_BUTTON_TYPE, s);
        
        
        String level10availbleButton = props.getProperty(PathXPropertyType.IMAGE_MAP_AVAILABLE);
        sT = new SpriteType(LEVEL_10_AVAILABLE_BUTTON_TYPE);
        img = loadImage(imgPath + level10availbleButton);
        sT.addState(VISIBLE_STATE.toString(), img);
        String level10availbleButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_MAP_AVAILABLE_MOUSE_OVER);
        img = loadImage(imgPath + level10availbleButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, LEVEL_10_X, LEVEL_10_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_10_AVAILABLE_BUTTON_TYPE, s);
        
        String level10completeButton = props.getProperty(PathXPropertyType.IMAGE_MAP_COMPLETED);
        sT = new SpriteType(LEVEL_10_COMPLETE_BUTTON_TYPE);
        img = loadImage(imgPath + level10completeButton);
        sT.addState(VISIBLE_STATE.toString(), img);
        String level10completeButtonButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_MAP_COMPLETED_MOUSE_OVER);
        img = loadImage(imgPath + level10completeButtonButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, LEVEL_10_X, LEVEL_10_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_10_COMPLETE_BUTTON_TYPE, s);
        
        String level11Button = props.getProperty(PathXPropertyType.IMAGE_MAP_LOCKED);
        sT = new SpriteType(LEVEL_11_BUTTON_TYPE);
        img = loadImage(imgPath + level11Button);
        sT.addState(VISIBLE_STATE.toString(), img);
        String level11ButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_MAP_LOCKED_MOUSE_OVER);
        img = loadImage(imgPath + level11ButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, LEVEL_11_X, LEVEL_11_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_11_BUTTON_TYPE, s);
        
        String level11availbleButton = props.getProperty(PathXPropertyType.IMAGE_MAP_AVAILABLE);
        sT = new SpriteType(LEVEL_11_AVAILABLE_BUTTON_TYPE);
        img = loadImage(imgPath + level11availbleButton);
        sT.addState(VISIBLE_STATE.toString(), img);
        String level11availbleButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_MAP_AVAILABLE_MOUSE_OVER);
        img = loadImage(imgPath + level11availbleButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, LEVEL_11_X, LEVEL_11_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_11_AVAILABLE_BUTTON_TYPE, s);
        
        String level11completeButton = props.getProperty(PathXPropertyType.IMAGE_MAP_COMPLETED);
        sT = new SpriteType(LEVEL_11_COMPLETE_BUTTON_TYPE);
        img = loadImage(imgPath + level11completeButton);
        sT.addState(VISIBLE_STATE.toString(), img);
        String level11completeButtonButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_MAP_COMPLETED_MOUSE_OVER);
        img = loadImage(imgPath + level11completeButtonButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, LEVEL_11_X, LEVEL_11_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_11_COMPLETE_BUTTON_TYPE, s);
        
        String level12Button = props.getProperty(PathXPropertyType.IMAGE_MAP_LOCKED);
        sT = new SpriteType(LEVEL_12_BUTTON_TYPE);
        img = loadImage(imgPath + level12Button);
        sT.addState(VISIBLE_STATE.toString(), img);
        String level12ButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_MAP_LOCKED_MOUSE_OVER);
        img = loadImage(imgPath + level12ButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, LEVEL_12_X, LEVEL_12_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_12_BUTTON_TYPE, s);
        
        String level12availbleButton = props.getProperty(PathXPropertyType.IMAGE_MAP_AVAILABLE);
        sT = new SpriteType(LEVEL_12_AVAILABLE_BUTTON_TYPE);
        img = loadImage(imgPath + level12availbleButton);
        sT.addState(VISIBLE_STATE.toString(), img);
        String level12availbleButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_MAP_AVAILABLE_MOUSE_OVER);
        img = loadImage(imgPath + level12availbleButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, LEVEL_12_X, LEVEL_12_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_12_AVAILABLE_BUTTON_TYPE, s);
        
        String level12completeButton = props.getProperty(PathXPropertyType.IMAGE_MAP_COMPLETED);
        sT = new SpriteType(LEVEL_12_COMPLETE_BUTTON_TYPE);
        img = loadImage(imgPath + level12completeButton);
        sT.addState(VISIBLE_STATE.toString(), img);
        String level12completeButtonButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_MAP_COMPLETED_MOUSE_OVER);
        img = loadImage(imgPath + level12completeButtonButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, LEVEL_12_X, LEVEL_12_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_12_COMPLETE_BUTTON_TYPE, s);
        
        String level13Button = props.getProperty(PathXPropertyType.IMAGE_MAP_LOCKED);
        sT = new SpriteType(LEVEL_13_BUTTON_TYPE);
        img = loadImage(imgPath + level13Button);
        sT.addState(VISIBLE_STATE.toString(), img);
        String level13ButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_MAP_LOCKED_MOUSE_OVER);
        img = loadImage(imgPath + level13ButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, LEVEL_13_X, LEVEL_13_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_13_BUTTON_TYPE, s);
        
        String level13availbleButton = props.getProperty(PathXPropertyType.IMAGE_MAP_AVAILABLE);
        sT = new SpriteType(LEVEL_13_AVAILABLE_BUTTON_TYPE);
        img = loadImage(imgPath + level13availbleButton);
        sT.addState(VISIBLE_STATE.toString(), img);
        String level13availbleButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_MAP_AVAILABLE_MOUSE_OVER);
        img = loadImage(imgPath + level13availbleButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, LEVEL_13_X, LEVEL_13_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_13_AVAILABLE_BUTTON_TYPE, s);
        
        String level13completeButton = props.getProperty(PathXPropertyType.IMAGE_MAP_COMPLETED);
        sT = new SpriteType(LEVEL_13_COMPLETE_BUTTON_TYPE);
        img = loadImage(imgPath + level13completeButton);
        sT.addState(VISIBLE_STATE.toString(), img);
        String level13completeButtonButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_MAP_COMPLETED_MOUSE_OVER);
        img = loadImage(imgPath + level13completeButtonButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, LEVEL_13_X, LEVEL_13_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_13_COMPLETE_BUTTON_TYPE, s);
        
        String level14Button = props.getProperty(PathXPropertyType.IMAGE_MAP_LOCKED);
        sT = new SpriteType(LEVEL_14_BUTTON_TYPE);
        img = loadImage(imgPath + level14Button);
        sT.addState(VISIBLE_STATE.toString(), img);
        String level14ButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_MAP_LOCKED_MOUSE_OVER);
        img = loadImage(imgPath + level14ButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, LEVEL_14_X, LEVEL_14_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_14_BUTTON_TYPE, s);
        
                
        String level14availbleButton = props.getProperty(PathXPropertyType.IMAGE_MAP_AVAILABLE);
        sT = new SpriteType(LEVEL_14_AVAILABLE_BUTTON_TYPE);
        img = loadImage(imgPath + level14availbleButton);
        sT.addState(VISIBLE_STATE.toString(), img);
        String level14availbleButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_MAP_AVAILABLE_MOUSE_OVER);
        img = loadImage(imgPath + level14availbleButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, LEVEL_14_X, LEVEL_14_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_14_AVAILABLE_BUTTON_TYPE, s);
        
        String level14completeButton = props.getProperty(PathXPropertyType.IMAGE_MAP_COMPLETED);
        sT = new SpriteType(LEVEL_14_COMPLETE_BUTTON_TYPE);
        img = loadImage(imgPath + level14completeButton);
        sT.addState(VISIBLE_STATE.toString(), img);
        String level14completeButtonButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_MAP_COMPLETED_MOUSE_OVER);
        img = loadImage(imgPath + level14completeButtonButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, LEVEL_14_X, LEVEL_14_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_14_COMPLETE_BUTTON_TYPE, s);
        
        String level15Button = props.getProperty(PathXPropertyType.IMAGE_MAP_LOCKED);
        sT = new SpriteType(LEVEL_15_BUTTON_TYPE);
        img = loadImage(imgPath + level15Button);
        sT.addState(VISIBLE_STATE.toString(), img);
        String level15ButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_MAP_LOCKED_MOUSE_OVER);
        img = loadImage(imgPath + level15ButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, LEVEL_15_X, LEVEL_15_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_15_BUTTON_TYPE, s);
        
        String level15availbleButton = props.getProperty(PathXPropertyType.IMAGE_MAP_AVAILABLE);
        sT = new SpriteType(LEVEL_15_AVAILABLE_BUTTON_TYPE);
        img = loadImage(imgPath + level15availbleButton);
        sT.addState(VISIBLE_STATE.toString(), img);
        String level15availbleButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_MAP_AVAILABLE_MOUSE_OVER);
        img = loadImage(imgPath + level15availbleButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, LEVEL_15_X, LEVEL_15_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_15_AVAILABLE_BUTTON_TYPE, s);
        
        String level15completeButton = props.getProperty(PathXPropertyType.IMAGE_MAP_COMPLETED);
        sT = new SpriteType(LEVEL_15_COMPLETE_BUTTON_TYPE);
        img = loadImage(imgPath + level15completeButton);
        sT.addState(VISIBLE_STATE.toString(), img);
        String level15completeButtonButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_MAP_COMPLETED_MOUSE_OVER);
        img = loadImage(imgPath + level15completeButtonButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, LEVEL_15_X, LEVEL_15_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_15_COMPLETE_BUTTON_TYPE, s);
        
        String level16Button = props.getProperty(PathXPropertyType.IMAGE_MAP_LOCKED);
        sT = new SpriteType(LEVEL_16_BUTTON_TYPE);
        img = loadImage(imgPath + level16Button);
        sT.addState(VISIBLE_STATE.toString(), img);
        String level16ButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_MAP_LOCKED_MOUSE_OVER);
        img = loadImage(imgPath + level16ButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, LEVEL_16_X, LEVEL_16_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_16_BUTTON_TYPE, s);
        
        String level16availbleButton = props.getProperty(PathXPropertyType.IMAGE_MAP_AVAILABLE);
        sT = new SpriteType(LEVEL_16_AVAILABLE_BUTTON_TYPE);
        img = loadImage(imgPath + level16availbleButton);
        sT.addState(VISIBLE_STATE.toString(), img);
        String level16availbleButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_MAP_AVAILABLE_MOUSE_OVER);
        img = loadImage(imgPath + level16availbleButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, LEVEL_16_X, LEVEL_16_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_16_AVAILABLE_BUTTON_TYPE, s);
        
        String level16completeButton = props.getProperty(PathXPropertyType.IMAGE_MAP_COMPLETED);
        sT = new SpriteType(LEVEL_16_COMPLETE_BUTTON_TYPE);
        img = loadImage(imgPath + level16completeButton);
        sT.addState(VISIBLE_STATE.toString(), img);
        String level16completeButtonButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_MAP_COMPLETED_MOUSE_OVER);
        img = loadImage(imgPath + level16completeButtonButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, LEVEL_16_X, LEVEL_16_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_16_COMPLETE_BUTTON_TYPE, s);
        
        String level17Button = props.getProperty(PathXPropertyType.IMAGE_MAP_LOCKED);
        sT = new SpriteType(LEVEL_17_BUTTON_TYPE);
        img = loadImage(imgPath + level17Button);
        sT.addState(VISIBLE_STATE.toString(), img);
        String level17ButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_MAP_LOCKED_MOUSE_OVER);
        img = loadImage(imgPath + level17ButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, LEVEL_17_X, LEVEL_17_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_17_BUTTON_TYPE, s);
        
        String level17availbleButton = props.getProperty(PathXPropertyType.IMAGE_MAP_AVAILABLE);
        sT = new SpriteType(LEVEL_17_AVAILABLE_BUTTON_TYPE);
        img = loadImage(imgPath + level17availbleButton);
        sT.addState(VISIBLE_STATE.toString(), img);
        String level17availbleButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_MAP_AVAILABLE_MOUSE_OVER);
        img = loadImage(imgPath + level17availbleButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, LEVEL_17_X, LEVEL_17_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_17_AVAILABLE_BUTTON_TYPE, s);
        
        String level17completeButton = props.getProperty(PathXPropertyType.IMAGE_MAP_COMPLETED);
        sT = new SpriteType(LEVEL_17_COMPLETE_BUTTON_TYPE);
        img = loadImage(imgPath + level17completeButton);
        sT.addState(VISIBLE_STATE.toString(), img);
        String level17completeButtonButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_MAP_COMPLETED_MOUSE_OVER);
        img = loadImage(imgPath + level17completeButtonButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, LEVEL_17_X, LEVEL_17_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_17_COMPLETE_BUTTON_TYPE, s);
        
        String level18Button = props.getProperty(PathXPropertyType.IMAGE_MAP_LOCKED);
        sT = new SpriteType(LEVEL_18_BUTTON_TYPE);
        img = loadImage(imgPath + level18Button);
        sT.addState(VISIBLE_STATE.toString(), img);
        String level18ButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_MAP_LOCKED_MOUSE_OVER);
        img = loadImage(imgPath + level18ButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, LEVEL_18_X, LEVEL_18_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_18_BUTTON_TYPE, s);
        
        String level18availbleButton = props.getProperty(PathXPropertyType.IMAGE_MAP_AVAILABLE);
        sT = new SpriteType(LEVEL_18_AVAILABLE_BUTTON_TYPE);
        img = loadImage(imgPath + level18availbleButton);
        sT.addState(VISIBLE_STATE.toString(), img);
        String level18availbleButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_MAP_AVAILABLE_MOUSE_OVER);
        img = loadImage(imgPath + level18availbleButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, LEVEL_18_X, LEVEL_18_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_18_AVAILABLE_BUTTON_TYPE, s);
        
        String level18completeButton = props.getProperty(PathXPropertyType.IMAGE_MAP_COMPLETED);
        sT = new SpriteType(LEVEL_18_COMPLETE_BUTTON_TYPE);
        img = loadImage(imgPath + level18completeButton);
        sT.addState(VISIBLE_STATE.toString(), img);
        String level18completeButtonButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_MAP_COMPLETED_MOUSE_OVER);
        img = loadImage(imgPath + level18completeButtonButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, LEVEL_18_X, LEVEL_18_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_18_COMPLETE_BUTTON_TYPE, s);
        
        String level19Button = props.getProperty(PathXPropertyType.IMAGE_MAP_LOCKED);
        sT = new SpriteType(LEVEL_19_BUTTON_TYPE);
        img = loadImage(imgPath + level19Button);
        sT.addState(VISIBLE_STATE.toString(), img);
        String level19ButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_MAP_LOCKED_MOUSE_OVER);
        img = loadImage(imgPath + level19ButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, LEVEL_19_X, LEVEL_19_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_19_BUTTON_TYPE, s);
        
        String level19availbleButton = props.getProperty(PathXPropertyType.IMAGE_MAP_AVAILABLE);
        sT = new SpriteType(LEVEL_19_AVAILABLE_BUTTON_TYPE);
        img = loadImage(imgPath + level19availbleButton);
        sT.addState(VISIBLE_STATE.toString(), img);
        String level19availbleButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_MAP_AVAILABLE_MOUSE_OVER);
        img = loadImage(imgPath + level19availbleButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, LEVEL_19_X, LEVEL_19_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_19_AVAILABLE_BUTTON_TYPE, s);
        
        String level19completeButton = props.getProperty(PathXPropertyType.IMAGE_MAP_COMPLETED);
        sT = new SpriteType(LEVEL_19_COMPLETE_BUTTON_TYPE);
        img = loadImage(imgPath + level19completeButton);
        sT.addState(VISIBLE_STATE.toString(), img);
        String level19completeButtonButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_MAP_COMPLETED_MOUSE_OVER);
        img = loadImage(imgPath + level19completeButtonButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, LEVEL_19_X, LEVEL_19_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_19_COMPLETE_BUTTON_TYPE, s);
        
        String level20Button = props.getProperty(PathXPropertyType.IMAGE_MAP_LOCKED);
        sT = new SpriteType(LEVEL_20_BUTTON_TYPE);
        img = loadImage(imgPath + level20Button);
        sT.addState(VISIBLE_STATE.toString(), img);
        String level20ButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_MAP_LOCKED_MOUSE_OVER);
        img = loadImage(imgPath + level20ButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, LEVEL_20_X, LEVEL_20_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_20_BUTTON_TYPE, s);
        
        String level20availbleButton = props.getProperty(PathXPropertyType.IMAGE_MAP_AVAILABLE);
        sT = new SpriteType(LEVEL_20_AVAILABLE_BUTTON_TYPE);
        img = loadImage(imgPath + level20availbleButton);
        sT.addState(VISIBLE_STATE.toString(), img);
        String level20availbleButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_MAP_AVAILABLE_MOUSE_OVER);
        img = loadImage(imgPath + level20availbleButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, LEVEL_20_X, LEVEL_20_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_20_AVAILABLE_BUTTON_TYPE, s);
        
        String level20completeButton = props.getProperty(PathXPropertyType.IMAGE_MAP_COMPLETED);
        sT = new SpriteType(LEVEL_20_COMPLETE_BUTTON_TYPE);
        img = loadImage(imgPath + level20completeButton);
        sT.addState(VISIBLE_STATE.toString(), img);
        String level20completeButtonButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_MAP_COMPLETED_MOUSE_OVER);
        img = loadImage(imgPath + level20completeButtonButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, LEVEL_20_X, LEVEL_20_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(LEVEL_20_COMPLETE_BUTTON_TYPE, s);
        
        String pausebutton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_PAUSE);
        sT = new SpriteType(PAUSE_BUTTON_TYPE);
        img = loadImage(imgPath + pausebutton);
        sT.addState(VISIBLE_STATE.toString(), img);
        String pauseButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_PAUSE_MOUSE_OVER);
        img = loadImage(imgPath + pauseButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, PAUSE_X, PAUSE_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(PAUSE_BUTTON_TYPE, s);
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

        String winDialog = props.getProperty(PathXPropertyType.IMAGE_DIALOG_WIN_LEVEL);
        sT = new SpriteType(LEVEL_COMPLETE_DIALOG_TYPE);
        img = loadImage(imgPath + winDialog);
        sT.addState(PathXCarState.VISIBLE_STATE.toString(), img);
        //change around maybe
        ex = (WINDOW_WIDTH / 2 - 200);
        why = (WINDOW_HEIGHT / 2 - 200);
        s = new Sprite(sT, ex, why, 0, 0, INVISIBLE_STATE.toString());
        guiDialogs.put(LEVEL_COMPLETE_DIALOG_TYPE, s);
        
        String loseDialog = props.getProperty(PathXPropertyType.IMAGE_DIALOG_LOSE_LEVEL);
        sT = new SpriteType(LEVEL_FAIL_DIALOG_TYPE);
        img = loadImage(imgPath + loseDialog);
        sT.addState(PathXCarState.VISIBLE_STATE.toString(), img);
        //change around maybe
        ex = (WINDOW_WIDTH / 2 - 200);
        why = (WINDOW_HEIGHT / 2 - 200);
        s = new Sprite(sT, ex, why, 0, 0, INVISIBLE_STATE.toString());
        guiDialogs.put(LEVEL_FAIL_DIALOG_TYPE, s);
        
        String closeLevelButton = props.getProperty(PathXPropertyType.LEVEL_CLOSE_BUTTON);
        sT = new SpriteType(CLOSE_DIALOG_BUTTON_TYPE);
        img = loadImage(imgPath + closeLevelButton);
        sT.addState(VISIBLE_STATE.toString(), img);
        String closeLevelButtonMouseOverButton = props.getProperty(PathXPropertyType.LEVEL_CLOSE_BUTTON_MOUSE_OVER);
        img = loadImage(imgPath + closeLevelButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, LEVEL_CLOSE_X, LEVEL_CLOSE_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(CLOSE_DIALOG_BUTTON_TYPE, s);

        //RENDER THE SPECIALS
        String greenLightButton = props.getProperty(PathXPropertyType.IMAGE_SPECIAL_GREEN_LIGHT);
        sT = new SpriteType(SPECIAL_GREENLIGHT_TYPE);
        img = loadImage(imgPath + greenLightButton);
        sT.addState(VISIBLE_STATE.toString(), img);
        String greenLightButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_SPECIAL_GREEN_LIGHT_MOUSE_OVER);
        img = loadImage(imgPath + greenLightButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, GREEN_LIGHT_X,GREEN_LIGHT_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(SPECIAL_GREENLIGHT_TYPE, s);
        
        String redLightButton = props.getProperty(PathXPropertyType.IMAGE_SPECIAL_RED_LIGHT);
        sT = new SpriteType(SPECIAL_REDLIGHT_TYPE);
        img = loadImage(imgPath + redLightButton);
        sT.addState(VISIBLE_STATE.toString(), img);
        String redLightButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_SPECIAL_RED_LIGHT_MOUSE_OVER);
        img = loadImage(imgPath + redLightButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, RED_LIGHT_X,RED_LIGHT_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(SPECIAL_REDLIGHT_TYPE, s);
        
        String freezeTimeButton = props.getProperty(PathXPropertyType.IMAGE_SPECIAL_FREEZE_TIME);
        sT = new SpriteType(SPECIAL_FREEZETIME_TYPE);
        img = loadImage(imgPath + freezeTimeButton);
        sT.addState(VISIBLE_STATE.toString(), img);
        String freezeTimeButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_SPECIAL_FREEZE_TIME_MOUSE_OVER);
        img = loadImage(imgPath + freezeTimeButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, FREEZE_TIME_X,FREEZE_TIME_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(SPECIAL_FREEZETIME_TYPE, s);
        
        String decreaseButton = props.getProperty(PathXPropertyType.IMAGE_SPECIAL_DECREASE_SPEED);
        sT = new SpriteType(SPECIAL_DECREASE_SPEED_TYPE);
        img = loadImage(imgPath + decreaseButton);
        sT.addState(VISIBLE_STATE.toString(), img);
        String decreaseButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_SPECIAL_DECREASE_SPEED_MOUSE_OVER);
        img = loadImage(imgPath + decreaseButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, DECREASE_SPEED_X,DECREASE_SPEED_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(SPECIAL_DECREASE_SPEED_TYPE, s);
        
        String increaseButton = props.getProperty(PathXPropertyType.IMAGE_SPECIAL_INCREASE_SPEED);
        sT = new SpriteType(SPECIAL_INCREASE_SPEED_TYPE);
        img = loadImage(imgPath + increaseButton);
        sT.addState(VISIBLE_STATE.toString(), img);
        String increaseButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_SPECIAL_INCREASE_SPEED_MOUSE_OVER);
        img = loadImage(imgPath + increaseButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, INCREASE_SPEED_X,INCREASE_SPEED_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(SPECIAL_INCREASE_SPEED_TYPE, s);
        
        String increasePlayerButton = props.getProperty(PathXPropertyType.IMAGE_SPECIAL_INCREASE_PLAYER_SPEED);
        sT = new SpriteType(SPECIAL_PLAYER_INCREASE_SPEED_TYPE);
        img = loadImage(imgPath + increasePlayerButton);
        sT.addState(VISIBLE_STATE.toString(), img);
        String increasePlayerButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_SPECIAL_INCREASE_PLAYER_SPEED_MOUSE_OVER);
        img = loadImage(imgPath + increasePlayerButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, INCREASE_PLAYER_SPEED_X,INCREASE_PLAYER_SPEED_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(SPECIAL_PLAYER_INCREASE_SPEED_TYPE, s);
        
        String flatTireButton = props.getProperty(PathXPropertyType.IMAGE_SPECIAL_FLAT_TIRE);
        sT = new SpriteType(SPECIAL_FLAT_TIRE);
        img = loadImage(imgPath + flatTireButton);
        sT.addState(VISIBLE_STATE.toString(), img);
        String flatTireButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_SPECIAL_FLAT_TIRE_MOUSE_OVER);
        img = loadImage(imgPath + flatTireButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, FLAT_TIRE_X,FLAT_TIRE_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(SPECIAL_FLAT_TIRE, s);
        
        String emptyGasButton = props.getProperty(PathXPropertyType.IMAGE_SPECIAL_EMPTY_GAS);
        sT = new SpriteType(SPECIAL_EMPTY_GAS);
        img = loadImage(imgPath + emptyGasButton);
        sT.addState(VISIBLE_STATE.toString(), img);
        String emptyGasButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_SPECIAL_EMPTY_GAS_MOUSE_OVER);
        img = loadImage(imgPath + emptyGasButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, EMPTY_GAS_X,EMPTY_GAS_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(SPECIAL_EMPTY_GAS, s);
        
        String closeroadButton = props.getProperty(PathXPropertyType.IMAGE_SPECIAL_CLOSE_ROAD);
        sT = new SpriteType(SPECIAL_CLOSE_ROAD);
        img = loadImage(imgPath + closeroadButton);
        sT.addState(VISIBLE_STATE.toString(), img);
        String closeroadButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_SPECIAL_CLOSE_ROAD_MOUSE_OVER);
        img = loadImage(imgPath + closeroadButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, CLOSE_ROAD_X,CLOSE_ROAD_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(SPECIAL_CLOSE_ROAD, s);
        
        String closeintersectionButton = props.getProperty(PathXPropertyType.IMAGE_SPECIAL_CLOSE_INTERSECTION);
        sT = new SpriteType(SPECIAL_CLOSE_INTERSECTION);
        img = loadImage(imgPath + closeintersectionButton);
        sT.addState(VISIBLE_STATE.toString(), img);
        String closeintersectionButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_SPECIAL_CLOSE_INTERSECTION_MOUSE_OVER);
        img = loadImage(imgPath + closeintersectionButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, CLOSE_INTERSECTION_X,CLOSE_INTERSECTION_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(SPECIAL_CLOSE_INTERSECTION, s);
        
        String openintersectionButton = props.getProperty(PathXPropertyType.IMAGE_SPECIAL_OPEN_INTERSECTION);
        sT = new SpriteType(SPECIAL_OPEN_INTERSECTION);
        img = loadImage(imgPath + openintersectionButton);
        sT.addState(VISIBLE_STATE.toString(), img);
        String openintersectionButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_SPECIAL_OPEN_INTERSECTION_MOUSE_OVER);
        img = loadImage(imgPath + openintersectionButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, OPEN_INTERSECTION_X,OPEN_INTERSECTION_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(SPECIAL_OPEN_INTERSECTION, s);
        
        String stealButton = props.getProperty(PathXPropertyType.IMAGE_SPECIAL_STEAL);
        sT = new SpriteType(SPECIAL_STEAL);
        img = loadImage(imgPath + stealButton);
        sT.addState(VISIBLE_STATE.toString(), img);
        String stealButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_SPECIAL_STEAL_MOUSE_OVER);
        img = loadImage(imgPath + stealButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, STEAL_X,STEAL_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(SPECIAL_STEAL, s);
        
        String mindcontrolButton = props.getProperty(PathXPropertyType.IMAGE_SPECIAL_MIND_CONTROL);
        sT = new SpriteType(SPECIAL_MIND_CONTROL);
        img = loadImage(imgPath + mindcontrolButton);
        sT.addState(VISIBLE_STATE.toString(), img);
        String mindcontrolButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_SPECIAL_MIND_CONTROL_MOUSE_OVER);
        img = loadImage(imgPath + mindcontrolButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, MIND_CONTROL_X,MIND_CONTROL_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(SPECIAL_MIND_CONTROL, s);
        
        String intangButton = props.getProperty(PathXPropertyType.IMAGE_SPECIAL_INTANG);
        sT = new SpriteType(SPECIAL_INTANG);
        img = loadImage(imgPath + intangButton);
        sT.addState(VISIBLE_STATE.toString(), img);
        String intangButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_SPECIAL_INTANG_MOUSE_OVER);
        img = loadImage(imgPath + intangButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, INTANG_X,INTANG_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(SPECIAL_INTANG, s);
        
        String mtButton = props.getProperty(PathXPropertyType.IMAGE_SPECIAL_MINDLESS_TERROR);
        sT = new SpriteType(SPECIAL_MINDLESS_TERROR);
        img = loadImage(imgPath + mtButton);
        sT.addState(VISIBLE_STATE.toString(), img);
        String mtButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_SPECIAL_MINDLESS_TERROR_MOUSE_OVER);
        img = loadImage(imgPath + mtButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, MIND_TERROR_X,MIND_TERROR_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(SPECIAL_MINDLESS_TERROR, s);
        
        String flyButton = props.getProperty(PathXPropertyType.IMAGE_SPECIAL_FLYING);
        sT = new SpriteType(SPECIAL_FLYING);
        img = loadImage(imgPath + flyButton);
        sT.addState(VISIBLE_STATE.toString(), img);
        String flyButtonMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_SPECIAL_FLYING_MOUSE_OVER);
        img = loadImage(imgPath + flyButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, FLYING_X,FLYING_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(SPECIAL_FLYING, s);
        
        String invinButton = props.getProperty(PathXPropertyType.IMAGE_SPECIAL_INVINCIBILITY);
        sT = new SpriteType(SPECIAL_INVINCIBILITY);
        img = loadImage(imgPath + invinButton);
        sT.addState(VISIBLE_STATE.toString(), img);
        String invinButtonMouseOverButton = props.getProperty(PathXPropertyType. IMAGE_SPECIAL_INVINCIBILITY_MOUSE_OVER);
        img = loadImage(imgPath + invinButtonMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, INVINC_X,INVINC_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(SPECIAL_INVINCIBILITY, s);
        
        String playAgainButton = props.getProperty(PathXPropertyType.IMAGE_TRY_AGAIN);
        sT = new SpriteType(TRY_AGAIN_TYPE);
        img = loadImage(imgPath + playAgainButton);
        sT.addState(VISIBLE_STATE.toString(), img);
        String playAgainMouseOver= props.getProperty(PathXPropertyType.IMAGE_TRY_AGAIN_MOUSE_OVER);
        img = loadImage(imgPath + playAgainMouseOver);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, TRY_AGAIN_X, TRY_AGAIN_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(TRY_AGAIN_TYPE, s);
        
        String quitlvlButton = props.getProperty(PathXPropertyType.IMAGE_QUIT_LEVEL);
        sT = new SpriteType(QUIT_LEVEL_TYPE);
        img = loadImage(imgPath + quitlvlButton);
        sT.addState(VISIBLE_STATE.toString(), img);
        String quitlvlMouseOver= props.getProperty(PathXPropertyType.IMAGE_QUIT_LEVEL_MOUSE_OVER);
        img = loadImage(imgPath + quitlvlMouseOver);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, QUIT_LEVEL_X, QUIT_LEVEL_Y, 0, 0, INVISIBLE_STATE.toString());
        guiButtons.put(QUIT_LEVEL_TYPE, s);
        
        String player = props.getProperty(PathXPropertyType.IMAGE_PLAYER);
        sT = new SpriteType(PLAYER_TYPE);
        img = loadImage(imgPath + player);
        sT.addState(VISIBLE_STATE.toString(), img);
        String playerMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_PLAYER);
        img = loadImage(imgPath + playerMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, PLAYER_X, PLAYER_Y, 0, 0, INVISIBLE_STATE.toString());
        guiDecor.put(PLAYER_TYPE, s);
        
        String police = props.getProperty(PathXPropertyType.IMAGE_POLICE);
        sT = new SpriteType(POLICE_TYPE);
        img = loadImage(imgPath + police);
        sT.addState(VISIBLE_STATE.toString(), img);
        String policeMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_POLICE);
        img = loadImage(imgPath + policeMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, PLAYER_X, PLAYER_Y, 0, 0, INVISIBLE_STATE.toString());
        guiDecor.put(POLICE_TYPE, s);
        
        String police2 = props.getProperty(PathXPropertyType.IMAGE_POLICE);
        sT = new SpriteType(POLICE_TYPE_2);
        img = loadImage(imgPath + police2);
        sT.addState(VISIBLE_STATE.toString(), img);
        String police2MouseOverButton = props.getProperty(PathXPropertyType.IMAGE_POLICE);
        img = loadImage(imgPath + police2MouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, PLAYER_X, PLAYER_Y, 0, 0, INVISIBLE_STATE.toString());
        guiDecor.put(POLICE_TYPE_2, s);
        
        String police3 = props.getProperty(PathXPropertyType.IMAGE_POLICE);
        sT = new SpriteType(POLICE_TYPE_3);
        img = loadImage(imgPath + police3);
        sT.addState(VISIBLE_STATE.toString(), img);
        String police3MouseOverButton = props.getProperty(PathXPropertyType.IMAGE_POLICE);
        img = loadImage(imgPath + police3MouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, PLAYER_X, PLAYER_Y, 0, 0, INVISIBLE_STATE.toString());
        guiDecor.put(POLICE_TYPE_3, s);
        
        String police4 = props.getProperty(PathXPropertyType.IMAGE_POLICE);
        sT = new SpriteType(POLICE_TYPE_4);
        img = loadImage(imgPath + police4);
        sT.addState(VISIBLE_STATE.toString(), img);
        String police4MouseOverButton = props.getProperty(PathXPropertyType.IMAGE_POLICE);
        img = loadImage(imgPath + police4MouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, PLAYER_X, PLAYER_Y, 0, 0, INVISIBLE_STATE.toString());
        guiDecor.put(POLICE_TYPE_4, s);
        
        String police5 = props.getProperty(PathXPropertyType.IMAGE_POLICE);
        sT = new SpriteType(POLICE_TYPE_5);
        img = loadImage(imgPath + police5);
        sT.addState(VISIBLE_STATE.toString(), img);
        String police5MouseOverButton = props.getProperty(PathXPropertyType.IMAGE_POLICE);
        img = loadImage(imgPath + police5MouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, PLAYER_X, PLAYER_Y, 0, 0, INVISIBLE_STATE.toString());
        guiDecor.put(POLICE_TYPE_5, s);
        
        String police6 = props.getProperty(PathXPropertyType.IMAGE_POLICE);
        sT = new SpriteType(POLICE_TYPE_6);
        img = loadImage(imgPath + police6);
        sT.addState(VISIBLE_STATE.toString(), img);
        String police6MouseOverButton = props.getProperty(PathXPropertyType.IMAGE_POLICE);
        img = loadImage(imgPath + police6MouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, PLAYER_X, PLAYER_Y, 0, 0, INVISIBLE_STATE.toString());
        guiDecor.put(POLICE_TYPE_6, s);
        
        String bandit1 = props.getProperty(PathXPropertyType.IMAGE_BANDIT);
        sT = new SpriteType(BANDIT_TYPE);
        img = loadImage(imgPath + bandit1);
        sT.addState(VISIBLE_STATE.toString(), img);
        String bandit1MouseOverButton = props.getProperty(PathXPropertyType.IMAGE_BANDIT);
        img = loadImage(imgPath + bandit1MouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, PLAYER_X, PLAYER_Y, 0, 0, INVISIBLE_STATE.toString());
        guiDecor.put(BANDIT_TYPE, s);
        
        String bandit2 = props.getProperty(PathXPropertyType.IMAGE_BANDIT);
        sT = new SpriteType(BANDIT_TYPE_2);
        img = loadImage(imgPath + bandit2);
        sT.addState(VISIBLE_STATE.toString(), img);
        String bandit2MouseOverButton = props.getProperty(PathXPropertyType.IMAGE_BANDIT);
        img = loadImage(imgPath + bandit2MouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, PLAYER_X, PLAYER_Y, 0, 0, INVISIBLE_STATE.toString());
        guiDecor.put(BANDIT_TYPE_2, s);
        
        String bandit3 = props.getProperty(PathXPropertyType.IMAGE_BANDIT);
        sT = new SpriteType(BANDIT_TYPE_3);
        img = loadImage(imgPath + bandit3);
        sT.addState(VISIBLE_STATE.toString(), img);
        String bandit3MouseOverButton = props.getProperty(PathXPropertyType.IMAGE_BANDIT);
        img = loadImage(imgPath + bandit3MouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, PLAYER_X, PLAYER_Y, 0, 0, INVISIBLE_STATE.toString());
        guiDecor.put(BANDIT_TYPE_3, s);
        
        String bandit4 = props.getProperty(PathXPropertyType.IMAGE_BANDIT);
        sT = new SpriteType(BANDIT_TYPE_4);
        img = loadImage(imgPath + bandit4);
        sT.addState(VISIBLE_STATE.toString(), img);
        String bandit4MouseOverButton = props.getProperty(PathXPropertyType.IMAGE_BANDIT);
        img = loadImage(imgPath + bandit4MouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, PLAYER_X, PLAYER_Y, 0, 0, INVISIBLE_STATE.toString());
        guiDecor.put(BANDIT_TYPE_4, s);
        
        String bandit5 = props.getProperty(PathXPropertyType.IMAGE_BANDIT);
        sT = new SpriteType(BANDIT_TYPE_5);
        img = loadImage(imgPath + bandit5);
        sT.addState(VISIBLE_STATE.toString(), img);
        String bandit5MouseOverButton = props.getProperty(PathXPropertyType.IMAGE_BANDIT);
        img = loadImage(imgPath + bandit5MouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, PLAYER_X, PLAYER_Y, 0, 0, INVISIBLE_STATE.toString());
        guiDecor.put(BANDIT_TYPE_5, s);
        
        String bandit6 = props.getProperty(PathXPropertyType.IMAGE_BANDIT);
        sT = new SpriteType(BANDIT_TYPE_6);
        img = loadImage(imgPath + bandit6);
        sT.addState(VISIBLE_STATE.toString(), img);
        String bandit6MouseOverButton = props.getProperty(PathXPropertyType.IMAGE_BANDIT);
        img = loadImage(imgPath + bandit6MouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, PLAYER_X, PLAYER_Y, 0, 0, INVISIBLE_STATE.toString());
        guiDecor.put(BANDIT_TYPE_6, s);
        
        String zombie1 = props.getProperty(PathXPropertyType.IMAGE_ZOMBIE);
        sT = new SpriteType(ZOMBIE_TYPE);
        img = loadImage(imgPath + zombie1);
        sT.addState(VISIBLE_STATE.toString(), img);
        String zombieMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_ZOMBIE);
        img = loadImage(imgPath + zombieMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, PLAYER_X, PLAYER_Y, 0, 0, INVISIBLE_STATE.toString());
        guiDecor.put(ZOMBIE_TYPE, s);
        
        x = guiDecor.get(BACKGROUND_TYPE).getX();
        y = guiDecor.get(BACKGROUND_TYPE).getY();
        setInitialValues(x, y);

    }

    public void switchToHelpScreen() {
        //System.out.println("IN SWITCH TO HELP SCREEN");
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
        guiButtons.get(LEVEL_3_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_3_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_4_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_4_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_5_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_5_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_6_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_6_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_7_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_7_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_8_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_8_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_9_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_9_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_10_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_10_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_11_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_11_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_12_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_12_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_13_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_13_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_14_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_14_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_15_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_15_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_16_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_16_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_17_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_17_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_18_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_18_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_19_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_19_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_20_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_20_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(CLOSE_DIALOG_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(CLOSE_DIALOG_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(PAUSE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(PAUSE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SPECIAL_GREENLIGHT_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(SPECIAL_GREENLIGHT_TYPE).setEnabled(false);
        guiButtons.get(SPECIAL_REDLIGHT_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(SPECIAL_REDLIGHT_TYPE).setEnabled(false);
        guiButtons.get( SPECIAL_DECREASE_SPEED_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get( SPECIAL_DECREASE_SPEED_TYPE).setEnabled(false);
        guiButtons.get( SPECIAL_INCREASE_SPEED_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get( SPECIAL_INCREASE_SPEED_TYPE).setEnabled(false);
        guiButtons.get( SPECIAL_PLAYER_INCREASE_SPEED_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get( SPECIAL_PLAYER_INCREASE_SPEED_TYPE).setEnabled(false);
        guiButtons.get( SPECIAL_FLAT_TIRE).setState(INVISIBLE_STATE.toString());
        guiButtons.get( SPECIAL_FLAT_TIRE).setEnabled(false);
        guiButtons.get(SPECIAL_EMPTY_GAS).setState(INVISIBLE_STATE.toString());
        guiButtons.get(SPECIAL_EMPTY_GAS).setEnabled(false);
        guiButtons.get(SPECIAL_CLOSE_ROAD).setState(INVISIBLE_STATE.toString());
        guiButtons.get(SPECIAL_CLOSE_ROAD).setEnabled(false);
        guiButtons.get(SPECIAL_CLOSE_INTERSECTION).setState(INVISIBLE_STATE.toString());
        guiButtons.get(SPECIAL_CLOSE_INTERSECTION).setEnabled(false);
        guiButtons.get(SPECIAL_OPEN_INTERSECTION).setState(INVISIBLE_STATE.toString());
        guiButtons.get(SPECIAL_OPEN_INTERSECTION).setEnabled(false);
        guiButtons.get(SPECIAL_STEAL).setState(INVISIBLE_STATE.toString());
        guiButtons.get(SPECIAL_STEAL).setEnabled(false);
        guiButtons.get(SPECIAL_MIND_CONTROL).setState(INVISIBLE_STATE.toString());
        guiButtons.get(SPECIAL_MIND_CONTROL).setEnabled(false);
        guiButtons.get(SPECIAL_INTANG).setState(INVISIBLE_STATE.toString());
        guiButtons.get(SPECIAL_INTANG).setEnabled(false);
        guiButtons.get(SPECIAL_MINDLESS_TERROR).setState(INVISIBLE_STATE.toString());
        guiButtons.get(SPECIAL_MINDLESS_TERROR).setEnabled(false);
        guiButtons.get(SPECIAL_FLYING).setState(INVISIBLE_STATE.toString());
        guiButtons.get(SPECIAL_FLYING).setEnabled(false);
        guiButtons.get(SPECIAL_INVINCIBILITY).setState(INVISIBLE_STATE.toString());
        guiButtons.get(SPECIAL_INVINCIBILITY).setEnabled(false);
        
        guiDecor.get(PLAYER_TYPE).setState(INVISIBLE_STATE.toString());
        guiDecor.get(PLAYER_TYPE).setEnabled(false);
        currentScreenState = HELP_SCREEN_STATE;

        if (enableMusic==true){
        audio.play(PathXPropertyType.SONG_CUE_MENU_SCREEN.toString(), true);
        audio.stop(PathXPropertyType.SONG_CUE_LEVEL_SCREEN.toString());
        audio.stop(PathXPropertyType.SONG_CUE_GAME_SCREEN.toString());
        }
        else{
        audio.stop(PathXPropertyType.SONG_CUE_MENU_SCREEN.toString());
        audio.stop(PathXPropertyType.SONG_CUE_LEVEL_SCREEN.toString());
        audio.stop(PathXPropertyType.SONG_CUE_GAME_SCREEN.toString());
        }
        guiDialogs.get(SETTINGS_DIALOG_TYPE).setState(INVISIBLE_STATE.toString());
        guiDialogs.get(LEVEL_INFO_DIALOG_TYPE).setState(INVISIBLE_STATE.toString());
        MakeWinDialogInvisible();
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
        guiButtons.get(LEVEL_3_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_3_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_4_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_4_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_5_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_5_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_6_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_6_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_7_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_7_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_8_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_8_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_9_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_9_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_10_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_10_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_11_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_11_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_12_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_12_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_13_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_13_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_14_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_14_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_15_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_15_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_16_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_16_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_17_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_17_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_18_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_18_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_19_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_19_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_20_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_20_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(CLOSE_DIALOG_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(CLOSE_DIALOG_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(PAUSE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(PAUSE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SPECIAL_GREENLIGHT_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(SPECIAL_GREENLIGHT_TYPE).setEnabled(false);
        guiButtons.get(SPECIAL_REDLIGHT_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(SPECIAL_REDLIGHT_TYPE).setEnabled(false);
        guiButtons.get( SPECIAL_FREEZETIME_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get( SPECIAL_FREEZETIME_TYPE).setEnabled(false);
        guiButtons.get( SPECIAL_DECREASE_SPEED_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get( SPECIAL_DECREASE_SPEED_TYPE).setEnabled(false);
        guiButtons.get( SPECIAL_INCREASE_SPEED_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get( SPECIAL_INCREASE_SPEED_TYPE).setEnabled(false);
        guiButtons.get( SPECIAL_PLAYER_INCREASE_SPEED_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get( SPECIAL_PLAYER_INCREASE_SPEED_TYPE).setEnabled(false);
        guiButtons.get( SPECIAL_FLAT_TIRE).setState(INVISIBLE_STATE.toString());
        guiButtons.get( SPECIAL_FLAT_TIRE).setEnabled(false);
        guiButtons.get(  SPECIAL_EMPTY_GAS).setState(INVISIBLE_STATE.toString());
        guiButtons.get(  SPECIAL_EMPTY_GAS).setEnabled(false);
        guiButtons.get(SPECIAL_CLOSE_ROAD).setState(INVISIBLE_STATE.toString());
        guiButtons.get(SPECIAL_CLOSE_ROAD).setEnabled(false);
        guiButtons.get(SPECIAL_CLOSE_INTERSECTION).setState(INVISIBLE_STATE.toString());
        guiButtons.get(SPECIAL_CLOSE_INTERSECTION).setEnabled(false);
        guiButtons.get(SPECIAL_OPEN_INTERSECTION).setState(INVISIBLE_STATE.toString());
        guiButtons.get(SPECIAL_OPEN_INTERSECTION).setEnabled(false);
        guiButtons.get(SPECIAL_STEAL).setState(INVISIBLE_STATE.toString());
        guiButtons.get(SPECIAL_STEAL).setEnabled(false);
        guiButtons.get(SPECIAL_MIND_CONTROL).setState(INVISIBLE_STATE.toString());
        guiButtons.get(SPECIAL_MIND_CONTROL).setEnabled(false);
        guiButtons.get(SPECIAL_INTANG).setState(INVISIBLE_STATE.toString());
        guiButtons.get(SPECIAL_INTANG).setEnabled(false);
        guiButtons.get(SPECIAL_MINDLESS_TERROR).setState(INVISIBLE_STATE.toString());
        guiButtons.get(SPECIAL_MINDLESS_TERROR).setEnabled(false);
        guiButtons.get(SPECIAL_FLYING).setState(INVISIBLE_STATE.toString());
        guiButtons.get(SPECIAL_FLYING).setEnabled(false);
        guiButtons.get(SPECIAL_INVINCIBILITY).setState(INVISIBLE_STATE.toString());
        guiButtons.get(SPECIAL_INVINCIBILITY).setEnabled(false);
        
        if(enableSound==false){
        guiButtons.get(SOUND_UNSELECTED_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(SOUND_UNSELECTED_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(SOUND_SELECTED_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(SOUND_SELECTED_BUTTON_TYPE).setEnabled(false);
        }
        else{
        guiButtons.get(SOUND_UNSELECTED_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(SOUND_UNSELECTED_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SOUND_SELECTED_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(SOUND_SELECTED_BUTTON_TYPE).setEnabled(true);
        }
        
        if(enableMusic==false){
        guiButtons.get(MUSIC_UNSELECTED_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(MUSIC_UNSELECTED_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(MUSIC_SELECTED_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(MUSIC_SELECTED_BUTTON_TYPE).setEnabled(false);
        }
        else{
        guiButtons.get(MUSIC_UNSELECTED_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(MUSIC_UNSELECTED_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(MUSIC_SELECTED_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(MUSIC_SELECTED_BUTTON_TYPE).setEnabled(true);
        }
        guiDecor.get(PLAYER_TYPE).setState(INVISIBLE_STATE.toString());
        guiDecor.get(PLAYER_TYPE).setEnabled(false);
            guiDecor.get(ZOMBIE_TYPE).setState(INVISIBLE_STATE.toString());
         guiDecor.get(ZOMBIE_TYPE).setEnabled(false);
        currentScreenState = SETTINGS_SCREEN_STATE;

        if(enableMusic==true){
        audio.play(PathXPropertyType.SONG_CUE_MENU_SCREEN.toString(), true);
        audio.stop(PathXPropertyType.SONG_CUE_LEVEL_SCREEN.toString());
        audio.stop(PathXPropertyType.SONG_CUE_GAME_SCREEN.toString());
        }
        else{
        audio.stop(PathXPropertyType.SONG_CUE_MENU_SCREEN.toString());
        audio.stop(PathXPropertyType.SONG_CUE_LEVEL_SCREEN.toString());
        audio.stop(PathXPropertyType.SONG_CUE_GAME_SCREEN.toString());
        }
        guiDialogs.get(SETTINGS_DIALOG_TYPE).setState(VISIBLE_STATE.toString());
        guiDialogs.get(LEVEL_INFO_DIALOG_TYPE).setState(INVISIBLE_STATE.toString());
        guiDialogs.get(LEVEL_FAIL_DIALOG_TYPE).setState(INVISIBLE_STATE.toString());
        
        MakeWinDialogInvisible();
        float x = getX();
        float y = getY();
        resetBackground(x, y);
        setScroll(false);
    }
    public void switchToGameScreen() {
        
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
        guiButtons.get(UP_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(UP_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(DOWN_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(DOWN_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEFT_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEFT_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(RIGHT_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(RIGHT_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_1_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_1_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_1_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_1_COMPLETE_BUTTON_TYPE).setEnabled(false); 
        guiButtons.get(LEVEL_2_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_2_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_2_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_3_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_3_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_3_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_3_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_3_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_3_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_4_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_4_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_4_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_4_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_4_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_4_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_5_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_5_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_5_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_5_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_5_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_5_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_6_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_6_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_6_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_6_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_6_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_6_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_7_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_7_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_7_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_7_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_7_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_7_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_8_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_8_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_8_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_8_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_8_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_8_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_9_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_9_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_9_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_9_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_9_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_9_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_10_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_10_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_10_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_10_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_10_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_10_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_11_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_11_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_11_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_11_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_11_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_11_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_12_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_12_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_12_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_12_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_12_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_12_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_13_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_13_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_13_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_13_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_13_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_13_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_14_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_14_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_14_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_14_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_14_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_14_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_15_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_15_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_15_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_15_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_15_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_15_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_16_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_16_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_16_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_16_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_16_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_16_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_17_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_17_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_17_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_17_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_17_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_17_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_18_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_18_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_18_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_18_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_18_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_18_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_19_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_19_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_19_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_19_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_19_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_19_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_20_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_20_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_20_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_20_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_20_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_20_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(PAUSE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(PAUSE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(MUSIC_SELECTED_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(MUSIC_SELECTED_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(MUSIC_UNSELECTED_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(MUSIC_UNSELECTED_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SOUND_SELECTED_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(SOUND_SELECTED_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SOUND_UNSELECTED_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(SOUND_UNSELECTED_BUTTON_TYPE).setEnabled(false);
        
        guiDecor.get(PLAYER_TYPE).setState(VISIBLE_STATE.toString());
        guiDecor.get(PLAYER_TYPE).setEnabled(true);
        guiDecor.get(POLICE_TYPE).setState(VISIBLE_STATE.toString());
        guiDecor.get(POLICE_TYPE).setEnabled(true);
        
        guiButtons.get(TRY_AGAIN_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(TRY_AGAIN_TYPE).setEnabled(true);
        guiButtons.get(QUIT_LEVEL_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(QUIT_LEVEL_TYPE).setEnabled(true);
        currentScreenState = GAME_SCREEN_STATE;
        
        if(enableMusic==true){
        audio.play(PathXPropertyType.SONG_CUE_GAME_SCREEN.toString(), true);
        audio.stop(PathXPropertyType.SONG_CUE_LEVEL_SCREEN.toString());
        audio.stop(PathXPropertyType.SONG_CUE_MENU_SCREEN.toString());
        }
        else{
        audio.stop(PathXPropertyType.SONG_CUE_GAME_SCREEN.toString());
        audio.stop(PathXPropertyType.SONG_CUE_LEVEL_SCREEN.toString());
        audio.stop(PathXPropertyType.SONG_CUE_MENU_SCREEN.toString());
        }
        if (level>=2){
            guiButtons.get(SPECIAL_GREENLIGHT_TYPE).setState(VISIBLE_STATE.toString());
            guiButtons.get(SPECIAL_GREENLIGHT_TYPE).setEnabled(true);
        }
        if(level>=3){
        guiButtons.get(SPECIAL_REDLIGHT_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(SPECIAL_REDLIGHT_TYPE).setEnabled(true);
        }
        if (level>=4){
        guiButtons.get( SPECIAL_FREEZETIME_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get( SPECIAL_FREEZETIME_TYPE).setEnabled(true);
        }
        if(level>=5){
        guiButtons.get( SPECIAL_DECREASE_SPEED_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get( SPECIAL_DECREASE_SPEED_TYPE).setEnabled(true);
            
        }
        if (level>=6){
        guiButtons.get( SPECIAL_INCREASE_SPEED_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get( SPECIAL_INCREASE_SPEED_TYPE).setEnabled(true);
        }
        if(level>=7){
        guiButtons.get( SPECIAL_PLAYER_INCREASE_SPEED_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get( SPECIAL_PLAYER_INCREASE_SPEED_TYPE).setEnabled(true);
        }
        if (level>=8){
        guiButtons.get( SPECIAL_FLAT_TIRE).setState(VISIBLE_STATE.toString());
        guiButtons.get( SPECIAL_FLAT_TIRE).setEnabled(true);
        }
        if(level>=9){
        guiButtons.get(  SPECIAL_EMPTY_GAS).setState(VISIBLE_STATE.toString());
        guiButtons.get(  SPECIAL_EMPTY_GAS).setEnabled(true);
        }
        if(level>=10){
        guiButtons.get(SPECIAL_CLOSE_ROAD).setState(VISIBLE_STATE.toString());
        guiButtons.get(SPECIAL_CLOSE_ROAD).setEnabled(true);
        }
        if(level>=11){
        guiButtons.get(SPECIAL_CLOSE_INTERSECTION).setState(VISIBLE_STATE.toString());
        guiButtons.get(SPECIAL_CLOSE_INTERSECTION).setEnabled(true);
       
        }
        if(level>=12){
        guiButtons.get(SPECIAL_OPEN_INTERSECTION).setState(VISIBLE_STATE.toString());
        guiButtons.get(SPECIAL_OPEN_INTERSECTION).setEnabled(true);
        }
        if(level>=13){
        guiButtons.get(SPECIAL_STEAL).setState(VISIBLE_STATE.toString());
        guiButtons.get(SPECIAL_STEAL).setEnabled(true);
        }
        if(level>=14){
        guiButtons.get(SPECIAL_MIND_CONTROL).setState(VISIBLE_STATE.toString());
        guiButtons.get(SPECIAL_MIND_CONTROL).setEnabled(true);
        }
        if(level>=15){
        guiButtons.get(SPECIAL_INTANG).setState(VISIBLE_STATE.toString());
        guiButtons.get(SPECIAL_INTANG).setEnabled(true);
        }
        if(level>=16){
        guiButtons.get(SPECIAL_MINDLESS_TERROR).setState(VISIBLE_STATE.toString());
        guiButtons.get(SPECIAL_MINDLESS_TERROR).setEnabled(true);
        }
       if(level>=17){
        guiButtons.get(SPECIAL_FLYING).setState(VISIBLE_STATE.toString());
        guiButtons.get(SPECIAL_FLYING).setEnabled(true);
       }
       if(level>=18){
        guiButtons.get(SPECIAL_INVINCIBILITY).setState(VISIBLE_STATE.toString());
        guiButtons.get(SPECIAL_INVINCIBILITY).setEnabled(true);
       }
       
        guiDialogs.get(SETTINGS_DIALOG_TYPE).setState(INVISIBLE_STATE.toString());
        
        //render the player in its correct location
        Sprite player = guiDecor.get(PLAYER_TYPE);
        Intersection first=((PathXDataModel)getDataModel()).getStartingLocation();
        int x = first.getX();
        int y = first.getY();
        player.setX(x + 30);
        player.setY(y - 15);
        
       
        ArrayList <Intersection> intersections=((PathXDataModel)getDataModel()).getLevel().getIntersections();
       int policenum= ((PathXDataModel)getDataModel()).getLevel().getNumPolice();
       if (policenum>=2){
        guiDecor.get(POLICE_TYPE_2).setState(VISIBLE_STATE.toString());
        guiDecor.get(POLICE_TYPE_2).setEnabled(true);
       }
        if (policenum>=3){
        guiDecor.get(POLICE_TYPE_3).setState(VISIBLE_STATE.toString());
        guiDecor.get(POLICE_TYPE_3).setEnabled(true);
       }
        if (policenum>=4){
        guiDecor.get(POLICE_TYPE_4).setState(VISIBLE_STATE.toString());
        guiDecor.get(POLICE_TYPE_4).setEnabled(true);
       }
         if (policenum>=5){
        guiDecor.get(POLICE_TYPE_5).setState(VISIBLE_STATE.toString());
        guiDecor.get(POLICE_TYPE_5).setEnabled(true);
       }
       if (policenum==6){
        guiDecor.get(POLICE_TYPE_6).setState(VISIBLE_STATE.toString());
        guiDecor.get(POLICE_TYPE_6).setEnabled(true);
       }
       ArrayList <Intersection> used= new ArrayList();
        for (int i=0; i<policenum; i++){
            Sprite actual=guiDecor.get(POLICE_TYPE);
            if (i==1){
                actual=guiDecor.get(POLICE_TYPE_2);
            }
            if (i==2){
                actual=guiDecor.get(POLICE_TYPE_3);
            }
            if (i==3){
                actual=guiDecor.get(POLICE_TYPE_4);
            }
            if (i==4){
                actual=guiDecor.get(POLICE_TYPE_5);
            }
            if (i==5){
                actual=guiDecor.get(POLICE_TYPE_6);
            }
            int rando= 1+ (int) (Math.random()* intersections.size()-2);
            Intersection val= intersections.get(rando);
            
            while (val.equals(((PathXDataModel)getDataModel()).getLevel().getDestination())|| val.equals(((PathXDataModel)getDataModel()).getLevel().getStartingLocation()) || (used!=null && used.contains(val)) ){
                rando= 1+ (int) (Math.random()* intersections.size()-2);
                val= intersections.get(rando);
            }
            used.add(val);
            actual.setX(val.getX());
            actual.setY(val.getY());
            
            
           
        }
         int numBandits= ((PathXDataModel)getDataModel()).getLevel().getNumBandits();
            if (numBandits>=1){
         guiDecor.get(BANDIT_TYPE).setState(VISIBLE_STATE.toString());
         guiDecor.get(BANDIT_TYPE).setEnabled(true);
            }
            if (numBandits>=2){
         guiDecor.get(BANDIT_TYPE_2).setState(VISIBLE_STATE.toString());
         guiDecor.get(BANDIT_TYPE_2).setEnabled(true);
            }
             if (numBandits>=3){
         guiDecor.get(BANDIT_TYPE_3).setState(VISIBLE_STATE.toString());
         guiDecor.get(BANDIT_TYPE_3).setEnabled(true);
            }
              if (numBandits>=4){
         guiDecor.get(BANDIT_TYPE_4).setState(VISIBLE_STATE.toString());
         guiDecor.get(BANDIT_TYPE_4).setEnabled(true);
            }
               if (numBandits>=5){
         guiDecor.get(BANDIT_TYPE_5).setState(VISIBLE_STATE.toString());
         guiDecor.get(BANDIT_TYPE_5).setEnabled(true);
            }
                if (numBandits==6){
         guiDecor.get(BANDIT_TYPE_6).setState(VISIBLE_STATE.toString());
         guiDecor.get(BANDIT_TYPE_6).setEnabled(true);
            }
            if (numBandits>0){    
           for (int i=0; i< numBandits; i++){
            Sprite actual=guiDecor.get(BANDIT_TYPE);
            if (i==1){
                actual=guiDecor.get(BANDIT_TYPE_2);
            }
            if (i==2){
                actual=guiDecor.get(BANDIT_TYPE_3);
            }
            if (i==3){
                actual=guiDecor.get(BANDIT_TYPE_4);
            }
            if (i==4){
                actual=guiDecor.get(BANDIT_TYPE_5);
            }
            if (i==5){
                actual=guiDecor.get(BANDIT_TYPE_6);
            }
            int rando= 1+ (int) (Math.random()* intersections.size()-2);
            Intersection val= intersections.get(rando);
            while (val.equals(((PathXDataModel)getDataModel()).getLevel().getDestination())|| val.equals(((PathXDataModel)getDataModel()).getLevel().getStartingLocation()) || (used!=null && used.contains(val)) ){
            rando= 1+ (int) (Math.random()* intersections.size()-2);
            val= intersections.get(rando);
            }
            used.add(val);
            actual.setX(val.getX());
            actual.setY(val.getY());
           }     
            }
            {
          int numZombies= ((PathXDataModel)getDataModel()).getLevel().getNumZombies();
          if (numZombies>=1){
         guiDecor.get(ZOMBIE_TYPE).setState(VISIBLE_STATE.toString());
         guiDecor.get(ZOMBIE_TYPE).setEnabled(true);
            }
          Sprite actual=guiDecor.get(ZOMBIE_TYPE);
            int rando= 1+ (int) (Math.random()* intersections.size()-2);
            Intersection val= intersections.get(rando);
            while (val.equals(((PathXDataModel)getDataModel()).getLevel().getDestination())|| val.equals(((PathXDataModel)getDataModel()).getLevel().getStartingLocation()) || (used!=null && used.contains(val)) ){
            rando= 1+ (int) (Math.random()* intersections.size()-2);
            val= intersections.get(rando);
            }
            used.add(val);
            actual.setX(val.getX());
            actual.setY(val.getY());
            }
        guiDialogs.get(LEVEL_INFO_DIALOG_TYPE).setState(VISIBLE_STATE.toString());
        guiDialogs.get(LEVEL_COMPLETE_DIALOG_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(CLOSE_DIALOG_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(CLOSE_DIALOG_BUTTON_TYPE).setEnabled(true);
        //MakeWinDialogInvisible();
    
        //player.setX(511);
        //player.setY(226);
        float ex = getX();
        float why = getY();
        resetBackground(ex, why);
        setScroll(false);
    }
    public void switchToWinScreen(){
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
        guiButtons.get(UP_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(UP_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(DOWN_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(DOWN_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEFT_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEFT_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(RIGHT_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(RIGHT_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_1_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_1_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_1_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_1_COMPLETE_BUTTON_TYPE).setEnabled(false); 
        guiButtons.get(LEVEL_2_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_2_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_2_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_3_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_3_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_3_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_3_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_3_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_3_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_4_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_4_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_4_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_4_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_4_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_4_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_5_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_5_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_5_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_5_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_5_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_5_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_6_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_6_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_6_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_6_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_6_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_6_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_7_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_7_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_7_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_7_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_7_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_7_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_8_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_8_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_8_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_8_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_8_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_8_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_9_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_9_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_9_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_9_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_9_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_9_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_10_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_10_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_10_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_10_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_10_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_10_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_11_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_11_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_11_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_11_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_11_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_11_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_12_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_12_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_12_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_12_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_12_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_12_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_13_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_13_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_13_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_13_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_13_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_13_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_14_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_14_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_14_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_14_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_14_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_14_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_15_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_15_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_15_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_15_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_15_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_15_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_16_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_16_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_16_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_16_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_16_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_16_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_17_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_17_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_17_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_17_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_17_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_17_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_18_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_18_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_18_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_18_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_18_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_18_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_19_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_19_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_19_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_19_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_19_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_19_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_20_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_20_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_20_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_20_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_20_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_20_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(PAUSE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(PAUSE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(MUSIC_SELECTED_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(MUSIC_SELECTED_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(MUSIC_UNSELECTED_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(MUSIC_UNSELECTED_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SOUND_SELECTED_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(SOUND_SELECTED_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SOUND_UNSELECTED_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(SOUND_UNSELECTED_BUTTON_TYPE).setEnabled(false);
        
        guiDecor.get(PLAYER_TYPE).setState(INVISIBLE_STATE.toString());
        guiDecor.get(PLAYER_TYPE).setEnabled(false);
        guiDecor.get(POLICE_TYPE).setState(INVISIBLE_STATE.toString());
        guiDecor.get(POLICE_TYPE).setEnabled(false);
        guiDecor.get(POLICE_TYPE_2).setState(INVISIBLE_STATE.toString());
        guiDecor.get(POLICE_TYPE_2).setEnabled(false);
        guiDecor.get(POLICE_TYPE_3).setState(INVISIBLE_STATE.toString());
        guiDecor.get(POLICE_TYPE_3).setEnabled(false);
        guiDecor.get(POLICE_TYPE_4).setState(INVISIBLE_STATE.toString());
        guiDecor.get(POLICE_TYPE_4).setEnabled(false);
        guiDecor.get(POLICE_TYPE_5).setState(INVISIBLE_STATE.toString());
        guiDecor.get(POLICE_TYPE_5).setEnabled(false);
        guiDecor.get(POLICE_TYPE_6).setState(INVISIBLE_STATE.toString());
        guiDecor.get(POLICE_TYPE_6).setEnabled(false);
        guiDecor.get(BANDIT_TYPE).setState(INVISIBLE_STATE.toString());
        guiDecor.get(BANDIT_TYPE).setEnabled(false);
        guiDecor.get(BANDIT_TYPE_2).setState(INVISIBLE_STATE.toString());
        guiDecor.get(BANDIT_TYPE_2).setEnabled(false);
        guiDecor.get(BANDIT_TYPE_3).setState(INVISIBLE_STATE.toString());
        guiDecor.get(BANDIT_TYPE_3).setEnabled(false);
        guiDecor.get(BANDIT_TYPE_4).setState(INVISIBLE_STATE.toString());
        guiDecor.get(BANDIT_TYPE_4).setEnabled(false);
        guiDecor.get(BANDIT_TYPE_5).setState(INVISIBLE_STATE.toString());
        guiDecor.get(BANDIT_TYPE_5).setEnabled(false);
        guiDecor.get(BANDIT_TYPE_6).setState(INVISIBLE_STATE.toString());
        guiDecor.get(BANDIT_TYPE_6).setEnabled(false);
        guiDecor.get(ZOMBIE_TYPE).setState(INVISIBLE_STATE.toString());
         guiDecor.get(ZOMBIE_TYPE).setEnabled(false);
        currentScreenState = WIN_SCREEN_STATE;
        
        if(enableMusic==true){
        audio.play(PathXPropertyType.SONG_CUE_GAME_SCREEN.toString(), true);
        audio.stop(PathXPropertyType.SONG_CUE_LEVEL_SCREEN.toString());
        audio.stop(PathXPropertyType.SONG_CUE_MENU_SCREEN.toString());
        }
        else{
        audio.stop(PathXPropertyType.SONG_CUE_GAME_SCREEN.toString());
        audio.stop(PathXPropertyType.SONG_CUE_LEVEL_SCREEN.toString());
        audio.stop(PathXPropertyType.SONG_CUE_MENU_SCREEN.toString());
        }
        if (level>=2){
            guiButtons.get(SPECIAL_GREENLIGHT_TYPE).setState(VISIBLE_STATE.toString());
            guiButtons.get(SPECIAL_GREENLIGHT_TYPE).setEnabled(true);
        }
        if(level>=3){
        guiButtons.get(SPECIAL_REDLIGHT_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(SPECIAL_REDLIGHT_TYPE).setEnabled(true);
        }
        if (level>=4){
        guiButtons.get( SPECIAL_FREEZETIME_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get( SPECIAL_FREEZETIME_TYPE).setEnabled(true);
        }
        if(level>=5){
        guiButtons.get( SPECIAL_DECREASE_SPEED_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get( SPECIAL_DECREASE_SPEED_TYPE).setEnabled(true);
            
        }
        if (level>=6){
        guiButtons.get( SPECIAL_INCREASE_SPEED_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get( SPECIAL_INCREASE_SPEED_TYPE).setEnabled(true);
        }
        if(level>=7){
        guiButtons.get( SPECIAL_PLAYER_INCREASE_SPEED_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get( SPECIAL_PLAYER_INCREASE_SPEED_TYPE).setEnabled(true);
        }
        if (level>=8){
        guiButtons.get( SPECIAL_FLAT_TIRE).setState(VISIBLE_STATE.toString());
        guiButtons.get( SPECIAL_FLAT_TIRE).setEnabled(true);
        }
        if(level>=9){
        guiButtons.get(  SPECIAL_EMPTY_GAS).setState(VISIBLE_STATE.toString());
        guiButtons.get(  SPECIAL_EMPTY_GAS).setEnabled(true);
        }
        if(level>=10){
        guiButtons.get(SPECIAL_CLOSE_ROAD).setState(VISIBLE_STATE.toString());
        guiButtons.get(SPECIAL_CLOSE_ROAD).setEnabled(true);
        }
        if(level>=11){
        guiButtons.get(SPECIAL_CLOSE_INTERSECTION).setState(VISIBLE_STATE.toString());
        guiButtons.get(SPECIAL_CLOSE_INTERSECTION).setEnabled(true);
       
        }
        if(level>=12){
        guiButtons.get(SPECIAL_OPEN_INTERSECTION).setState(VISIBLE_STATE.toString());
        guiButtons.get(SPECIAL_OPEN_INTERSECTION).setEnabled(true);
        }
        if(level>=13){
        guiButtons.get(SPECIAL_STEAL).setState(VISIBLE_STATE.toString());
        guiButtons.get(SPECIAL_STEAL).setEnabled(true);
        }
        if(level>=14){
        guiButtons.get(SPECIAL_MIND_CONTROL).setState(VISIBLE_STATE.toString());
        guiButtons.get(SPECIAL_MIND_CONTROL).setEnabled(true);
        }
        if(level>=15){
        guiButtons.get(SPECIAL_INTANG).setState(VISIBLE_STATE.toString());
        guiButtons.get(SPECIAL_INTANG).setEnabled(true);
        }
        if(level>=16){
        guiButtons.get(SPECIAL_MINDLESS_TERROR).setState(VISIBLE_STATE.toString());
        guiButtons.get(SPECIAL_MINDLESS_TERROR).setEnabled(true);
        }
       if(level>=17){
        guiButtons.get(SPECIAL_FLYING).setState(VISIBLE_STATE.toString());
        guiButtons.get(SPECIAL_FLYING).setEnabled(true);
       }
       if(level>=18){
        guiButtons.get(SPECIAL_INVINCIBILITY).setState(VISIBLE_STATE.toString());
        guiButtons.get(SPECIAL_INVINCIBILITY).setEnabled(true);
       }
       
        guiDialogs.get(SETTINGS_DIALOG_TYPE).setState(INVISIBLE_STATE.toString());
        
       
         makeDialogInvisible();
         guiDialogs.get(LEVEL_COMPLETE_DIALOG_TYPE).setState(VISIBLE_STATE.toString());
          
   
        
    
        //player.setX(511);
        //player.setY(226);
        float ex = getX();
        float why = getY();
        resetBackground(ex, why);
        setScroll(false);
    }
    
    
    public void switchToLoseScreen(){
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
        guiButtons.get(UP_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(UP_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(DOWN_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(DOWN_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEFT_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEFT_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(RIGHT_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(RIGHT_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_1_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_1_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_1_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_1_COMPLETE_BUTTON_TYPE).setEnabled(false); 
        guiButtons.get(LEVEL_2_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_2_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_2_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_3_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_3_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_3_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_3_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_3_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_3_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_4_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_4_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_4_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_4_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_4_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_4_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_5_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_5_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_5_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_5_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_5_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_5_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_6_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_6_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_6_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_6_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_6_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_6_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_7_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_7_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_7_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_7_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_7_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_7_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_8_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_8_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_8_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_8_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_8_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_8_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_9_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_9_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_9_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_9_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_9_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_9_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_10_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_10_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_10_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_10_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_10_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_10_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_11_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_11_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_11_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_11_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_11_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_11_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_12_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_12_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_12_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_12_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_12_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_12_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_13_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_13_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_13_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_13_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_13_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_13_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_14_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_14_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_14_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_14_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_14_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_14_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_15_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_15_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_15_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_15_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_15_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_15_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_16_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_16_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_16_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_16_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_16_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_16_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_17_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_17_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_17_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_17_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_17_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_17_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_18_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_18_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_18_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_18_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_18_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_18_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_19_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_19_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_19_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_19_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_19_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_19_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_20_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_20_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_20_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_20_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_20_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_20_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(PAUSE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(PAUSE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(MUSIC_SELECTED_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(MUSIC_SELECTED_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(MUSIC_UNSELECTED_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(MUSIC_UNSELECTED_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SOUND_SELECTED_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(SOUND_SELECTED_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SOUND_UNSELECTED_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(SOUND_UNSELECTED_BUTTON_TYPE).setEnabled(false);
        
        guiDecor.get(PLAYER_TYPE).setState(VISIBLE_STATE.toString());
        guiDecor.get(PLAYER_TYPE).setEnabled(true);
        guiDecor.get(POLICE_TYPE).setState(INVISIBLE_STATE.toString());
        guiDecor.get(POLICE_TYPE).setEnabled(false);
        guiDecor.get(POLICE_TYPE_2).setState(INVISIBLE_STATE.toString());
        guiDecor.get(POLICE_TYPE_2).setEnabled(false);
        guiDecor.get(POLICE_TYPE_3).setState(INVISIBLE_STATE.toString());
        guiDecor.get(POLICE_TYPE_3).setEnabled(false);
        guiDecor.get(POLICE_TYPE_4).setState(INVISIBLE_STATE.toString());
        guiDecor.get(POLICE_TYPE_4).setEnabled(false);
        guiDecor.get(POLICE_TYPE_5).setState(INVISIBLE_STATE.toString());
        guiDecor.get(POLICE_TYPE_5).setEnabled(false);
        guiDecor.get(POLICE_TYPE_6).setState(INVISIBLE_STATE.toString());
        guiDecor.get(POLICE_TYPE_6).setEnabled(false);
       guiDecor.get(BANDIT_TYPE).setState(INVISIBLE_STATE.toString());
        guiDecor.get(BANDIT_TYPE).setEnabled(false);
        guiDecor.get(BANDIT_TYPE_2).setState(INVISIBLE_STATE.toString());
        guiDecor.get(BANDIT_TYPE_2).setEnabled(false);
        guiDecor.get(BANDIT_TYPE_3).setState(INVISIBLE_STATE.toString());
        guiDecor.get(BANDIT_TYPE_3).setEnabled(false);
        guiDecor.get(BANDIT_TYPE_4).setState(INVISIBLE_STATE.toString());
        guiDecor.get(BANDIT_TYPE_4).setEnabled(false);
        guiDecor.get(BANDIT_TYPE_5).setState(INVISIBLE_STATE.toString());
        guiDecor.get(BANDIT_TYPE_5).setEnabled(false);
        guiDecor.get(BANDIT_TYPE_6).setState(INVISIBLE_STATE.toString());
        guiDecor.get(BANDIT_TYPE_6).setEnabled(false);
        guiDecor.get(ZOMBIE_TYPE).setState(INVISIBLE_STATE.toString());
         guiDecor.get(ZOMBIE_TYPE).setEnabled(false);
        currentScreenState = LOSE_SCREEN_STATE;
        
        if(enableMusic==true){
        audio.play(PathXPropertyType.SONG_CUE_GAME_SCREEN.toString(), true);
        audio.stop(PathXPropertyType.SONG_CUE_LEVEL_SCREEN.toString());
        audio.stop(PathXPropertyType.SONG_CUE_MENU_SCREEN.toString());
        }
        else{
        audio.stop(PathXPropertyType.SONG_CUE_GAME_SCREEN.toString());
        audio.stop(PathXPropertyType.SONG_CUE_LEVEL_SCREEN.toString());
        audio.stop(PathXPropertyType.SONG_CUE_MENU_SCREEN.toString());
        }
        if (level>=2){
            guiButtons.get(SPECIAL_GREENLIGHT_TYPE).setState(VISIBLE_STATE.toString());
            guiButtons.get(SPECIAL_GREENLIGHT_TYPE).setEnabled(true);
        }
        if(level>=3){
        guiButtons.get(SPECIAL_REDLIGHT_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(SPECIAL_REDLIGHT_TYPE).setEnabled(true);
        }
        if (level>=4){
        guiButtons.get( SPECIAL_FREEZETIME_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get( SPECIAL_FREEZETIME_TYPE).setEnabled(true);
        }
        if(level>=5){
        guiButtons.get( SPECIAL_DECREASE_SPEED_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get( SPECIAL_DECREASE_SPEED_TYPE).setEnabled(true);
            
        }
        if (level>=6){
        guiButtons.get( SPECIAL_INCREASE_SPEED_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get( SPECIAL_INCREASE_SPEED_TYPE).setEnabled(true);
        }
        if(level>=7){
        guiButtons.get( SPECIAL_PLAYER_INCREASE_SPEED_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get( SPECIAL_PLAYER_INCREASE_SPEED_TYPE).setEnabled(true);
        }
        if (level>=8){
        guiButtons.get( SPECIAL_FLAT_TIRE).setState(VISIBLE_STATE.toString());
        guiButtons.get( SPECIAL_FLAT_TIRE).setEnabled(true);
        }
        if(level>=9){
        guiButtons.get(  SPECIAL_EMPTY_GAS).setState(VISIBLE_STATE.toString());
        guiButtons.get(  SPECIAL_EMPTY_GAS).setEnabled(true);
        }
        if(level>=10){
        guiButtons.get(SPECIAL_CLOSE_ROAD).setState(VISIBLE_STATE.toString());
        guiButtons.get(SPECIAL_CLOSE_ROAD).setEnabled(true);
        }
        if(level>=11){
        guiButtons.get(SPECIAL_CLOSE_INTERSECTION).setState(VISIBLE_STATE.toString());
        guiButtons.get(SPECIAL_CLOSE_INTERSECTION).setEnabled(true);
       
        }
        if(level>=12){
        guiButtons.get(SPECIAL_OPEN_INTERSECTION).setState(VISIBLE_STATE.toString());
        guiButtons.get(SPECIAL_OPEN_INTERSECTION).setEnabled(true);
        }
        if(level>=13){
        guiButtons.get(SPECIAL_STEAL).setState(VISIBLE_STATE.toString());
        guiButtons.get(SPECIAL_STEAL).setEnabled(true);
        }
        if(level>=14){
        guiButtons.get(SPECIAL_MIND_CONTROL).setState(VISIBLE_STATE.toString());
        guiButtons.get(SPECIAL_MIND_CONTROL).setEnabled(true);
        }
        if(level>=15){
        guiButtons.get(SPECIAL_INTANG).setState(VISIBLE_STATE.toString());
        guiButtons.get(SPECIAL_INTANG).setEnabled(true);
        }
        if(level>=16){
        guiButtons.get(SPECIAL_MINDLESS_TERROR).setState(VISIBLE_STATE.toString());
        guiButtons.get(SPECIAL_MINDLESS_TERROR).setEnabled(true);
        }
       if(level>=17){
        guiButtons.get(SPECIAL_FLYING).setState(VISIBLE_STATE.toString());
        guiButtons.get(SPECIAL_FLYING).setEnabled(true);
       }
       if(level>=18){
        guiButtons.get(SPECIAL_INVINCIBILITY).setState(VISIBLE_STATE.toString());
        guiButtons.get(SPECIAL_INVINCIBILITY).setEnabled(true);
       }
       
        guiDialogs.get(SETTINGS_DIALOG_TYPE).setState(INVISIBLE_STATE.toString());
        
       
         makeDialogInvisible();
         guiDialogs.get(LEVEL_COMPLETE_DIALOG_TYPE).setState(INVISIBLE_STATE.toString());
         guiButtons.get(TRY_AGAIN_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(TRY_AGAIN_TYPE).setEnabled(true);
        guiButtons.get(QUIT_LEVEL_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(QUIT_LEVEL_TYPE).setEnabled(true);
         guiDialogs.get(LEVEL_FAIL_DIALOG_TYPE).setState(VISIBLE_STATE.toString());
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
        guiButtons.get(LEVEL_3_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_3_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_4_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_4_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_5_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_5_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_6_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_6_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_7_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_7_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_8_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_8_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_9_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_9_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_10_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_10_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_11_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_11_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_12_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_12_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_13_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_13_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_14_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_14_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_15_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_15_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_16_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_16_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_17_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_17_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_18_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_18_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_19_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_19_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_20_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_20_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(PAUSE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(PAUSE_BUTTON_TYPE).setEnabled(false);
        guiDecor.get(BANDIT_TYPE).setState(INVISIBLE_STATE.toString());
        guiDecor.get(BANDIT_TYPE).setEnabled(false);
        guiDecor.get(BANDIT_TYPE_2).setState(INVISIBLE_STATE.toString());
        guiDecor.get(BANDIT_TYPE_2).setEnabled(false);
        guiDecor.get(BANDIT_TYPE_3).setState(INVISIBLE_STATE.toString());
        guiDecor.get(BANDIT_TYPE_3).setEnabled(false);
        guiDecor.get(BANDIT_TYPE_4).setState(INVISIBLE_STATE.toString());
        guiDecor.get(BANDIT_TYPE_4).setEnabled(false);
        guiDecor.get(BANDIT_TYPE_5).setState(INVISIBLE_STATE.toString());
        guiDecor.get(BANDIT_TYPE_5).setEnabled(false);
        guiDecor.get(BANDIT_TYPE_6).setState(INVISIBLE_STATE.toString());
        guiDecor.get(BANDIT_TYPE_6).setEnabled(false);
         guiDecor.get(ZOMBIE_TYPE).setState(INVISIBLE_STATE.toString());
         guiDecor.get(ZOMBIE_TYPE).setEnabled(false);
        
        if (levelwon==1){
          //if(level==levelwon){
         guiButtons.get(LEVEL_1_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_1_BUTTON_TYPE).setEnabled(false); 
        guiButtons.get(LEVEL_1_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_1_COMPLETE_BUTTON_TYPE).setEnabled(true); 
        guiButtons.get(LEVEL_2_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_2_AVAILABLE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_AVAILABLE_BUTTON_TYPE).setEnabled(true);
            //}
        }
         if (levelwon==2){
             //if(level==levelwon){
        guiButtons.get(LEVEL_1_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_1_BUTTON_TYPE).setEnabled(false); 
        guiButtons.get(LEVEL_1_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_1_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_2_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_2_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_2_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_3_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_3_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_3_AVAILABLE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_3_AVAILABLE_BUTTON_TYPE).setEnabled(true);
        
             //}
         }
        if (levelwon==3){
            // if(level==levelwon){
        guiButtons.get(LEVEL_1_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_1_BUTTON_TYPE).setEnabled(false); 
        guiButtons.get(LEVEL_1_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_1_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_2_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_2_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_2_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_3_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_3_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_3_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_3_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_3_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_3_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_4_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_4_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_4_AVAILABLE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_4_AVAILABLE_BUTTON_TYPE).setEnabled(true);
             //}
         }
        
         if (levelwon==4){
         //if(level==levelwon){
        guiButtons.get(LEVEL_1_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_1_BUTTON_TYPE).setEnabled(false); 
        guiButtons.get(LEVEL_1_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_1_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_2_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_2_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_2_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_3_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_3_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_3_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_3_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_3_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_3_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_4_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_4_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_4_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_4_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_4_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_4_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_5_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_5_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_5_AVAILABLE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_5_AVAILABLE_BUTTON_TYPE).setEnabled(true);
             //}
         }
         
        if (levelwon==5){
        //if(level==levelwon){
        guiButtons.get(LEVEL_1_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_1_BUTTON_TYPE).setEnabled(false); 
        guiButtons.get(LEVEL_1_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_1_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_2_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_2_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_2_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_3_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_3_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_3_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_3_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_3_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_3_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_4_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_4_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_4_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_4_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_4_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_4_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_5_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_5_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_5_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_5_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_5_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_5_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_6_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_6_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_6_AVAILABLE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_6_AVAILABLE_BUTTON_TYPE).setEnabled(true);
             //}
         }
        
        if (levelwon==6){
        //if(level==levelwon){
        guiButtons.get(LEVEL_1_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_1_BUTTON_TYPE).setEnabled(false); 
        guiButtons.get(LEVEL_1_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_1_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_2_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_2_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_2_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_3_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_3_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_3_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_3_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_3_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_3_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_4_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_4_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_4_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_4_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_4_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_4_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_5_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_5_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_5_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_5_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_5_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_5_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_6_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_6_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_6_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_6_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_6_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_6_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_7_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_7_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_7_AVAILABLE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_7_AVAILABLE_BUTTON_TYPE).setEnabled(true);
             //}
         }
        
        if (levelwon==7){
        //if(level==levelwon){
        guiButtons.get(LEVEL_1_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_1_BUTTON_TYPE).setEnabled(false); 
        guiButtons.get(LEVEL_1_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_1_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_2_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_2_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_2_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_3_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_3_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_3_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_3_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_3_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_3_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_4_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_4_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_4_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_4_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_4_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_4_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_5_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_5_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_5_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_5_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_5_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_5_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_6_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_6_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_6_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_6_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_6_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_6_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_7_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_7_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_7_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_7_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_7_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_7_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_8_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_8_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_8_AVAILABLE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_8_AVAILABLE_BUTTON_TYPE).setEnabled(true);
             //}
         }
        
        if (levelwon==8){
        //if(level==levelwon){
        guiButtons.get(LEVEL_1_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_1_BUTTON_TYPE).setEnabled(false); 
        guiButtons.get(LEVEL_1_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_1_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_2_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_2_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_2_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_3_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_3_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_3_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_3_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_3_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_3_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_4_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_4_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_4_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_4_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_4_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_4_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_5_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_5_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_5_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_5_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_5_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_5_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_6_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_6_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_6_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_6_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_6_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_6_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_7_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_7_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_7_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_7_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_7_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_7_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_8_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_8_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_8_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_8_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_8_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_8_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_9_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_9_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_9_AVAILABLE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_9_AVAILABLE_BUTTON_TYPE).setEnabled(true);
             //}
         }
        if (levelwon==9){
        //if(level==levelwon){
        guiButtons.get(LEVEL_1_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_1_BUTTON_TYPE).setEnabled(false); 
        guiButtons.get(LEVEL_1_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_1_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_2_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_2_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_2_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_3_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_3_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_3_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_3_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_3_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_3_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_4_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_4_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_4_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_4_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_4_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_4_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_5_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_5_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_5_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_5_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_5_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_5_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_6_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_6_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_6_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_6_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_6_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_6_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_7_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_7_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_7_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_7_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_7_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_7_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_8_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_8_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_8_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_8_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_8_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_8_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_9_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_9_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_9_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_9_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_9_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_9_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_10_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_10_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_10_AVAILABLE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_10_AVAILABLE_BUTTON_TYPE).setEnabled(true);
             //}
         }
        if (levelwon==10){
        //if(level==levelwon){
        guiButtons.get(LEVEL_1_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_1_BUTTON_TYPE).setEnabled(false); 
        guiButtons.get(LEVEL_1_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_1_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_2_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_2_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_2_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_3_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_3_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_3_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_3_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_3_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_3_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_4_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_4_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_4_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_4_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_4_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_4_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_5_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_5_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_5_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_5_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_5_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_5_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_6_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_6_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_6_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_6_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_6_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_6_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_7_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_7_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_7_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_7_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_7_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_7_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_8_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_8_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_8_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_8_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_8_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_8_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_9_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_9_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_9_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_9_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_9_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_9_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_10_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_10_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_10_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_10_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_10_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_10_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_11_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_11_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_11_AVAILABLE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_11_AVAILABLE_BUTTON_TYPE).setEnabled(true);
             //}
         }
        
        if (levelwon==11){
        //if(level==levelwon){
        guiButtons.get(LEVEL_1_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_1_BUTTON_TYPE).setEnabled(false); 
        guiButtons.get(LEVEL_1_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_1_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_2_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_2_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_2_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_3_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_3_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_3_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_3_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_3_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_3_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_4_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_4_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_4_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_4_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_4_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_4_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_5_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_5_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_5_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_5_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_5_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_5_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_6_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_6_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_6_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_6_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_6_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_6_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_7_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_7_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_7_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_7_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_7_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_7_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_8_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_8_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_8_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_8_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_8_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_8_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_9_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_9_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_9_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_9_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_9_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_9_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_10_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_10_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_10_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_10_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_10_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_10_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_11_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_11_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_11_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_11_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_11_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_11_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_12_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_12_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_12_AVAILABLE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_12_AVAILABLE_BUTTON_TYPE).setEnabled(true);
             //}
         }
        
        if (levelwon==12){
        //if(level==levelwon){
        guiButtons.get(LEVEL_1_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_1_BUTTON_TYPE).setEnabled(false); 
        guiButtons.get(LEVEL_1_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_1_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_2_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_2_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_2_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_3_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_3_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_3_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_3_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_3_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_3_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_4_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_4_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_4_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_4_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_4_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_4_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_5_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_5_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_5_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_5_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_5_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_5_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_6_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_6_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_6_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_6_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_6_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_6_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_7_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_7_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_7_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_7_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_7_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_7_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_8_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_8_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_8_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_8_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_8_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_8_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_9_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_9_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_9_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_9_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_9_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_9_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_10_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_10_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_10_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_10_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_10_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_10_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_11_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_11_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_11_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_11_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_11_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_11_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_12_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_12_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_12_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_12_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_12_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_12_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_13_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_13_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_13_AVAILABLE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_13_AVAILABLE_BUTTON_TYPE).setEnabled(true);
            // }
         }
        
        if (levelwon==13){
        //if(level==levelwon){
        guiButtons.get(LEVEL_1_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_1_BUTTON_TYPE).setEnabled(false); 
        guiButtons.get(LEVEL_1_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_1_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_2_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_2_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_2_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_3_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_3_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_3_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_3_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_3_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_3_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_4_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_4_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_4_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_4_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_4_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_4_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_5_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_5_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_5_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_5_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_5_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_5_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_6_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_6_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_6_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_6_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_6_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_6_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_7_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_7_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_7_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_7_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_7_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_7_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_8_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_8_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_8_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_8_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_8_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_8_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_9_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_9_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_9_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_9_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_9_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_9_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_10_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_10_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_10_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_10_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_10_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_10_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_11_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_11_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_11_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_11_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_11_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_11_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_12_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_12_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_12_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_12_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_12_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_12_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_13_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_13_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_13_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_13_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_13_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_13_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_14_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_14_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_14_AVAILABLE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_14_AVAILABLE_BUTTON_TYPE).setEnabled(true);
             //}
         }
        
        if (levelwon==14){
        //if(level==levelwon){
        guiButtons.get(LEVEL_1_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_1_BUTTON_TYPE).setEnabled(false); 
        guiButtons.get(LEVEL_1_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_1_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_2_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_2_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_2_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_3_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_3_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_3_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_3_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_3_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_3_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_4_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_4_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_4_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_4_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_4_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_4_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_5_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_5_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_5_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_5_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_5_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_5_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_6_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_6_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_6_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_6_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_6_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_6_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_7_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_7_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_7_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_7_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_7_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_7_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_8_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_8_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_8_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_8_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_8_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_8_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_9_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_9_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_9_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_9_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_9_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_9_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_10_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_10_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_10_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_10_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_10_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_10_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_11_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_11_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_11_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_11_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_11_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_11_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_12_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_12_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_12_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_12_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_12_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_12_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_13_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_13_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_13_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_13_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_13_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_13_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_14_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_14_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_14_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_14_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_14_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_14_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_15_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_15_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_15_AVAILABLE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_15_AVAILABLE_BUTTON_TYPE).setEnabled(true);
             //}
         }
        
        if (levelwon==15){
        //if(level==levelwon){
        guiButtons.get(LEVEL_1_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_1_BUTTON_TYPE).setEnabled(false); 
        guiButtons.get(LEVEL_1_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_1_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_2_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_2_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_2_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_3_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_3_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_3_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_3_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_3_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_3_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_4_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_4_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_4_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_4_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_4_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_4_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_5_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_5_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_5_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_5_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_5_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_5_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_6_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_6_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_6_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_6_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_6_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_6_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_7_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_7_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_7_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_7_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_7_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_7_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_8_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_8_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_8_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_8_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_8_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_8_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_9_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_9_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_9_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_9_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_9_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_9_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_10_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_10_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_10_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_10_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_10_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_10_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_11_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_11_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_11_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_11_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_11_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_11_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_12_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_12_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_12_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_12_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_12_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_12_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_13_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_13_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_13_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_13_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_13_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_13_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_14_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_14_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_14_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_14_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_14_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_14_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_15_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_15_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_15_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_15_AVAILABLE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_15_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_15_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_16_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_16_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_16_AVAILABLE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_16_AVAILABLE_BUTTON_TYPE).setEnabled(true);
             //}
         }
    
        if (levelwon==16){
        //if(level==levelwon){
        guiButtons.get(LEVEL_1_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_1_BUTTON_TYPE).setEnabled(false); 
        guiButtons.get(LEVEL_1_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_1_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_2_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_2_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_2_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_3_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_3_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_3_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_3_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_3_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_3_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_4_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_4_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_4_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_4_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_4_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_4_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_5_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_5_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_5_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_5_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_5_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_5_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_6_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_6_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_6_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_6_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_6_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_6_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_7_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_7_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_7_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_7_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_7_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_7_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_8_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_8_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_8_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_8_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_8_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_8_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_9_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_9_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_9_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_9_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_9_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_9_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_10_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_10_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_10_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_10_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_10_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_10_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_11_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_11_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_11_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_11_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_11_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_11_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_12_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_12_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_12_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_12_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_12_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_12_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_13_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_13_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_13_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_13_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_13_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_13_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_14_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_14_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_14_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_14_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_14_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_14_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_15_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_15_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_15_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_15_AVAILABLE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_15_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_15_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_16_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_16_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_16_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_16_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_16_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_16_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_17_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_17_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_17_AVAILABLE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_17_AVAILABLE_BUTTON_TYPE).setEnabled(true);
             //}
         }
         if (levelwon==17){
        
        guiButtons.get(LEVEL_1_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_1_BUTTON_TYPE).setEnabled(false); 
        guiButtons.get(LEVEL_1_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_1_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_2_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_2_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_2_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_3_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_3_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_3_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_3_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_3_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_3_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_4_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_4_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_4_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_4_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_4_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_4_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_5_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_5_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_5_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_5_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_5_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_5_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_6_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_6_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_6_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_6_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_6_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_6_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_7_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_7_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_7_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_7_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_7_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_7_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_8_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_8_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_8_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_8_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_8_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_8_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_9_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_9_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_9_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_9_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_9_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_9_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_10_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_10_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_10_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_10_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_10_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_10_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_11_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_11_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_11_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_11_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_11_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_11_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_12_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_12_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_12_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_12_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_12_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_12_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_13_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_13_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_13_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_13_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_13_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_13_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_14_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_14_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_14_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_14_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_14_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_14_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_15_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_15_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_15_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_15_AVAILABLE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_15_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_15_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_16_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_16_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_16_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_16_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_16_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_16_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_17_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_17_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_17_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_17_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_17_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_17_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_18_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_18_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_18_AVAILABLE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_18_AVAILABLE_BUTTON_TYPE).setEnabled(true);
             
         }
        if (levelwon==18){
        //if(level==levelwon){
        guiButtons.get(LEVEL_1_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_1_BUTTON_TYPE).setEnabled(false); 
        guiButtons.get(LEVEL_1_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_1_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_2_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_2_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_2_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_3_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_3_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_3_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_3_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_3_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_3_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_4_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_4_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_4_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_4_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_4_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_4_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_5_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_5_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_5_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_5_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_5_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_5_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_6_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_6_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_6_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_6_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_6_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_6_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_7_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_7_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_7_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_7_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_7_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_7_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_8_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_8_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_8_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_8_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_8_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_8_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_9_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_9_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_9_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_9_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_9_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_9_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_10_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_10_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_10_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_10_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_10_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_10_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_11_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_11_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_11_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_11_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_11_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_11_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_12_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_12_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_12_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_12_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_12_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_12_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_13_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_13_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_13_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_13_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_13_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_13_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_14_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_14_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_14_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_14_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_14_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_14_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_15_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_15_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_15_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_15_AVAILABLE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_15_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_15_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_16_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_16_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_16_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_16_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_16_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_16_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_17_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_17_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_17_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_17_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_17_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_17_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_18_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_18_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_18_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_18_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_18_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_18_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_19_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_19_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_19_AVAILABLE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_19_AVAILABLE_BUTTON_TYPE).setEnabled(true);
             //}
         }
                
         if (levelwon==19){
        //if(level==levelwon){
        guiButtons.get(LEVEL_1_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_1_BUTTON_TYPE).setEnabled(false); 
        guiButtons.get(LEVEL_1_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_1_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_2_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_2_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_2_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_3_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_3_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_3_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_3_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_3_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_3_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_4_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_4_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_4_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_4_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_4_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_4_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_5_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_5_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_5_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_5_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_5_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_5_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_6_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_6_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_6_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_6_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_6_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_6_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_7_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_7_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_7_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_7_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_7_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_7_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_8_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_8_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_8_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_8_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_8_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_8_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_9_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_9_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_9_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_9_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_9_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_9_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_10_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_10_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_10_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_10_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_10_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_10_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_11_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_11_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_11_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_11_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_11_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_11_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_12_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_12_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_12_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_12_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_12_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_12_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_13_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_13_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_13_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_13_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_13_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_13_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_14_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_14_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_14_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_14_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_14_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_14_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_15_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_15_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_15_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_15_AVAILABLE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_15_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_15_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_16_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_16_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_16_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_16_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_16_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_16_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_17_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_17_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_17_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_17_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_17_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_17_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_18_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_18_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_18_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_18_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_18_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_18_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_19_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_19_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_19_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_19_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_19_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_19_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_20_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_20_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_20_AVAILABLE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_20_AVAILABLE_BUTTON_TYPE).setEnabled(true);
            // }
         }
         
        if (levelwon==20){
        //if(level==levelwon){
        guiButtons.get(LEVEL_1_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_1_BUTTON_TYPE).setEnabled(false); 
        guiButtons.get(LEVEL_1_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_1_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_2_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_2_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_2_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_3_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_3_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_3_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_3_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_3_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_3_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_4_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_4_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_4_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_4_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_4_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_4_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_5_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_5_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_5_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_5_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_5_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_5_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_6_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_6_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_6_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_6_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_6_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_6_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_7_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_7_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_7_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_7_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_7_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_7_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_8_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_8_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_8_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_8_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_8_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_8_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_9_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_9_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_9_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_9_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_9_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_9_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_10_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_10_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_10_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_10_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_10_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_10_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_11_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_11_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_11_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_11_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_11_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_11_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_12_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_12_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_12_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_12_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_12_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_12_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_13_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_13_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_13_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_13_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_13_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_13_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_14_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_14_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_14_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_14_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_14_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_14_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_15_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_15_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_15_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_15_AVAILABLE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_15_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_15_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_16_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_16_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_16_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_16_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_16_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_16_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_17_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_17_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_17_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_17_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_17_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_17_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_18_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_18_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_18_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_18_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_18_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_18_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_19_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_19_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_19_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_19_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_19_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_19_COMPLETE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEVEL_20_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_20_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_20_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_20_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_20_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(LEVEL_20_COMPLETE_BUTTON_TYPE).setEnabled(true);
             //}
         } 
         
        guiDecor.get(PLAYER_TYPE).setState(INVISIBLE_STATE.toString());
        guiDecor.get(PLAYER_TYPE).setEnabled(false);
        guiDecor.get(POLICE_TYPE).setState(INVISIBLE_STATE.toString());
        guiDecor.get(POLICE_TYPE).setEnabled(false);
        guiDecor.get(POLICE_TYPE_2).setState(INVISIBLE_STATE.toString());
        guiDecor.get(POLICE_TYPE_2).setEnabled(false);
        guiDecor.get(POLICE_TYPE_3).setState(INVISIBLE_STATE.toString());
        guiDecor.get(POLICE_TYPE_3).setEnabled(false);
        guiDecor.get(POLICE_TYPE_4).setState(INVISIBLE_STATE.toString());
        guiDecor.get(POLICE_TYPE_4).setEnabled(false);
        guiDecor.get(POLICE_TYPE_5).setState(INVISIBLE_STATE.toString());
        guiDecor.get(POLICE_TYPE_5).setEnabled(false);
        guiDecor.get(POLICE_TYPE_6).setState(INVISIBLE_STATE.toString());
        guiDecor.get(POLICE_TYPE_6).setEnabled(false);
        guiButtons.get(MUSIC_SELECTED_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(MUSIC_SELECTED_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(CLOSE_DIALOG_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(CLOSE_DIALOG_BUTTON_TYPE).setEnabled(false);
        
        guiButtons.get(SPECIAL_GREENLIGHT_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(SPECIAL_GREENLIGHT_TYPE).setEnabled(false);
        guiButtons.get(SPECIAL_REDLIGHT_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(SPECIAL_REDLIGHT_TYPE).setEnabled(false);
        guiButtons.get( SPECIAL_FREEZETIME_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get( SPECIAL_FREEZETIME_TYPE).setEnabled(false);
        guiButtons.get( SPECIAL_DECREASE_SPEED_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get( SPECIAL_DECREASE_SPEED_TYPE).setEnabled(false);
        guiButtons.get( SPECIAL_INCREASE_SPEED_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get( SPECIAL_INCREASE_SPEED_TYPE).setEnabled(false);
        guiButtons.get( SPECIAL_PLAYER_INCREASE_SPEED_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get( SPECIAL_PLAYER_INCREASE_SPEED_TYPE).setEnabled(false);
        guiButtons.get( SPECIAL_FLAT_TIRE).setState(INVISIBLE_STATE.toString());
        guiButtons.get( SPECIAL_FLAT_TIRE).setEnabled(false);
        guiButtons.get(  SPECIAL_EMPTY_GAS).setState(INVISIBLE_STATE.toString());
        guiButtons.get(  SPECIAL_EMPTY_GAS).setEnabled(false);
        guiButtons.get(SPECIAL_CLOSE_ROAD).setState(INVISIBLE_STATE.toString());
        guiButtons.get(SPECIAL_CLOSE_ROAD).setEnabled(false);
        guiButtons.get(  SPECIAL_CLOSE_INTERSECTION).setState(INVISIBLE_STATE.toString());
        guiButtons.get(  SPECIAL_CLOSE_INTERSECTION).setEnabled(false);
        guiButtons.get( SPECIAL_OPEN_INTERSECTION).setState(INVISIBLE_STATE.toString());
        guiButtons.get(SPECIAL_OPEN_INTERSECTION).setEnabled(false);
        guiButtons.get(SPECIAL_STEAL).setState(INVISIBLE_STATE.toString());
        guiButtons.get(SPECIAL_STEAL).setEnabled(false);
        guiButtons.get(SPECIAL_MIND_CONTROL).setState(INVISIBLE_STATE.toString());
        guiButtons.get(SPECIAL_MIND_CONTROL).setEnabled(false);
        guiButtons.get(SPECIAL_INTANG).setState(INVISIBLE_STATE.toString());
        guiButtons.get(SPECIAL_INTANG).setEnabled(false);
        guiButtons.get(SPECIAL_MINDLESS_TERROR).setState(INVISIBLE_STATE.toString());
        guiButtons.get(SPECIAL_MINDLESS_TERROR).setEnabled(false);
        guiButtons.get(SPECIAL_FLYING).setState(INVISIBLE_STATE.toString());
        guiButtons.get(SPECIAL_FLYING).setEnabled(false);
        guiButtons.get(SPECIAL_INVINCIBILITY).setState(INVISIBLE_STATE.toString());
        guiButtons.get(SPECIAL_INVINCIBILITY).setEnabled(false);
        currentScreenState = LEVEL_SELECT_SCREEN_STATE;

        guiDialogs.get(SETTINGS_DIALOG_TYPE).setState(INVISIBLE_STATE.toString());
        guiDialogs.get(LEVEL_INFO_DIALOG_TYPE).setState(INVISIBLE_STATE.toString());
        MakeWinDialogInvisible();
         guiDialogs.get(LEVEL_FAIL_DIALOG_TYPE).setState(INVISIBLE_STATE.toString());
        // PLAY THE GAMEPLAY SCREEN SONG
        if (enableMusic==true){
        audio.stop(PathXPropertyType.SONG_CUE_MENU_SCREEN.toString());
        audio.stop(PathXPropertyType.SONG_CUE_GAME_SCREEN.toString());
        audio.play(PathXPropertyType.SONG_CUE_LEVEL_SCREEN.toString(), true);
        }
        else{
        audio.stop(PathXPropertyType.SONG_CUE_MENU_SCREEN.toString());
        audio.stop(PathXPropertyType.SONG_CUE_GAME_SCREEN.toString());
        audio.stop(PathXPropertyType.SONG_CUE_LEVEL_SCREEN.toString());
        }
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
        guiButtons.get(LEVEL_1_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_1_COMPLETE_BUTTON_TYPE).setEnabled(false); 
        guiButtons.get(LEVEL_2_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_2_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_2_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_2_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_3_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_3_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_3_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_3_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_3_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_3_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_4_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_4_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_4_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_4_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_4_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_4_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_5_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_5_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_5_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_5_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_5_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_5_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_6_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_6_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_6_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_6_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_6_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_6_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_7_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_7_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_7_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_7_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_7_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_7_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_8_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_8_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_8_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_8_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_8_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_8_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_9_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_9_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_9_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_9_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_9_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_9_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_10_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_10_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_10_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_10_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_10_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_10_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_11_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_11_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_11_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_11_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_11_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_11_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_12_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_12_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_12_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_12_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_12_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_12_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_13_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_13_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_13_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_13_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_13_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_13_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_14_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_14_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_14_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_14_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_14_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_14_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_15_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_15_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_15_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_15_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_15_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_15_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_16_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_16_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_16_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_16_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_16_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_16_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_17_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_17_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_17_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_17_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_17_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_17_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_18_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_18_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_18_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_18_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_18_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_18_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_19_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_19_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_19_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_19_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_19_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_19_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_20_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_20_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_20_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_20_COMPLETE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(LEVEL_20_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(LEVEL_20_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(CLOSE_DIALOG_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(CLOSE_DIALOG_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(PAUSE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(PAUSE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SPECIAL_GREENLIGHT_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(SPECIAL_GREENLIGHT_TYPE).setEnabled(false);
        guiButtons.get(SPECIAL_REDLIGHT_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(SPECIAL_REDLIGHT_TYPE).setEnabled(false);
        guiButtons.get( SPECIAL_FREEZETIME_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get( SPECIAL_FREEZETIME_TYPE).setEnabled(false);
        guiButtons.get( SPECIAL_DECREASE_SPEED_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get( SPECIAL_DECREASE_SPEED_TYPE).setEnabled(false);
        guiButtons.get( SPECIAL_INCREASE_SPEED_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get( SPECIAL_INCREASE_SPEED_TYPE).setEnabled(false);
        guiButtons.get( SPECIAL_PLAYER_INCREASE_SPEED_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get( SPECIAL_PLAYER_INCREASE_SPEED_TYPE).setEnabled(false);
        guiButtons.get( SPECIAL_FLAT_TIRE).setState(INVISIBLE_STATE.toString());
        guiButtons.get( SPECIAL_FLAT_TIRE).setEnabled(false);
        guiButtons.get(  SPECIAL_EMPTY_GAS).setState(INVISIBLE_STATE.toString());
        guiButtons.get(  SPECIAL_EMPTY_GAS).setEnabled(false);
        guiButtons.get(SPECIAL_CLOSE_ROAD).setState(INVISIBLE_STATE.toString());
        guiButtons.get(SPECIAL_CLOSE_ROAD).setEnabled(false);
        guiButtons.get(  SPECIAL_CLOSE_INTERSECTION).setState(INVISIBLE_STATE.toString());
        guiButtons.get(  SPECIAL_CLOSE_INTERSECTION).setEnabled(false);
        guiButtons.get( SPECIAL_OPEN_INTERSECTION).setState(INVISIBLE_STATE.toString());
        guiButtons.get(SPECIAL_OPEN_INTERSECTION).setEnabled(false);
         guiButtons.get(SPECIAL_STEAL).setState(INVISIBLE_STATE.toString());
        guiButtons.get(SPECIAL_STEAL).setEnabled(false);
        guiButtons.get(SPECIAL_MIND_CONTROL).setState(INVISIBLE_STATE.toString());
        guiButtons.get(SPECIAL_MIND_CONTROL).setEnabled(false);
        guiButtons.get(SPECIAL_INTANG).setState(INVISIBLE_STATE.toString());
        guiButtons.get(SPECIAL_INTANG).setEnabled(false);
        guiButtons.get(SPECIAL_MINDLESS_TERROR).setState(INVISIBLE_STATE.toString());
        guiButtons.get(SPECIAL_MINDLESS_TERROR).setEnabled(false);
        guiButtons.get(SPECIAL_FLYING).setState(INVISIBLE_STATE.toString());
        guiButtons.get(SPECIAL_FLYING).setEnabled(false);
        guiButtons.get(SPECIAL_INVINCIBILITY).setState(INVISIBLE_STATE.toString());
        guiButtons.get(SPECIAL_INVINCIBILITY).setEnabled(false);
        guiDecor.get(PLAYER_TYPE).setState(INVISIBLE_STATE.toString());
        guiDecor.get(PLAYER_TYPE).setEnabled(false);
        guiDecor.get(POLICE_TYPE).setState(INVISIBLE_STATE.toString());
        guiDecor.get(POLICE_TYPE).setEnabled(false);
        guiDecor.get(POLICE_TYPE_2).setState(INVISIBLE_STATE.toString());
        guiDecor.get(POLICE_TYPE_2).setEnabled(false);
        guiDecor.get(POLICE_TYPE_3).setState(INVISIBLE_STATE.toString());
        guiDecor.get(POLICE_TYPE_3).setEnabled(false);
        guiDecor.get(POLICE_TYPE_4).setState(INVISIBLE_STATE.toString());
        guiDecor.get(POLICE_TYPE_4).setEnabled(false);
        guiDecor.get(POLICE_TYPE_5).setState(INVISIBLE_STATE.toString());
        guiDecor.get(POLICE_TYPE_5).setEnabled(false);
        guiDecor.get(POLICE_TYPE_6).setState(INVISIBLE_STATE.toString());
        guiDecor.get(POLICE_TYPE_6).setEnabled(false);
        guiDecor.get(BANDIT_TYPE).setState(INVISIBLE_STATE.toString());
        guiDecor.get(BANDIT_TYPE).setEnabled(false);
        guiDecor.get(BANDIT_TYPE_2).setState(INVISIBLE_STATE.toString());
        guiDecor.get(BANDIT_TYPE_2).setEnabled(false);
        guiDecor.get(BANDIT_TYPE_3).setState(INVISIBLE_STATE.toString());
        guiDecor.get(BANDIT_TYPE_3).setEnabled(false);
        guiDecor.get(BANDIT_TYPE_4).setState(INVISIBLE_STATE.toString());
        guiDecor.get(BANDIT_TYPE_4).setEnabled(false);
        guiDecor.get(BANDIT_TYPE_5).setState(INVISIBLE_STATE.toString());
        guiDecor.get(BANDIT_TYPE_5).setEnabled(false);
        guiDecor.get(BANDIT_TYPE_6).setState(INVISIBLE_STATE.toString());
        guiDecor.get(BANDIT_TYPE_6).setEnabled(false);
        guiDecor.get(ZOMBIE_TYPE).setState(INVISIBLE_STATE.toString());
        guiDecor.get(ZOMBIE_TYPE).setEnabled(false);
        guiButtons.get(MUSIC_SELECTED_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(MUSIC_SELECTED_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(MUSIC_UNSELECTED_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(MUSIC_UNSELECTED_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SOUND_SELECTED_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(SOUND_SELECTED_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SOUND_UNSELECTED_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(SOUND_UNSELECTED_BUTTON_TYPE).setEnabled(false);
        currentScreenState = MENU_SCREEN_STATE;
        data.setGameState(MiniGameState.NOT_STARTED);
        
        if (enableMusic==true){
        audio.play(PathXPropertyType.SONG_CUE_MENU_SCREEN.toString(), true);
        audio.stop(PathXPropertyType.SONG_CUE_LEVEL_SCREEN.toString());
        audio.stop(PathXPropertyType.SONG_CUE_GAME_SCREEN.toString());
        }
        else{
             
        audio.stop(PathXPropertyType.SONG_CUE_MENU_SCREEN.toString());
        audio.stop(PathXPropertyType.SONG_CUE_LEVEL_SCREEN.toString());
        audio.stop(PathXPropertyType.SONG_CUE_GAME_SCREEN.toString());
        }
        guiDialogs.get(SETTINGS_DIALOG_TYPE).setState(INVISIBLE_STATE.toString());
        guiDialogs.get(LEVEL_INFO_DIALOG_TYPE).setState(INVISIBLE_STATE.toString());
        MakeWinDialogInvisible();
        guiDialogs.get(LEVEL_FAIL_DIALOG_TYPE).setState(INVISIBLE_STATE.toString());
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
    public void MakeLoseDialogInvisible(){
        guiDialogs.get(LEVEL_FAIL_DIALOG_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(TRY_AGAIN_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(TRY_AGAIN_TYPE).setEnabled(false);
        guiButtons.get(QUIT_LEVEL_TYPE).setState(VISIBLE_STATE.toString());
        guiButtons.get(QUIT_LEVEL_TYPE).setEnabled(false);
    }
    public void MakeWinDialogInvisible(){
        guiDialogs.get(LEVEL_COMPLETE_DIALOG_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(TRY_AGAIN_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(TRY_AGAIN_TYPE).setEnabled(false);
        guiButtons.get(QUIT_LEVEL_TYPE).setState(INVISIBLE_STATE.toString());
        guiButtons.get(QUIT_LEVEL_TYPE).setEnabled(false);
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
         guiButtons.get(TRY_AGAIN_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToTryAgain();
            }

        });
          guiButtons.get(QUIT_LEVEL_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToQuitLevel();
            }

        });
          guiButtons.get(SPECIAL_GREENLIGHT_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToGreenLight();
            }

        });
           guiButtons.get(SPECIAL_REDLIGHT_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToRedLight();
            }

        });
           guiButtons.get(SPECIAL_FREEZETIME_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToFreeze();
            }

        });
              guiButtons.get(SPECIAL_DECREASE_SPEED_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToSpeedLimit();
            }

        });
                guiButtons.get(SPECIAL_INCREASE_SPEED_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToIncreaseSpeedLimit();
            }

        });
             guiButtons.get(SPECIAL_PLAYER_INCREASE_SPEED_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToPlayerSpeed();
            }

        });   
                guiButtons.get( SPECIAL_FLAT_TIRE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToFlatTire();
            }

        }); 
                   guiButtons.get( SPECIAL_EMPTY_GAS).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToEmptyGas();
            }

        }); 
                 
                   guiButtons.get( SPECIAL_CLOSE_ROAD).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToCloseRoad();
            }
             }); 
             
                       guiButtons.get( SPECIAL_OPEN_INTERSECTION).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToOpenIntersection();
            }
             }); 
                   guiButtons.get( SPECIAL_CLOSE_INTERSECTION).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToCloseIntersection();
            }
             }); 
                 guiButtons.get( SPECIAL_STEAL).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToSteal();
            }
             }); 
                guiButtons.get( SPECIAL_MIND_CONTROL).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToMindControl();
            }
             }); 
                  guiButtons.get(SPECIAL_INTANG).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToIntangibility();
            }
             });
             guiButtons.get( SPECIAL_MINDLESS_TERROR).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToMindlessTerror();
            }
             });
                 guiButtons.get( SPECIAL_FLYING).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToFlying();
            }
             });
               guiButtons.get( SPECIAL_INVINCIBILITY).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.respondToInvincibility();
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
                
                level=1;
                if(level<=access)
                eventHandler.respondToGameScreenSelect(level);
            }

        });
         guiButtons.get(LEVEL_1_COMPLETE_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                
                level=1;
                if(level<=access)
                eventHandler.respondToGameScreenSelect(level);
            }

        });
        
         guiButtons.get(LEVEL_2_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                level=2;
                 if(level<=access)
                eventHandler.respondToGameScreenSelect(level);
            }

        });
           guiButtons.get(LEVEL_2_COMPLETE_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                
                level=2;
                if(level<=access)
                eventHandler.respondToGameScreenSelect(level);
            }

        });
           guiButtons.get(LEVEL_2_AVAILABLE_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                
                level=2;
                if(level<=access)
                eventHandler.respondToGameScreenSelect(level);
            }

        });
           guiButtons.get(LEVEL_3_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                level=3;
                 if(level<=access)
                eventHandler.respondToGameScreenSelect(level);
            }

        });
           guiButtons.get(LEVEL_3_COMPLETE_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                
                level=3;
                if(level<=access)
                eventHandler.respondToGameScreenSelect(level);
            }

        });
           guiButtons.get(LEVEL_3_AVAILABLE_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                
                level=3;
                if(level<=access)
                eventHandler.respondToGameScreenSelect(level);
            }

        });
             guiButtons.get(LEVEL_4_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                level=4;
                 if(level<=access)
                eventHandler.respondToGameScreenSelect(level);
            }

        });
             guiButtons.get(LEVEL_4_COMPLETE_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                
                level=4;
                if(level<=access)
                eventHandler.respondToGameScreenSelect(level);
            }

        });
           guiButtons.get(LEVEL_4_AVAILABLE_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                
                level=4;
                if(level<=access)
                eventHandler.respondToGameScreenSelect(level);
            }

        });
              guiButtons.get(LEVEL_5_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                level=5;
                 if(level<=access)
                eventHandler.respondToGameScreenSelect(level);
            }

        });
              
            guiButtons.get(LEVEL_5_COMPLETE_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                
                level=5;
                if(level<=access)
                eventHandler.respondToGameScreenSelect(level);
            }

        });
           guiButtons.get(LEVEL_5_AVAILABLE_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                
                level=5;
                if(level<=access)
                eventHandler.respondToGameScreenSelect(level);
            }

        });
            guiButtons.get(LEVEL_6_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                level=6;
                 if(level<=access)
                eventHandler.respondToGameScreenSelect(level);
            }

        });
        guiButtons.get(LEVEL_6_AVAILABLE_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                
                level=6;
                if(level<=access)
                eventHandler.respondToGameScreenSelect(level);
            }

        });
                  guiButtons.get(LEVEL_6_COMPLETE_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                
                level=6;
                if(level<=access)
                eventHandler.respondToGameScreenSelect(level);
            }

        });

                  guiButtons.get(LEVEL_7_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                level=7;
                 if(level<=access)
                eventHandler.respondToGameScreenSelect(level);
            }

        });
        guiButtons.get(LEVEL_7_COMPLETE_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                
                level=7;
                if(level<=access)
                eventHandler.respondToGameScreenSelect(level);
            }

        });
           guiButtons.get(LEVEL_7_AVAILABLE_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                
                level=7;
                if(level<=access)
                eventHandler.respondToGameScreenSelect(level);
            }

        });
            guiButtons.get(LEVEL_8_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                level=8;
                 if(level<=access)
                eventHandler.respondToGameScreenSelect(level);
            }

        });
           guiButtons.get(LEVEL_8_COMPLETE_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                
                level=8;
                if(level<=access)
                eventHandler.respondToGameScreenSelect(level);
            }

        });
           guiButtons.get(LEVEL_8_AVAILABLE_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                
                level=8;
                if(level<=access)
                eventHandler.respondToGameScreenSelect(level);
            }

        });
             guiButtons.get(LEVEL_9_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                level=9;
                 if(level<=access)
                eventHandler.respondToGameScreenSelect(level);
            }

        });
           guiButtons.get(LEVEL_9_COMPLETE_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                
                level=9;
                if(level<=access)
                eventHandler.respondToGameScreenSelect(level);
            }

        });
           guiButtons.get(LEVEL_9_AVAILABLE_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                
                level=9;
                if(level<=access)
                eventHandler.respondToGameScreenSelect(level);
            }

        });
           guiButtons.get(LEVEL_10_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                level=10;
                if(level<=access)
                eventHandler.respondToGameScreenSelect(level);
            }

        });
          guiButtons.get(LEVEL_10_COMPLETE_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                
                level=10;
                if(level<=access)
                eventHandler.respondToGameScreenSelect(level);
            }

        });
           guiButtons.get(LEVEL_10_AVAILABLE_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                
                level=10;
                if(level<=access)
                eventHandler.respondToGameScreenSelect(level);
            }

        });
          guiButtons.get(LEVEL_11_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                level=11;
                if(level<=access)
                eventHandler.respondToGameScreenSelect(level);
            }

        });
            guiButtons.get(LEVEL_11_COMPLETE_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                
                level=11;
                if(level<=access)
                eventHandler.respondToGameScreenSelect(level);
            }

        });
           guiButtons.get(LEVEL_11_AVAILABLE_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                
                level=11;
                if(level<=access)
                eventHandler.respondToGameScreenSelect(level);
            }

        });
         guiButtons.get(LEVEL_12_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                level=12;
                if(level<=access)
                eventHandler.respondToGameScreenSelect(level);
            }

        });
            guiButtons.get(LEVEL_12_COMPLETE_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                
                level=12;
                if(level<=access)
                eventHandler.respondToGameScreenSelect(level);
            }

        });
           guiButtons.get(LEVEL_12_AVAILABLE_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                
                level=12;
                if(level<=access)
                eventHandler.respondToGameScreenSelect(level);
            }

        });
           guiButtons.get(LEVEL_13_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                level=13;
                if(level<=access)
                eventHandler.respondToGameScreenSelect(level);
            }

        });
            guiButtons.get(LEVEL_13_COMPLETE_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                
                level=13;
                if(level<=access)
                eventHandler.respondToGameScreenSelect(level);
            }

        });
           guiButtons.get(LEVEL_13_AVAILABLE_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                
                level=13;
                if(level<=access)
                eventHandler.respondToGameScreenSelect(level);
            }

        });
            guiButtons.get(LEVEL_14_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                level=14;
                if(level<=access)
                eventHandler.respondToGameScreenSelect(level);
            }

        });
          guiButtons.get(LEVEL_14_COMPLETE_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                
                level=14;
                if(level<=access)
                eventHandler.respondToGameScreenSelect(level);
            }

        });
           guiButtons.get(LEVEL_14_AVAILABLE_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                
                level=14;
                if(level<=access)
                eventHandler.respondToGameScreenSelect(level);
            }

        });
             guiButtons.get(LEVEL_15_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                level=15;
                if(level<=access)
                eventHandler.respondToGameScreenSelect(level);
            }

        });
            guiButtons.get(LEVEL_15_COMPLETE_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                
                level=15;
                if(level<=access)
                eventHandler.respondToGameScreenSelect(level);
            }

        });
           guiButtons.get(LEVEL_15_AVAILABLE_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                
                level=15;
                if(level<=access)
                eventHandler.respondToGameScreenSelect(level);
            }

        });
              guiButtons.get(LEVEL_16_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                level=16;
                if(level<=access)
                eventHandler.respondToGameScreenSelect(level);
            }

        });
        guiButtons.get(LEVEL_16_COMPLETE_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                
                level=16;
                if(level<=access)
                eventHandler.respondToGameScreenSelect(level);
            }

        });
           guiButtons.get(LEVEL_16_AVAILABLE_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                
                level=16;
                if(level<=access)
                eventHandler.respondToGameScreenSelect(level);
            }

        });
            guiButtons.get(LEVEL_17_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                level=17;
                if(level<=access)
                eventHandler.respondToGameScreenSelect(level);
            }

        });
           guiButtons.get(LEVEL_17_COMPLETE_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                
                level=17;
                if(level<=access)
                eventHandler.respondToGameScreenSelect(level);
            }

        });
           guiButtons.get(LEVEL_17_AVAILABLE_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                
                level=17;
                if(level<=access)
                eventHandler.respondToGameScreenSelect(level);
            }

        });
             guiButtons.get(LEVEL_18_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                level=18;
                if(level<=access)
                eventHandler.respondToGameScreenSelect(level);
            }

        });
             guiButtons.get(LEVEL_18_COMPLETE_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                
                level=18;
                if(level<=access)
                eventHandler.respondToGameScreenSelect(level);
            }

        });
           guiButtons.get(LEVEL_18_AVAILABLE_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                
                level=18;
                if(level<=access)
                eventHandler.respondToGameScreenSelect(level);
            }

        });
              guiButtons.get(LEVEL_19_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                level=19;
                if(level<=access)
                eventHandler.respondToGameScreenSelect(level);
            }

        });
          guiButtons.get(LEVEL_19_COMPLETE_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                
                level=19;
                if(level<=access)
                eventHandler.respondToGameScreenSelect(level);
            }

        });
           guiButtons.get(LEVEL_19_AVAILABLE_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                
                level=19;
                if(level<=access)
                eventHandler.respondToGameScreenSelect(level);
            }

        });
               guiButtons.get(LEVEL_20_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                level=20;
                if(level<=access)
                eventHandler.respondToGameScreenSelect(level);
            }

        });
            guiButtons.get(LEVEL_20_COMPLETE_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                
                level=20;
                if(level<=access)
                eventHandler.respondToGameScreenSelect(level);
            }

        });
           guiButtons.get(LEVEL_20_AVAILABLE_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                
                level=20;
                if(level<=access)
                eventHandler.respondToGameScreenSelect(level);
            }

        });
        guiButtons.get(RIGHT_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                ScrollRight();
                ScrollRight();
                ScrollRight();
            }

        });
        guiButtons.get(MUSIC_SELECTED_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
              eventHandler.respondToMusicSelected();
              eventHandler.respondToSettingsSelect();
            }

        });
        guiButtons.get(MUSIC_UNSELECTED_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
              eventHandler.respondToMusicUnSelected();
              eventHandler.respondToSettingsSelect();
              
            }

        });
           guiButtons.get(SOUND_SELECTED_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
              eventHandler.respondToSoundSelected();
              eventHandler.respondToSettingsSelect();
            }

        });
        guiButtons.get(SOUND_UNSELECTED_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
              eventHandler.respondToSoundUnSelected();
              eventHandler.respondToSettingsSelect();
              
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
       
        data = new PathXDataModel(this);
        fileManager = new PathXFileManager(this, (PathXDataModel)data);
        graph= new PathXGameGraph(this);
       ((PathXDataModel)data).setGraph(graph);
        //System.out.println("no problem");
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
            loadAudioCue(PathXPropertyType.AUDIO_CUE_INCREASE_SPEED_LIMIT);
            loadAudioCue(PathXPropertyType.AUDIO_CUE_DECREASE_SPEED_LIMIT);
            loadAudioCue(PathXPropertyType.AUDIO_CUE_TIME_FREEZE);
            loadAudioCue(PathXPropertyType.AUDIO_CUE_GREEN_LIGHT);
            loadAudioCue(PathXPropertyType.AUDIO_CUE_RED_LIGHT);
            loadAudioCue(PathXPropertyType.AUDIO_CUE_PLAYER_SPEED);
            loadAudioCue(PathXPropertyType.AUDIO_CUE_FLAT_TIRE);
            loadAudioCue(PathXPropertyType.AUDIO_CUE_GAS);
            loadAudioCue(PathXPropertyType.AUDIO_CUE_CLOSE_ROAD);
            loadAudioCue(PathXPropertyType.AUDIO_CUE_CLOSE_INTERSECTION);
            loadAudioCue(PathXPropertyType.AUDIO_CUE_OPEN_INTERSECTION);
            loadAudioCue(PathXPropertyType.AUDIO_CUE_STEAL);
            loadAudioCue(PathXPropertyType.AUDIO_CUE_MIND_CONTROL);
            loadAudioCue(PathXPropertyType.AUDIO_CUE_MIND_TERROR);
            loadAudioCue(PathXPropertyType.AUDIO_CUE_INTANGIBILITY);
             loadAudioCue(PathXPropertyType.AUDIO_CUE_INVINCIBILITY);
             loadAudioCue(PathXPropertyType.AUDIO_CUE_FLYING);
            loadAudioCue(PathXPropertyType.SONG_CUE_MENU_SCREEN);
            loadAudioCue(PathXPropertyType.SONG_CUE_GAME_SCREEN);
            loadAudioCue(PathXPropertyType.SONG_CUE_LEVEL_SCREEN);
            loadAudioCue(PathXPropertyType.AUDIO_CUE_POLICE);
            loadAudioCue(PathXPropertyType.AUDIO_CUE_BANDIT);
            loadAudioCue(PathXPropertyType.AUDIO_CUE_ZOMBIE);

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
    
      /**
       * The scrolling methods for the level screen viewport
       */

    public void ScrollUp() {
        if(currentScreenState.equals(GAME_SCREEN_STATE)){
          PathXDataModel d = (PathXDataModel) data;
            //System.out.println(d.getLevel().getIntersections());
            ArrayList<Intersection> intersections = d.getLevel().getIntersections();
              Sprite player = guiDecor.get(PLAYER_TYPE);
              Sprite police1= guiDecor.get(POLICE_TYPE);
              Sprite police2= guiDecor.get(POLICE_TYPE_2);
              Sprite police3= guiDecor.get(POLICE_TYPE_3);
              Sprite police4= guiDecor.get(POLICE_TYPE_4);
              Sprite police5= guiDecor.get(POLICE_TYPE_5);
              Sprite police6= guiDecor.get(POLICE_TYPE_6);
              Sprite bandit1= guiDecor.get(BANDIT_TYPE);
              Sprite bandit2= guiDecor.get(BANDIT_TYPE_2);
              Sprite bandit3= guiDecor.get(BANDIT_TYPE_3);
              Sprite bandit4= guiDecor.get(BANDIT_TYPE_4);
              Sprite bandit5= guiDecor.get(BANDIT_TYPE_5);
              Sprite bandit6= guiDecor.get(BANDIT_TYPE_6);
            for (Intersection i: intersections) {
                i.setY(i.getY() - 15);
            }
            player.setY(player.getY()-15);
            police1.setY(police1.getY()-15);
            police2.setY(police2.getY()-15);
            police3.setY(police3.getY()-15);
            police4.setY(police4.getY()-15);
            police5.setY(police5.getY()-15);
            police6.setY(police6.getY()-15);
            bandit1.setY(bandit1.getY()-15);
            bandit2.setY(bandit2.getY()-15);
            bandit3.setY(bandit3.getY()-15);
            bandit4.setY(bandit4.getY()-15);
            bandit5.setY(bandit5.getY()-15);
            bandit6.setY(bandit6.getY()-15);
            //float x = guiDecor.get(PLAYER_TYPE).getX();
            //float y = guiDecor.get(PLAYER_TYPE).getY();
            //guiDecor.get(PLAYER_TYPE).setX(x);
           //guiDecor.get(PLAYER_TYPE).setY(y + 45);
            getCanvas().repaint();
           //Player playa=((PathXDataModel)data).getPlayer();
           //playa.setX(x);
           //playa.setY(y+45);
        }
        scrollNum = 0;
        boolean scrollvalue = getScroll();
        if (scrollvalue == true) {
            float x = guiDecor.get(BACKGROUND_TYPE).getX();
            float y = guiDecor.get(BACKGROUND_TYPE).getY();
            if ((y) < 0) {
                guiDecor.get(BACKGROUND_TYPE).setX(x);
                guiDecor.get(BACKGROUND_TYPE).setY(y + 15);
            }

            //System.out.println("y:" + y);
        }
        setLevelVisibility();
        updateGUI();
    }

    public void setLevelVisibility() {
        float x = guiDecor.get(BACKGROUND_TYPE).getX();
        float y = guiDecor.get(BACKGROUND_TYPE).getY();
       if (currentScreenState.equals(LEVEL_SELECT_SCREEN_STATE)){
        //level 1
        if (x < -45 || y < -285) {
            guiButtons.get(LEVEL_1_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
            guiButtons.get(LEVEL_1_BUTTON_TYPE).setEnabled(false);
            guiButtons.get(LEVEL_1_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
            guiButtons.get(LEVEL_1_COMPLETE_BUTTON_TYPE).setEnabled(false);
        } else {
            if(levelwon>=1){
            guiButtons.get(LEVEL_1_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
            guiButtons.get(LEVEL_1_COMPLETE_BUTTON_TYPE).setEnabled(true);
            }
            else{
            guiButtons.get(LEVEL_1_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
            guiButtons.get(LEVEL_1_BUTTON_TYPE).setEnabled(true);
            }
            
            
            guiButtons.get(LEVEL_1_BUTTON_TYPE).setX(LEVEL_1_X + x);
            guiButtons.get(LEVEL_1_BUTTON_TYPE).setY(LEVEL_1_Y + y);
            guiButtons.get(LEVEL_1_COMPLETE_BUTTON_TYPE).setX(LEVEL_1_X + x);
            guiButtons.get(LEVEL_1_COMPLETE_BUTTON_TYPE).setY(LEVEL_1_Y + y);

        }
        if (x < -45 || y < -390) {
            guiButtons.get(LEVEL_2_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
            guiButtons.get(LEVEL_2_BUTTON_TYPE).setEnabled(false);
            guiButtons.get(LEVEL_2_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
            guiButtons.get(LEVEL_2_COMPLETE_BUTTON_TYPE).setEnabled(false);
            guiButtons.get(LEVEL_2_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
            guiButtons.get(LEVEL_2_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        } else {
            
            if(levelwon>=2){
            guiButtons.get(LEVEL_2_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
            guiButtons.get(LEVEL_2_COMPLETE_BUTTON_TYPE).setEnabled(true);
            }
            else if(access==2){
            guiButtons.get(LEVEL_2_AVAILABLE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
            guiButtons.get(LEVEL_2_AVAILABLE_BUTTON_TYPE).setEnabled(true); 
            }
            else{
            guiButtons.get(LEVEL_2_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
            guiButtons.get(LEVEL_2_BUTTON_TYPE).setEnabled(true);
            }
            
            guiButtons.get(LEVEL_2_BUTTON_TYPE).setX(LEVEL_2_X + x);
            guiButtons.get(LEVEL_2_BUTTON_TYPE).setY(LEVEL_2_Y + y);
            guiButtons.get(LEVEL_2_AVAILABLE_BUTTON_TYPE).setX(LEVEL_2_X + x);
            guiButtons.get(LEVEL_2_AVAILABLE_BUTTON_TYPE).setY(LEVEL_2_Y + y);
            guiButtons.get(LEVEL_2_COMPLETE_BUTTON_TYPE).setX(LEVEL_2_X + x);
            guiButtons.get(LEVEL_2_COMPLETE_BUTTON_TYPE).setY(LEVEL_2_Y + y);
        }

        if (x < -75 || y < -465) {
            guiButtons.get(LEVEL_3_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
            guiButtons.get(LEVEL_3_BUTTON_TYPE).setEnabled(false);
            guiButtons.get(LEVEL_3_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
            guiButtons.get(LEVEL_3_COMPLETE_BUTTON_TYPE).setEnabled(false);
            guiButtons.get(LEVEL_3_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
            guiButtons.get(LEVEL_3_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        } else {
            if(levelwon>=3){
            guiButtons.get(LEVEL_3_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
            guiButtons.get(LEVEL_3_COMPLETE_BUTTON_TYPE).setEnabled(true);
            }
            else if(access==3){
            guiButtons.get(LEVEL_3_AVAILABLE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
            guiButtons.get(LEVEL_3_AVAILABLE_BUTTON_TYPE).setEnabled(true); 
            }
            else{
            guiButtons.get(LEVEL_3_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
            guiButtons.get(LEVEL_3_BUTTON_TYPE).setEnabled(true);
              }        
            guiButtons.get(LEVEL_3_BUTTON_TYPE).setX(LEVEL_3_X + x);
            guiButtons.get(LEVEL_3_BUTTON_TYPE).setY(LEVEL_3_Y + y);
            guiButtons.get(LEVEL_3_AVAILABLE_BUTTON_TYPE).setX(LEVEL_3_X + x);
            guiButtons.get(LEVEL_3_AVAILABLE_BUTTON_TYPE).setY(LEVEL_3_Y + y);
            guiButtons.get(LEVEL_3_COMPLETE_BUTTON_TYPE).setX(LEVEL_3_X + x);
            guiButtons.get(LEVEL_3_COMPLETE_BUTTON_TYPE).setY(LEVEL_3_Y + y);

        }

        if (x < -345 || y < -285) {
            guiButtons.get(LEVEL_4_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
            guiButtons.get(LEVEL_4_BUTTON_TYPE).setEnabled(false);
            guiButtons.get(LEVEL_4_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
            guiButtons.get(LEVEL_4_COMPLETE_BUTTON_TYPE).setEnabled(false);
            guiButtons.get(LEVEL_4_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
            guiButtons.get(LEVEL_4_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        } else {
            
            if(levelwon>=4){
            guiButtons.get(LEVEL_4_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
            guiButtons.get(LEVEL_4_COMPLETE_BUTTON_TYPE).setEnabled(true);
            }
            else if(access==4){
            guiButtons.get(LEVEL_4_AVAILABLE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
            guiButtons.get(LEVEL_4_AVAILABLE_BUTTON_TYPE).setEnabled(true); 
            } 
            else{
            guiButtons.get(LEVEL_4_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
            guiButtons.get(LEVEL_4_BUTTON_TYPE).setEnabled(true);
                    }
            guiButtons.get(LEVEL_4_BUTTON_TYPE).setX(LEVEL_4_X + x);
            guiButtons.get(LEVEL_4_BUTTON_TYPE).setY(LEVEL_4_Y + y);
            guiButtons.get(LEVEL_4_AVAILABLE_BUTTON_TYPE).setX(LEVEL_4_X + x);
            guiButtons.get(LEVEL_4_AVAILABLE_BUTTON_TYPE).setY(LEVEL_4_Y + y);
            guiButtons.get(LEVEL_4_COMPLETE_BUTTON_TYPE).setX(LEVEL_4_X + x);
            guiButtons.get(LEVEL_4_COMPLETE_BUTTON_TYPE).setY(LEVEL_4_Y + y);

        }

        if (x < -345 || y < -465) {
            guiButtons.get(LEVEL_5_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
            guiButtons.get(LEVEL_5_BUTTON_TYPE).setEnabled(false);
            guiButtons.get(LEVEL_5_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
            guiButtons.get(LEVEL_5_COMPLETE_BUTTON_TYPE).setEnabled(false);
            guiButtons.get(LEVEL_5_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
            guiButtons.get(LEVEL_5_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        } else {
            
           if(levelwon>=5){
            guiButtons.get(LEVEL_5_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
            guiButtons.get(LEVEL_5_COMPLETE_BUTTON_TYPE).setEnabled(true);
            }
            else if(access==5){
            guiButtons.get(LEVEL_5_AVAILABLE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
            guiButtons.get(LEVEL_5_AVAILABLE_BUTTON_TYPE).setEnabled(true); 
            } 
            else{
            guiButtons.get(LEVEL_5_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
            guiButtons.get(LEVEL_5_BUTTON_TYPE).setEnabled(true);
                    }
            guiButtons.get(LEVEL_5_BUTTON_TYPE).setX(LEVEL_5_X + x);
            guiButtons.get(LEVEL_5_BUTTON_TYPE).setY(LEVEL_5_Y + y);
            guiButtons.get(LEVEL_5_AVAILABLE_BUTTON_TYPE).setX(LEVEL_5_X + x);
            guiButtons.get(LEVEL_5_AVAILABLE_BUTTON_TYPE).setY(LEVEL_5_Y + y);
            guiButtons.get(LEVEL_5_COMPLETE_BUTTON_TYPE).setX(LEVEL_5_X + x);
            guiButtons.get(LEVEL_5_COMPLETE_BUTTON_TYPE).setY(LEVEL_5_Y + y);

        }
        
        if (x < -575 || y < -285) {
            guiButtons.get(LEVEL_6_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
            guiButtons.get(LEVEL_6_BUTTON_TYPE).setEnabled(false);
            guiButtons.get(LEVEL_6_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
            guiButtons.get(LEVEL_6_COMPLETE_BUTTON_TYPE).setEnabled(false);
            guiButtons.get(LEVEL_6_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
            guiButtons.get(LEVEL_6_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        } else {
            if(levelwon>=6){
            guiButtons.get(LEVEL_6_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
            guiButtons.get(LEVEL_6_COMPLETE_BUTTON_TYPE).setEnabled(true);
            }
            else if(access==6){
            guiButtons.get(LEVEL_6_AVAILABLE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
            guiButtons.get(LEVEL_6_AVAILABLE_BUTTON_TYPE).setEnabled(true); 
            } 
            else{
            guiButtons.get(LEVEL_6_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
            guiButtons.get(LEVEL_6_BUTTON_TYPE).setEnabled(true);
                    }
            guiButtons.get(LEVEL_6_BUTTON_TYPE).setX(LEVEL_6_X + x);
            guiButtons.get(LEVEL_6_BUTTON_TYPE).setY(LEVEL_6_Y + y);
            guiButtons.get(LEVEL_6_AVAILABLE_BUTTON_TYPE).setX(LEVEL_6_X + x);
            guiButtons.get(LEVEL_6_AVAILABLE_BUTTON_TYPE).setY(LEVEL_6_Y + y);
            guiButtons.get(LEVEL_6_COMPLETE_BUTTON_TYPE).setX(LEVEL_6_X + x);
            guiButtons.get(LEVEL_6_COMPLETE_BUTTON_TYPE).setY(LEVEL_6_Y + y);

        }
        
           if (x < -575 || y < -480) {
            guiButtons.get(LEVEL_7_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
            guiButtons.get(LEVEL_7_BUTTON_TYPE).setEnabled(false);
            guiButtons.get(LEVEL_7_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
            guiButtons.get(LEVEL_7_COMPLETE_BUTTON_TYPE).setEnabled(false);
            guiButtons.get(LEVEL_7_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
            guiButtons.get(LEVEL_7_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        } else {
            if(levelwon>=7){
            guiButtons.get(LEVEL_7_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
            guiButtons.get(LEVEL_7_COMPLETE_BUTTON_TYPE).setEnabled(true);
            }
            else if(access==7){
            guiButtons.get(LEVEL_7_AVAILABLE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
            guiButtons.get(LEVEL_7_AVAILABLE_BUTTON_TYPE).setEnabled(true); 
            } 
               else{   
            guiButtons.get(LEVEL_7_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
            guiButtons.get(LEVEL_7_BUTTON_TYPE).setEnabled(true);
                       }
            guiButtons.get(LEVEL_7_BUTTON_TYPE).setX(LEVEL_7_X + x);
            guiButtons.get(LEVEL_7_BUTTON_TYPE).setY(LEVEL_7_Y + y);
            guiButtons.get(LEVEL_7_AVAILABLE_BUTTON_TYPE).setX(LEVEL_7_X + x);
            guiButtons.get(LEVEL_7_AVAILABLE_BUTTON_TYPE).setY(LEVEL_7_Y + y);
            guiButtons.get(LEVEL_7_COMPLETE_BUTTON_TYPE).setX(LEVEL_7_X + x);
            guiButtons.get(LEVEL_7_COMPLETE_BUTTON_TYPE).setY(LEVEL_7_Y + y);

        }
          if (x < -575 || y < -480) {
            guiButtons.get(LEVEL_8_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
            guiButtons.get(LEVEL_8_BUTTON_TYPE).setEnabled(false);
            guiButtons.get(LEVEL_8_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
            guiButtons.get(LEVEL_8_COMPLETE_BUTTON_TYPE).setEnabled(false);
            guiButtons.get(LEVEL_8_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
            guiButtons.get(LEVEL_8_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        } else {
            if(levelwon>=8){
            guiButtons.get(LEVEL_8_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
            guiButtons.get(LEVEL_8_COMPLETE_BUTTON_TYPE).setEnabled(true);
            }
            else if(access==8){
            guiButtons.get(LEVEL_8_AVAILABLE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
            guiButtons.get(LEVEL_8_AVAILABLE_BUTTON_TYPE).setEnabled(true); 
            } 
              else{  
            guiButtons.get(LEVEL_8_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
            guiButtons.get(LEVEL_8_BUTTON_TYPE).setEnabled(true);
                      }      
            guiButtons.get(LEVEL_8_BUTTON_TYPE).setX(LEVEL_8_X + x);
            guiButtons.get(LEVEL_8_BUTTON_TYPE).setY(LEVEL_8_Y + y);
            guiButtons.get(LEVEL_8_AVAILABLE_BUTTON_TYPE).setX(LEVEL_8_X + x);
            guiButtons.get(LEVEL_8_AVAILABLE_BUTTON_TYPE).setY(LEVEL_8_Y + y);
            guiButtons.get(LEVEL_8_COMPLETE_BUTTON_TYPE).setX(LEVEL_8_X + x);
            guiButtons.get(LEVEL_8_COMPLETE_BUTTON_TYPE).setY(LEVEL_8_Y + y);

        }
          if (x < -660 || y < -480) {
            guiButtons.get(LEVEL_9_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
            guiButtons.get(LEVEL_9_BUTTON_TYPE).setEnabled(false);
            guiButtons.get(LEVEL_9_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
            guiButtons.get(LEVEL_9_COMPLETE_BUTTON_TYPE).setEnabled(false);
            guiButtons.get(LEVEL_9_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
            guiButtons.get(LEVEL_9_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        } else {
            if(levelwon>=9){
            guiButtons.get(LEVEL_9_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
            guiButtons.get(LEVEL_9_COMPLETE_BUTTON_TYPE).setEnabled(true);
            }
            else if(access==9){
            guiButtons.get(LEVEL_9_AVAILABLE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
            guiButtons.get(LEVEL_9_AVAILABLE_BUTTON_TYPE).setEnabled(true); 
            } 
              else{
            guiButtons.get(LEVEL_9_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
            guiButtons.get(LEVEL_9_BUTTON_TYPE).setEnabled(true);
                      }
            guiButtons.get(LEVEL_9_BUTTON_TYPE).setX(LEVEL_9_X + x);
            guiButtons.get(LEVEL_9_BUTTON_TYPE).setY(LEVEL_9_Y + y);
            guiButtons.get(LEVEL_9_AVAILABLE_BUTTON_TYPE).setX(LEVEL_9_X + x);
            guiButtons.get(LEVEL_9_AVAILABLE_BUTTON_TYPE).setY(LEVEL_9_Y + y);
            guiButtons.get(LEVEL_9_COMPLETE_BUTTON_TYPE).setX(LEVEL_9_X + x);
            guiButtons.get(LEVEL_9_COMPLETE_BUTTON_TYPE).setY(LEVEL_9_Y + y);

        }
           if (x < -660 || y < -480) {
            guiButtons.get(LEVEL_10_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
            guiButtons.get(LEVEL_10_BUTTON_TYPE).setEnabled(false);
            guiButtons.get(LEVEL_10_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
            guiButtons.get(LEVEL_10_COMPLETE_BUTTON_TYPE).setEnabled(false);
            guiButtons.get(LEVEL_10_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
            guiButtons.get(LEVEL_10_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        } else {
               
            if(levelwon>=10){
            guiButtons.get(LEVEL_10_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
            guiButtons.get(LEVEL_10_COMPLETE_BUTTON_TYPE).setEnabled(true);
            }
            else if(access==10){
            guiButtons.get(LEVEL_10_AVAILABLE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
            guiButtons.get(LEVEL_10_AVAILABLE_BUTTON_TYPE).setEnabled(true); 
            } 
               
               else{   
            guiButtons.get(LEVEL_10_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
            guiButtons.get(LEVEL_10_BUTTON_TYPE).setEnabled(true);
                       }
            guiButtons.get(LEVEL_10_BUTTON_TYPE).setX(LEVEL_10_X + x);
            guiButtons.get(LEVEL_10_BUTTON_TYPE).setY(LEVEL_10_Y + y);
            guiButtons.get(LEVEL_10_AVAILABLE_BUTTON_TYPE).setX(LEVEL_10_X + x);
            guiButtons.get(LEVEL_10_AVAILABLE_BUTTON_TYPE).setY(LEVEL_10_Y + y);
            guiButtons.get(LEVEL_10_COMPLETE_BUTTON_TYPE).setX(LEVEL_10_X + x);
            guiButtons.get(LEVEL_10_COMPLETE_BUTTON_TYPE).setY(LEVEL_10_Y + y);

        }
            if (x < -765 || y < -485) {
            guiButtons.get(LEVEL_11_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
            guiButtons.get(LEVEL_11_BUTTON_TYPE).setEnabled(false);
            guiButtons.get(LEVEL_11_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
            guiButtons.get(LEVEL_11_COMPLETE_BUTTON_TYPE).setEnabled(false);
            guiButtons.get(LEVEL_11_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
            guiButtons.get(LEVEL_11_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        } else {
             if(levelwon>=11){
            guiButtons.get(LEVEL_11_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
            guiButtons.get(LEVEL_11_COMPLETE_BUTTON_TYPE).setEnabled(true);
            }
            else if(access==11){
            guiButtons.get(LEVEL_11_AVAILABLE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
            guiButtons.get(LEVEL_11_AVAILABLE_BUTTON_TYPE).setEnabled(true); 
            } 
               
            else{
            guiButtons.get(LEVEL_11_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
            guiButtons.get(LEVEL_11_BUTTON_TYPE).setEnabled(true);
                        }
            guiButtons.get(LEVEL_11_BUTTON_TYPE).setX(LEVEL_11_X + x);
            guiButtons.get(LEVEL_11_BUTTON_TYPE).setY(LEVEL_11_Y + y);
            guiButtons.get(LEVEL_11_AVAILABLE_BUTTON_TYPE).setX(LEVEL_11_X + x);
            guiButtons.get(LEVEL_11_AVAILABLE_BUTTON_TYPE).setY(LEVEL_11_Y + y);
            guiButtons.get(LEVEL_11_COMPLETE_BUTTON_TYPE).setX(LEVEL_11_X + x);
            guiButtons.get(LEVEL_11_COMPLETE_BUTTON_TYPE).setY(LEVEL_11_Y + y);

        }
            if (x < -765 || y < -485) {
            guiButtons.get(LEVEL_12_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
            guiButtons.get(LEVEL_12_BUTTON_TYPE).setEnabled(false);
            guiButtons.get(LEVEL_12_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
            guiButtons.get(LEVEL_12_COMPLETE_BUTTON_TYPE).setEnabled(false);
            guiButtons.get(LEVEL_12_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
            guiButtons.get(LEVEL_12_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        } else {
                
           if(levelwon>=12){
            guiButtons.get(LEVEL_12_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
            guiButtons.get(LEVEL_12_COMPLETE_BUTTON_TYPE).setEnabled(true);
            }
            else if(access==12){
            guiButtons.get(LEVEL_12_AVAILABLE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
            guiButtons.get(LEVEL_12_AVAILABLE_BUTTON_TYPE).setEnabled(true); 
            } 
            else{   
            guiButtons.get(LEVEL_12_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
            guiButtons.get(LEVEL_12_BUTTON_TYPE).setEnabled(true);
            }
            guiButtons.get(LEVEL_12_BUTTON_TYPE).setX(LEVEL_12_X + x);
            guiButtons.get(LEVEL_12_BUTTON_TYPE).setY(LEVEL_12_Y + y);
            guiButtons.get(LEVEL_12_AVAILABLE_BUTTON_TYPE).setX(LEVEL_12_X + x);
            guiButtons.get(LEVEL_12_AVAILABLE_BUTTON_TYPE).setY(LEVEL_12_Y + y);
            guiButtons.get(LEVEL_12_COMPLETE_BUTTON_TYPE).setX(LEVEL_12_X + x);
            guiButtons.get(LEVEL_12_COMPLETE_BUTTON_TYPE).setY(LEVEL_12_Y + y);

        }
          if (x < -795 || y < -485) {
            guiButtons.get(LEVEL_13_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
            guiButtons.get(LEVEL_13_BUTTON_TYPE).setEnabled(false);
            guiButtons.get(LEVEL_13_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
            guiButtons.get(LEVEL_13_COMPLETE_BUTTON_TYPE).setEnabled(false);
            guiButtons.get(LEVEL_13_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
            guiButtons.get(LEVEL_13_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        } else {
              
            if(levelwon>=13){
            guiButtons.get(LEVEL_13_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
            guiButtons.get(LEVEL_13_COMPLETE_BUTTON_TYPE).setEnabled(true);
            }
            else if(access==13){
            guiButtons.get(LEVEL_13_AVAILABLE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
            guiButtons.get(LEVEL_13_AVAILABLE_BUTTON_TYPE).setEnabled(true); 
            } 
            else{   
            guiButtons.get(LEVEL_13_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
            guiButtons.get(LEVEL_13_BUTTON_TYPE).setEnabled(true);
            }
            guiButtons.get(LEVEL_13_BUTTON_TYPE).setX(LEVEL_13_X + x);
            guiButtons.get(LEVEL_13_BUTTON_TYPE).setY(LEVEL_13_Y + y);
            guiButtons.get(LEVEL_13_AVAILABLE_BUTTON_TYPE).setX(LEVEL_13_X + x);
            guiButtons.get(LEVEL_13_AVAILABLE_BUTTON_TYPE).setY(LEVEL_13_Y + y);
            guiButtons.get(LEVEL_13_COMPLETE_BUTTON_TYPE).setX(LEVEL_13_X + x);
            guiButtons.get(LEVEL_13_COMPLETE_BUTTON_TYPE).setY(LEVEL_13_Y + y);

        }
          if (x < -795 || y < -485) {
            guiButtons.get(LEVEL_14_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
            guiButtons.get(LEVEL_14_BUTTON_TYPE).setEnabled(false);
            guiButtons.get(LEVEL_14_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
            guiButtons.get(LEVEL_14_COMPLETE_BUTTON_TYPE).setEnabled(false);
            guiButtons.get(LEVEL_14_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
            guiButtons.get(LEVEL_14_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        } else {
            if(levelwon>=14){
            guiButtons.get(LEVEL_14_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
            guiButtons.get(LEVEL_14_COMPLETE_BUTTON_TYPE).setEnabled(true);
            }
            else if(access==14){
            guiButtons.get(LEVEL_14_AVAILABLE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
            guiButtons.get(LEVEL_14_AVAILABLE_BUTTON_TYPE).setEnabled(true); 
            } 
               
              else{  
            guiButtons.get(LEVEL_14_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
            guiButtons.get(LEVEL_14_BUTTON_TYPE).setEnabled(true);
                      }
            guiButtons.get(LEVEL_14_BUTTON_TYPE).setX(LEVEL_14_X + x);
            guiButtons.get(LEVEL_14_BUTTON_TYPE).setY(LEVEL_14_Y + y);
            guiButtons.get(LEVEL_14_AVAILABLE_BUTTON_TYPE).setX(LEVEL_14_X + x);
            guiButtons.get(LEVEL_14_AVAILABLE_BUTTON_TYPE).setY(LEVEL_14_Y + y);
            guiButtons.get(LEVEL_14_COMPLETE_BUTTON_TYPE).setX(LEVEL_14_X + x);
            guiButtons.get(LEVEL_14_COMPLETE_BUTTON_TYPE).setY(LEVEL_14_Y + y);

        }
           if (x < -960 || y < -485) {
            guiButtons.get(LEVEL_15_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
            guiButtons.get(LEVEL_15_BUTTON_TYPE).setEnabled(false);
            guiButtons.get(LEVEL_15_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
            guiButtons.get(LEVEL_15_COMPLETE_BUTTON_TYPE).setEnabled(false);
            guiButtons.get(LEVEL_15_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
            guiButtons.get(LEVEL_15_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        } else {
            if(levelwon>=15){
            guiButtons.get(LEVEL_15_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
            guiButtons.get(LEVEL_15_COMPLETE_BUTTON_TYPE).setEnabled(true);
            }
            else if(access==15){
            guiButtons.get(LEVEL_15_AVAILABLE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
            guiButtons.get(LEVEL_15_AVAILABLE_BUTTON_TYPE).setEnabled(true); 
            } 
               
               else{   
            guiButtons.get(LEVEL_15_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
            guiButtons.get(LEVEL_15_BUTTON_TYPE).setEnabled(true);
                       }
            guiButtons.get(LEVEL_15_BUTTON_TYPE).setX(LEVEL_15_X + x);
            guiButtons.get(LEVEL_15_BUTTON_TYPE).setY(LEVEL_15_Y + y);
            guiButtons.get(LEVEL_15_AVAILABLE_BUTTON_TYPE).setX(LEVEL_15_X + x);
            guiButtons.get(LEVEL_15_AVAILABLE_BUTTON_TYPE).setY(LEVEL_15_Y + y);
            guiButtons.get(LEVEL_15_COMPLETE_BUTTON_TYPE).setX(LEVEL_15_X + x);
            guiButtons.get(LEVEL_15_COMPLETE_BUTTON_TYPE).setY(LEVEL_15_Y + y);

        }
            if (x < -990 || y < -485) {
            guiButtons.get(LEVEL_16_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
            guiButtons.get(LEVEL_16_BUTTON_TYPE).setEnabled(false);
            guiButtons.get(LEVEL_16_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
            guiButtons.get(LEVEL_16_COMPLETE_BUTTON_TYPE).setEnabled(false);
            guiButtons.get(LEVEL_16_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
            guiButtons.get(LEVEL_16_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        } else {
           if(levelwon>=16){
            guiButtons.get(LEVEL_16_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
            guiButtons.get(LEVEL_16_COMPLETE_BUTTON_TYPE).setEnabled(true);
            }
            else if(access==16){
            guiButtons.get(LEVEL_16_AVAILABLE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
            guiButtons.get(LEVEL_16_AVAILABLE_BUTTON_TYPE).setEnabled(true); 
            } 
               
                else{    
            guiButtons.get(LEVEL_16_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
            guiButtons.get(LEVEL_16_BUTTON_TYPE).setEnabled(true);
                        }
            guiButtons.get(LEVEL_16_BUTTON_TYPE).setX(LEVEL_16_X + x);
            guiButtons.get(LEVEL_16_BUTTON_TYPE).setY(LEVEL_16_Y + y);
            guiButtons.get(LEVEL_16_AVAILABLE_BUTTON_TYPE).setX(LEVEL_16_X + x);
            guiButtons.get(LEVEL_16_AVAILABLE_BUTTON_TYPE).setY(LEVEL_16_Y + y);
            guiButtons.get(LEVEL_16_COMPLETE_BUTTON_TYPE).setX(LEVEL_16_X + x);
            guiButtons.get(LEVEL_16_COMPLETE_BUTTON_TYPE).setY(LEVEL_16_Y + y);

        }
            if (x < -990 || y < -485) {
            guiButtons.get(LEVEL_17_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
            guiButtons.get(LEVEL_17_BUTTON_TYPE).setEnabled(false);
            guiButtons.get(LEVEL_17_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
            guiButtons.get(LEVEL_17_COMPLETE_BUTTON_TYPE).setEnabled(false);
            guiButtons.get(LEVEL_17_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
            guiButtons.get(LEVEL_17_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        } else {
           if(levelwon>=17){
            guiButtons.get(LEVEL_17_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
            guiButtons.get(LEVEL_17_COMPLETE_BUTTON_TYPE).setEnabled(true);
            }
            else if(access==17){
            guiButtons.get(LEVEL_17_AVAILABLE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
            guiButtons.get(LEVEL_17_AVAILABLE_BUTTON_TYPE).setEnabled(true); 
            } 
               
                else{    
            guiButtons.get(LEVEL_17_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
            guiButtons.get(LEVEL_17_BUTTON_TYPE).setEnabled(true);
                        }
            guiButtons.get(LEVEL_17_BUTTON_TYPE).setX(LEVEL_17_X + x);
            guiButtons.get(LEVEL_17_BUTTON_TYPE).setY(LEVEL_17_Y + y);
            guiButtons.get(LEVEL_17_AVAILABLE_BUTTON_TYPE).setX(LEVEL_17_X + x);
            guiButtons.get(LEVEL_17_AVAILABLE_BUTTON_TYPE).setY(LEVEL_17_Y + y);
            guiButtons.get(LEVEL_17_COMPLETE_BUTTON_TYPE).setX(LEVEL_17_X + x);
            guiButtons.get(LEVEL_17_COMPLETE_BUTTON_TYPE).setY(LEVEL_17_Y + y);

        }
             if (x < -1050 || y < -485) {
            guiButtons.get(LEVEL_18_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
            guiButtons.get(LEVEL_18_BUTTON_TYPE).setEnabled(false);
            guiButtons.get(LEVEL_18_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
            guiButtons.get(LEVEL_18_COMPLETE_BUTTON_TYPE).setEnabled(false);
            guiButtons.get(LEVEL_18_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
            guiButtons.get(LEVEL_18_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        } else {
            if(levelwon>=18){
            guiButtons.get(LEVEL_18_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
            guiButtons.get(LEVEL_18_COMPLETE_BUTTON_TYPE).setEnabled(true);
            }
            else if(access==18){
            guiButtons.get(LEVEL_18_AVAILABLE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
            guiButtons.get(LEVEL_18_AVAILABLE_BUTTON_TYPE).setEnabled(true); 
            } 
            else{   
            guiButtons.get(LEVEL_18_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
            guiButtons.get(LEVEL_18_BUTTON_TYPE).setEnabled(true);
            }
            guiButtons.get(LEVEL_18_BUTTON_TYPE).setX(LEVEL_18_X + x);
            guiButtons.get(LEVEL_18_BUTTON_TYPE).setY(LEVEL_18_Y + y);
            guiButtons.get(LEVEL_18_AVAILABLE_BUTTON_TYPE).setX(LEVEL_18_X + x);
            guiButtons.get(LEVEL_18_AVAILABLE_BUTTON_TYPE).setY(LEVEL_18_Y + y);
            guiButtons.get(LEVEL_18_COMPLETE_BUTTON_TYPE).setX(LEVEL_18_X + x);
            guiButtons.get(LEVEL_18_COMPLETE_BUTTON_TYPE).setY(LEVEL_18_Y + y);

        }
            if (x < -1050 || y < -485) {
            guiButtons.get(LEVEL_19_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
            guiButtons.get(LEVEL_19_BUTTON_TYPE).setEnabled(false);
            guiButtons.get(LEVEL_19_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
            guiButtons.get(LEVEL_19_COMPLETE_BUTTON_TYPE).setEnabled(false);
            guiButtons.get(LEVEL_19_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
            guiButtons.get(LEVEL_19_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        } else {
            if(levelwon>=19){
            guiButtons.get(LEVEL_19_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
            guiButtons.get(LEVEL_19_COMPLETE_BUTTON_TYPE).setEnabled(true);
            }
            else if(access==19){
            guiButtons.get(LEVEL_19_AVAILABLE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
            guiButtons.get(LEVEL_19_AVAILABLE_BUTTON_TYPE).setEnabled(true); 
            } 
               
                else{    
            guiButtons.get(LEVEL_19_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
            guiButtons.get(LEVEL_19_BUTTON_TYPE).setEnabled(true);
                        }
            guiButtons.get(LEVEL_19_BUTTON_TYPE).setX(LEVEL_19_X + x);
            guiButtons.get(LEVEL_19_BUTTON_TYPE).setY(LEVEL_19_Y + y);
            guiButtons.get(LEVEL_19_AVAILABLE_BUTTON_TYPE).setX(LEVEL_19_X + x);
            guiButtons.get(LEVEL_19_AVAILABLE_BUTTON_TYPE).setY(LEVEL_19_Y + y);
            guiButtons.get(LEVEL_19_COMPLETE_BUTTON_TYPE).setX(LEVEL_19_X + x);
            guiButtons.get(LEVEL_19_COMPLETE_BUTTON_TYPE).setY(LEVEL_19_Y + y);

        }
            if (x < -1050 || y < -485) {
            guiButtons.get(LEVEL_20_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
            guiButtons.get(LEVEL_20_BUTTON_TYPE).setEnabled(false);
            guiButtons.get(LEVEL_20_COMPLETE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
            guiButtons.get(LEVEL_20_COMPLETE_BUTTON_TYPE).setEnabled(false);
            guiButtons.get(LEVEL_20_AVAILABLE_BUTTON_TYPE).setState(INVISIBLE_STATE.toString());
            guiButtons.get(LEVEL_20_AVAILABLE_BUTTON_TYPE).setEnabled(false);
        } else {
            if(levelwon>=20){
            guiButtons.get(LEVEL_20_COMPLETE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
            guiButtons.get(LEVEL_20_COMPLETE_BUTTON_TYPE).setEnabled(true);
            }
            else if(access==20){
            guiButtons.get(LEVEL_20_AVAILABLE_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
            guiButtons.get(LEVEL_20_AVAILABLE_BUTTON_TYPE).setEnabled(true); 
            } 
               
                else{    
            guiButtons.get(LEVEL_20_BUTTON_TYPE).setState(VISIBLE_STATE.toString());
            guiButtons.get(LEVEL_20_BUTTON_TYPE).setEnabled(true);
                        }
            guiButtons.get(LEVEL_20_BUTTON_TYPE).setX(LEVEL_20_X + x);
            guiButtons.get(LEVEL_20_BUTTON_TYPE).setY(LEVEL_20_Y + y);
            guiButtons.get(LEVEL_20_AVAILABLE_BUTTON_TYPE).setX(LEVEL_20_X + x);
            guiButtons.get(LEVEL_20_AVAILABLE_BUTTON_TYPE).setY(LEVEL_20_Y + y);
            guiButtons.get(LEVEL_20_COMPLETE_BUTTON_TYPE).setX(LEVEL_20_X + x);
            guiButtons.get(LEVEL_20_COMPLETE_BUTTON_TYPE).setY(LEVEL_20_Y + y);

        }
       }
        updateGUI();
    }

    public void ScrollDown() {
        if(currentScreenState.equals(GAME_SCREEN_STATE)){
       PathXDataModel d = (PathXDataModel) data;
            //System.out.println(d.getLevel().getIntersections());
            ArrayList<Intersection> intersections = d.getLevel().getIntersections();
//      
            for (Intersection i: intersections) {
                i.setY(i.getY() +15);
            }
        }
        Sprite player = guiDecor.get(PLAYER_TYPE);
          Sprite police1= guiDecor.get(POLICE_TYPE);
              Sprite police2= guiDecor.get(POLICE_TYPE_2);
              Sprite police3= guiDecor.get(POLICE_TYPE_3);
              Sprite police4= guiDecor.get(POLICE_TYPE_4);
              Sprite police5= guiDecor.get(POLICE_TYPE_5);
              Sprite police6= guiDecor.get(POLICE_TYPE_6);
              Sprite bandit1= guiDecor.get(BANDIT_TYPE);
              Sprite bandit2= guiDecor.get(BANDIT_TYPE_2);
              Sprite bandit3= guiDecor.get(BANDIT_TYPE_3);
              Sprite bandit4= guiDecor.get(BANDIT_TYPE_4);
              Sprite bandit5= guiDecor.get(BANDIT_TYPE_5);
              Sprite bandit6= guiDecor.get(BANDIT_TYPE_6);
        player.setY(player.getY()+15);
        police1.setY(police1.getY()+15);
            police2.setY(police2.getY()+15);
            police3.setY(police3.getY()+15);
            police4.setY(police4.getY()+15);
            police5.setY(police5.getY()+15);
            police6.setY(police6.getY()+15);
            bandit1.setY(bandit1.getY()+15);
            bandit2.setY(bandit2.getY()+15);
            bandit3.setY(bandit3.getY()+15);
            bandit4.setY(bandit4.getY()+15);
            bandit5.setY(bandit5.getY()+15);
            bandit6.setY(bandit6.getY()+15);
        scrollNum = 1;
        boolean scrollvalue = getScroll();
        if (scrollvalue == true) {
            float x = guiDecor.get(BACKGROUND_TYPE).getX();
            float y = guiDecor.get(BACKGROUND_TYPE).getY();
            if (y > -468) {
                guiDecor.get(BACKGROUND_TYPE).setX(x);
                guiDecor.get(BACKGROUND_TYPE).setY(y - 15);
            }
            //System.out.println("y:" + y);
        }
        setLevelVisibility();

        updateGUI();
    }

    public void ScrollLeft() {
        if (currentScreenState.equals(GAME_SCREEN_STATE)){
   
        PathXDataModel d = (PathXDataModel) data;
            
            ArrayList<Intersection> intersections = d.getLevel().getIntersections();
        Sprite player = guiDecor.get(PLAYER_TYPE);
              Sprite police1= guiDecor.get(POLICE_TYPE);
              Sprite police2= guiDecor.get(POLICE_TYPE_2);
              Sprite police3= guiDecor.get(POLICE_TYPE_3);
              Sprite police4= guiDecor.get(POLICE_TYPE_4);
              Sprite police5= guiDecor.get(POLICE_TYPE_5);
              Sprite police6= guiDecor.get(POLICE_TYPE_6);
              Sprite bandit1= guiDecor.get(BANDIT_TYPE);
              Sprite bandit2= guiDecor.get(BANDIT_TYPE_2);
              Sprite bandit3= guiDecor.get(BANDIT_TYPE_3);
              Sprite bandit4= guiDecor.get(BANDIT_TYPE_4);
              Sprite bandit5= guiDecor.get(BANDIT_TYPE_5);
              Sprite bandit6= guiDecor.get(BANDIT_TYPE_6);
            player.setX(player.getX()-15);
            police1.setX(police1.getX()-15);
            police2.setX(police2.getX()-15);
            police3.setX(police3.getX()-15);
            police4.setX(police4.getX()-15);
            police5.setX(police5.getX()-15);
            police6.setX(police6.getX()-15);
            bandit1.setX(bandit1.getX()-15);
            bandit2.setX(bandit2.getX()-15);
            bandit3.setX(bandit3.getX()-15);
            bandit4.setX(bandit4.getX()-15);
            bandit5.setX(bandit5.getX()-15);
            bandit6.setX(bandit6.getX()-15);
            for (Intersection i: intersections) {
                i.setX(i.getX() - 15);
            }
        }
        scrollNum = 2;
        boolean scrollvalue = getScroll();
        if (scrollvalue == true) {
            float x = guiDecor.get(BACKGROUND_TYPE).getX();
            float y = guiDecor.get(BACKGROUND_TYPE).getY();
            if (Math.abs(x) > 0.0) {
                guiDecor.get(BACKGROUND_TYPE).setX(x + 15);
                guiDecor.get(BACKGROUND_TYPE).setY(y);
            }
            //System.out.println("x:" + x);

        }
        setLevelVisibility();
        updateGUI();
    }

    public void ScrollRight() {
        if(currentScreenState.equals(GAME_SCREEN_STATE)){
             PathXDataModel d = (PathXDataModel) data;
            //System.out.println(d.getLevel().getIntersections());
             Sprite player = guiDecor.get(PLAYER_TYPE);
             Sprite police1= guiDecor.get(POLICE_TYPE);
              Sprite police2= guiDecor.get(POLICE_TYPE_2);
              Sprite police3= guiDecor.get(POLICE_TYPE_3);
              Sprite police4= guiDecor.get(POLICE_TYPE_4);
              Sprite police5= guiDecor.get(POLICE_TYPE_5);
              Sprite police6= guiDecor.get(POLICE_TYPE_6);
              Sprite bandit1= guiDecor.get(BANDIT_TYPE);
              Sprite bandit2= guiDecor.get(BANDIT_TYPE_2);
              Sprite bandit3= guiDecor.get(BANDIT_TYPE_3);
              Sprite bandit4= guiDecor.get(BANDIT_TYPE_4);
              Sprite bandit5= guiDecor.get(BANDIT_TYPE_5);
              Sprite bandit6= guiDecor.get(BANDIT_TYPE_6);
             player.setX(player.getX()+15);
            police1.setX(police1.getX()+15);
            police2.setX(police2.getX()+15);
            police3.setX(police3.getX()+15);
            police4.setX(police4.getX()+15);
            police5.setX(police5.getX()+15);
            police6.setX(police6.getX()+15);
            bandit1.setX(bandit1.getX()+15);
            bandit2.setX(bandit2.getX()+15);
            bandit3.setX(bandit3.getX()+15);
            bandit4.setX(bandit4.getX()+15);
            bandit5.setX(bandit5.getX()+15);
            bandit6.setX(bandit6.getX()+15);
            ArrayList<Intersection> intersections = d.getLevel().getIntersections();
            for (Intersection i: intersections) {
                i.setX(i.getX() + 15);
            }
        }
        scrollNum = 3;
        boolean scrollvalue = getScroll();
        if (scrollvalue == true) {
            float x = guiDecor.get(BACKGROUND_TYPE).getX();
            float y = guiDecor.get(BACKGROUND_TYPE).getY();
            if ((x) > -1245) {
                guiDecor.get(BACKGROUND_TYPE).setX(x - 15);
                guiDecor.get(BACKGROUND_TYPE).setY(y);
            }
            //System.out.println("x:" + x);

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
        guiButtons.get(LEVEL_1_COMPLETE_BUTTON_TYPE).setX(LEVEL_1_X);
        guiButtons.get(LEVEL_1_COMPLETE_BUTTON_TYPE).setY(LEVEL_1_Y);
        guiButtons.get(LEVEL_2_BUTTON_TYPE).setX(LEVEL_2_X);
        guiButtons.get(LEVEL_2_BUTTON_TYPE).setY(LEVEL_2_Y);
        guiButtons.get(LEVEL_2_COMPLETE_BUTTON_TYPE).setX(LEVEL_2_X);
        guiButtons.get(LEVEL_2_COMPLETE_BUTTON_TYPE).setY(LEVEL_2_Y);
        guiButtons.get(LEVEL_2_AVAILABLE_BUTTON_TYPE).setX(LEVEL_2_X);
        guiButtons.get(LEVEL_2_AVAILABLE_BUTTON_TYPE).setY(LEVEL_2_Y);
        guiButtons.get(LEVEL_3_BUTTON_TYPE).setX(LEVEL_3_X);
        guiButtons.get(LEVEL_3_BUTTON_TYPE).setY(LEVEL_3_Y);
        guiButtons.get(LEVEL_3_COMPLETE_BUTTON_TYPE).setX(LEVEL_3_X);
        guiButtons.get(LEVEL_3_COMPLETE_BUTTON_TYPE).setY(LEVEL_3_Y);
        guiButtons.get(LEVEL_3_AVAILABLE_BUTTON_TYPE).setX(LEVEL_3_X);
        guiButtons.get(LEVEL_3_AVAILABLE_BUTTON_TYPE).setY(LEVEL_3_Y);
        guiButtons.get(LEVEL_4_BUTTON_TYPE).setX(LEVEL_4_X);
        guiButtons.get(LEVEL_4_BUTTON_TYPE).setY(LEVEL_4_Y);
        guiButtons.get(LEVEL_4_COMPLETE_BUTTON_TYPE).setX(LEVEL_4_X);
        guiButtons.get(LEVEL_4_COMPLETE_BUTTON_TYPE).setY(LEVEL_4_Y);
        guiButtons.get(LEVEL_4_AVAILABLE_BUTTON_TYPE).setX(LEVEL_4_X);
        guiButtons.get(LEVEL_4_AVAILABLE_BUTTON_TYPE).setY(LEVEL_4_Y);
        guiButtons.get(LEVEL_5_BUTTON_TYPE).setX(LEVEL_5_X);
        guiButtons.get(LEVEL_5_BUTTON_TYPE).setY(LEVEL_5_Y);
        guiButtons.get(LEVEL_5_COMPLETE_BUTTON_TYPE).setX(LEVEL_5_X);
        guiButtons.get(LEVEL_5_COMPLETE_BUTTON_TYPE).setY(LEVEL_5_Y);
        guiButtons.get(LEVEL_5_AVAILABLE_BUTTON_TYPE).setX(LEVEL_5_X);
        guiButtons.get(LEVEL_5_AVAILABLE_BUTTON_TYPE).setY(LEVEL_5_Y);
        guiButtons.get(LEVEL_6_BUTTON_TYPE).setX(LEVEL_6_X);
        guiButtons.get(LEVEL_6_BUTTON_TYPE).setY(LEVEL_6_Y);
        guiButtons.get(LEVEL_6_COMPLETE_BUTTON_TYPE).setX(LEVEL_6_X);
        guiButtons.get(LEVEL_6_COMPLETE_BUTTON_TYPE).setY(LEVEL_6_Y);
        guiButtons.get(LEVEL_6_AVAILABLE_BUTTON_TYPE).setX(LEVEL_6_X);
        guiButtons.get(LEVEL_6_AVAILABLE_BUTTON_TYPE).setY(LEVEL_6_Y);
        guiButtons.get(LEVEL_7_BUTTON_TYPE).setX(LEVEL_7_X);
        guiButtons.get(LEVEL_7_BUTTON_TYPE).setY(LEVEL_7_Y);
        guiButtons.get(LEVEL_7_COMPLETE_BUTTON_TYPE).setX(LEVEL_7_X);
        guiButtons.get(LEVEL_7_COMPLETE_BUTTON_TYPE).setY(LEVEL_7_Y);
        guiButtons.get(LEVEL_7_AVAILABLE_BUTTON_TYPE).setX(LEVEL_7_X);
        guiButtons.get(LEVEL_7_AVAILABLE_BUTTON_TYPE).setY(LEVEL_7_Y);
        guiButtons.get(LEVEL_8_BUTTON_TYPE).setX(LEVEL_8_X);
        guiButtons.get(LEVEL_8_BUTTON_TYPE).setY(LEVEL_8_Y);
        guiButtons.get(LEVEL_8_COMPLETE_BUTTON_TYPE).setX(LEVEL_8_X);
        guiButtons.get(LEVEL_8_COMPLETE_BUTTON_TYPE).setY(LEVEL_8_Y);
        guiButtons.get(LEVEL_8_AVAILABLE_BUTTON_TYPE).setX(LEVEL_8_X);
        guiButtons.get(LEVEL_8_AVAILABLE_BUTTON_TYPE).setY(LEVEL_8_Y);
        guiButtons.get(LEVEL_9_BUTTON_TYPE).setX(LEVEL_9_X);
        guiButtons.get(LEVEL_9_BUTTON_TYPE).setY(LEVEL_9_Y);
        guiButtons.get(LEVEL_9_COMPLETE_BUTTON_TYPE).setX(LEVEL_9_X);
        guiButtons.get(LEVEL_9_COMPLETE_BUTTON_TYPE).setY(LEVEL_9_Y);
        guiButtons.get(LEVEL_9_AVAILABLE_BUTTON_TYPE).setX(LEVEL_9_X);
        guiButtons.get(LEVEL_9_AVAILABLE_BUTTON_TYPE).setY(LEVEL_9_Y);
        guiButtons.get(LEVEL_10_BUTTON_TYPE).setX(LEVEL_10_X);
        guiButtons.get(LEVEL_10_BUTTON_TYPE).setY(LEVEL_10_Y);
        guiButtons.get(LEVEL_10_COMPLETE_BUTTON_TYPE).setX(LEVEL_10_X);
        guiButtons.get(LEVEL_10_COMPLETE_BUTTON_TYPE).setY(LEVEL_10_Y);
        guiButtons.get(LEVEL_10_AVAILABLE_BUTTON_TYPE).setX(LEVEL_10_X);
        guiButtons.get(LEVEL_10_AVAILABLE_BUTTON_TYPE).setY(LEVEL_10_Y);
        guiButtons.get(LEVEL_11_BUTTON_TYPE).setX(LEVEL_11_X);
        guiButtons.get(LEVEL_11_BUTTON_TYPE).setY(LEVEL_11_Y);
        guiButtons.get(LEVEL_11_COMPLETE_BUTTON_TYPE).setX(LEVEL_11_X);
        guiButtons.get(LEVEL_11_COMPLETE_BUTTON_TYPE).setY(LEVEL_11_Y);
        guiButtons.get(LEVEL_11_AVAILABLE_BUTTON_TYPE).setX(LEVEL_11_X);
        guiButtons.get(LEVEL_11_AVAILABLE_BUTTON_TYPE).setY(LEVEL_11_Y);
        guiButtons.get(LEVEL_12_BUTTON_TYPE).setX(LEVEL_12_X);
        guiButtons.get(LEVEL_12_BUTTON_TYPE).setY(LEVEL_12_Y);
        guiButtons.get(LEVEL_12_COMPLETE_BUTTON_TYPE).setX(LEVEL_12_X);
        guiButtons.get(LEVEL_12_COMPLETE_BUTTON_TYPE).setY(LEVEL_12_Y);
        guiButtons.get(LEVEL_12_AVAILABLE_BUTTON_TYPE).setX(LEVEL_12_X);
        guiButtons.get(LEVEL_12_AVAILABLE_BUTTON_TYPE).setY(LEVEL_12_Y);
        guiButtons.get(LEVEL_13_BUTTON_TYPE).setX(LEVEL_13_X);
        guiButtons.get(LEVEL_13_BUTTON_TYPE).setY(LEVEL_13_Y);
        guiButtons.get(LEVEL_13_COMPLETE_BUTTON_TYPE).setX(LEVEL_13_X);
        guiButtons.get(LEVEL_13_COMPLETE_BUTTON_TYPE).setY(LEVEL_13_Y);
        guiButtons.get(LEVEL_13_AVAILABLE_BUTTON_TYPE).setX(LEVEL_13_X);
        guiButtons.get(LEVEL_13_AVAILABLE_BUTTON_TYPE).setY(LEVEL_13_Y);
        guiButtons.get(LEVEL_14_BUTTON_TYPE).setX(LEVEL_14_X);
        guiButtons.get(LEVEL_14_BUTTON_TYPE).setY(LEVEL_14_Y);
        guiButtons.get(LEVEL_14_COMPLETE_BUTTON_TYPE).setX(LEVEL_14_X);
        guiButtons.get(LEVEL_14_COMPLETE_BUTTON_TYPE).setY(LEVEL_14_Y);
        guiButtons.get(LEVEL_14_AVAILABLE_BUTTON_TYPE).setX(LEVEL_14_X);
        guiButtons.get(LEVEL_14_AVAILABLE_BUTTON_TYPE).setY(LEVEL_14_Y);
        guiButtons.get(LEVEL_15_BUTTON_TYPE).setX(LEVEL_15_X);
        guiButtons.get(LEVEL_15_BUTTON_TYPE).setY(LEVEL_15_Y);
        guiButtons.get(LEVEL_15_COMPLETE_BUTTON_TYPE).setX(LEVEL_15_X);
        guiButtons.get(LEVEL_15_COMPLETE_BUTTON_TYPE).setY(LEVEL_15_Y);
        guiButtons.get(LEVEL_15_AVAILABLE_BUTTON_TYPE).setX(LEVEL_15_X);
        guiButtons.get(LEVEL_15_AVAILABLE_BUTTON_TYPE).setY(LEVEL_15_Y);
        guiButtons.get(LEVEL_16_BUTTON_TYPE).setX(LEVEL_16_X);
        guiButtons.get(LEVEL_16_BUTTON_TYPE).setY(LEVEL_16_Y);
        guiButtons.get(LEVEL_16_COMPLETE_BUTTON_TYPE).setX(LEVEL_16_X);
        guiButtons.get(LEVEL_16_COMPLETE_BUTTON_TYPE).setY(LEVEL_16_Y);
        guiButtons.get(LEVEL_16_AVAILABLE_BUTTON_TYPE).setX(LEVEL_16_X);
        guiButtons.get(LEVEL_16_AVAILABLE_BUTTON_TYPE).setY(LEVEL_16_Y);
        guiButtons.get(LEVEL_17_BUTTON_TYPE).setX(LEVEL_17_X);
        guiButtons.get(LEVEL_17_BUTTON_TYPE).setY(LEVEL_17_Y);
        guiButtons.get(LEVEL_17_COMPLETE_BUTTON_TYPE).setX(LEVEL_17_X);
        guiButtons.get(LEVEL_17_COMPLETE_BUTTON_TYPE).setY(LEVEL_17_Y);
        guiButtons.get(LEVEL_17_AVAILABLE_BUTTON_TYPE).setX(LEVEL_17_X);
        guiButtons.get(LEVEL_17_AVAILABLE_BUTTON_TYPE).setY(LEVEL_17_Y);
        guiButtons.get(LEVEL_18_BUTTON_TYPE).setX(LEVEL_18_X);
        guiButtons.get(LEVEL_18_BUTTON_TYPE).setY(LEVEL_18_Y);
        guiButtons.get(LEVEL_18_COMPLETE_BUTTON_TYPE).setX(LEVEL_18_X);
        guiButtons.get(LEVEL_18_COMPLETE_BUTTON_TYPE).setY(LEVEL_18_Y);
        guiButtons.get(LEVEL_18_AVAILABLE_BUTTON_TYPE).setX(LEVEL_18_X);
        guiButtons.get(LEVEL_18_AVAILABLE_BUTTON_TYPE).setY(LEVEL_18_Y);
        guiButtons.get(LEVEL_19_BUTTON_TYPE).setX(LEVEL_19_X);
        guiButtons.get(LEVEL_19_BUTTON_TYPE).setY(LEVEL_19_Y);
        guiButtons.get(LEVEL_19_COMPLETE_BUTTON_TYPE).setX(LEVEL_19_X);
        guiButtons.get(LEVEL_19_COMPLETE_BUTTON_TYPE).setY(LEVEL_19_Y);
        guiButtons.get(LEVEL_19_AVAILABLE_BUTTON_TYPE).setX(LEVEL_19_X);
        guiButtons.get(LEVEL_19_AVAILABLE_BUTTON_TYPE).setY(LEVEL_19_Y);
        guiButtons.get(LEVEL_20_BUTTON_TYPE).setX(LEVEL_20_X);
        guiButtons.get(LEVEL_20_BUTTON_TYPE).setY(LEVEL_20_Y);
        guiButtons.get(LEVEL_20_COMPLETE_BUTTON_TYPE).setX(LEVEL_20_X);
        guiButtons.get(LEVEL_20_COMPLETE_BUTTON_TYPE).setY(LEVEL_20_Y);
        guiButtons.get(LEVEL_20_AVAILABLE_BUTTON_TYPE).setX(LEVEL_20_X);
        guiButtons.get(LEVEL_20_AVAILABLE_BUTTON_TYPE).setY(LEVEL_20_Y);
        updateGUI();
    }
    
    //use this to use the repaint method
  public PathXPanel getCanvas(){
        return (PathXPanel)canvas;
    }
  
  public void increasePlayerMoney(){
     ((PathXDataModel)data).setBalance(((PathXDataModel)data).getBalance()+500);
  }
  
  public void setWinMoney(){
      if (level==1){
       ((PathXDataModel)data).setBalance(((PathXDataModel)data).getBalance()+20);
      }
      if (level==2){
       ((PathXDataModel)data).setBalance(((PathXDataModel)data).getBalance()+40);
      }
      if (level==3){
       ((PathXDataModel)data).setBalance(((PathXDataModel)data).getBalance()+60);
      }
      if (level==4){
       ((PathXDataModel)data).setBalance(((PathXDataModel)data).getBalance()+80);
      }
      if (level==5){
       ((PathXDataModel)data).setBalance(((PathXDataModel)data).getBalance()+100);
      }
      if (level==6){
       ((PathXDataModel)data).setBalance(((PathXDataModel)data).getBalance()+120);
      }
      if (level==7){
       ((PathXDataModel)data).setBalance(((PathXDataModel)data).getBalance()+140);
      }
      if (level==8){
       ((PathXDataModel)data).setBalance(((PathXDataModel)data).getBalance()+160);
      }
      if (level==9){
       ((PathXDataModel)data).setBalance(((PathXDataModel)data).getBalance()+180);
      }
       if (level==10){
       ((PathXDataModel)data).setBalance(((PathXDataModel)data).getBalance()+200);
      }
       if (level==11){
       ((PathXDataModel)data).setBalance(((PathXDataModel)data).getBalance()+220);
      }
       if (level==12){
       ((PathXDataModel)data).setBalance(((PathXDataModel)data).getBalance()+240);
      }
        if (level==13){
       ((PathXDataModel)data).setBalance(((PathXDataModel)data).getBalance()+260);
      }
        if (level==14){
       ((PathXDataModel)data).setBalance(((PathXDataModel)data).getBalance()+280);
      }
       if (level==15){
       ((PathXDataModel)data).setBalance(((PathXDataModel)data).getBalance()+300);
      }
       if (level==16){
       ((PathXDataModel)data).setBalance(((PathXDataModel)data).getBalance()+320);
      }
       if (level==17){
       ((PathXDataModel)data).setBalance(((PathXDataModel)data).getBalance()+340);
      }
       if (level==18){
       ((PathXDataModel)data).setBalance(((PathXDataModel)data).getBalance()+360);
      }
       if (level==19){
       ((PathXDataModel)data).setBalance(((PathXDataModel)data).getBalance()+380);
      }
       if (level==20){
       ((PathXDataModel)data).setBalance(((PathXDataModel)data).getBalance()+400);
      }
  }
  public void winLevel(){
     int x= ((PathXDataModel)data).getDestination().getX();
     int y= ((PathXDataModel)data).getDestination().getY();
     
     
     Sprite player = guiDecor.get(PLAYER_TYPE);
     player.setX(x);
     player.setY(y);
     Player playa=((PathXDataModel)data).getPlayer();
     playa.setX(x);
     playa.setY(y);
     win=true;
     
    
     levelwon=level;
     
     access=level+1;
     
    
     setWinMoney(); 
     //canvas.updateUI();
     //switchToGameScreen();
    
   
     eventHandler.respondToWinSelect(level);
      
  }
  public void loseLevel(){
    
      int bal= (int) ((int)((PathXDataModel)data).getBalance() * .9);
      lost=((PathXDataModel)data).getBalance()-bal;
      ((PathXDataModel)data).setBalance(bal);
      if(enableSound==true){
          
          audio.play(PathXPropertyType.AUDIO_CUE_POLICE.toString(), false);
      }
      eventHandler.respondToLoseSelect(level);
  }
  public void interactWithBandits(){
       if (level==1){
       int money=(int)(.10 * 20);    
       ((PathXDataModel)data).setBalance(((PathXDataModel)data).getBalance()-money);
      }
      if (level==2){
        int money=(int)(.10 * 40); 
       ((PathXDataModel)data).setBalance(((PathXDataModel)data).getBalance()-money);
      }
      if (level==3){
        int money=(int)(.10 * 60); 
       ((PathXDataModel)data).setBalance(((PathXDataModel)data).getBalance()-money);
      }
      if (level==4){
        int money=(int)(.10 * 80); 
       ((PathXDataModel)data).setBalance(((PathXDataModel)data).getBalance()-money);
      }
      if (level==5){
       int money=(int)(.10 * 100); 
       ((PathXDataModel)data).setBalance(((PathXDataModel)data).getBalance()-money);
      }
      if (level==6){
          int money = (int) (.10 * 120); 
       ((PathXDataModel)data).setBalance(((PathXDataModel)data).getBalance()-money);
      }
      if (level==7){
        int money = (int) (.10 * 140); 
       ((PathXDataModel)data).setBalance(((PathXDataModel)data).getBalance()-money);
      }
      if (level==8){
          int money = (int) (.10 * 160); 
       ((PathXDataModel)data).setBalance(((PathXDataModel)data).getBalance()- money);
      }
      if (level==9){
           int money = (int) (.10 * 180); 
       ((PathXDataModel)data).setBalance(((PathXDataModel)data).getBalance()-money);
      }
       if (level==10){
           int money = (int) (.10 * 200); 
       ((PathXDataModel)data).setBalance(((PathXDataModel)data).getBalance()-money);
      }
       if (level==11){
            int money = (int) (.10 * 220); 
       ((PathXDataModel)data).setBalance(((PathXDataModel)data).getBalance()-money);
      }
       if (level==12){
           int money = (int) (.10 * 240); 
       ((PathXDataModel)data).setBalance(((PathXDataModel)data).getBalance()-money);
      }
        if (level==13){
            int money=(int) (.10 * 260);
       ((PathXDataModel)data).setBalance(((PathXDataModel)data).getBalance()-money);
      }
        if (level==14){
            int money=(int) (.10 * 280);
       ((PathXDataModel)data).setBalance(((PathXDataModel)data).getBalance()-money);
      }
       if (level==15){
           int money=(int) (.10 * 300);
       ((PathXDataModel)data).setBalance(((PathXDataModel)data).getBalance()-money);
      }
       if (level==16){
           int money=(int) (.10 * 320);
       ((PathXDataModel)data).setBalance(((PathXDataModel)data).getBalance()-money);
      }
       if (level==17){
           int money= (int) (.10 *340);
       ((PathXDataModel)data).setBalance(((PathXDataModel)data).getBalance()-money);
      }
       if (level==18){
           int money= (int)(.10 * 360);
       ((PathXDataModel)data).setBalance(((PathXDataModel)data).getBalance()-money);
      }
       if (level==19){
           int money= (int)(.10 * 380);
       ((PathXDataModel)data).setBalance(((PathXDataModel)data).getBalance()-money);
      }
       if (level==20){
           int money= (int)(.10 * 400);
       ((PathXDataModel)data).setBalance(((PathXDataModel)data).getBalance()-money);
      }
       
      if (enableSound==true){
          audio.play(PathXPropertyType.AUDIO_CUE_BANDIT.toString(), false);
      } 
  }
  public void interactWithZombies(){
    if (enableSound==true){
          audio.play(PathXPropertyType.AUDIO_CUE_ZOMBIE.toString(), false);
      }   
  }
  
}
