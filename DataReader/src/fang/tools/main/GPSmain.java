package fang.tools.main;

import org.jfree.ui.RefineryUtilities;

import fang.tools.*;
import fang.tools.controller.GPSController;
import fang.tools.model.GPSNmeaModel;
import fang.tools.view.GPSView;

public class GPSmain {
	
	public static void main(String[] args) {
		GPSNmeaModel model = new GPSNmeaModel();
		GPSController control = new GPSController(model);
		//control.start();
	}
}

