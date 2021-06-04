import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

class ImageDraw {

    public static void draw(JPanel p, File f) {
        try {
            p.getGraphics().drawImage(ImageIO.read(f), 0, 0, ImageIO.read(f).getWidth(), ImageIO.read(f).getHeight(), p);
        } catch (Exception e) {
        }
    }
}
