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
        
        // LOADED FROM THE GAME FLAVOR PROPERTIES XML FILE
            // path_x_properties.xml
                
        /* IMAGE FILE NAMES */
        IMAGE_BACKGROUND_GAME,
        IMAGE_BACKGROUND_MENU,
        IMAGE_BUTTON_HELP,
        IMAGE_BUTTON_HELP_MOUSE_OVER,
        IMAGE_BUTTON_CLOSE,
        IMAGE_BUTTON_CLOSE_MOUSE_OVER,
        IMAGE_BUTTON_HOME,
        IMAGE_BUTTON_PLAY,
        IMAGE_BUTTON_PLAY_MOUSE_OVER,
        IMAGE_BUTTON_RESET,
        IMAGE_BUTTON_RESET_MOUSE_OVER,
        IMAGE_BUTTON_SETTINGS,
        IMAGE_BUTTON_SETTINGS_MOUSE_OVER,
        IMAGE_WINDOW_ICON,
     
        
        /* GAME TEXT */
        TEXT_ERROR_LOADING_AUDIO,
        TEXT_ERROR_LOADING_LEVEL,
        TEXT_ERROR_LOADING_RECORD,
        TEXT_ERROR_LOADING_XML_FILE,
        TEXT_ERROR_SAVING_RECORD,
        TEXT_PROMPT_EXIT,
        TEXT_TITLE_BAR_GAME,
        TEXT_TITLE_BAR_ERROR,
        
       //Audio stuff
        AUDIO_CUE_BAD_MOVE,
        AUDIO_CUE_CHEAT,
        AUDIO_CUE_DESELECT_TILE,
        AUDIO_CUE_GOOD_MOVE,
        AUDIO_CUE_SELECT_TILE,
        AUDIO_CUE_UNDO,
        AUDIO_CUE_WIN,
        SONG_CUE_GAME_SCREEN,
        SONG_CUE_MENU_SCREEN,
        
        
        /* TILE LOADING STUFF 
        LEVEL_OPTIONS,
        LEVEL_IMAGE_OPTIONS,
        LEVEL_MOUSE_OVER_IMAGE_OPTIONS 
        */
   }
}
