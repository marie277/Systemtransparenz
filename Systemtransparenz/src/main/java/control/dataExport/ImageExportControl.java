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
	
	public ImageExportControl(ModelView modelView) {
        this.modelView = modelView;
    }
	
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
			if(applicationView.getWidthHeight().getX() + applicationView.getWidth() > dimensions.getWidth()) {
				dimensions = new Dimension2D(applicationView.getWidthHeight().getX() + applicationView.getWidth(), dimensions.getHeight());
			}
			if(applicationView.getWidthHeight().getY() + applicationView.getHeight() > dimensions.getHeight()) {
				dimensions = new Dimension2D(dimensions.getWidth(), applicationView.getWidthHeight().getY() + applicationView.getHeight());
			}
		}
		for(RelationView relationView : this.modelView.getRelations()) {
			if(relationView.getWidthHeight().getX() + relationView.getWidth() > dimensions.getWidth()) {
				dimensions = new Dimension2D(relationView.getWidthHeight().getX() + relationView.getWidth(), dimensions.getHeight());
			}
			if(relationView.getWidthHeight().getY() + relationView.getHeight() > dimensions.getHeight()) {
				dimensions = new Dimension2D(dimensions.getWidth(), relationView.getWidthHeight().getY() + relationView.getHeight());
			}
		}
		dimensions = new Dimension2D(dimensions.getWidth()+20.0, dimensions.getHeight()+20.0);
		return dimensions;
	}

	public void setImageLocation(File imageLocation) {
        this.imageLocation = imageLocation;
    }
	
	public File getImageLocation() {
		return (this.imageLocation == null) ? new File("") : this.imageLocation;
	}

}
