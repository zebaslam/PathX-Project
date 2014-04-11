/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
// created image and data folder
package pathx;
import xml_utilities.InvalidXMLFileFormatException;
import properties_manager.PropertiesManager;

/**
 *
 * @author zeb
 */
public class PathX {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
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
        IMAGE_BUTTON_CLOSE,
        IMAGE_BUTTON_HOME,
        IMAGE_BUTTON_PLAY,
        IMAGE_BUTTON_RESET,
        IMAGE_BUTTON_SETTINGS,
     
        
        /* GAME TEXT */
        TEXT_ERROR_LOADING_AUDIO,
        TEXT_ERROR_LOADING_LEVEL,
        TEXT_ERROR_LOADING_RECORD,
        TEXT_ERROR_LOADING_XML_FILE,
        TEXT_ERROR_SAVING_RECORD,
        TEXT_PROMPT_EXIT,
        TEXT_TITLE_BAR_GAME,
        TEXT_TITLE_BAR_ERROR,
        
        /* AUDIO CUES 
        AUDIO_CUE_BAD_MOVE,
        AUDIO_CUE_CHEAT,
        AUDIO_CUE_DESELECT_TILE,
        AUDIO_CUE_GOOD_MOVE,
        AUDIO_CUE_SELECT_TILE,
        AUDIO_CUE_UNDO,
        AUDIO_CUE_WIN,
        SONG_CUE_GAME_SCREEN,
        SONG_CUE_MENU_SCREEN,
        */
        
        /* TILE LOADING STUFF 
        LEVEL_OPTIONS,
        LEVEL_IMAGE_OPTIONS,
        LEVEL_MOUSE_OVER_IMAGE_OPTIONS 
        */
   }
}
