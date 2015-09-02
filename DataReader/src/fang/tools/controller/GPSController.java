package fang.tools.controller;

import fang.tools.model.GPSModelInterface;
import fang.tools.view.GPSView;

public class GPSController implements ControllerInterface
{
	private GPSModelInterface mModel = null;
	private GPSView mView = null;
	
	public GPSController(GPSModelInterface model)
	{
		mModel = model;
		mView = new GPSView(this, model);
		
	}
	
	public void start()
	{
		mModel.start(mView.getFileName());
	}
	
	public void stop()
	{
		
	}
}