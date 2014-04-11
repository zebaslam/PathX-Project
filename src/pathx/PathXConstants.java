package pathx;
import java.awt.Color;
import java.awt.Font;

/**
 *This class stores all the constants used by The PathX application. We'll
 * do this here rather than load them from files because many of these are
 * derived from each other.
 * @author Zeb Aslam (108041523)
 * @author Richard Mckenna 
 */
public class PathXConstants {
    // WE NEED THESE CONSTANTS JUST TO GET STARTED
    // LOADING SETTINGS FROM OUR XML FILES
    public static String PROPERTY_TYPES_LIST = "property_types.txt";
    public static String PROPERTIES_FILE_NAME = "properties.xml";
    public static String PROPERTIES_SCHEMA_FILE_NAME = "properties_schema.xsd";    
    public static String PATH_DATA = "./data/";
    
    // THESE ARE THE TYPES OF CONTROLS, WE USE THESE CONSTANTS BECAUSE WE'LL
    // STORE THEM BY TYPE, SO THESE WILL PROVIDE A MEANS OF IDENTIFYING THEM
    
    // EACH SCREEN HAS ITS OWN BACKGROUND TYPE
    public static final String BACKGROUND_TYPE = "BACKGROUND_TYPE";
    
    // THIS REPRESENTS THE BUTTONS ON THE MENU SCREEN FOR LEVEL SELECTION
    //public static final String LEVEL_SELECT_BUTTON_TYPE = "LEVEL_SELECT_BUTTON_TYPE";
    public static final String HELP_BUTTON_TYPE= "HELP_BUTTON_TYPE";
    public static final String CLOSE_BUTTON_TYPE= "CLOSE_BUTTON_TYPE";
    public static final String HOME_BUTTON_TYPE= "HOME_BUTTON_TYPE";
    public static final String PLAY_BUTTON_TYPE= "PLAY_BUTTON_TYPE";
    public static final String RESET_BUTTON_TYPE= "RESET_BUTTON_TYPE"; 
    public static final String SETTINGS_BUTTON_TYPE= "SETTINGS_BUTTON_TYPE";
    
    // WE'LL USE THESE STATES TO CONTROL SWITCHING BETWEEN THE FIVE SCREENS
    public static final String MENU_SCREEN_STATE = "MENU_SCREEN_STATE";
    public static final String LEVEL_SELECT_SCREEN_STATE= "LEVEL_SELECT_STATE";
    public static final String GAME_SCREEN_STATE = "GAME_SCREEN_STATE";   
    public static final String SETTINGS_SCREEN_STATE= "SETTINGS_SCREEN_STATE";
    public static final String HELP_SCREEN_STATE="HELP_SCREEN_STATE";
    
    public static final int FPS = 30;

    // UI CONTROL SIZE AND POSITION SETTINGS
    public static final int WINDOW_WIDTH = 640;
    public static final int WINDOW_HEIGHT = 480;
    public static final int VIEWPORT_MARGIN_LEFT = 20;
    public static final int VIEWPORT_MARGIN_RIGHT = 20;
    public static final int VIEWPORT_MARGIN_TOP = 20;
    public static final int VIEWPORT_MARGIN_BOTTOM = 20;
    public static final int LEVEL_BUTTON_WIDTH = 200;
    public static final int LEVEL_BUTTON_MARGIN = 5;
    public static final int LEVEL_BUTTON_Y = 570;
    public static final int VIEWPORT_INC = 5;
        
}
