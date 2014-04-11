/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pathx.data;

import mini_game.MiniGameDataModel;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Iterator;
import pathx.PathX.PathXPropertyType;
import mini_game.MiniGame;
import mini_game.MiniGameDataModel;
import mini_game.SpriteType;
import properties_manager.PropertiesManager;
import static pathx.PathXConstants.*;
import pathx.ui.PathXMiniGame;
import pathx.ui.PathXPanel;
/**
 *
 * @author zeb
 */
public class PathXDataModel extends MiniGameDataModel {
    private MiniGame miniGame;
    private String currentLevel;
    
    public PathXDataModel(MiniGame initMiniGame){
        miniGame= initMiniGame;
        
    }

    @Override
    public void checkMousePressOnSprites(MiniGame game, int x, int y) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void reset(MiniGame game) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateAll(MiniGame game) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateDebugText(MiniGame game) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
