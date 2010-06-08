package SketchMaster.Stroke.features;

import java.util.ArrayList;

import SketchMaster.Stroke.StrokeData.InkInterface;

public interface InkFeatureSet {

	public InkInterface getInk();

	public void initAll();

	public void computeFeatures();

	public void computeFeatures(InkInterface ink);

	public ArrayList getFeatures();

}
