package control.dataExport;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Dimension2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import view.ApplicationView;
import view.ModelView;
import view.RelationView;

public class ImageExportControl {
	
	private File imageLocation;
	private ModelView modelView;
	
	public void saveImage() throws IOException {
		File file = this.imageLocation;
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Bild speichern");
		String[] fileExtension = {".png"};
		ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Bild-Dateien", fileExtension);
		fileChooser.getExtensionFilters().add(extensionFilter);
		if(file != null) {
			final File parentFile = new File(file.getParent());
			fileChooser.setInitialDirectory(parentFile);
		}
		
		file = fileChooser.showSaveDialog(this.modelView.getScene().getWindow());
		if(file != null) {
			Dimension2D dimensions = this.imageDimensions();
			int width = (int)dimensions.getWidth();
			int height = (int)dimensions.getHeight();
			WritableImage writableImage = new WritableImage(width, height);
			BufferedImage bufferedImage = SwingFXUtils.fromFXImage((Image)this.modelView.snapshot(new SnapshotParameters(), writableImage), null);
			ImageIO.write(bufferedImage, "png", file);
			this.imageLocation = file;
			return;		
		}
		throw new IOException("Achtung! Bild konnte nicht gespeichert werden.");
	}

	private Dimension2D imageDimensions() {
		Dimension2D dimensions = new Dimension2D(0.0, 0.0);
		for(ApplicationView applicationView : this.modelView.getApplications()) {
			double applicationPositionX = applicationView.getWidthHeight().getX();
			double applicationWidth = applicationView.getWidth();
			double dimensionsWidth = dimensions.getWidth();
			boolean applicationWider = applicationPositionX + applicationWidth > dimensionsWidth;
			double applicationPositionY = applicationView.getWidthHeight().getY();
			double applicationHeight = applicationView.getWidth();
			double dimensionsHeight = dimensions.getHeight();
			boolean applicationHigher = applicationPositionY + applicationHeight > dimensionsHeight;
			if(applicationWider) {
				dimensions = new Dimension2D(applicationPositionX + applicationWidth, dimensionsHeight);
			}
			if(applicationHigher) {
				dimensions = new Dimension2D(dimensionsWidth, applicationPositionY + applicationHeight);
			}
		}
		for(RelationView relationView : this.modelView.getRelations()) {
			double relationPositionX = relationView.getWidthHeight().getX();
			double relationWidth = relationView.getWidth();
			double dimensionsWidth = dimensions.getWidth();
			boolean relationWider = relationPositionX + relationWidth > dimensionsWidth;
			double relationPositionY = relationView.getWidthHeight().getY();
			double relationHeight = relationView.getWidth();
			double dimensionsHeight = dimensions.getHeight();
			boolean relationHigher = relationPositionY + relationHeight > dimensionsHeight;
			if(relationWider) {
				dimensions = new Dimension2D(relationPositionX + relationWidth, dimensionsHeight);
			}
			if(relationHigher) {
				dimensions = new Dimension2D(dimensionsWidth, relationPositionY + relationHeight);
			}
		}
		dimensions = new Dimension2D(dimensions.getWidth()+20.0, dimensions.getHeight()+20.0);
		return dimensions;
	}

	public File getImageLocation() {
		return (this.imageLocation == null) ? new File("") : this.imageLocation;
	}

}
