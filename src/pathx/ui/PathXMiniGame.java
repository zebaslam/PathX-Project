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
import javax.swing.JFrame;
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
//import sorting_hat.file.SortingHatFileManager;
//import sorting_hat.data.SortingHatRecord;

/**
 *
 * @author zeb
 * @author Richard Mckenna
 */
public class PathXMiniGame extends MiniGame {

    private PathXErrorHandler errorHandler;
    private PathXEventHandler eventHandler;
    private PathXFileManager  fileManager;
    // THE SCREEN CURRENTLY BEING PLAYED
    private String currentScreenState;
    static PathXMiniGame miniGame = new PathXMiniGame();
   
    public PathXErrorHandler getErrorHandler()
    {
        return errorHandler;
    }

      public PathXFileManager getFileManager()
    {
        return fileManager;
    }
     
      public void savePlayerRecord()
    {
        //TODO 
    }
    public boolean isCurrentScreenState(String testScreenState) {
        return testScreenState.equals(currentScreenState);
    }

    @Override
    public void initAudioContent() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void initData() {
         errorHandler = new PathXErrorHandler(window);
    }
 public void switchToSplashScreen(){
     
 }
    @Override
    public void initGUIControls() {
        BufferedImage img;
        float x, y;
        SpriteType sT;
        Sprite s;
        
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty(PathXPropertyType.PATH_IMG);        
        String windowIconFile = props.getProperty(PathXPropertyType.IMAGE_WINDOW_ICON);
        img = loadImage(imgPath + windowIconFile);
        window.setIconImage(img);
        
        // CONSTRUCT THE PANEL WHERE WE'LL DRAW EVERYTHING
        canvas = new PathXPanel(this, (PathXDataModel)data);
          // LOAD THE BACKGROUNDS, WHICH ARE GUI DECOR
        currentScreenState = MENU_SCREEN_STATE;
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_BACKGROUND_MENU));
        sT = new SpriteType(BACKGROUND_TYPE);
        sT.addState(MENU_SCREEN_STATE, img);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_BACKGROUND_GAME));
        sT.addState(GAME_SCREEN_STATE, img);
        s = new Sprite(sT, 0, 0, 0, 0, MENU_SCREEN_STATE);
        guiDecor.put(BACKGROUND_TYPE, s);
        
        String playButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_PLAY);
        sT = new SpriteType(PLAY_BUTTON_TYPE);
	img = loadImage(imgPath + playButton);
        sT.addState(PathXCarState.VISIBLE_STATE.toString(), img);
        String newMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_BUTTON_PLAY_MOUSE_OVER);
        img = loadImage(imgPath + newMouseOverButton);
        sT.addState(PathXCarState.MOUSE_OVER_STATE.toString(), img);
        s = new Sprite(sT, PLAY_BUTTON_X, PLAY_BUTTON_Y, 0, 0, PathXCarState.INVISIBLE_STATE.toString());
        guiButtons.put(PLAY_BUTTON_TYPE, s);
    }

    @Override
    public void initGUIHandlers() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateGUI() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
