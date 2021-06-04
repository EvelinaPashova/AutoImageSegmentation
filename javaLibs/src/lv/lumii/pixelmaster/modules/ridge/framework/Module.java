
package lv.lumii.pixelmaster.modules.ridge.framework;

import lv.lumii.pixelmaster.core.api.framework.IControlElementRegistry;
import lv.lumii.pixelmaster.core.api.framework.IMenuItemAction;
import lv.lumii.pixelmaster.core.api.framework.IModule;
import lv.lumii.pixelmaster.core.api.framework.IWorkbench;

import lv.lumii.pixelmaster.modules.ridge.gui.ShowRecognitionText;
import lv.lumii.pixelmaster.modules.ridge.domain.Symbol;
import lv.lumii.pixelmaster.modules.ridge.domain.SymbolInfo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.JOptionPane;
import lv.lumii.pixelmaster.core.api.domain.RasterImage;
import lv.lumii.pixelmaster.core.api.domain.SizeConstraintViolationException;
import lv.lumii.pixelmaster.modules.ridge.domain.Util;

/**
 * 
 */
public class Module implements IModule {

	// actions
	private RecognizeText recognizeText = new RecognizeText();
	private CreateNewDatabase newDatabase = new CreateNewDatabase();
	private PaintRidges paintRidges = new PaintRidges();
	private PaintValleys paintValleys = new PaintValleys();
	private PaintEdges paintEdges = new PaintEdges();
	private SmoothImage smoothImage = new SmoothImage();
	private TraceLinesRidge traceLinesRidge = new TraceLinesRidge();
	private TraceLinesValley traceLinesValley = new TraceLinesValley();
	private TraceLinesEdge traceLinesEdge = new TraceLinesEdge();
	private DrawLineSegmentsRidge drawLineSegmentsRidge = new DrawLineSegmentsRidge();
	private DrawLineSegmentsValley drawLineSegmentsValley = new DrawLineSegmentsValley();
	private DrawLineSegmentsEdge drawLineSegmentsEdge = new DrawLineSegmentsEdge();
	private ConcatenateSegmentsRidge concatenateSegmentsRidge = new ConcatenateSegmentsRidge();
	private ConcatenateSegmentsValley concatenateSegmentsValley = new ConcatenateSegmentsValley();
	private ConcatenateSegmentsEdge concatenateSegmentsEdge = new ConcatenateSegmentsEdge();
	private ClusterRidge clusterRidge = new ClusterRidge();
	private ClusterValley clusterValley = new ClusterValley();
	private ClusterEdge clusterEdge = new ClusterEdge();

	// workbench
	private IWorkbench workbench;


	public Module() { }

	@Override
	public void init(IControlElementRegistry registry, IWorkbench workbench) {
		this.workbench = workbench;

		registry.registerMenuItem(recognizeText);
		registry.registerMenuItem(newDatabase);

		registry.registerMenuItem(paintRidges);
		registry.registerMenuItem(paintValleys);
		registry.registerMenuItem(paintEdges);

		registry.registerMenuItem(smoothImage);

		registry.registerMenuItem(traceLinesRidge);
		registry.registerMenuItem(traceLinesValley);
		registry.registerMenuItem(traceLinesEdge);

		registry.registerMenuItem(drawLineSegmentsRidge);
		registry.registerMenuItem(drawLineSegmentsValley);
		registry.registerMenuItem(drawLineSegmentsEdge);

		registry.registerMenuItem(concatenateSegmentsRidge);
		registry.registerMenuItem(concatenateSegmentsValley);
		registry.registerMenuItem(concatenateSegmentsEdge);

		registry.registerMenuItem(clusterRidge);
		registry.registerMenuItem(clusterValley);
		registry.registerMenuItem(clusterEdge);
	}


	private final class RecognizeText implements IMenuItemAction {

		private final String menuTitle = "Recognition";
		private final String actionName = "Recognize text";
		private final Icon smallIcon = null;

		private RecognizeText() { }

		@Override
		public void actionPerformed() {
            ArrayList<Symbol> symbols = Util.getSymbols(workbench.getActiveImage());
            ArrayList<SymbolInfo> symbList = new ArrayList<SymbolInfo>();

			try {

				File file = new File(System.getProperty("user.home") +
					"/.pixelmaster/modules/ridge/charValuesDB.txt");

				/*
				 * Recreating the character database.
				 * The database will be copied from the "lv/lumii/pixelmaster/modules/ridge/domain/charValuesDB.txt"
				 * to the "<user home directory>/.pixelmaster/modules/ridge/charValuesDB.txt"
				 */
				if (!file.canRead()) {
					Util.newDatabase();
				}

				ArrayList<SymbolInfo> symbDB = null;
				symbDB = SymbolInfo.fileRead(file);
				for (Symbol item: symbols) {
					SymbolInfo tmpS = new SymbolInfo(item);
					tmpS.setName("$");
					symbList.add(tmpS);
				}
				String result = "";
				for (SymbolInfo item: symbList) {
					item.compare(symbDB, 6, 50, 50);
					result += item.getName();
					result += " ";
				}
				ShowRecognitionText resultWin = new ShowRecognitionText(result, symbList, file);
				// draw symbols

//				OGraph chacInfo = ImageProcessor.printLineToGraph(parentFrame.rImage, 6);

//				OGraphReader.write(chacInfo, new File("ChackFile.txt"));
//
//				RasterImage tmp=Util.printLine(workbench.getActiveImage(), 6);
//
//				try {
//					workbench.setActiveImage(tmp, IWorkbench.OWNERSHIP_CALLEE);
//				}
//				catch (SizeConstraintViolationException exc) {
//					assert false;
//				}
			}
			catch (IOException ex) {
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
		private final String actionName = "New database for text recognition";
		private final Icon smallIcon = null;

		private CreateNewDatabase() { }

		@Override
		public void actionPerformed() {
			try {
				Util.newDatabase();
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

	private final class PaintRidges implements IMenuItemAction {

		private final String menuTitle = "Ridge";
		private final String actionName = "Paint Ridges";
		private final Icon smallIcon = null;

		private PaintRidges() { }

		@Override
		public void actionPerformed() {
			RasterImage tmp = Util.ridgeImage(workbench.getActiveImage(), 1);
			try {
				workbench.setActiveImage(tmp, IWorkbench.OWNERSHIP_CALLEE);
			}
			catch (SizeConstraintViolationException exc) {
				assert false;
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

	private final class PaintValleys implements IMenuItemAction {

		private final String menuTitle = "Ridge";
		private final String actionName = "Paint Valleys";
		private final Icon smallIcon = null;

		private PaintValleys() { }

		@Override
		public void actionPerformed() {
			RasterImage tmp = Util.ridgeImage(workbench.getActiveImage(), 2);
			try {
				workbench.setActiveImage(tmp, IWorkbench.OWNERSHIP_CALLEE);
			}
			catch (SizeConstraintViolationException exc) {
				assert false;
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

	private final class PaintEdges implements IMenuItemAction {

		private final String menuTitle = "Ridge";
		private final String actionName = "Paint Edges";
		private final Icon smallIcon = null;

		private PaintEdges() { }

		@Override
		public void actionPerformed() {
			RasterImage tmp = Util.ridgeImage(workbench.getActiveImage(), 3);
			try {
				workbench.setActiveImage(tmp, IWorkbench.OWNERSHIP_CALLEE);
			}
			catch (SizeConstraintViolationException exc) {
				assert false;
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

	private final class SmoothImage implements IMenuItemAction {

		private final String menuTitle = "Ridge";
		private final String actionName = "Smooth Image";
		private final Icon smallIcon = null;

		private SmoothImage() { }

		@Override
		public void actionPerformed() {
			RasterImage tmp = Util.ridgeImage(workbench.getActiveImage(), 0);
			try {
				workbench.setActiveImage(tmp, IWorkbench.OWNERSHIP_CALLEE);
			}
			catch (SizeConstraintViolationException exc) {
				assert false;
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

	private final class TraceLinesRidge implements IMenuItemAction {

		private final String menuTitle = "Ridge";
		private final String actionName = "Trace Lines Ridge";
		private final Icon smallIcon = null;

		private TraceLinesRidge() { }

		@Override
		public void actionPerformed() {
			RasterImage tmp = Util.printLine(workbench.getActiveImage(), 1);
			try {
				workbench.setActiveImage(tmp, IWorkbench.OWNERSHIP_CALLEE);
			}
			catch (SizeConstraintViolationException exc) {
				assert false;
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

	private final class TraceLinesValley implements IMenuItemAction {

		private final String menuTitle = "Ridge";
		private final String actionName = "Trace Lines Valley";
		private final Icon smallIcon = null;

		private TraceLinesValley() { }

		@Override
		public void actionPerformed() {
			RasterImage tmp = Util.printLine(workbench.getActiveImage(), 2);
			try {
				workbench.setActiveImage(tmp, IWorkbench.OWNERSHIP_CALLEE);
			}
			catch (SizeConstraintViolationException exc) {
				assert false;
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

	private final class TraceLinesEdge implements IMenuItemAction {

		private final String menuTitle = "Ridge";
		private final String actionName = "Trace Lines Edge";
		private final Icon smallIcon = null;

		private TraceLinesEdge() { }

		@Override
		public void actionPerformed() {
			RasterImage tmp = Util.printLine(workbench.getActiveImage(), 3);
			try {
				workbench.setActiveImage(tmp, IWorkbench.OWNERSHIP_CALLEE);
			}
			catch (SizeConstraintViolationException exc) {
				assert false;
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

	private final class DrawLineSegmentsRidge implements IMenuItemAction {

		private final String menuTitle = "Ridge";
		private final String actionName = "Draw line segments: Ridge";
		private final Icon smallIcon = null;

		private DrawLineSegmentsRidge() { }

		@Override
		public void actionPerformed() {
			RasterImage tmp = Util.printLine(workbench.getActiveImage(), 4);
			try {
				workbench.setActiveImage(tmp, IWorkbench.OWNERSHIP_CALLEE);
			}
			catch (SizeConstraintViolationException exc) {
				assert false;
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

	private final class DrawLineSegmentsValley implements IMenuItemAction {

		private final String menuTitle = "Ridge";
		private final String actionName = "Draw line segments: Valley";
		private final Icon smallIcon = null;

		private DrawLineSegmentsValley() { }

		@Override
		public void actionPerformed() {
			RasterImage tmp = Util.printLine(workbench.getActiveImage(), 5);
			try {
				workbench.setActiveImage(tmp, IWorkbench.OWNERSHIP_CALLEE);
			}
			catch (SizeConstraintViolationException exc) {
				assert false;
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

	private final class DrawLineSegmentsEdge implements IMenuItemAction {

		private final String menuTitle = "Ridge";
		private final String actionName = "Draw line segments: Edge";
		private final Icon smallIcon = null;

		private DrawLineSegmentsEdge() { }

		@Override
		public void actionPerformed() {
			RasterImage tmp = Util.printLine(workbench.getActiveImage(), 6);
			try {
				workbench.setActiveImage(tmp, IWorkbench.OWNERSHIP_CALLEE);
			}
			catch (SizeConstraintViolationException exc) {
				assert false;
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

	private final class ConcatenateSegmentsRidge implements IMenuItemAction {

		private final String menuTitle = "Ridge";
		private final String actionName = "Concatenate segments: Ridge";
		private final Icon smallIcon = null;

		private ConcatenateSegmentsRidge() { }

		@Override
		public void actionPerformed() {
			RasterImage tmp = Util.printLine(workbench.getActiveImage(), 7);
			try {
				workbench.setActiveImage(tmp, IWorkbench.OWNERSHIP_CALLEE);
			}
			catch (SizeConstraintViolationException exc) {
				assert false;
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

	private final class ConcatenateSegmentsValley implements IMenuItemAction {

		private final String menuTitle = "Ridge";
		private final String actionName = "Concatenate segments: Valley";
		private final Icon smallIcon = null;

		private ConcatenateSegmentsValley() { }

		@Override
		public void actionPerformed() {
			RasterImage tmp = Util.printLine(workbench.getActiveImage(), 8);
			try {
				workbench.setActiveImage(tmp, IWorkbench.OWNERSHIP_CALLEE);
			}
			catch (SizeConstraintViolationException exc) {
				assert false;
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

	private final class ConcatenateSegmentsEdge implements IMenuItemAction {

		private final String menuTitle = "Ridge";
		private final String actionName = "Concatenate segments: Edge";
		private final Icon smallIcon = null;

		private ConcatenateSegmentsEdge() { }

		@Override
		public void actionPerformed() {
			RasterImage tmp = Util.printLine(workbench.getActiveImage(), 9);
			try {
				workbench.setActiveImage(tmp, IWorkbench.OWNERSHIP_CALLEE);
			}
			catch (SizeConstraintViolationException exc) {
				assert false;
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

	private final class ClusterRidge implements IMenuItemAction {

		private final String menuTitle = "Ridge";
		private final String actionName = "Cluster: Ridge";
		private final Icon smallIcon = null;

		private ClusterRidge() { }

		@Override
		public void actionPerformed() {
			RasterImage tmp = Util.printLine(workbench.getActiveImage(), 10);
			try {
				workbench.setActiveImage(tmp, IWorkbench.OWNERSHIP_CALLEE);
			}
			catch (SizeConstraintViolationException exc) {
				assert false;
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

	private final class ClusterValley implements IMenuItemAction {

		private final String menuTitle = "Ridge";
		private final String actionName = "Cluster: Valley";
		private final Icon smallIcon = null;

		private ClusterValley() { }

		@Override
		public void actionPerformed() {
			RasterImage tmp = Util.printLine(workbench.getActiveImage(), 11);
			try {
				workbench.setActiveImage(tmp, IWorkbench.OWNERSHIP_CALLEE);
			}
			catch (SizeConstraintViolationException exc) {
				assert false;
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

	private final class ClusterEdge implements IMenuItemAction {

		private final String menuTitle = "Ridge";
		private final String actionName = "Cluster: Edge";
		private final Icon smallIcon = null;

		private ClusterEdge() { }

		@Override
		public void actionPerformed() {
			RasterImage tmp = Util.printLine(workbench.getActiveImage(), 12);
			try {
				workbench.setActiveImage(tmp, IWorkbench.OWNERSHIP_CALLEE);
			}
			catch (SizeConstraintViolationException exc) {
				assert false;
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
}
