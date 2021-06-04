
package lv.lumii.pixelmaster.modules.recognition.framework;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import lv.lumii.pixelmaster.core.api.framework.IControlElementRegistry;
import lv.lumii.pixelmaster.core.api.framework.IMenuItemAction;
import lv.lumii.pixelmaster.core.api.framework.IModule;
import lv.lumii.pixelmaster.core.api.framework.IWorkbench;

import javax.swing.Icon;
import javax.swing.JOptionPane;
import lv.lumii.pixelmaster.core.api.domain.ImageConverter;
import lv.lumii.pixelmaster.core.api.domain.RasterImage;
import lv.lumii.pixelmaster.core.api.domain.SizeConstraintViolationException;
import lv.lumii.pixelmaster.modules.binarization.domain.Binarization;
import lv.lumii.pixelmaster.modules.recognition.domain.CharacterInfo;
import lv.lumii.pixelmaster.modules.recognition.domain.CutCharacter;
import lv.lumii.pixelmaster.modules.recognition.domain.FindVectors;
import lv.lumii.pixelmaster.modules.recognition.domain.ImageModification;
import lv.lumii.pixelmaster.modules.recognition.domain.ResizeImage;
import lv.lumii.pixelmaster.modules.recognition.domain.database.*;
import lv.lumii.pixelmaster.modules.recognition.domain.*;
import lv.lumii.pixelmaster.modules.recognition.gui.ShowRecognitionResult;
import lv.lumii.pixelmaster.modules.textarea.domain.Util;

/**
 * 
 */
public class Module implements IModule {

	// actions
	private RecognizeCharacter recognizeCharacter = new RecognizeCharacter();
	private CreateNewDatabase newDatabase = new CreateNewDatabase();

	// workbench
	private IWorkbench workbench;


	public Module() { }

	@Override
	public void init(IControlElementRegistry registry, IWorkbench workbench) {
		this.workbench = workbench;
		registry.registerMenuItem(recognizeCharacter);
		registry.registerMenuItem(newDatabase);
	}


	private final class RecognizeCharacter implements IMenuItemAction {

		private final String menuTitle = "Recognition";
		private final String actionName = "Recognize character";
		private final Icon smallIcon = null;

		private RecognizeCharacter() { }

		@Override
		public void actionPerformed() {
			RasterImage tmpImage = new RasterImage(workbench.getActiveImage());
			int threshold = Binarization.OtsuThresholding(tmpImage, 10, 30, 60);
			Binarization.binary(tmpImage, tmpImage, threshold, 10, 30, 60);
			tmpImage = CutCharacter.findBorders(tmpImage);
			//skatās, kurš lielums ir lielāks - augstums vai garums
			int templateSize = 100;
			int newW, newH;
			if(tmpImage.getHeight()>=tmpImage.getWidth()){
				newH = templateSize;
				newW = ResizeImage.resizePropertiesNewW(tmpImage, templateSize);
			}else{
				newW = templateSize;
				newH = ResizeImage.resizePropertiesNewH(tmpImage, templateSize);
			}
			BufferedImage bufOrigImage = ImageConverter.toBuffered(tmpImage);
			bufOrigImage=ResizeImage.resizeImage(bufOrigImage, newW, newH,bufOrigImage.getWidth(),bufOrigImage.getHeight());
			tmpImage.resize(newW, newH);
			ImageConverter.convertBufImage(bufOrigImage, tmpImage);

			CharacterInfo table = new CharacterInfo();
			tmpImage = ImageModification.drawWhiteBack(tmpImage,templateSize);
			threshold = Binarization.OtsuThresholding(tmpImage, 10, 30, 60);
			Binarization.binary(tmpImage, tmpImage, threshold, 10, 30, 60);
			table = FindVectors.findVectorLength(tmpImage,table);
			CharacterDatabase cDB = new CharacterDatabase();
			try {
				File f = new File(System.getProperty("user.home") +
					"/.pixelmaster/modules/recognition/charDB.txt");
				if (!f.canRead()) {
					NewDatabase.createNewDatabase();
				}
				cDB = DatabaseProcessor.readDatabase(f);
				int record;
				if(table.V1==0) record = Correlation.Correlation(table, cDB,1);
				else if(table.V2==0) record = Correlation.Correlation(table, cDB,2);
				else if(table.V3==0) record = Correlation.Correlation(table, cDB,3);
				else if(table.V4==0) record = Correlation.Correlation(table, cDB,4);
				else record = Correlation.Correlation(table, cDB,0);
				ShowRecognitionResult result = new ShowRecognitionResult(tmpImage,record,cDB,table);
			} catch (IOException ex) {
				JOptionPane.showMessageDialog(null, "Input/output error occurred");
			}
		}

		@Override
		public boolean isEnabled() {
			return workbench.imageIsLoaded();
		}

		@Override
		public Icon getSmallIcon() {
			return smallIcon;
		}

		@Override
		public String getName() {
			return actionName;
		}

		@Override
		public String getMenuTitle() {
			return menuTitle;
		}
	}

	private final class CreateNewDatabase implements IMenuItemAction {

		private final String menuTitle = "Recognition";
		private final String actionName = "New database for character recognition";
		private final Icon smallIcon = null;

		private CreateNewDatabase() { }

		@Override
		public void actionPerformed() {
			try {
				NewDatabase.createNewDatabase();
				JOptionPane.showMessageDialog(null, "Database successfully created");
			}
			catch (IOException ex) {
				JOptionPane.showMessageDialog(null, "Input/output error occurred");
			}
		}

		@Override
		public boolean isEnabled() {
			return true;
		}

		@Override
		public Icon getSmallIcon() {
			return smallIcon;
		}

		@Override
		public String getName() {
			return actionName;
		}

		@Override
		public String getMenuTitle() {
			return menuTitle;
		}
	}
}
