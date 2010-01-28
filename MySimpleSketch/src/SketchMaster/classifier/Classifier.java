/**
 * 
 */
package SketchMaster.classifier;

import SketchMaster.Stroke.StrokeData.InkInterface;

/**
 * @author maha
 * 
 */
public abstract class Classifier {

	public abstract void train();

	public abstract Classification Classify(InkInterface ink);

}
