package control.dataImport;

import java.util.LinkedList;

import model.ApplicationModel;

public interface Import {
	
	public LinkedList<ApplicationModel> importApplications();
}
