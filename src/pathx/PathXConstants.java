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
    public static final String UP_BUTTON_TYPE="UP_BUTTON_TYPE";
    public static final String DOWN_BUTTON_TYPE="DOWN_BUTTON_TYPE";
    public static final String LEFT_BUTTON_TYPE="LEFT_BUTTON_TYPE";
    public static final String RIGHT_BUTTON_TYPE="RIGHT_BUTTON_TYPE";
    
    // WE'LL USE THESE STATES TO CONTROL SWITCHING BETWEEN THE FIVE SCREENS
    public static final String MENU_SCREEN_STATE = "MENU_SCREEN_STATE";
    public static final String LEVEL_SELECT_SCREEN_STATE= "LEVEL_SELECT_STATE";
    public static final String GAME_SCREEN_STATE = "GAME_SCREEN_STATE";   
    public static final String SETTINGS_SCREEN_STATE= "SETTINGS_SCREEN_STATE";
    public static final String HELP_SCREEN_STATE="HELP_SCREEN_STATE";
    
    public static final String INVISIBLE_STATE="INVISIBLE_STATE";
    public static final String VISIBLE_STATE="VISIBLE_STATE";
    public static final int FPS = 30;

    // UI CONTROL SIZE AND POSITION SETTINGS
    public static final int WINDOW_WIDTH = 750;
    public static final int WINDOW_HEIGHT = 650;
    public static final float GAME_PICTURE_HEIGHT=1997;
    public static final float GAME_PICTURE_WIDTH=1001;
    public static final int VIEWPORT_MARGIN_LEFT = 20;
    public static final int VIEWPORT_MARGIN_RIGHT = 20;
    public static final int VIEWPORT_MARGIN_TOP = 20;
    public static final int VIEWPORT_MARGIN_BOTTOM = 20;
    public static final int LEVEL_BUTTON_WIDTH = 200;
    public static final int LEVEL_BUTTON_MARGIN = 5;
    public static final int LEVEL_BUTTON_Y = 570;
    public static final int VIEWPORT_INC = 5;
    
    public static final int EXIT_X=650;
    public static final int EXIT_Y=30;
    
    
    public static final int PLAY_BUTTON_X = 150;
    public static final int PLAY_BUTTON_Y = 500;
    
    public static final int RESET_X= PLAY_BUTTON_X +100;
    public static final int RESET_Y= 500;
    
    public static final int HELP_X= RESET_X +110;
    public static final int HELP_Y= 500;
    
    public static final int SETTINGS_X= RESET_X+210;
    public static final int SETTINGS_Y=500;
    
    public static final int HOME_X= EXIT_X-43;
    public static final int HOME_Y=30;
    
    public static final int UP_X=85;
    public static final int UP_Y=499;
    
    public static final int DOWN_X=85;
    public static final int DOWN_Y=550;
    
    public static final int LEFT_X=55;
    public static final int LEFT_Y=520;
    
    public static final int RIGHT_X=115;
    public static final int RIGHT_Y=520;
    
    //For dialogs
    public static final String LEVEL_INFO_DIALOG_TYPE = "LEVEL_INFO_DIALOG_TYPE";
    public static final String LEVEL_COMPLETE_DIALOG_TYPE = "LEVEL_COMPLETE_DIALOG_TYPE";
    public static final String LEVEL_FAIL_DIALOG_TYPE = "LEVEL_FAIL_DIALOG_TYPE";
    public static final String SETTINGS_DIALOG_TYPE = "SETTINGS_DIALOG_TYPE";
    // COLORS USED FOR RENDERING VARIOUS THINGS, INCLUDING THE
    // COLOR KEY, WHICH REFERS TO THE COLOR TO IGNORE WHEN
    // LOADING ART.
    public static final Color COLOR_KEY = new Color(255, 174, 201);
    public static final Color COLOR_DEBUG_TEXT = Color.BLACK;
    public static final Color COLOR_TEXT_DISPLAY = new Color (10, 160, 10);
    
    // FONTS USED DURING FOR TEXTUAL GAME DISPLAYS
    public static final Font FONT_TEXT_DISPLAY = new Font(Font.SANS_SERIF, Font.BOLD, 48);
    public static final Font FONT_DEBUG_TEXT = new Font(Font.MONOSPACED, Font.BOLD, 14);
    public static final Font FONT_STATS = new Font(Font.MONOSPACED, Font.BOLD, 20);
        
}
