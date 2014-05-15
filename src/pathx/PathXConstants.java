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
    public static final String  LEVELS_PATH = PATH_DATA + "levels/";
    
    //From pathxleveleditor
        
    // WE'LL NEED THESE TO DYNAMICALLY BUILD TEXT
    public static final String TITLE                        = "pathX Level Editor";
    public static final String EMPTY_TEXT                   = "";
    public static final String XML_LEVEL_FILE_EXTENSION         = ".xml";
    public static final String BIN_LEVEL_FILE_EXTENSION         = ".bin";
    public static final String APP_NAME                     = "PathX Level Editor";
    public static final String APP_NAME_FILE_NAME_SEPARATOR = " - ";
    public static final String PNG_FORMAT_NAME              = "png";
    public static final String PNG_FILE_EXTENSION           = "." + PNG_FORMAT_NAME;  
    public static final String LEVEL_SELECT_BUTTON_TYPE = "LEVEL_SELECT_BUTTON_TYPE";
    
    public static final String DEFAULT_BG_IMG       = "PortlandBackground.png";
    public static final String DEFAULT_START_IMG    = "DefaultStartLocation.png";
    public static final String DEFAULT_DEST_IMG     = "DefaultDestination.png";
    
    // RENDERING SETTINGS
    public static final int INTERSECTION_RADIUS = 20;
    public static final int INT_STROKE = 3;
    public static final int ONE_WAY_TRIANGLE_HEIGHT = 40;
    public static final int ONE_WAY_TRIANGLE_WIDTH = 60;

     // FOR SCROLLING THE VIEWPORT
    public static final int SCROLL_SPEED = 6;
    
   public static final int MAX_MOVE_VELOCITY = 20;
    
    // INITIAL START/DEST LOCATIONS
    public static final int DEFAULT_START_X = 32;
    public static final int DEFAULT_START_Y = 100;
    public static final int DEFAULT_DEST_X = 650;
    public static final int DEFAULT_DEST_Y = 100;
    
     // AND FOR THE ROAD SPEED LIMITS
    public static final int DEFAULT_SPEED_LIMIT = 30;
    public static final int MIN_SPEED_LIMIT = 10;
    public static final int MAX_SPEED_LIMIT = 100;
    public static final int SPEED_LIMIT_STEP = 10;
    
    // DEFAULT COLORS
    public static final Color   INT_OUTLINE_COLOR   = Color.BLACK;
    public static final Color   HIGHLIGHTED_COLOR = Color.YELLOW;
    public static final Color   OPEN_INT_COLOR      = Color.GREEN;
    public static final Color   CLOSED_INT_COLOR    = Color.RED;
    
        // FOR LOADING STUFF FROM OUR LEVEL XML FILES    
    // THIS IS THE NAME OF THE SCHEMA
    public static final String  LEVEL_SCHEMA = "PathXLevelSchema.xsd";
    
    // CONSTANTS FOR LOADING DATA FROM THE XML FILES
    // THESE ARE THE XML NODES
    public static final String LEVEL_NODE = "level";
    public static final String INTERSECTIONS_NODE = "intersections";
    public static final String INTERSECTION_NODE = "intersection";
    public static final String ROADS_NODE = "roads";
    public static final String ROAD_NODE = "road";
    public static final String START_INTERSECTION_NODE = "start_intersection";
    public static final String DESTINATION_INTERSECTION_NODE = "destination_intersection";
    public static final String MONEY_NODE = "money";
    public static final String POLICE_NODE = "police";
    public static final String BANDITS_NODE = "bandits";
    public static final String ZOMBIES_NODE = "zombies";

    //AND THE ATTRIBUTES FOR THOSE NODES
    public static final String NAME_ATT = "name";
    public static final String IMAGE_ATT = "image";
    public static final String ID_ATT = "id";
    public static final String X_ATT = "x";
    public static final String Y_ATT = "y";
    public static final String OPEN_ATT = "open";
    public static final String INT_ID1_ATT = "int_id1";
    public static final String INT_ID2_ATT = "int_id2";
    public static final String SPEED_LIMIT_ATT = "speed_limit";
    public static final String ONE_WAY_ATT = "one_way";
    public static final String AMOUNT_ATT = "amount";
    public static final String NUM_ATT = "num";

    // FOR NICELY FORMATTED XML OUTPUT
    public static final String XML_INDENT_PROPERTY = "{http://xml.apache.org/xslt}indent-amount";
    public static final String XML_INDENT_VALUE = "5";
    public static final String YES_VALUE = "Yes";
    // THESE ARE THE TYPES OF CONTROLS, WE USE THESE CONSTANTS BECAUSE WE'LL
    // STORE THEM BY TYPE, SO THESE WILL PROVIDE A MEANS OF IDENTIFYING THEM
    
    // EACH SCREEN HAS ITS OWN BACKGROUND TYPE
    public static final String BACKGROUND_TYPE = "BACKGROUND_TYPE";
    
    // THIS REPRESENTS THE BUTTONS ON THE MENU SCREEN FOR LEVEL SELECTION
    //public static final String LEVEL_SELECT_BUTTON_TYPE = "LEVEL_SELECT_BUTTON_TYPE";
    public static final String HELP_BUTTON_TYPE= "HELP_BUTTON_TYPE";
    public static final String MUSIC_SELECTED_BUTTON_TYPE="MUSIC_SELECTED_BUTTON_TYPE";
    public static final String MUSIC_UNSELECTED_BUTTON_TYPE="MUSIC_UNSELECTED_BUTTON_TYPE";
    public static final String SOUND_SELECTED_BUTTON_TYPE="SOUND_SELECTED_BUTTON_TYPE";
    public static final String SOUND_UNSELECTED_BUTTON_TYPE="SOUND_UNSELECTED_BUTTON_TYPE";
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
    public static final String PAUSE_BUTTON_TYPE="PAUSE_BUTTON_TYPE";    
    public static final String LEVEL_1_BUTTON_TYPE="LEVEL_1_BUTTON_TYPE";
    public static final String LEVEL_1_COMPLETE_BUTTON_TYPE="LEVEL_1_COMPLETE_BUTTON_TYPE";
    public static final String LEVEL_2_BUTTON_TYPE="LEVEL_2_BUTTON_TYPE";
    public static final String LEVEL_2_AVAILABLE_BUTTON_TYPE="LEVEL_2_AVAILABLE_BUTTON_TYPE";
    public static final String LEVEL_2_COMPLETE_BUTTON_TYPE="LEVEL_2_COMPLETE_BUTTON_TYPE";
    public static final String LEVEL_3_BUTTON_TYPE="LEVEL_3_BUTTON_TYPE";
    public static final String LEVEL_3_AVAILABLE_BUTTON_TYPE="LEVEL_3_AVAILABLE_BUTTON_TYPE";
    public static final String LEVEL_3_COMPLETE_BUTTON_TYPE="LEVEL_3_COMPLETE_BUTTON_TYPE";
    public static final String LEVEL_4_BUTTON_TYPE="LEVEL_4_BUTTON_TYPE";
    public static final String LEVEL_4_AVAILABLE_BUTTON_TYPE="LEVEL_4_AVAILABLE_BUTTON_TYPE";
    public static final String LEVEL_4_COMPLETE_BUTTON_TYPE="LEVEL_4_COMPLETE_BUTTON_TYPE";
    public static final String LEVEL_5_BUTTON_TYPE="LEVEL_5_BUTTON_TYPE";
    public static final String LEVEL_5_AVAILABLE_BUTTON_TYPE="LEVEL_5_AVAILABLE_BUTTON_TYPE";
    public static final String LEVEL_5_COMPLETE_BUTTON_TYPE="LEVEL_5_COMPLETE_BUTTON_TYPE";
    public static final String LEVEL_6_BUTTON_TYPE="LEVEL_6_BUTTON_TYPE";
    public static final String LEVEL_6_AVAILABLE_BUTTON_TYPE="LEVEL_6_AVAILABLE_BUTTON_TYPE";
    public static final String LEVEL_6_COMPLETE_BUTTON_TYPE="LEVEL_6_COMPLETE_BUTTON_TYPE";
    public static final String LEVEL_7_BUTTON_TYPE="LEVEL_7_BUTTON_TYPE";
    public static final String LEVEL_7_AVAILABLE_BUTTON_TYPE="LEVEL_7_AVAILABLE_BUTTON_TYPE";
    public static final String LEVEL_7_COMPLETE_BUTTON_TYPE="LEVEL_7_COMPLETE_BUTTON_TYPE";
    public static final String LEVEL_8_BUTTON_TYPE="LEVEL_8_BUTTON_TYPE";
    public static final String LEVEL_8_AVAILABLE_BUTTON_TYPE="LEVEL_8_AVAILABLE_BUTTON_TYPE";
    public static final String LEVEL_8_COMPLETE_BUTTON_TYPE="LEVEL_8_COMPLETE_BUTTON_TYPE";
    public static final String LEVEL_9_AVAILABLE_BUTTON_TYPE="LEVEL_9_AVAILABLE_BUTTON_TYPE";
    public static final String LEVEL_9_COMPLETE_BUTTON_TYPE="LEVEL_9_COMPLETE_BUTTON_TYPE";
    public static final String LEVEL_9_BUTTON_TYPE="LEVEL_9_BUTTON_TYPE";
    public static final String LEVEL_10_BUTTON_TYPE="LEVEL_10_BUTTON_TYPE";
    public static final String LEVEL_10_AVAILABLE_BUTTON_TYPE="LEVEL_10_AVAILABLE_BUTTON_TYPE";
    public static final String LEVEL_10_COMPLETE_BUTTON_TYPE="LEVEL_10_COMPLETE_BUTTON_TYPE";
    public static final String LEVEL_11_BUTTON_TYPE="LEVEL_11_BUTTON_TYPE";
    public static final String LEVEL_11_AVAILABLE_BUTTON_TYPE="LEVEL_11_AVAILABLE_BUTTON_TYPE";
    public static final String LEVEL_11_COMPLETE_BUTTON_TYPE="LEVEL_11_COMPLETE_BUTTON_TYPE";
    public static final String LEVEL_12_BUTTON_TYPE="LEVEL_12_BUTTON_TYPE";
    public static final String LEVEL_12_AVAILABLE_BUTTON_TYPE="LEVEL_12_AVAILABLE_BUTTON_TYPE";
    public static final String LEVEL_12_COMPLETE_BUTTON_TYPE="LEVEL_12_COMPLETE_BUTTON_TYPE";
    public static final String LEVEL_13_BUTTON_TYPE="LEVEL_13_BUTTON_TYPE";
    public static final String LEVEL_13_AVAILABLE_BUTTON_TYPE="LEVEL_13_AVAILABLE_BUTTON_TYPE";
    public static final String LEVEL_13_COMPLETE_BUTTON_TYPE="LEVEL_13_COMPLETE_BUTTON_TYPE";
    public static final String LEVEL_14_BUTTON_TYPE="LEVEL_14_BUTTON_TYPE";
    public static final String LEVEL_14_AVAILABLE_BUTTON_TYPE="LEVEL_14_AVAILABLE_BUTTON_TYPE";
    public static final String LEVEL_14_COMPLETE_BUTTON_TYPE="LEVEL_14_COMPLETE_BUTTON_TYPE";
    public static final String LEVEL_15_BUTTON_TYPE="LEVEL_15_BUTTON_TYPE";
    public static final String LEVEL_15_AVAILABLE_BUTTON_TYPE="LEVEL_15_AVAILABLE_BUTTON_TYPE";
    public static final String LEVEL_15_COMPLETE_BUTTON_TYPE="LEVEL_15_COMPLETE_BUTTON_TYPE";
    public static final String LEVEL_16_BUTTON_TYPE="LEVEL_16_BUTTON_TYPE";
    public static final String LEVEL_16_AVAILABLE_BUTTON_TYPE="LEVEL_16_AVAILABLE_BUTTON_TYPE";
    public static final String LEVEL_16_COMPLETE_BUTTON_TYPE="LEVEL_16_COMPLETE_BUTTON_TYPE";
    public static final String LEVEL_17_BUTTON_TYPE="LEVEL_17_BUTTON_TYPE";
    public static final String LEVEL_17_AVAILABLE_BUTTON_TYPE="LEVEL_17_AVAILABLE_BUTTON_TYPE";
    public static final String LEVEL_17_COMPLETE_BUTTON_TYPE="LEVEL_17_COMPLETE_BUTTON_TYPE";
    public static final String LEVEL_18_BUTTON_TYPE="LEVEL_18_BUTTON_TYPE";
    public static final String LEVEL_18_AVAILABLE_BUTTON_TYPE="LEVEL_18_AVAILABLE_BUTTON_TYPE";
    public static final String LEVEL_18_COMPLETE_BUTTON_TYPE="LEVEL_18_COMPLETE_BUTTON_TYPE";
    public static final String LEVEL_19_BUTTON_TYPE="LEVEL_19_BUTTON_TYPE";
    public static final String LEVEL_19_AVAILABLE_BUTTON_TYPE="LEVEL_19_AVAILABLE_BUTTON_TYPE";
    public static final String LEVEL_19_COMPLETE_BUTTON_TYPE="LEVEL_19_COMPLETE_BUTTON_TYPE";
    public static final String LEVEL_20_BUTTON_TYPE="LEVEL_20_BUTTON_TYPE";
    public static final String LEVEL_20_AVAILABLE_BUTTON_TYPE="LEVEL_20_AVAILABLE_BUTTON_TYPE";
    public static final String LEVEL_20_COMPLETE_BUTTON_TYPE="LEVEL_20_COMPLETE_BUTTON_TYPE";
    
    //rendering dialog related information for the win dialog
    public static final String TRY_AGAIN_TYPE="TRY_AGAIN_TYPE";
    public static final String QUIT_LEVEL_TYPE="QUIT_LEVEL_TYPE";
    
    //police and player
    public static final String PLAYER_TYPE="PLAYER_TYPE";
    public static final String POLICE_TYPE="POLICE_TYPE";
    public static final String POLICE_TYPE_2="POLICE_TYPE_2";
    public static final String POLICE_TYPE_3="POLICE_TYPE_3";
    public static final String POLICE_TYPE_4="POLICE_TYPE_4";
    public static final String POLICE_TYPE_5="POLICE_TYPE_5";
    public static final String POLICE_TYPE_6="POLICE_TYPE_6";
    
    public static final String SPECIAL_GREENLIGHT_TYPE="SPECIAL_GREENLIGHT_TYPE";
    public static final String SPECIAL_REDLIGHT_TYPE="SPECIAL_REDLIGHT_TYPE";
    public static final String SPECIAL_FREEZETIME_TYPE="SPECIAL_FREEZETIME_TYPE";
    public static final String SPECIAL_DECREASE_SPEED_TYPE="SPECIAL_DECREASE_SPEED_TYPE";
    public static final String SPECIAL_INCREASE_SPEED_TYPE="SPECIAL_INCREASE_SPEED_TYPE";
    public static final String SPECIAL_PLAYER_INCREASE_SPEED_TYPE="SPECIAL_PLAYER_INCREASE_SPEED_TYPE";
    public static final String SPECIAL_FLAT_TIRE="SPECIAL_FLAT_TIRE";
    public static final String SPECIAL_EMPTY_GAS="SPECIAL_EMPTY_GAS";
    public static final String SPECIAL_CLOSE_INTERSECTION="SPECIAL_CLOSE_INTERSECTION";
    public static final String SPECIAL_OPEN_INTERSECTION="SPECIAL_OPEN_INTERSECTION";
    public static final String SPECIAL_CLOSE_ROAD="SPECIAL_CLOSE_ROAD";
    public static final String SPECIAL_STEAL="SPECIAL_STEAL";
    public static final String SPECIAL_MIND_CONTROL="SPECIAL_MIND_CONTROL";
    public static final String SPECIAL_INTANG="SPECIAL_INTANG";
    public static final String SPECIAL_MINDLESS_TERROR="SPECIAL_MINDLESS_TERROR";
    public static final String SPECIAL_FLYING="SPECIAL_FLYING";
    public static final String SPECIAL_INVINCIBILITY="SPECIAL_INVINCIBILITY";
    // WE'LL USE THESE STATES TO CONTROL SWITCHING BETWEEN THE FIVE SCREENS
    public static final String MENU_SCREEN_STATE = "MENU_SCREEN_STATE";
    public static final String LEVEL_SELECT_SCREEN_STATE= "LEVEL_SELECT_STATE";
    public static final String GAME_SCREEN_STATE = "GAME_SCREEN_STATE";   
    public static final String SETTINGS_SCREEN_STATE= "SETTINGS_SCREEN_STATE";
    public static final String HELP_SCREEN_STATE="HELP_SCREEN_STATE";
    public static final String WIN_SCREEN_STATE="WIN_SCREEN_STATE";
    public static final String LOSE_SCREEN_STATE="LOSE_SCREEN_STATE";
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
    
    public static final int EXIT_X=690;
    public static final int EXIT_Y=0;
    
    
    public static final int PLAY_BUTTON_X = 150;
    public static final int PLAY_BUTTON_Y = 500;
    
    public static final int RESET_X= PLAY_BUTTON_X +100;
    public static final int RESET_Y= 500;
    
    public static final int HELP_X= RESET_X +110;
    public static final int HELP_Y= 500;
    
    public static final int SETTINGS_X= RESET_X+210;
    public static final int SETTINGS_Y=500;
    
    public static final int HOME_X= EXIT_X-43;
    public static final int HOME_Y=0;
    
    public static final int PAUSE_X=HOME_X-40;
    public static final int PAUSE_Y=0;
    public static final int UP_X=85;
    public static final int UP_Y=499;
    
    public static final int DOWN_X=85;
    public static final int DOWN_Y=550;
    
    public static final int LEFT_X=55;
    public static final int LEFT_Y=520;
    
    public static final int RIGHT_X=115;
    public static final int RIGHT_Y=520;
    
    public static final int MUSIC_SELECTED_X=310;
    public static final int MUSIC_SELECTED_Y=255;
    
    public static final int SOUND_SELECTED_X=MUSIC_SELECTED_X;
    public static final int SOUND_SELECTED_Y=MUSIC_SELECTED_Y+70;
    //Level locations for dots 
    
    //portland, oregon
    public static final int LEVEL_1_X=50;
    public static final int LEVEL_1_Y=250;
    
    //sacremento,california
    public static final int LEVEL_2_X=LEVEL_1_X;
    public static final int LEVEL_2_Y=LEVEL_1_Y+100;
    
    //san jose, california 
    public static final int LEVEL_3_X=LEVEL_2_X+30;
    public static final int LEVEL_3_Y=LEVEL_2_Y+100;
    
    //boise, idaho
    public static final int LEVEL_4_X=LEVEL_1_X+300;
    public static final int LEVEL_4_Y=LEVEL_1_Y;
    
    //las vegas, nevada
    public static final int LEVEL_5_X=LEVEL_4_X-75;
    public static final int LEVEL_5_Y=LEVEL_4_Y+200;
    
    //cheyenne, wyoming
    public static final int LEVEL_6_X=LEVEL_1_X+500;
    public static final int LEVEL_6_Y=LEVEL_1_Y;
    
    //salt like city, utah
    public static final int LEVEL_7_X=LEVEL_1_X+400;
    public static final int LEVEL_7_Y=LEVEL_3_Y;
    
    //phoenix, arizona
    public static final int LEVEL_8_X=LEVEL_1_X+400;
    public static final int LEVEL_8_Y=LEVEL_3_Y+150;
    
    //denver, colorado
    public static final int LEVEL_9_X=LEVEL_6_X+100;
    public static final int LEVEL_9_Y=LEVEL_3_Y;
    
    //santa fe, new mexico
    public static final int LEVEL_10_X=LEVEL_1_X+600;
    public static final int LEVEL_10_Y=LEVEL_3_Y+150;
    
    //Bismask, north dakota
    public static final int LEVEL_11_X=LEVEL_1_X+700;
    public static final int LEVEL_11_Y=LEVEL_1_Y-200;
    
    //Pierre, south dakota
    public static final int LEVEL_12_X=LEVEL_11_X;
    public static final int LEVEL_12_Y=LEVEL_11_Y+150;
    
    //Lincoln, nebraska
    public static final int LEVEL_13_X=LEVEL_12_X+50;
    public static final int LEVEL_13_Y=LEVEL_11_Y+250;
    
    //Topeka, kansas
    public static final int LEVEL_14_X=LEVEL_13_X;
    public static final int LEVEL_14_Y=LEVEL_13_Y+150;
    
    //oaklahoma city, oaklahoma
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
    
    public static final int GAME_BALANCE_X=10;
    public static final int GAME_BALANCE_Y=30;
    
    public static final int GAME_LEVEL_NAME_X=GAME_BALANCE_X+70;
    public static final int GAME_LEVEL_NAME_Y=GAME_BALANCE_Y;
    
    public static final int GOAL_X= 0;
    public static final int GOAL_Y= BALANCE_Y+20;
    
    public static final int PLAYER_X=0;
    public static final int PLAYER_Y=0;
    
    //specials coordinates
    public static final int GREEN_LIGHT_X=GAME_LEVEL_NAME_X+180;
    public static final int GREEN_LIGHT_Y=0;
    
    public static final int RED_LIGHT_X=GREEN_LIGHT_X+30;
    public static final int RED_LIGHT_Y=0;
    
    public static final int FREEZE_TIME_X=RED_LIGHT_X+30;
    public static final int FREEZE_TIME_Y=0;
    
    public static final int DECREASE_SPEED_X=FREEZE_TIME_X+30;
    public static final int DECREASE_SPEED_Y=0;
    
    public static final int INCREASE_SPEED_X=DECREASE_SPEED_X+30;
    public static final int INCREASE_SPEED_Y=0;
    
    public static final int INCREASE_PLAYER_SPEED_X=INCREASE_SPEED_X+30;
    public static final int INCREASE_PLAYER_SPEED_Y=0;
    
    public static final int FLAT_TIRE_X=INCREASE_PLAYER_SPEED_X+30;
    public static final int FLAT_TIRE_Y=0;
    
    public static final int EMPTY_GAS_X=FLAT_TIRE_X+30;
    public static final int EMPTY_GAS_Y=0;
    
    public static final int CLOSE_ROAD_X=GREEN_LIGHT_X;
    public static final int CLOSE_ROAD_Y=32;
    
    public static final int CLOSE_INTERSECTION_X=CLOSE_ROAD_X+30;
    public static final int CLOSE_INTERSECTION_Y=32;
    
    public static final int OPEN_INTERSECTION_X=CLOSE_INTERSECTION_X+30;
    public static final int OPEN_INTERSECTION_Y=32;
    
    public static final int STEAL_X=OPEN_INTERSECTION_X+30;
    public static final int STEAL_Y=32;
    
    public static final int MIND_CONTROL_X=STEAL_X+30;
    public static final int MIND_CONTROL_Y=32;
    
    public static final int INTANG_X=MIND_CONTROL_X+30;
    public static final int INTANG_Y=32;
    
    public static final int MIND_TERROR_X=INTANG_X+30;
    public static final int MIND_TERROR_Y=32;
    
    public static final int FLYING_X=MIND_TERROR_X+30;
    public static final int FLYING_Y=32;
    
    public static final int INVINC_X=FLYING_X+30;
    public static final int INVINC_Y=32;
    

    //For dialogs
    public static final String LEVEL_INFO_DIALOG_TYPE = "LEVEL_INFO_DIALOG_TYPE";
    public static final String LEVEL_COMPLETE_DIALOG_TYPE = "LEVEL_COMPLETE_DIALOG_TYPE";
    public static final String LEVEL_FAIL_DIALOG_TYPE = "LEVEL_FAIL_DIALOG_TYPE";
    public static final String SETTINGS_DIALOG_TYPE = "SETTINGS_DIALOG_TYPE";
    
    //close values for the close button on the level info diaglog
    public static final int LEVEL_CLOSE_X=300;
    public static final int LEVEL_CLOSE_Y=400;
    
    public static final int TRY_AGAIN_X=LEVEL_CLOSE_X-80;
    public static final int TRY_AGAIN_Y=LEVEL_CLOSE_Y;
    
    public static final int QUIT_LEVEL_X=TRY_AGAIN_X+175;
    public static final int QUIT_LEVEL_Y=LEVEL_CLOSE_Y;
    
    //values for rendering text on the level info dialog
    public static final int LEVEL_INFO_TEXT_X=LEVEL_CLOSE_X-100;
    public static final int LEVEL_INFO_TEXT_Y=LEVEL_CLOSE_Y-225;
    public static final int LEVEL_TEXT_X=LEVEL_INFO_TEXT_X;
    public static final int LEVEL_TEXT_Y=LEVEL_INFO_TEXT_Y+50;
    
    // COLORS USED FOR RENDERING VARIOUS THINGS, INCLUDING THE
    // COLOR KEY, WHICH REFERS TO THE COLOR TO IGNORE WHEN
    // LOADING ART.
    public static final Color COLOR_KEY = new Color(255, 174, 201);
    public static final Color FONT_COLOR = Color.WHITE;
    public static final Color COLOR_BLACK = Color.BLACK;
    public static final Color COLOR_RED = Color.RED;
    public static final Color COLOR_DEBUG_TEXT = Color.BLACK;
    public static final Color COLOR_TEXT_DISPLAY = new Color (10, 160, 10);
    
    // FONTS USED DURING FOR TEXTUAL GAME DISPLAYS
    public static final Font FONT_TEXT_DISPLAY = new Font(Font.SANS_SERIF, Font.BOLD, 20);
    public static final Font TEXT_FONT= new Font("Arial", Font.BOLD, 20);
    public static final Font TOP_FONT= new Font("Arial", Font.BOLD, 22);
    public static final Font FONT_HEADER = new Font(Font.SANS_SERIF, Font.BOLD, 14);
    public static final Font FONT_DEBUG_TEXT = new Font(Font.MONOSPACED, Font.BOLD, 16);
    public static final Font FONT_STATS = new Font(Font.MONOSPACED, Font.BOLD, 20);
        
}
