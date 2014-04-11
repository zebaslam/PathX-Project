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

  
 public void switchToSplashScreen(){
     System.out.println("IN SWITCH TO SPLASH SCREEN");
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
     
     currentScreenState = MENU_SCREEN_STATE;
     data.setGameState(MiniGameState.NOT_STARTED);
     
     audio.play(PathXPropertyType.SONG_CUE_MENU_SCREEN.toString(), true); 
     audio.stop(PathXPropertyType.SONG_CUE_GAME_SCREEN.toString()); 
 }
 
public void switchToHelpScreen(){
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
     
      currentScreenState = HELP_SCREEN_STATE;
      
      
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
        //img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_BACKGROUND_GAME));
        //sT.addState(GAME_SCREEN_STATE, img);       
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
        
    }

    @Override
    public void initGUIHandlers() {
        eventHandler = new PathXEventHandler(this);
        window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        window.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we) 
            { eventHandler.respondToExitRequest(); }
        });
        
        //Event handler for close button
           guiButtons.get(CLOSE_BUTTON_TYPE).setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   eventHandler.respondToExitRequest();     }
        });
           
        //Event Handler for home button
           guiButtons.get(HOME_BUTTON_TYPE).setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae)
            {   eventHandler.respondToHomeRequest();        }
        });
    }
  
    @Override
    public void initData() {
         errorHandler = new PathXErrorHandler(window);
         fileManager= new PathXFileManager(this);
         data= new PathXDataModel(this);
    }
    @Override
    public void reset() {
        data.reset(this);
    }

    @Override
    public void updateGUI() {
        // GO THROUGH THE VISIBLE BUTTONS TO TRIGGER MOUSE OVERS
        Iterator<Sprite> buttonsIt = guiButtons.values().iterator();
        while (buttonsIt.hasNext())
        {
            Sprite button = buttonsIt.next();
            
            // ARE WE ENTERING A BUTTON?
            if (button.getState().equals(PathXCarState.VISIBLE_STATE.toString()))
            {
                if (button.containsPoint(data.getLastMouseX(), data.getLastMouseY()))
                {
                    button.setState(PathXCarState.MOUSE_OVER_STATE.toString());
                }
            }
            // ARE WE EXITING A BUTTON?
            else if (button.getState().equals(PathXCarState.MOUSE_OVER_STATE.toString()))
            {
                 if (!button.containsPoint(data.getLastMouseX(), data.getLastMouseY()))
                {
                    button.setState(PathXCarState.VISIBLE_STATE.toString());
                }
            }
        }
    }    
    
    @Override
    public void initAudioContent() {
              try
        {
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

            // PLAY THE WELCOME SCREEN SONG
            audio.play(PathXPropertyType.SONG_CUE_MENU_SCREEN.toString(), true);
        }
        catch(UnsupportedAudioFileException | IOException | LineUnavailableException | InvalidMidiDataException | MidiUnavailableException e)
        {
            errorHandler.processError(PathXPropertyType.TEXT_ERROR_LOADING_AUDIO);
        } 
    }

    private void loadAudioCue(PathXPropertyType audioCueType) 
            throws  UnsupportedAudioFileException, IOException, LineUnavailableException, 
                    InvalidMidiDataException, MidiUnavailableException
    {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String audioPath = props.getProperty(PathXPropertyType.PATH_AUDIO);
        String cue = props.getProperty(audioCueType.toString());
        audio.loadAudio(audioCueType.toString(), audioPath + cue);        
    }
    }

