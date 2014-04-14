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
    
    
    public static final String LEVEL_SELECT_BUTTON_TYPE = "LEVEL_SELECT_BUTTON_TYPE";
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
    public static final String CLOSE_DIALOG_BUTTON_TYPE="CLOSE_DIALOG_BUTTON_TYPE";
    
    public static final String LEVEL_1_BUTTON_TYPE="LEVEL_1_BUTTON_TYPE";
    public static final String LEVEL_2_BUTTON_TYPE="LEVEL_2_BUTTON_TYPE";
    public static final String LEVEL_3_BUTTON_TYPE="LEVEL_3_BUTTON_TYPE";
    public static final String LEVEL_4_BUTTON_TYPE="LEVEL_4_BUTTON_TYPE";
    public static final String LEVEL_5_BUTTON_TYPE="LEVEL_5_BUTTON_TYPE";
    public static final String LEVEL_6_BUTTON_TYPE="LEVEL_6_BUTTON_TYPE";
    public static final String LEVEL_7_BUTTON_TYPE="LEVEL_7_BUTTON_TYPE";
    public static final String LEVEL_8_BUTTON_TYPE="LEVEL_8_BUTTON_TYPE";
    public static final String LEVEL_9_BUTTON_TYPE="LEVEL_9_BUTTON_TYPE";
    public static final String LEVEL_10_BUTTON_TYPE="LEVEL_10_BUTTON_TYPE";
    public static final String LEVEL_11_BUTTON_TYPE="LEVEL_11_BUTTON_TYPE";
    public static final String LEVEL_12_BUTTON_TYPE="LEVEL_12_BUTTON_TYPE";
    public static final String LEVEL_13_BUTTON_TYPE="LEVEL_13_BUTTON_TYPE";
    public static final String LEVEL_14_BUTTON_TYPE="LEVEL_14_BUTTON_TYPE";
    public static final String LEVEL_15_BUTTON_TYPE="LEVEL_15_BUTTON_TYPE";
    public static final String LEVEL_16_BUTTON_TYPE="LEVEL_16_BUTTON_TYPE";
    public static final String LEVEL_17_BUTTON_TYPE="LEVEL_17_BUTTON_TYPE";
    public static final String LEVEL_18_BUTTON_TYPE="LEVEL_18_BUTTON_TYPE";
    public static final String LEVEL_19_BUTTON_TYPE="LEVEL_19_BUTTON_TYPE";
    public static final String LEVEL_20_BUTTON_TYPE="LEVEL_20_BUTTON_TYPE";
    
    
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
    
    //Level locations for dots 
    
    //portland, oregon
    public static final int LEVEL_1_X=50;
    public static final int LEVEL_1_Y=250;
    
    public static final int LEVEL_2_X=LEVEL_1_X;
    public static final int LEVEL_2_Y=LEVEL_1_Y+100;
    
    public static final int LEVEL_3_X=LEVEL_2_X+30;
    public static final int LEVEL_3_Y=LEVEL_2_Y+100;
    
    public static final int LEVEL_4_X=LEVEL_1_X+300;
    public static final int LEVEL_4_Y=LEVEL_1_Y;
    
    public static final int LEVEL_5_X=LEVEL_4_X-75;
    public static final int LEVEL_5_Y=LEVEL_4_Y+200;
    
    public static final int LEVEL_6_X=LEVEL_1_X+500;
    public static final int LEVEL_6_Y=LEVEL_1_Y;
    
    public static final int LEVEL_7_X=LEVEL_1_X+400;
    public static final int LEVEL_7_Y=LEVEL_3_Y;
    
    public static final int LEVEL_8_X=LEVEL_1_X+400;
    public static final int LEVEL_8_Y=LEVEL_3_Y+150;
    
    public static final int LEVEL_9_X=LEVEL_6_X+100;
    public static final int LEVEL_9_Y=LEVEL_3_Y;
    
    public static final int LEVEL_10_X=LEVEL_1_X+600;
    public static final int LEVEL_10_Y=LEVEL_3_Y+150;
    
    //north dakota
    public static final int LEVEL_11_X=LEVEL_1_X+700;
    public static final int LEVEL_11_Y=LEVEL_1_Y-200;
    
    //south dakota
    public static final int LEVEL_12_X=LEVEL_11_X;
    public static final int LEVEL_12_Y=LEVEL_11_Y+150;
    
    //nebraska
    public static final int LEVEL_13_X=LEVEL_12_X+50;
    public static final int LEVEL_13_Y=LEVEL_11_Y+250;
    
    //kansas
    public static final int LEVEL_14_X=LEVEL_13_X;
    public static final int LEVEL_14_Y=LEVEL_13_Y+150;
    
    //oaklahoma 
    public static final int LEVEL_15_X=LEVEL_14_X+100;
    public static final int LEVEL_15_Y=LEVEL_14_Y+150;
   
    //dallas, texas
    public static final int LEVEL_16_X=LEVEL_15_X+50;
    public static final int LEVEL_16_Y=LEVEL_15_Y+100;
    
    //san antonio, texas
    public static final int LEVEL_17_X=LEVEL_15_X;
    public static final int LEVEL_17_Y=LEVEL_15_Y+280;
    
    //minnesota
    public static final int LEVEL_18_X=LEVEL_11_X+250;
    public static final int LEVEL_18_Y=LEVEL_11_Y;
    
    //iowa
    public static final int LEVEL_19_X=LEVEL_18_X;
    public static final int LEVEL_19_Y=LEVEL_18_Y+260;
    
    //MISSOURI
    public static final int LEVEL_20_X=LEVEL_18_X+100;
    public static final int LEVEL_20_Y=LEVEL_19_Y+150;
    
    //level information
    public static final int LEVEL_INFO_X=530;
    public static final int LEVEL_INFO_Y=600;
    
    public static final int BALANCE_X=0;
    public static final int BALANCE_Y=DOWN_Y+50;
    
    public static final int GOAL_X= 0;
    public static final int GOAL_Y= BALANCE_Y+20;
    
    //For dialogs
    public static final String LEVEL_INFO_DIALOG_TYPE = "LEVEL_INFO_DIALOG_TYPE";
    public static final String LEVEL_COMPLETE_DIALOG_TYPE = "LEVEL_COMPLETE_DIALOG_TYPE";
    public static final String LEVEL_FAIL_DIALOG_TYPE = "LEVEL_FAIL_DIALOG_TYPE";
    public static final String SETTINGS_DIALOG_TYPE = "SETTINGS_DIALOG_TYPE";
    
    //close values for the close button on the level info diaglog
    public static final int LEVEL_CLOSE_X=300;
    public static final int LEVEL_CLOSE_Y=400;
    
    // COLORS USED FOR RENDERING VARIOUS THINGS, INCLUDING THE
    // COLOR KEY, WHICH REFERS TO THE COLOR TO IGNORE WHEN
    // LOADING ART.
    public static final Color COLOR_KEY = new Color(255, 174, 201);
    public static final Color FONT_COLOR = Color.WHITE;
    public static final Color COLOR_BLACK = Color.BLACK;
    public static final Color COLOR_DEBUG_TEXT = Color.BLACK;
    public static final Color COLOR_TEXT_DISPLAY = new Color (10, 160, 10);
    
    // FONTS USED DURING FOR TEXTUAL GAME DISPLAYS
    public static final Font FONT_TEXT_DISPLAY = new Font(Font.SANS_SERIF, Font.BOLD, 20);
    public static final Font FONT_HEADER = new Font(Font.SANS_SERIF, Font.BOLD, 14);
    public static final Font FONT_DEBUG_TEXT = new Font(Font.MONOSPACED, Font.BOLD, 14);
    public static final Font FONT_STATS = new Font(Font.MONOSPACED, Font.BOLD, 20);
        
}
