/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pathx.ui;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
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
import pathx.data.PathXDataModel;
import pathx.ui.PathXMiniGame;

/**
 *
 * @author zeb
 */
public class PathXPanel extends JPanel{
     private MiniGame game;
    
    // AND HERE IS ALL THE GAME DATA THAT WE NEED TO RENDER
    private PathXDataModel data;
    
    // WE'LL USE THIS TO FORMAT SOME TEXT FOR DISPLAY PURPOSES
    private NumberFormat numberFormatter;
    
    public PathXPanel(MiniGame initGame, PathXDataModel initData)
    {
        game = initGame;
        data = initData;
        numberFormatter = NumberFormat.getNumberInstance();
        numberFormatter.setMinimumFractionDigits(3);
        numberFormatter.setMaximumFractionDigits(3);
    }
    public void paintComponent(Graphics g){
        try{
        game.beginUsingData();
        
            // CLEAR THE PANEL
            super.paintComponent(g);
        
            // RENDER THE BACKGROUND, WHICHEVER SCREEN WE'RE ON
            renderBackground(g);
            
            renderGUIControls(g);
            
            renderDialogs(g);
        
             if (((PathXMiniGame)game).isCurrentScreenState(LEVEL_SELECT_SCREEN_STATE) ){
            renderLevelInfo(g);
            renderLevelHeader(g);
        }
        
        }
         finally
        {
            // RELEASE THE LOCK
            game.endUsingData();    
        }
    }
    
      public void renderGUIControls(Graphics g)
    {
        // GET EACH DECOR IMAGE ONE AT A TIME
        Collection<Sprite> decorSprites = game.getGUIDecor().values();
        for (Sprite s : decorSprites)
        {
            if (s.getSpriteType().getSpriteTypeID() != BACKGROUND_TYPE)
                renderSprite(g, s);
        }
        
        // AND NOW RENDER THE BUTTONS
        Collection<Sprite> buttonSprites = game.getGUIButtons().values();
        for (Sprite s : buttonSprites)
        {
            if(s.getSpriteType().getSpriteTypeID().equalsIgnoreCase(CLOSE_DIALOG_BUTTON_TYPE)){
                continue;
            }
            
            renderSprite(g, s);
        }
    }  
  public void renderBackground(Graphics g)
    {
        // THERE IS ONLY ONE CURRENTLY SET
        Sprite bg = game.getGUIDecor().get(BACKGROUND_TYPE);
        renderSprite(g, bg);
           if (!bg.getState().equals(INVISIBLE_STATE.toString()))
        {
            SpriteType bgST = bg.getSpriteType();
            Image img = bgST.getStateImage(bg.getState());
            g.drawImage(img, (int)bg.getX(), (int)bg.getY(),null); 
        }
    }
  
  public void renderSprite(Graphics g, Sprite s)
    {
        // ONLY RENDER THE VISIBLE ONES
        if (!s.getState().equals(INVISIBLE_STATE.toString()))
        {
            SpriteType bgST = s.getSpriteType();
            Image img = bgST.getStateImage(s.getState());
            g.drawImage(img, (int)s.getX(), (int)s.getY(), bgST.getWidth(), bgST.getHeight(), null); 
            
            
            
        }
    }
  
  public void renderLevelDialog(Graphics g, Sprite s){
      if (((PathXMiniGame)game).isCurrentScreenState(GAME_SCREEN_STATE) && game.getGUIDialogs().get(LEVEL_INFO_DIALOG_TYPE).getState().equals(VISIBLE_STATE.toString())){
          SpriteType bgST = s.getSpriteType();
          Image img = bgST.getStateImage(s.getState());
         g.drawImage(img, (int)s.getX(), (int)s.getY(), bgST.getWidth(), bgST.getHeight(), null); 
         
        
      }
  }
  
  public void renderLevelHeader(Graphics g){
      PropertiesManager props = PropertiesManager.getPropertiesManager(); 
      String balance = props.getProperty(PathXPropertyType.TEXT_LABEL_BALANCE);
      String goal=props.getProperty(PathXPropertyType.TEXT_LABEL_GOAL);
      int bal=data.getBalance();
      int gol=data.getGoal();
      g.setFont(FONT_HEADER);
      g.setColor(FONT_COLOR);
      g.drawString(balance+bal, BALANCE_X, BALANCE_Y);
      g.drawString(goal+gol, GOAL_X, GOAL_Y);
  }
  public void renderDialogs(Graphics g)
    {
        // GET EACH DECOR IMAGE ONE AT A TIME
        Collection<Sprite> dialogSprites = game.getGUIDialogs().values();
        for (Sprite s : dialogSprites)
        {
            // RENDER THE DIALOG, NOTE IT WILL ONLY DO IT IF IT'S VISIBLE
            renderSprite(g, s);
        }
        if (((PathXMiniGame)game).isCurrentScreenState(GAME_SCREEN_STATE) && game.getGUIDialogs().get(LEVEL_INFO_DIALOG_TYPE).getState().equals(VISIBLE_STATE.toString())){
        Sprite x=game.getGUIButtons().get(CLOSE_DIALOG_BUTTON_TYPE);
        renderSprite(g,x);
         
        
      }
    }
  
  
  public void renderLevelInfo(Graphics g){
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
      String state1= game.getGUIButtons().get(LEVEL_1_BUTTON_TYPE).getState();
      String state2= game.getGUIButtons().get(LEVEL_2_BUTTON_TYPE).getState();
      String state3= game.getGUIButtons().get(LEVEL_3_BUTTON_TYPE).getState();
      String state4= game.getGUIButtons().get(LEVEL_4_BUTTON_TYPE).getState();
      String state5= game.getGUIButtons().get(LEVEL_5_BUTTON_TYPE).getState();
      String state6= game.getGUIButtons().get(LEVEL_6_BUTTON_TYPE).getState();
      String state7= game.getGUIButtons().get(LEVEL_7_BUTTON_TYPE).getState();
      String state8= game.getGUIButtons().get(LEVEL_8_BUTTON_TYPE).getState();
      String state9= game.getGUIButtons().get(LEVEL_9_BUTTON_TYPE).getState();
      String state10= game.getGUIButtons().get(LEVEL_10_BUTTON_TYPE).getState();
      String state11= game.getGUIButtons().get(LEVEL_11_BUTTON_TYPE).getState();
      String state12= game.getGUIButtons().get(LEVEL_12_BUTTON_TYPE).getState();
      String state13= game.getGUIButtons().get(LEVEL_13_BUTTON_TYPE).getState();
      String state14= game.getGUIButtons().get(LEVEL_14_BUTTON_TYPE).getState();
      String state15= game.getGUIButtons().get(LEVEL_15_BUTTON_TYPE).getState();
      String state16= game.getGUIButtons().get(LEVEL_16_BUTTON_TYPE).getState();
      String state17= game.getGUIButtons().get(LEVEL_17_BUTTON_TYPE).getState();
      String state18= game.getGUIButtons().get(LEVEL_18_BUTTON_TYPE).getState();
      String state19= game.getGUIButtons().get(LEVEL_19_BUTTON_TYPE).getState();
      String state20= game.getGUIButtons().get(LEVEL_20_BUTTON_TYPE).getState();
     if( state1.equals(PathXCarState.MOUSE_OVER_STATE.toString())){
          g.setFont(FONT_TEXT_DISPLAY);
          g.setColor(FONT_COLOR);
         g.drawString(level1Info,                                     LEVEL_INFO_X, LEVEL_INFO_Y);
     }
     if( state2.equals(PathXCarState.MOUSE_OVER_STATE.toString())){
          g.setFont(FONT_TEXT_DISPLAY);
          g.setColor(FONT_COLOR);
         g.drawString(level2Info,                                     LEVEL_INFO_X, LEVEL_INFO_Y);
     }
      if( state3.equals(PathXCarState.MOUSE_OVER_STATE.toString())){
          g.setFont(FONT_TEXT_DISPLAY);
          g.setColor(FONT_COLOR);
         g.drawString(level3Info,                                     LEVEL_INFO_X, LEVEL_INFO_Y);
     }
      if( state4.equals(PathXCarState.MOUSE_OVER_STATE.toString())){
          g.setFont(FONT_TEXT_DISPLAY);
          g.setColor(FONT_COLOR);
         g.drawString(level4Info,                                     LEVEL_INFO_X, LEVEL_INFO_Y);
     }
       if( state5.equals(PathXCarState.MOUSE_OVER_STATE.toString())){
          g.setFont(FONT_TEXT_DISPLAY);
          g.setColor(FONT_COLOR);
         g.drawString(level5Info,                                     LEVEL_INFO_X, LEVEL_INFO_Y);
     }
        if( state6.equals(PathXCarState.MOUSE_OVER_STATE.toString())){
          g.setFont(FONT_TEXT_DISPLAY);
          g.setColor(FONT_COLOR);
         g.drawString(level6Info,                                     LEVEL_INFO_X, LEVEL_INFO_Y);
     }
         if( state7.equals(PathXCarState.MOUSE_OVER_STATE.toString())){
          g.setFont(FONT_TEXT_DISPLAY);
          g.setColor(FONT_COLOR);
         g.drawString(level7Info,                                     LEVEL_INFO_X, LEVEL_INFO_Y);
     }
          if( state8.equals(PathXCarState.MOUSE_OVER_STATE.toString())){
          g.setFont(FONT_TEXT_DISPLAY);
          g.setColor(FONT_COLOR);
         g.drawString(level8Info,                                     LEVEL_INFO_X, LEVEL_INFO_Y);
     }
           if( state9.equals(PathXCarState.MOUSE_OVER_STATE.toString())){
          g.setFont(FONT_TEXT_DISPLAY);
          g.setColor(FONT_COLOR);
         g.drawString(level9Info,                                     LEVEL_INFO_X, LEVEL_INFO_Y);
     }
           if( state10.equals(PathXCarState.MOUSE_OVER_STATE.toString())){
          g.setFont(FONT_TEXT_DISPLAY);
          g.setColor(FONT_COLOR);
         g.drawString(level10Info,                                     LEVEL_INFO_X, LEVEL_INFO_Y);
     }
          if( state11.equals(PathXCarState.MOUSE_OVER_STATE.toString())){
          g.setFont(FONT_TEXT_DISPLAY);
          g.setColor(FONT_COLOR);
         g.drawString(level11Info,                                     LEVEL_INFO_X, LEVEL_INFO_Y);
     }
          if( state12.equals(PathXCarState.MOUSE_OVER_STATE.toString())){
          g.setFont(FONT_TEXT_DISPLAY);
          g.setColor(FONT_COLOR);
         g.drawString(level12Info,                                     LEVEL_INFO_X, LEVEL_INFO_Y);
     }
          if(state13.equals(PathXCarState.MOUSE_OVER_STATE.toString())){
          g.setFont(FONT_TEXT_DISPLAY);
          g.setColor(FONT_COLOR);
         g.drawString(level13Info,                                     LEVEL_INFO_X, LEVEL_INFO_Y);
     }
          if( state14.equals(PathXCarState.MOUSE_OVER_STATE.toString())){
          g.setFont(FONT_TEXT_DISPLAY);
          g.setColor(FONT_COLOR);
         g.drawString(level14Info,                                     LEVEL_INFO_X, LEVEL_INFO_Y);
     }
           if( state15.equals(PathXCarState.MOUSE_OVER_STATE.toString())){
          g.setFont(FONT_TEXT_DISPLAY);
          g.setColor(FONT_COLOR);
         g.drawString(level15Info,                                     LEVEL_INFO_X, LEVEL_INFO_Y);
     }
          if( state16.equals(PathXCarState.MOUSE_OVER_STATE.toString())){
          g.setFont(FONT_TEXT_DISPLAY);
          g.setColor(FONT_COLOR);
         g.drawString(level16Info,                                     LEVEL_INFO_X, LEVEL_INFO_Y);
     }
          if( state17.equals(PathXCarState.MOUSE_OVER_STATE.toString())){
          g.setFont(FONT_TEXT_DISPLAY);
          g.setColor(FONT_COLOR);
         g.drawString(level17Info,                                     LEVEL_INFO_X, LEVEL_INFO_Y);
     }
           if( state18.equals(PathXCarState.MOUSE_OVER_STATE.toString())){
          g.setFont(FONT_TEXT_DISPLAY);
          g.setColor(FONT_COLOR);
         g.drawString(level18Info,                                     LEVEL_INFO_X, LEVEL_INFO_Y);
     }
          if( state19.equals(PathXCarState.MOUSE_OVER_STATE.toString())){
          g.setFont(FONT_TEXT_DISPLAY);
          g.setColor(FONT_COLOR);
         g.drawString(level19Info,                                     LEVEL_INFO_X, LEVEL_INFO_Y);
     }
          if( state20.equals(PathXCarState.MOUSE_OVER_STATE.toString())){
          g.setFont(FONT_TEXT_DISPLAY);
          g.setColor(FONT_COLOR);
         g.drawString(level20Info,                                     LEVEL_INFO_X, LEVEL_INFO_Y);
     }
  }
  
    public void renderDebuggingText(Graphics g)
    {
        // IF IT'S ACTIVATED
        if (data.isDebugTextRenderingActive())
        {
            // ENABLE PROPER RENDER SETTINGS
            g.setFont(FONT_DEBUG_TEXT);
            g.setColor(COLOR_DEBUG_TEXT);
            
            // GO THROUGH ALL THE DEBUG TEXT
            Iterator<String> it = data.getDebugText().iterator();
            int x = data.getDebugTextX();
            int y = data.getDebugTextY();
            while (it.hasNext())
            {
                // RENDER THE TEXT
                String text = it.next();
                g.drawString(text, x, y);
                y += 20;
            }   
        } 
    }
    
}
