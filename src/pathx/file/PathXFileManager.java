/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pathx.file;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import mini_game.Viewport;
import pathx.PathX.PathXPropertyType;
import pathx.data.PathXDataModel;
import pathx.data.PathXRecord;
import pathx.ui.PathXMiniGame;
import properties_manager.PropertiesManager;

/**
 *
 * @author zeb
 */
public class PathXFileManager {
    private PathXMiniGame miniGame;
    
    public PathXFileManager(PathXMiniGame initMiniGame)
    {
        // KEEP IT FOR LATER
        miniGame = initMiniGame;
    }
}
