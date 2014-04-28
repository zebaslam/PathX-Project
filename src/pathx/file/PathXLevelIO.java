/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pathx.file;

import java.io.File;
import pathx.data.PathXDataModel;
import pathx.data.PathXLevel;

/**
 *
 * @author zeb
 */
public interface PathXLevelIO {
    public boolean loadLevel(File levelFile, PathXDataModel model);
    public boolean saveLevel(File levelFile, PathXLevel levelToSave);
}
