
package pathx.ui;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import properties_manager.PropertiesManager;
import pathx.PathX.PathXPropertyType;
/**
 *
 * @author zeb
 */
public class PathXErrorHandler {
       private JFrame window;

    /**
     * This simple little class just needs the window.
     * 
     * @param initWindow 
     */
    public PathXErrorHandler (JFrame initWindow)
    {
        // KEEP THE WINDOW FOR LATER
        window = initWindow;
    }
    
     public void processError(PathXPropertyType errorType)
    {
        // GET THE FEEDBACK TEXT
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String errorFeedbackText = props.getProperty(errorType);
        
        // NOTE THAT WE'LL USE THE SAME DIALOG TITLE FOR ALL ERROR TYPES
        String errorTitle = props.getProperty(PathXPropertyType.TEXT_TITLE_BAR_ERROR);
        
        // POP OPEN A DIALOG TO DISPLAY TO THE USER
        JOptionPane.showMessageDialog(window, errorFeedbackText, errorTitle, JOptionPane.ERROR_MESSAGE);
    } 
}
