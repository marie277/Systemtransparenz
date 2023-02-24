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

//Klasse zur Steuerung des PNG-Datei-Exports 
public class ImageExportControl {
	
	private File imageLocation;
	private ModelView modelView;
	
	//Konstruktor
	public ImageExportControl(ModelView modelView) {
        this.modelView = modelView;
    }
	
	//Methode zum Speichern einer Modell-Ansicht als Bild-Datei
	public void saveImage() throws IOException {
		File file = this.imageLocation;
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Bild speichern");
		String fileExtension = ".png";
		ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Bild-Dateien", fileExtension);
		fileChooser.getExtensionFilters().add(extensionFilter);
		if(file != null) {
			File parentFile = new File(file.getParent());
			fileChooser.setInitialDirectory(parentFile);
		}
		file = fileChooser.showSaveDialog(this.modelView.getScene().getWindow());
		if(file != null) {
			Dimension2D dimensions = new Dimension2D(0.0, 0.0);
			for(ApplicationView applicationView : this.modelView.getApplications()) {
				if(applicationView.getLayout().getX() + applicationView.getElementRegion().getPrefWidth() > dimensions.getWidth()) {
					dimensions = new Dimension2D(applicationView.getLayout().getX() + applicationView.getElementRegion().getPrefWidth(), dimensions.getHeight());
				}
				if(applicationView.getLayout().getY() + applicationView.getElementRegion().getPrefHeight() > dimensions.getHeight()) {
					dimensions = new Dimension2D(dimensions.getWidth(), applicationView.getLayout().getY() + applicationView.getElementRegion().getPrefHeight());
				}
			}
			for(RelationView relationView : this.modelView.getRelations()) {
				if(relationView.getLayout().getX() + relationView.getElementRegion().getPrefWidth() > dimensions.getWidth()) {
					dimensions = new Dimension2D(relationView.getLayout().getX() + relationView.getElementRegion().getPrefWidth(), dimensions.getHeight());
				}
				if(relationView.getLayout().getY() + relationView.getElementRegion().getPrefHeight() > dimensions.getHeight()) {
					dimensions = new Dimension2D(dimensions.getWidth(), relationView.getLayout().getY() + relationView.getElementRegion().getPrefHeight());
				}
			}
			dimensions = new Dimension2D(dimensions.getWidth()+50.0, dimensions.getHeight()+50.0);
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

}
