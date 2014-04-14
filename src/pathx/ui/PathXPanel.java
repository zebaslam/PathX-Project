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
      g.setColor(COLOR_BLACK);
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
    }
  
  public void renderLevelInfo(Graphics g){
      PropertiesManager props = PropertiesManager.getPropertiesManager(); 
      String level1Info = props.getProperty(PathXPropertyType.TEXT_LABEL_LEVEL_1_INFO);
      
     if( game.guiButtons.get(AVAILABLE_BUTTON_TYPE).getState().equals(PathXCarState.MOUSE_OVER_STATE.toString())){
          g.setFont(FONT_TEXT_DISPLAY);
          g.setColor(FONT_COLOR);
         g.drawString(level1Info,                                     LEVEL_INFO_X, LEVEL_INFO_Y);
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
