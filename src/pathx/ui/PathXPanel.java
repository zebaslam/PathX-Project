/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pathx.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.JPanel;
import mini_game.MiniGame;
import mini_game.Sprite;
import mini_game.SpriteType;
import mini_game.Viewport;
import properties_manager.PropertiesManager;
import pathx.ui.PathXErrorHandler;
import static pathx.PathXConstants.*;
import pathx.PathX.PathXPropertyType;
import pathx.data.Intersection;
import pathx.data.PathXDataModel;
import pathx.data.Road;
import pathx.ui.PathXMiniGame;

/**
 *
 * @author zeb
 */
public class PathXPanel extends JPanel {

    private MiniGame game;

    // AND HERE IS ALL THE GAME DATA THAT WE NEED TO RENDER
    private PathXDataModel data;

    private PathXMiniGame game1;
    Viewport viewport;
    
    
    // WE'LL USE THIS AS THE BASE IMAGE FOR RENDERING OUR PLAYER
    private BufferedImage playerImage;

    // WE'LL USE THIS TO FORMAT SOME TEXT FOR DISPLAY PURPOSES
    private NumberFormat numberFormatter;

    Ellipse2D.Double recyclableCircle;
    Line2D.Double recyclableLine;
    HashMap<Integer, BasicStroke> recyclableStrokes;
    int triangleXPoints[] = {-ONE_WAY_TRIANGLE_WIDTH/2,  -ONE_WAY_TRIANGLE_WIDTH/2,  ONE_WAY_TRIANGLE_WIDTH/2};
    int triangleYPoints[] = {ONE_WAY_TRIANGLE_WIDTH/2, -ONE_WAY_TRIANGLE_WIDTH/2, 0};
    GeneralPath recyclableTriangle;
    
    private ArrayList <Sprite> policeSprites;

    public ArrayList<Sprite> getPoliceSprites() {
        return policeSprites;
    }

    public void setPoliceSprites(ArrayList<Sprite> policeSprites) {
        this.policeSprites = policeSprites;
    }
    
    public PathXPanel(MiniGame initGame, PathXDataModel initData) {
        game = initGame;
        game1 = (PathXMiniGame) game;
        data = (PathXDataModel)game.getDataModel();
        numberFormatter = NumberFormat.getNumberInstance();
        numberFormatter.setMinimumFractionDigits(3);
        numberFormatter.setMaximumFractionDigits(3);
        
        // MAKE THE RENDER OBJECTS TO BE RECYCLED
        recyclableCircle = new Ellipse2D.Double(0, 0, INTERSECTION_RADIUS * 2, INTERSECTION_RADIUS * 2);
        recyclableLine = new Line2D.Double(0,0,0,0);
        recyclableStrokes = new HashMap();
        for (int i = 1; i <= 10; i++)
        {
            recyclableStrokes.put(i, new BasicStroke(i*2));
        }
        
        // MAKING THE TRIANGLE FOR ONE WAY STREETS IS A LITTLE MORE INVOLVED
        recyclableTriangle =  new GeneralPath(   GeneralPath.WIND_EVEN_ODD,
                                                triangleXPoints.length);
        recyclableTriangle.moveTo(triangleXPoints[0], triangleYPoints[0]);
        for (int index = 1; index < triangleXPoints.length; index++) 
        {
            recyclableTriangle.lineTo(triangleXPoints[index], triangleYPoints[index]);
        };
        recyclableTriangle.closePath();
        policeSprites=new ArrayList();
    // WE'LL RECYCLE THESE DURING RENDERING

    }
    //private PathXDataModel model = data;
  
    public void paintComponent(Graphics g) {
        try {
            game.beginUsingData();
             // WE'LL USE THE Graphics2D FEATURES, WHICH IS 
        // THE ACTUAL TYPE OF THE g OBJECT
            Graphics2D g2 = (Graphics2D) g;
            // CLEAR THE PANEL
            super.paintComponent(g);

            // RENDER THE BACKGROUND, WHICHEVER SCREEN WE'RE ON
            renderBackground(g);
            renderGUIControls(g);

            renderDialogs(g);

            

            if (((PathXMiniGame) game).isCurrentScreenState(LEVEL_SELECT_SCREEN_STATE)) {
                renderLevelInfo(g);
                renderLevelHeader(g);
            }
            if (((PathXMiniGame) game).isCurrentScreenState(GAME_SCREEN_STATE)) {
             renderGameHeader(g);
              // RENDER THE BACKGROUND IMAGE
             if(game.getGUIDialogs().get(LEVEL_INFO_DIALOG_TYPE).getState().equals(INVISIBLE_STATE.toString())){
                 renderLevelBackground(g2);
                  renderGUIControls(g);
                 renderRoads(g2);
                 renderIntersections(g2);
                 renderPolice(g);
                 renderPlayer(g);
                 
             }
              
            // RENDER THE ROADS
           

            // RENDER THE INTERSECTIONS
           
            }

        } finally {
            // RELEASE THE LOCK
            game.endUsingData();
        }
    }

    public void renderGUIControls(Graphics g) {
        // GET EACH DECOR IMAGE ONE AT A TIME
        Collection<Sprite> decorSprites = game.getGUIDecor().values();
        for (Sprite s : decorSprites) {
            if (s.getSpriteType().getSpriteTypeID() != BACKGROUND_TYPE ) {
                if(s.getSpriteType().getSpriteTypeID().equals(PLAYER_TYPE)){
                    
                }
                else{
                renderSprite(g, s);
                }
            }
        }

        // AND NOW RENDER THE BUTTONS
        Collection<Sprite> buttonSprites = game.getGUIButtons().values();
        for (Sprite s : buttonSprites) {
            if (s.getSpriteType().getSpriteTypeID().equalsIgnoreCase(CLOSE_DIALOG_BUTTON_TYPE)) {
                continue;
            }
            if (s.getSpriteType().getSpriteTypeID().equalsIgnoreCase(MUSIC_UNSELECTED_BUTTON_TYPE)) {
                continue;
            }
            if (s.getSpriteType().getSpriteTypeID().equalsIgnoreCase(MUSIC_SELECTED_BUTTON_TYPE)) {
                continue;
            }
            if (s.getSpriteType().getSpriteTypeID().equalsIgnoreCase(SOUND_SELECTED_BUTTON_TYPE)) {
                continue;
            }
            if (s.getSpriteType().getSpriteTypeID().equalsIgnoreCase(SOUND_UNSELECTED_BUTTON_TYPE)) {
                continue;
            }
            if (s.getSpriteType().getSpriteTypeID().equalsIgnoreCase(TRY_AGAIN_TYPE)) {
                continue;
            }
            if (s.getSpriteType().getSpriteTypeID().equalsIgnoreCase(QUIT_LEVEL_TYPE)) {
                continue;
            }
            renderSprite(g, s);
        }
    }

    public void renderBackground(Graphics g) {
        // THERE IS ONLY ONE CURRENTLY SET
        Sprite bg = game.getGUIDecor().get(BACKGROUND_TYPE);
        renderSprite(g, bg);
        if (!bg.getState().equals(INVISIBLE_STATE.toString())) {
            SpriteType bgST = bg.getSpriteType();
            Image img = bgST.getStateImage(bg.getState());
            g.drawImage(img, (int) bg.getX(), (int) bg.getY(), null);
        }
    }

     //boolean used=true;
      public void renderPlayer(Graphics g) {
        // ONLY RENDER THE VISIBLE ONES
          Sprite s = game.getGUIDecor().get(PLAYER_TYPE);
          Intersection first= data.getStartingLocation();
          Player player= data.getPlayer();  
            
//            int x= first.getX();
//            int y=first.getY();
            //player.setX(x+90);
            //player.setY(y-45);
           
           // if(used==true){
//            s.setX(x+30);
//            s.setY(y-15);
            
           // }
            
        if (!s.getState().equals(INVISIBLE_STATE.toString())) {
            SpriteType bgST = s.getSpriteType();
            Image img = bgST.getStateImage(s.getState());
            
            g.drawImage(img, (int)s.getX(), (int)s.getY(), bgST.getWidth(), bgST.getHeight(), null);
            //used=false;
        }
       // if(used==false){
               // s.setX(player.getX());
                //s.setY(player.getY());
            //}
    }
      
//determine max number of police for a level
//create police_types for that many police
//render accordingly     
        public void renderPolice(Graphics g) {
        // ONLY RENDER THE VISIBLE ONES
          Sprite s = game.getGUIDecor().get(POLICE_TYPE);
         
          int police=data.getLevel().getNumPolice();
          
        
          for (int i=0; i< police; i++) {
              if(i==1){
                  s= game.getGUIDecor().get(POLICE_TYPE_2);
              }
              if (i==2){
                  game.getGUIDecor().get(POLICE_TYPE_3);
              }
              if (i==3){
                  game.getGUIDecor().get(POLICE_TYPE_4);
              }
              if (i==4){
                  game.getGUIDecor().get(POLICE_TYPE_5);
              }
              if (i==5){
                  game.getGUIDecor().get(POLICE_TYPE_6);
              }
        if (!s.getState().equals(INVISIBLE_STATE.toString())) {
            SpriteType bgST = s.getSpriteType();
            Image img = bgST.getStateImage(s.getState());
            policeSprites.add(s);
            g.drawImage(img, (int)s.getX(), (int)s.getY(), bgST.getWidth(), bgST.getHeight(), null);
            //used=false;
        }
          }
       // if(used==false){
               // s.setX(player.getX());
                //s.setY(player.getY());
            //}
    }
        
        
    public void renderSprite(Graphics g, Sprite s) {
        // ONLY RENDER THE VISIBLE ONES
        if (!s.getState().equals(INVISIBLE_STATE.toString())) {
            SpriteType bgST = s.getSpriteType();
            Image img = bgST.getStateImage(s.getState());
            g.drawImage(img, (int) s.getX(), (int) s.getY(), bgST.getWidth(), bgST.getHeight(), null);

        }
    }

    public void renderLevelDialog(Graphics g, Sprite s) {
        if (((PathXMiniGame) game).isCurrentScreenState(GAME_SCREEN_STATE) && game.getGUIDialogs().get(LEVEL_INFO_DIALOG_TYPE).getState().equals(VISIBLE_STATE.toString()) && ((PathXMiniGame)game).isWin()==false) {
            SpriteType bgST = s.getSpriteType();
            Image img = bgST.getStateImage(s.getState());
            g.drawImage(img, (int) s.getX(), (int) s.getY(), bgST.getWidth(), bgST.getHeight(), null);

        }
    }

    public void renderGameHeader(Graphics g){
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        int level = ((PathXMiniGame) game).getLevel();
        String levelprompt="";
        if(level==1){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_GAME_1_INFO);
            
            }
            if (level==2){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_GAME_2_INFO);
        
            }
            if (level==3){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_GAME_3_INFO);
      
            }
            if (level==4){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_GAME_4_INFO);
            
            }
            if (level==5){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_GAME_5_INFO);
            
            }
             if (level==6){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_GAME_6_INFO);
    
            }
            if (level==7){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_GAME_7_INFO);
      
            }
            if (level==8){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_GAME_8_INFO);
         
            }
            if (level==9){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_GAME_9_INFO);
            }
            if (level==10){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_GAME_10_INFO);
            }
             if (level==11){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_GAME_11_INFO);
            }
             if (level==12){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_GAME_12_INFO);
            }
             if (level==13){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_GAME_13_INFO);
            }
              if (level==14){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_GAME_14_INFO);
            }
                if (level==15){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_GAME_15_INFO);
            }
                 if (level==16){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_GAME_16_INFO);
            }
                 if (level==17){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_GAME_17_INFO);
            }
              if (level==18){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_GAME_18_INFO);
            }
              if (level==19){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_GAME_19_INFO);
            }
              if (level==20){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_GAME_20_INFO);
            }
        String money = "$";
        int bal = data.getBalance();
        g.setFont(TOP_FONT);
        g.setColor(FONT_COLOR);
        g.drawString(money +bal, GAME_BALANCE_X, GAME_BALANCE_Y);
        g.drawString(levelprompt,  GAME_LEVEL_NAME_X,  GAME_LEVEL_NAME_Y);
    }
    public void renderLevelHeader(Graphics g) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String balance = props.getProperty(PathXPropertyType.TEXT_LABEL_BALANCE);
        String goal = props.getProperty(PathXPropertyType.TEXT_LABEL_GOAL);
        int bal = data.getBalance();
        int gol = data.getGoal();
        g.setFont(FONT_HEADER);
        g.setColor(FONT_COLOR);
        g.drawString(balance + bal, BALANCE_X, BALANCE_Y);
        g.drawString(goal + gol, GOAL_X, GOAL_Y);
    }

    public void renderDialogs(Graphics g) {
        // GET EACH DECOR IMAGE ONE AT A TIME
        Collection<Sprite> dialogSprites = game.getGUIDialogs().values();
        for (Sprite s : dialogSprites) {
            // RENDER THE DIALOG, NOTE IT WILL ONLY DO IT IF IT'S VISIBLE
     
                renderSprite(g, s);
            
            
        }
        if (((PathXMiniGame) game).isCurrentScreenState(SETTINGS_SCREEN_STATE) && game.getGUIDialogs().get(SETTINGS_DIALOG_TYPE).getState().equals(VISIBLE_STATE.toString())) {
            boolean check= ((PathXMiniGame) game).getEnableMusic();
            boolean soundcheck= ((PathXMiniGame) game).getEnableSound();
            if (check==true){
                Sprite blah= game.getGUIButtons().get(MUSIC_SELECTED_BUTTON_TYPE);
                renderSprite(g, blah);
            }
            if (check==false){
                Sprite blah= game.getGUIButtons().get(MUSIC_UNSELECTED_BUTTON_TYPE);
                renderSprite(g, blah);
            }
            
             if (soundcheck==true){
                Sprite bleh= game.getGUIButtons().get(SOUND_SELECTED_BUTTON_TYPE);
                renderSprite(g, bleh);
            }
            if (soundcheck==false){
                Sprite bleh= game.getGUIButtons().get(SOUND_UNSELECTED_BUTTON_TYPE);
                renderSprite(g, bleh);
            }
             
        }
         if (((PathXMiniGame) game).isCurrentScreenState(LOSE_SCREEN_STATE) && game.getGUIDialogs().get(LEVEL_FAIL_DIALOG_TYPE).getState().equals(VISIBLE_STATE.toString())){
          Sprite x = game.getGUIButtons().get(TRY_AGAIN_TYPE);
          Sprite y = game.getGUIButtons().get(QUIT_LEVEL_TYPE);
             
            renderSprite(g, x);
            renderSprite(g, y);
            g.setFont(FONT_STATS);
            g.setColor(COLOR_RED);
            int level = ((PathXMiniGame) game).getLevel();
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            String levelprompt="";
            String leveltext="The police have captured you!";
            int balance= ((PathXMiniGame) game).getLost();
            String bal=Integer.toString(balance);
            String leveltext2="You lost $"+bal+" this level!";
            
            if(level==1){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_1_INFO);
          
            }
            if (level==2){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_2_INFO);
              
            }
            if (level==3){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_3_INFO);
            
            }
            if (level==4){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_4_INFO);
          
            }
            if (level==5){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_5_INFO);
            
            }
             if (level==6){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_6_INFO);
          
            }
            if (level==7){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_7_INFO);
          
            }
            if (level==8){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_8_INFO);
        
            }
            if (level==9){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_9_INFO);
            
            }
            if (level==10){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_10_INFO);
        
            }
             if (level==11){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_11_INFO);
            
            }
             if (level==12){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_12_INFO);
         
            }
             if (level==13){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_13_INFO);
          
            }
              if (level==14){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_14_INFO);
          
           
            }
                if (level==15){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_15_INFO);
            
            }
                 if (level==16){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_16_INFO);
          
            }
                 if (level==17){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_17_INFO);
          
            }
              if (level==18){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_18_INFO);
          
            }
              if (level==19){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_19_INFO);
            
            }
               if (level==20){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_20_INFO);
           
            }
            g.drawString(levelprompt, LEVEL_INFO_TEXT_X, LEVEL_INFO_TEXT_Y);
            g.setFont(TEXT_FONT);
            g.drawString(leveltext, LEVEL_TEXT_X, LEVEL_TEXT_Y);
            g.drawString(leveltext2, LEVEL_TEXT_X, LEVEL_TEXT_Y+50);
    
            
         
         }
        if (((PathXMiniGame) game).isCurrentScreenState(WIN_SCREEN_STATE) && game.getGUIDialogs().get(LEVEL_COMPLETE_DIALOG_TYPE).getState().equals(VISIBLE_STATE.toString())){
             Sprite x = game.getGUIButtons().get(TRY_AGAIN_TYPE);
             Sprite y = game.getGUIButtons().get(QUIT_LEVEL_TYPE);
             
            renderSprite(g, x);
            renderSprite(g, y);
            g.setFont(FONT_STATS);
            g.setColor(COLOR_RED);
           
            int level = ((PathXMiniGame) game).getLevel();
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            String levelprompt="";
            String leveltext="";
            String leveltext2="";
            String leveltext3="";
            if(level==1){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_1_INFO);
            leveltext= "YOU WON LEVEL ONE!";
            leveltext2="You have earned $20!";
            }
            if (level==2){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_2_INFO);
            leveltext= "YOU WON LEVEL TWO!";
            leveltext2="You have earned $40!";   
            }
            if (level==3){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_3_INFO);
            leveltext= "YOU WON LEVEL THREE!";
            leveltext2="You have earned $60!";   
            }
            if (level==4){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_4_INFO);
            leveltext= "YOU WON LEVEL FOUR!";
            leveltext2="You've earned $80!";  
            }
            if (level==5){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_5_INFO);
            leveltext= "YOU WON LEVEL FIVE!";
            leveltext2="You've earned $100!";  
            }
             if (level==6){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_6_INFO);
            leveltext= "YOU WON LEVEL SIX!";
            leveltext2="You've earned $120!";  
            }
            if (level==7){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_7_INFO);
            leveltext= "YOU WON LEVEL 7!";
            leveltext2="You've earned $140!"; 
           
            }
            if (level==8){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_8_INFO);
            leveltext= "YOU WON LEVEL 8!";
            leveltext2="You've earned $160!"; 
            }
            if (level==9){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_9_INFO);
            leveltext= "YOU WON LEVEL 9!";
            leveltext2="You've earned $180!"; 
            }
            if (level==10){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_10_INFO);
            leveltext= "YOU WON LEVEL 10!";
            leveltext2="You've earned $200!"; 
            }
             if (level==11){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_11_INFO);
            leveltext= "YOU WON LEVEL 11!";
            leveltext2="You've earned $220!"; 
            }
             if (level==12){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_12_INFO);
            leveltext= "YOU WON LEVEL 12!";
            leveltext2="You've earned $240!"; 
            }
             if (level==13){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_13_INFO);
            leveltext= "YOU WON LEVEL 13!";
            leveltext2="You've earned $260!"; 
            }
              if (level==14){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_14_INFO);
            leveltext= "YOU WON LEVEL 14!";
            leveltext2="You've earned $280!"; 
           
            }
                if (level==15){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_15_INFO);
            leveltext= "YOU WON LEVEL 15!";
            leveltext2="You've earned $300!"; 
            }
                 if (level==16){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_16_INFO);
            leveltext= "YOU WON LEVEL 16!";
            leveltext2="You've earned $320!"; 
            }
                 if (level==17){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_17_INFO);
            leveltext= "YOU WON LEVEL 17!";
            leveltext2="You've earned $340!"; 
            }
              if (level==18){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_18_INFO);
            leveltext= "YOU WON LEVEL 18!";
            leveltext2="You've earned $360!"; 
            }
              if (level==19){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_19_INFO);
            leveltext= "YOU WON LEVEL 19!";
            leveltext2="You've earned $380!"; 
            }
               if (level==20){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_20_INFO);
            leveltext= "YOU WON LEVEL 20!";
            leveltext2="You've earned $400!"; 
            }
            g.drawString(levelprompt, LEVEL_INFO_TEXT_X, LEVEL_INFO_TEXT_Y);
            g.setFont(TEXT_FONT);
            g.drawString(leveltext, LEVEL_TEXT_X, LEVEL_TEXT_Y);
            g.drawString(leveltext2, LEVEL_TEXT_X, LEVEL_TEXT_Y+50);
            g.drawString(leveltext3, LEVEL_TEXT_X, LEVEL_TEXT_Y+100);
        
        }
        if (((PathXMiniGame) game).isCurrentScreenState(GAME_SCREEN_STATE) && game.getGUIDialogs().get(LEVEL_INFO_DIALOG_TYPE).getState().equals(VISIBLE_STATE.toString())) {
            Sprite x = game.getGUIButtons().get(CLOSE_DIALOG_BUTTON_TYPE);
            renderSprite(g, x);
            g.setFont(FONT_STATS);
            g.setColor(COLOR_RED);
           
            int level = ((PathXMiniGame) game).getLevel();
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            String levelprompt="";
            String leveltext="";
            String leveltext2="";
            String leveltext3="";
            if(level==1){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_1_INFO);
            leveltext= "Rednecks have stolen your wallet";
            leveltext2="Retreive it to earn $20";
            }
            if (level==2){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_2_INFO);
            leveltext= "A gang burned down your home";
            leveltext2="Burn down theirs and earn $40";   
            }
            if (level==3){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_3_INFO);
            leveltext= "Your Ex-Boss fired you.";
            leveltext2="Go to his home and take $60";   
            }
            if (level==4){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_4_INFO);
            leveltext= "Haters have burned a farmer's";
            leveltext2="potato field. Rob $80 from";  
            leveltext3="them to restore the farmer's honor.";
            }
            if (level==5){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_5_INFO);
            leveltext= "Strippers stole your best";
            leveltext2="friend's money. Rob $100 from";  
            leveltext3="them to avenge your friend.";
            }
             if (level==6){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_6_INFO);
            leveltext= "Steal $120 from corrupt politicians";
            }
            if (level==7){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_7_INFO);
            leveltext= "Ex-Olympic officials are taking";
            leveltext2="children's money for drugs. Rob $140";
            leveltext3="from them to put the $ to better use.";
            }
            if (level==8){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_8_INFO);
            leveltext= "People are jumping off the ";
            leveltext2="grand canyon. Stop them and convince";
            leveltext3="them to give you $160";
            }
            if (level==9){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_9_INFO);
            leveltext= "Give fake skiing lessons to rich";
            leveltext2="people. Make sure you ask";
            leveltext3="them to give you $180";
            }
            if (level==10){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_10_INFO);
            leveltext= "Help immigrants cross the border";
            leveltext2="and collect $200";
            }
             if (level==11){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_11_INFO);
            leveltext= "Rob a coal mine";
            leveltext2="Don't worry the locals will pay";
            leveltext3="you $220";
            }
             if (level==12){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_12_INFO);
            leveltext= "A librarian has been mugged.";
            leveltext2="Avenge her honor and she'll give";
            leveltext3="you $240";
            }
             if (level==13){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_13_INFO);
            leveltext= "A horse-carriage driver is stealing";
            leveltext2="tourist's money. Steal $260 from";
            leveltext3="him to complete this level";
            }
              if (level==14){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_14_INFO);
            leveltext= "The wicked witch stole money from.";
            leveltext2="Dorothy. Avenge her honor and ask";
            leveltext3="for $280";
            }
                if (level==15){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_15_INFO);
            leveltext= "A drug cartel is selling crack";
            leveltext2="Steal $300 from them to complete";
            leveltext3="this level.";
            }
                 if (level==16){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_16_INFO);
            leveltext= "Your ex-karate teacher is homeless.";
            leveltext2="Avenge your sensei's honor. You will";
            leveltext3="earn $320.";
            }
                 if (level==17){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_17_INFO);
            leveltext= "A gambler has stolen money.";
            leveltext2="Steal the $340 from him for yourself.";
            }
              if (level==18){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_18_INFO);
            leveltext= "A former foe insults your mom.";
            leveltext2="Steal $360 from him to teach";
            leveltext3="him a lesson he wont forget.";
            }
              if (level==19){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_19_INFO);
            leveltext= "Your brother is a convict.";
            leveltext2="Steal $380 to get him out";
            leveltext3="of prison.";
            }
               if (level==20){
            levelprompt = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_20_INFO);
            leveltext= "Your chemistry teacher is a";
            leveltext2="drug tealer. Stop the trade";
            leveltext3="and collect $400.";
            }
            g.drawString(levelprompt, LEVEL_INFO_TEXT_X, LEVEL_INFO_TEXT_Y);
            g.setFont(TEXT_FONT);
            g.drawString(leveltext, LEVEL_TEXT_X, LEVEL_TEXT_Y);
            g.drawString(leveltext2, LEVEL_TEXT_X, LEVEL_TEXT_Y+50);
            g.drawString(leveltext3, LEVEL_TEXT_X, LEVEL_TEXT_Y+100);
           
        }
    }

    /**
     * This method renders the level information when a user mouse overs a node
     * on the level select screen
     *
     * @param g
     */
    public void renderLevelInfo(Graphics g) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String level1Info = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_1_INFO);
        String level2Info = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_2_INFO);
        String level3Info = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_3_INFO);
        String level4Info = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_4_INFO);
        String level5Info = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_5_INFO);
        String level6Info = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_6_INFO);
        String level7Info = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_7_INFO);
        String level8Info = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_8_INFO);
        String level9Info = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_9_INFO);
        String level10Info = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_10_INFO);
        String level11Info = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_11_INFO);
        String level12Info = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_12_INFO);
        String level13Info = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_13_INFO);
        String level14Info = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_14_INFO);
        String level15Info = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_15_INFO);
        String level16Info = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_16_INFO);
        String level17Info = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_17_INFO);
        String level18Info = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_18_INFO);
        String level19Info = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_19_INFO);
        String level20Info = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_20_INFO);
        String state1 = game.getGUIButtons().get(LEVEL_1_BUTTON_TYPE).getState();
        String stateONE= game.getGUIButtons().get(LEVEL_1_COMPLETE_BUTTON_TYPE).getState();
        String state2 = game.getGUIButtons().get(LEVEL_2_BUTTON_TYPE).getState();
        String stateTWO= game.getGUIButtons().get(LEVEL_2_COMPLETE_BUTTON_TYPE).getState();
        String stateTwo=game.getGUIButtons().get(LEVEL_2_AVAILABLE_BUTTON_TYPE).getState();
        String state3 = game.getGUIButtons().get(LEVEL_3_BUTTON_TYPE).getState();
        String stateTHREE= game.getGUIButtons().get(LEVEL_3_COMPLETE_BUTTON_TYPE).getState();
        String stateThree=game.getGUIButtons().get(LEVEL_3_AVAILABLE_BUTTON_TYPE).getState();
        String state4 = game.getGUIButtons().get(LEVEL_4_BUTTON_TYPE).getState();
        String stateFOUR= game.getGUIButtons().get(LEVEL_4_COMPLETE_BUTTON_TYPE).getState();
        String stateFour=game.getGUIButtons().get(LEVEL_4_AVAILABLE_BUTTON_TYPE).getState();
        String state5 = game.getGUIButtons().get(LEVEL_5_BUTTON_TYPE).getState();
        String stateFIVE= game.getGUIButtons().get(LEVEL_5_COMPLETE_BUTTON_TYPE).getState();
        String stateFive=game.getGUIButtons().get(LEVEL_5_AVAILABLE_BUTTON_TYPE).getState();
        String state6 = game.getGUIButtons().get(LEVEL_6_BUTTON_TYPE).getState();
        String stateSIX= game.getGUIButtons().get(LEVEL_6_COMPLETE_BUTTON_TYPE).getState();
        String stateSix=game.getGUIButtons().get(LEVEL_6_AVAILABLE_BUTTON_TYPE).getState();
        String state7 = game.getGUIButtons().get(LEVEL_7_BUTTON_TYPE).getState();
        String stateSEVEN= game.getGUIButtons().get(LEVEL_7_COMPLETE_BUTTON_TYPE).getState();
        String stateSeven=game.getGUIButtons().get(LEVEL_7_AVAILABLE_BUTTON_TYPE).getState();
        String state8 = game.getGUIButtons().get(LEVEL_8_BUTTON_TYPE).getState();
        String state88= game.getGUIButtons().get(LEVEL_8_COMPLETE_BUTTON_TYPE).getState();
        String state888=game.getGUIButtons().get(LEVEL_8_AVAILABLE_BUTTON_TYPE).getState();
        String state9 = game.getGUIButtons().get(LEVEL_9_BUTTON_TYPE).getState();
        String state99= game.getGUIButtons().get(LEVEL_9_COMPLETE_BUTTON_TYPE).getState();
        String state999=game.getGUIButtons().get(LEVEL_9_AVAILABLE_BUTTON_TYPE).getState();
        String state10 = game.getGUIButtons().get(LEVEL_10_BUTTON_TYPE).getState();
        String state100= game.getGUIButtons().get(LEVEL_10_COMPLETE_BUTTON_TYPE).getState();
        String state1000=game.getGUIButtons().get(LEVEL_10_AVAILABLE_BUTTON_TYPE).getState();
        String state11 = game.getGUIButtons().get(LEVEL_11_BUTTON_TYPE).getState();
        String state111= game.getGUIButtons().get(LEVEL_11_COMPLETE_BUTTON_TYPE).getState();
        String state1111=game.getGUIButtons().get(LEVEL_11_AVAILABLE_BUTTON_TYPE).getState();
        String state12 = game.getGUIButtons().get(LEVEL_12_BUTTON_TYPE).getState();
        String state122= game.getGUIButtons().get(LEVEL_12_COMPLETE_BUTTON_TYPE).getState();
        String state1222=game.getGUIButtons().get(LEVEL_12_AVAILABLE_BUTTON_TYPE).getState();
        String state13 = game.getGUIButtons().get(LEVEL_13_BUTTON_TYPE).getState();
        String state133= game.getGUIButtons().get(LEVEL_13_COMPLETE_BUTTON_TYPE).getState();
        String state1333=game.getGUIButtons().get(LEVEL_13_AVAILABLE_BUTTON_TYPE).getState();
        String state14 = game.getGUIButtons().get(LEVEL_14_BUTTON_TYPE).getState();
        String state144= game.getGUIButtons().get(LEVEL_14_COMPLETE_BUTTON_TYPE).getState();
        String state1444=game.getGUIButtons().get(LEVEL_14_AVAILABLE_BUTTON_TYPE).getState();
        String state15 = game.getGUIButtons().get(LEVEL_15_BUTTON_TYPE).getState();
        String state155= game.getGUIButtons().get(LEVEL_15_COMPLETE_BUTTON_TYPE).getState();
        String state1555=game.getGUIButtons().get(LEVEL_15_AVAILABLE_BUTTON_TYPE).getState();
        String state16 = game.getGUIButtons().get(LEVEL_16_BUTTON_TYPE).getState();
        String state166= game.getGUIButtons().get(LEVEL_16_COMPLETE_BUTTON_TYPE).getState();
        String state1666=game.getGUIButtons().get(LEVEL_16_AVAILABLE_BUTTON_TYPE).getState();
        String state17 = game.getGUIButtons().get(LEVEL_17_BUTTON_TYPE).getState();
        String state177= game.getGUIButtons().get(LEVEL_17_COMPLETE_BUTTON_TYPE).getState();
        String state1777=game.getGUIButtons().get(LEVEL_17_AVAILABLE_BUTTON_TYPE).getState();
        String state18 = game.getGUIButtons().get(LEVEL_18_BUTTON_TYPE).getState();
        String state188= game.getGUIButtons().get(LEVEL_18_COMPLETE_BUTTON_TYPE).getState();
        String state1888=game.getGUIButtons().get(LEVEL_18_AVAILABLE_BUTTON_TYPE).getState();
        String state19 = game.getGUIButtons().get(LEVEL_19_BUTTON_TYPE).getState();
        String state199= game.getGUIButtons().get(LEVEL_19_COMPLETE_BUTTON_TYPE).getState();
        String state1999=game.getGUIButtons().get(LEVEL_19_AVAILABLE_BUTTON_TYPE).getState();
        String state20 = game.getGUIButtons().get(LEVEL_20_BUTTON_TYPE).getState();
        String state200= game.getGUIButtons().get(LEVEL_20_COMPLETE_BUTTON_TYPE).getState();
        String state2000=game.getGUIButtons().get(LEVEL_20_AVAILABLE_BUTTON_TYPE).getState();
        if (state1.equals(PathXCarState.MOUSE_OVER_STATE.toString()) || stateONE.equals(PathXCarState.MOUSE_OVER_STATE.toString())) {
            g.setFont(FONT_TEXT_DISPLAY);
            g.setColor(FONT_COLOR);
            g.drawString(level1Info, LEVEL_INFO_X, LEVEL_INFO_Y);
        }
        if (state2.equals(PathXCarState.MOUSE_OVER_STATE.toString()) || stateTWO.equals(PathXCarState.MOUSE_OVER_STATE.toString()) || stateTwo.equals(PathXCarState.MOUSE_OVER_STATE.toString()) ) {
            g.setFont(FONT_TEXT_DISPLAY);
            g.setColor(FONT_COLOR);
            g.drawString(level2Info, LEVEL_INFO_X, LEVEL_INFO_Y);
        }
        if (state3.equals(PathXCarState.MOUSE_OVER_STATE.toString())|| stateTHREE.equals(PathXCarState.MOUSE_OVER_STATE.toString())|| stateThree.equals(PathXCarState.MOUSE_OVER_STATE.toString())) {
            g.setFont(FONT_TEXT_DISPLAY);
            g.setColor(FONT_COLOR);
            g.drawString(level3Info, LEVEL_INFO_X, LEVEL_INFO_Y);
        }
        if (state4.equals(PathXCarState.MOUSE_OVER_STATE.toString()) || stateFOUR.equals(PathXCarState.MOUSE_OVER_STATE.toString()) || stateFour.equals(PathXCarState.MOUSE_OVER_STATE.toString())) {
            g.setFont(FONT_TEXT_DISPLAY);
            g.setColor(FONT_COLOR);
            g.drawString(level4Info, LEVEL_INFO_X, LEVEL_INFO_Y);
        }
        if (state5.equals(PathXCarState.MOUSE_OVER_STATE.toString()) || stateFIVE.equals(PathXCarState.MOUSE_OVER_STATE.toString())|| stateFive.equals(PathXCarState.MOUSE_OVER_STATE.toString())) {
            g.setFont(FONT_TEXT_DISPLAY);
            g.setColor(FONT_COLOR);
            g.drawString(level5Info, LEVEL_INFO_X, LEVEL_INFO_Y);
        }
        if (state6.equals(PathXCarState.MOUSE_OVER_STATE.toString())|| stateSIX.equals(PathXCarState.MOUSE_OVER_STATE.toString()) || stateSix.equals(PathXCarState.MOUSE_OVER_STATE.toString())) {
            g.setFont(FONT_TEXT_DISPLAY);
            g.setColor(FONT_COLOR);
            g.drawString(level6Info, LEVEL_INFO_X, LEVEL_INFO_Y);
        }
        if (state7.equals(PathXCarState.MOUSE_OVER_STATE.toString())|| stateSEVEN.equals(PathXCarState.MOUSE_OVER_STATE.toString())|| stateSeven.equals(PathXCarState.MOUSE_OVER_STATE.toString())) {
            g.setFont(FONT_TEXT_DISPLAY);
            g.setColor(FONT_COLOR);
            g.drawString(level7Info, LEVEL_INFO_X, LEVEL_INFO_Y);
        }
        if (state8.equals(PathXCarState.MOUSE_OVER_STATE.toString()) || state88.equals(PathXCarState.MOUSE_OVER_STATE.toString()) || state888.equals(PathXCarState.MOUSE_OVER_STATE.toString())) {
            g.setFont(FONT_TEXT_DISPLAY);
            g.setColor(FONT_COLOR);
            g.drawString(level8Info, LEVEL_INFO_X, LEVEL_INFO_Y);
        }
        if (state9.equals(PathXCarState.MOUSE_OVER_STATE.toString()) || state99.equals(PathXCarState.MOUSE_OVER_STATE.toString())|| state999.equals(PathXCarState.MOUSE_OVER_STATE.toString())) {
            g.setFont(FONT_TEXT_DISPLAY);
            g.setColor(FONT_COLOR);
            g.drawString(level9Info, LEVEL_INFO_X, LEVEL_INFO_Y);
        }
        if (state10.equals(PathXCarState.MOUSE_OVER_STATE.toString()) || state100.equals(PathXCarState.MOUSE_OVER_STATE.toString()) || state1000.equals(PathXCarState.MOUSE_OVER_STATE.toString())) {
            g.setFont(FONT_TEXT_DISPLAY);
            g.setColor(FONT_COLOR);
            g.drawString(level10Info, LEVEL_INFO_X, LEVEL_INFO_Y);
        }
        if (state11.equals(PathXCarState.MOUSE_OVER_STATE.toString()) || state111.equals(PathXCarState.MOUSE_OVER_STATE.toString()) || state111.equals(PathXCarState.MOUSE_OVER_STATE.toString())) {
            g.setFont(FONT_TEXT_DISPLAY);
            g.setColor(FONT_COLOR);
            g.drawString(level11Info, LEVEL_INFO_X, LEVEL_INFO_Y);
        }
        if (state12.equals(PathXCarState.MOUSE_OVER_STATE.toString()) || state122.equals(PathXCarState.MOUSE_OVER_STATE.toString()) || state1222.equals(PathXCarState.MOUSE_OVER_STATE.toString())) {
            g.setFont(FONT_TEXT_DISPLAY);
            g.setColor(FONT_COLOR);
            g.drawString(level12Info, LEVEL_INFO_X, LEVEL_INFO_Y);
        }
        if (state13.equals(PathXCarState.MOUSE_OVER_STATE.toString()) || state133.equals(PathXCarState.MOUSE_OVER_STATE.toString()) || state1333.equals(PathXCarState.MOUSE_OVER_STATE.toString())) {
            g.setFont(FONT_TEXT_DISPLAY);
            g.setColor(FONT_COLOR);
            g.drawString(level13Info, LEVEL_INFO_X, LEVEL_INFO_Y);
        }
        if (state14.equals(PathXCarState.MOUSE_OVER_STATE.toString()) || state144.equals(PathXCarState.MOUSE_OVER_STATE.toString()) || state1444.equals(PathXCarState.MOUSE_OVER_STATE.toString())) {
            g.setFont(FONT_TEXT_DISPLAY);
            g.setColor(FONT_COLOR);
            g.drawString(level14Info, LEVEL_INFO_X, LEVEL_INFO_Y);
        }
        if (state15.equals(PathXCarState.MOUSE_OVER_STATE.toString()) || state155.equals(PathXCarState.MOUSE_OVER_STATE.toString()) || state1555.equals(PathXCarState.MOUSE_OVER_STATE.toString())) {
            g.setFont(FONT_TEXT_DISPLAY);
            g.setColor(FONT_COLOR);
            g.drawString(level15Info, LEVEL_INFO_X, LEVEL_INFO_Y);
        }
        if (state16.equals(PathXCarState.MOUSE_OVER_STATE.toString()) || state166.equals(PathXCarState.MOUSE_OVER_STATE.toString()) || state1666.equals(PathXCarState.MOUSE_OVER_STATE.toString())) {
            g.setFont(FONT_TEXT_DISPLAY);
            g.setColor(FONT_COLOR);
            g.drawString(level16Info, LEVEL_INFO_X, LEVEL_INFO_Y);
        }
        if (state17.equals(PathXCarState.MOUSE_OVER_STATE.toString()) || state177.equals(PathXCarState.MOUSE_OVER_STATE.toString()) || state1777.equals(PathXCarState.MOUSE_OVER_STATE.toString())) {
            g.setFont(FONT_TEXT_DISPLAY);
            g.setColor(FONT_COLOR);
            g.drawString(level17Info, LEVEL_INFO_X, LEVEL_INFO_Y);
        }
        if (state18.equals(PathXCarState.MOUSE_OVER_STATE.toString()) || state188.equals(PathXCarState.MOUSE_OVER_STATE.toString()) || state1888.equals(PathXCarState.MOUSE_OVER_STATE.toString())) {
            g.setFont(FONT_TEXT_DISPLAY);
            g.setColor(FONT_COLOR);
            g.drawString(level18Info, LEVEL_INFO_X, LEVEL_INFO_Y);
        }
        if (state19.equals(PathXCarState.MOUSE_OVER_STATE.toString()) || state199.equals(PathXCarState.MOUSE_OVER_STATE.toString()) || state1999.equals(PathXCarState.MOUSE_OVER_STATE.toString())) {
            g.setFont(FONT_TEXT_DISPLAY);
            g.setColor(FONT_COLOR);
            g.drawString(level19Info, LEVEL_INFO_X, LEVEL_INFO_Y);
        }
        if (state20.equals(PathXCarState.MOUSE_OVER_STATE.toString() ) || state200.equals(PathXCarState.MOUSE_OVER_STATE.toString() )  || state2000.equals(PathXCarState.MOUSE_OVER_STATE.toString() )) {
            g.setFont(FONT_TEXT_DISPLAY);
            g.setColor(FONT_COLOR);
            g.drawString(level20Info, LEVEL_INFO_X, LEVEL_INFO_Y);
        }
    }

    public void renderDebuggingText(Graphics g) {
        // IF IT'S ACTIVATED
        if (data.isDebugTextRenderingActive()) {
            // ENABLE PROPER RENDER SETTINGS
            g.setFont(FONT_DEBUG_TEXT);
            g.setColor(COLOR_DEBUG_TEXT);

            // GO THROUGH ALL THE DEBUG TEXT
            Iterator<String> it = data.getDebugText().iterator();
            int x = data.getDebugTextX();
            int y = data.getDebugTextY();
            while (it.hasNext()) {
                // RENDER THE TEXT
                String text = it.next();
                g.drawString(text, x, y);
                y += 20;
            }
        }
    }
// HELPER METHOD FOR RENDERING THE LEVEL ROADS
   // HELPER METHOD FOR RENDERING THE LEVEL BACKGROUND
    private void renderLevelBackground(Graphics2D g2)
    {
        Image backgroundImage = data.getBackgroundImage();
        g2.drawImage(backgroundImage, 0, 75, 750, 650, 0,0, 750, 650, null);
    }

    // HELPER METHOD FOR RENDERING THE LEVEL ROADS
    private void renderRoads(Graphics2D g2)
    {
        data = (PathXDataModel)game.getDataModel();
        // GO THROUGH THE ROADS AND RENDER ALL OF THEM
        //Viewport viewport = data.getViewport();
        Iterator<Road> it = data.roadsIterator();
        g2.setStroke(recyclableStrokes.get(INT_STROKE));
        while (it.hasNext())
        {
            Road road = it.next();
            if (!data.isSelectedRoad(road))
                renderRoad(g2, road, INT_OUTLINE_COLOR);
        }
        
        // NOW DRAW THE LINE BEING ADDED, IF THERE IS ONE
       
        {
        
        }

        // AND RENDER THE SELECTED ONE, IF THERE IS ONE
        Road selectedRoad = data.getSelectedRoad();
        if (selectedRoad != null)
        {
            renderRoad(g2, selectedRoad, HIGHLIGHTED_COLOR);
        }
    }
    
    // HELPER METHOD FOR RENDERING A SINGLE ROAD
    private void renderRoad(Graphics2D g2, Road road, Color c)
    {
        g2.setColor(c);
        int strokeId = road.getSpeedLimit()/10;

        // CLAMP THE SPEED LIMIT STROKE
        if (strokeId < 1) strokeId = 1;
        if (strokeId > 10) strokeId = 10;
        g2.setStroke(recyclableStrokes.get(strokeId));

        // LOAD ALL THE DATA INTO THE RECYCLABLE LINE
        recyclableLine.x1 = road.getNode1().x;
        recyclableLine.y1 = road.getNode1().y;
        recyclableLine.x2 = road.getNode2().x;
        recyclableLine.y2 = road.getNode2().y;

        // AND DRAW IT
        g2.draw(recyclableLine);
        
        // AND IF IT'S A ONE WAY ROAD DRAW THE MARKER
        if (road.isOneWay())
        {
            this.renderOneWaySignalsOnRecyclableLine(g2);
        }
    }

    // HELPER METHOD FOR RENDERING AN INTERSECTION
    private void renderIntersections(Graphics2D g2)
    {
        Iterator<Intersection> it = data.intersectionsIterator();
        while (it.hasNext())
        {
            Intersection intersection = it.next();

            // ONLY RENDER IT THIS WAY IF IT'S NOT THE START OR DESTINATION
            // AND IT IS IN THE VIEWPORT
            if ((!data.isStartingLocation(intersection))
                    && (!data.isDestination(intersection))
                    )
            {
                // FIRST FILL
                if (intersection.isOpen())
                {
                    g2.setColor(OPEN_INT_COLOR);
                } else
                {
                    g2.setColor(CLOSED_INT_COLOR);
                }
                recyclableCircle.x = intersection.x  - INTERSECTION_RADIUS;
                recyclableCircle.y = intersection.y - INTERSECTION_RADIUS;
                g2.fill(recyclableCircle);

                // AND NOW THE OUTLINE
                if (data.isSelectedIntersection(intersection))
                {
                    g2.setColor(HIGHLIGHTED_COLOR);
                } else
                {
                    g2.setColor(INT_OUTLINE_COLOR);
                }
                Stroke s = recyclableStrokes.get(INT_STROKE);
                g2.setStroke(s);
                g2.draw(recyclableCircle);
            }
        }

        // AND NOW RENDER THE START AND DESTINATION LOCATIONS
        Image startImage = data.getStartingLocationImage();
        Intersection startInt = data.getStartingLocation();
        renderIntersectionImage(g2, startImage, startInt);

        Image destImage = data.getDesinationImage();
        Intersection destInt =data.getDestination();
        renderIntersectionImage(g2, destImage, destInt);
    }

    // HELPER METHOD FOR RENDERING AN IMAGE AT AN INTERSECTION, WHICH IS
    // NEEDED BY THE STARTING LOCATION AND THE DESTINATION
    private void renderIntersectionImage(Graphics2D g2, Image img, Intersection i)
    {
        // CALCULATE WHERE TO RENDER IT
        int w = img.getWidth(null);
        int h = img.getHeight(null);
        int x1 = i.x-(w/2);
        int y1 = i.y-(h/2);
        int x2 = x1 + img.getWidth(null);
        int y2 = y1 + img.getHeight(null);
        
        // ONLY RENDER IF INSIDE THE VIEWPORT
        //if (viewport.isRectInsideViewport(x1, y1, x2, y2));
        {
            g2.drawImage(img, x1 , y1 , null);
        }        
    }


    
    // YOU'LL LIKELY AT THE VERY LEAST WANT THIS ONE. IT RENDERS A NICE
    // LITTLE POINTING TRIANGLE ON ONE-WAY ROADS
    private void renderOneWaySignalsOnRecyclableLine(Graphics2D g2)
    {
        // CALCULATE THE ROAD LINE SLOPE
        double diffX = recyclableLine.x2 - recyclableLine.x1;
        double diffY = recyclableLine.y2 - recyclableLine.y1;
        double slope = diffY/diffX;
        
        // AND THEN FIND THE LINE MIDPOINT
        double midX = (recyclableLine.x1 + recyclableLine.x2)/2.0;
        double midY = (recyclableLine.y1 + recyclableLine.y2)/2.0;
        
        // GET THE RENDERING TRANSFORM, WE'LL RETORE IT BACK
        // AT THE END
        AffineTransform oldAt = g2.getTransform();
        
        // CALCULATE THE ROTATION ANGLE
        double theta = Math.atan(slope);
        if (recyclableLine.x2 < recyclableLine.x1)
            theta = (theta + Math.PI);
        
        // MAKE A NEW TRANSFORM FOR THIS TRIANGLE AND SET IT
        // UP WITH WHERE WE WANT TO PLACE IT AND HOW MUCH WE
        // WANT TO ROTATE IT
        AffineTransform at = new AffineTransform();        
        at.setToIdentity();
        at.translate(midX, midY);
        at.rotate(theta);
        g2.setTransform(at);
        
        // AND RENDER AS A SOLID TRIANGLE
        g2.fill(recyclableTriangle);
        
        // RESTORE THE OLD TRANSFORM SO EVERYTHING DOESN'T END UP ROTATED 0
        g2.setTransform(oldAt);
    }

      /**
     * This mutator method sets the base image to use for rendering tiles.
     * 
     * @param initBlankTileImage The image to use as the base for rendering tiles.
     */
    public void setPlayerImage(BufferedImage initBlankTileImage)
    {
        playerImage = initBlankTileImage;
    }
}
