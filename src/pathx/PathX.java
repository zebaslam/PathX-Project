/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
// created image and data folder
package pathx;
import xml_utilities.InvalidXMLFileFormatException;
import properties_manager.PropertiesManager;
import pathx.ui.PathXMiniGame;
import pathx.ui.PathXErrorHandler;
import static pathx.PathXConstants.*;


/**
 *
 * @author zeb
 */
public class PathX {

    /**
     * @param args the command line arguments
     */
     static PathXMiniGame miniGame = new PathXMiniGame();
    public static void main(String[] args) {
        try{
        PropertiesManager props = PropertiesManager.getPropertiesManager();
            props.addProperty(PropertiesManager.DATA_PATH_PROPERTY, PATH_DATA);
            props.loadProperties(PROPERTIES_FILE_NAME, PROPERTIES_SCHEMA_FILE_NAME);
            
            // THEN WE'LL LOAD THE GAME FLAVOR AS SPECIFIED BY THE PROPERTIES FILE
            String gameFlavorFile = props.getProperty(PathXPropertyType.FILE_GAME_PROPERTIES);
            props.loadProperties(gameFlavorFile, PROPERTIES_SCHEMA_FILE_NAME);
            
            String appTitle = props.getProperty(PathXPropertyType.TEXT_TITLE_BAR_GAME);
            miniGame.initMiniGame(appTitle, FPS, WINDOW_WIDTH, WINDOW_HEIGHT);
            
            // GET THE PROPER WINDOW DIMENSIONS
            miniGame.startGame();
        }
        
            catch(InvalidXMLFileFormatException ixmlffe)
        {
            // LET THE ERROR HANDLER PROVIDE THE RESPONSE
           PathXErrorHandler errorHandler = miniGame.getErrorHandler();
           errorHandler.processError(PathXPropertyType.TEXT_ERROR_LOADING_XML_FILE);
        }
        
    }
    
   public enum PathXPropertyType {
          // LOADED FROM properties.xml
        
        /* SETUP FILE NAMES */
        FILE_GAME_PROPERTIES,
        FILE_PLAYER_RECORD,

        /* DIRECTORY PATHS FOR FILE LOADING */
        PATH_AUDIO,
        PATH_IMG,
        PATH_DATA,
        // LOADED FROM THE GAME FLAVOR PROPERTIES XML FILE
            // path_x_properties.xml
                
        /* IMAGE FILE NAMES */
        IMAGE_BACKGROUND_GAME,
        IMAGE_BACKGROUND_MENU,
        IMAGE_BACKGROUND_HELP,
        IMAGE_BACKGROUND_LEVEL_SELECT,
        IMAGE_BACKGROUND_SETTINGS,
        IMAGE_BUTTON_HELP,
        IMAGE_BUTTON_HELP_MOUSE_OVER,
        IMAGE_BUTTON_CLOSE,
        IMAGE_BUTTON_CLOSE_MOUSE_OVER,
        IMAGE_BUTTON_HOME,
        IMAGE_BUTTON_HOME_MOUSE_OVER,
        IMAGE_BUTTON_PLAY,
        IMAGE_BUTTON_PLAY_MOUSE_OVER,
        IMAGE_BUTTON_RESET,
        IMAGE_BUTTON_RESET_MOUSE_OVER,
        IMAGE_BUTTON_SETTINGS,
        IMAGE_BUTTON_SETTINGS_MOUSE_OVER,
        IMAGE_BUTTON_PAUSE,
        IMAGE_BUTTON_PAUSE_MOUSE_OVER,  
        IMAGE_WINDOW_ICON,
        IMAGE_PLAYER,
        IMAGE_SETTINGS_MUSIC_SELECTED,
        IMAGE_SETTINGS_MUSIC_UNSELECTED,
        IMAGE_SETTINGS_SOUND_SELECTED,
        IMAGE_SETTINGS_SOUND_UNSELECTED,
     
        /*
        SPECIALS!!!
        */
        IMAGE_SPECIAL_GREEN_LIGHT,
        IMAGE_SPECIAL_GREEN_LIGHT_MOUSE_OVER,
        IMAGE_SPECIAL_RED_LIGHT,
        IMAGE_SPECIAL_RED_LIGHT_MOUSE_OVER,
        IMAGE_SPECIAL_FREEZE_TIME,
        IMAGE_SPECIAL_FREEZE_TIME_MOUSE_OVER,
        IMAGE_SPECIAL_DECREASE_SPEED,
        IMAGE_SPECIAL_DECREASE_SPEED_MOUSE_OVER,
        IMAGE_SPECIAL_INCREASE_SPEED,
        IMAGE_SPECIAL_INCREASE_SPEED_MOUSE_OVER,
        IMAGE_SPECIAL_INCREASE_PLAYER_SPEED,
        IMAGE_SPECIAL_INCREASE_PLAYER_SPEED_MOUSE_OVER,
        IMAGE_SPECIAL_FLAT_TIRE,
        IMAGE_SPECIAL_FLAT_TIRE_MOUSE_OVER,
        IMAGE_SPECIAL_EMPTY_GAS,
        IMAGE_SPECIAL_EMPTY_GAS_MOUSE_OVER,
        IMAGE_SPECIAL_CLOSE_ROAD,
        IMAGE_SPECIAL_CLOSE_ROAD_MOUSE_OVER,
        IMAGE_SPECIAL_CLOSE_INTERSECTION,
        IMAGE_SPECIAL_CLOSE_INTERSECTION_MOUSE_OVER,
        IMAGE_SPECIAL_OPEN_INTERSECTION,
        IMAGE_SPECIAL_OPEN_INTERSECTION_MOUSE_OVER,
        IMAGE_SPECIAL_STEAL,
        IMAGE_SPECIAL_STEAL_MOUSE_OVER,
        IMAGE_SPECIAL_MIND_CONTROL,
        IMAGE_SPECIAL_MIND_CONTROL_MOUSE_OVER,
        IMAGE_SPECIAL_INTANG,
        IMAGE_SPECIAL_INTANG_MOUSE_OVER,
        IMAGE_SPECIAL_MINDLESS_TERROR,
        IMAGE_SPECIAL_MINDLESS_TERROR_MOUSE_OVER,
        IMAGE_SPECIAL_FLYING,
        IMAGE_SPECIAL_FLYING_MOUSE_OVER,
        IMAGE_SPECIAL_INVINCIBILITY,
        IMAGE_SPECIAL_INVINCIBILITY_MOUSE_OVER,
        /*Direction Buttons*/
        IMAGE_BUTTON_UP,
        IMAGE_BUTTON_UP_MOUSE_OVER,
        IMAGE_BUTTON_DOWN,
        IMAGE_BUTTON_DOWN_MOUSE_OVER,
        IMAGE_BUTTON_LEFT,
        IMAGE_BUTTON_LEFT_MOUSE_OVER,
        IMAGE_BUTTON_RIGHT,
        IMAGE_BUTTON_RIGHT_MOUSE_OVER,
        //HELP_FILE_NAME,
        /* GAME TEXT */
        TEXT_ERROR_LOADING_AUDIO,
        TEXT_ERROR_LOADING_LEVEL,
        TEXT_ERROR_LOADING_RECORD,
        TEXT_ERROR_LOADING_XML_FILE,
        TEXT_ERROR_SAVING_RECORD,
        TEXT_PROMPT_EXIT,
        TEXT_TITLE_BAR_GAME,
        TEXT_TITLE_BAR_ERROR,
        TEXT_LABEL_BALANCE,
        TEXT_LABEL_GOAL,
        INVALID_URL_ERROR_TEXT,
        
       //Audio stuff
        AUDIO_CUE_GREEN_LIGHT,
        AUDIO_CUE_RED_LIGHT,
        AUDIO_CUE_DECREASE_SPEED_LIMIT,
        AUDIO_CUE_TIME_FREEZE,
        AUDIO_CUE_INCREASE_SPEED_LIMIT,
        AUDIO_CUE_PLAYER_SPEED,
        AUDIO_CUE_FLAT_TIRE,
        AUDIO_CUE_GAS,
        AUDIO_CUE_CLOSE_ROAD,
        AUDIO_CUE_CLOSE_INTERSECTION,
        AUDIO_CUE_OPEN_INTERSECTION,
        AUDIO_CUE_STEAL,
        AUDIO_CUE_MIND_CONTROL,
        AUDIO_CUE_MIND_TERROR,
        AUDIO_CUE_FLYING,
        AUDIO_CUE_INVINCIBILITY,
        AUDIO_CUE_INTANGIBILITY,
        SONG_CUE_GAME_SCREEN,
        SONG_CUE_MENU_SCREEN,
        SONG_CUE_LEVEL_SCREEN,
        
        //dialogs
        IMAGE_DIALOG_SETTINGS,
        IMAGE_DIALOG_LEVEL_SELECT,
        IMAGE_DIALOG_WIN_LEVEL,
        LEVEL_CLOSE_BUTTON,
        LEVEL_CLOSE_BUTTON_MOUSE_OVER,
        IMAGE_TRY_AGAIN,
        IMAGE_TRY_AGAIN_MOUSE_OVER,
        IMAGE_QUIT_LEVEL_MOUSE_OVER,
        IMAGE_QUIT_LEVEL,
        
        
        //map shit
        IMAGE_MAP_AVAILABLE,
        IMAGE_MAP_AVAILABLE_MOUSE_OVER,
        IMAGE_MAP_COMPLETED,
        IMAGE_MAP_COMPLETED_MOUSE_OVER,
        IMAGE_MAP_LOCKED,
        IMAGE_MAP_LOCKED_MOUSE_OVER,
        
        //LEVEL INFORMATION
        TEXT_LABEL_LEVEL_1_INFO,
        TEXT_LABEL_LEVEL_2_INFO,
        TEXT_LABEL_LEVEL_3_INFO,
        TEXT_LABEL_LEVEL_4_INFO,
        TEXT_LABEL_LEVEL_5_INFO,
        TEXT_LABEL_LEVEL_6_INFO,
        TEXT_LABEL_LEVEL_7_INFO,
        TEXT_LABEL_LEVEL_8_INFO,
        TEXT_LABEL_LEVEL_9_INFO,
        TEXT_LABEL_LEVEL_10_INFO,
        TEXT_LABEL_LEVEL_11_INFO,
        TEXT_LABEL_LEVEL_12_INFO,
        TEXT_LABEL_LEVEL_13_INFO,
        TEXT_LABEL_LEVEL_14_INFO,
        TEXT_LABEL_LEVEL_15_INFO,
        TEXT_LABEL_LEVEL_16_INFO,
        TEXT_LABEL_LEVEL_17_INFO,
        TEXT_LABEL_LEVEL_18_INFO,
        TEXT_LABEL_LEVEL_19_INFO,
        TEXT_LABEL_LEVEL_20_INFO,
        
        //level prompts
       TEXT_LABEL_GAME_1_INFO,
       TEXT_LABEL_GAME_2_INFO,
       TEXT_LABEL_GAME_3_INFO,
       TEXT_LABEL_GAME_4_INFO,
       TEXT_LABEL_GAME_5_INFO,
       TEXT_LABEL_GAME_6_INFO,
       TEXT_LABEL_GAME_7_INFO,
       TEXT_LABEL_GAME_8_INFO,
       TEXT_LABEL_GAME_9_INFO,
       TEXT_LABEL_GAME_10_INFO,
       TEXT_LABEL_GAME_11_INFO,
       TEXT_LABEL_GAME_12_INFO,
       TEXT_LABEL_GAME_13_INFO,
       TEXT_LABEL_GAME_14_INFO,
       TEXT_LABEL_GAME_15_INFO,
       TEXT_LABEL_GAME_16_INFO,
       TEXT_LABEL_GAME_17_INFO,
       TEXT_LABEL_GAME_18_INFO,
       TEXT_LABEL_GAME_19_INFO,
       TEXT_LABEL_GAME_20_INFO,
        /* TILE LOADING STUFF 
        LEVEL_OPTIONS,
        LEVEL_IMAGE_OPTIONS,
        LEVEL_MOUSE_OVER_IMAGE_OPTIONS 
        */
   }
}
