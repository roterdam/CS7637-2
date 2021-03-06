package ravensproject;

import java.util.ArrayList;




import ravensproject.AgentTransformation.mappingTransformations;

public class AgentMappingScore {

	int transformationScore = -1;
	//WHAT'S THE COST OF THE DIFFERENCE BETWEEN THESE TRANSFORMATIONS AND THE COMPARED ONE?
	int transformationDeltaCost = -1;
	//WHAT'S THE TOTAL COST OF THE TRANSFORMATIONS FOR THIS MAP?
	int transformationTotalCost = -1;
	ArrayList<AgentTransformation> anticipatedTransformations = null; 
	ArrayList<AgentTransformation> transformationDelta = null;
	ArrayList<AgentTransformation> totalTransformation = null;
	int correspondingMapIndex = -1;
	int agentIndex = -1;

	static int transformShapeChangeCost = 2; //DELETE AND CREATE - COUNT AS TWO
	static int transformSizeChangeCost = 2; //DELETE AND CREATE - COUNT AS TWO
	static int transformWidthChangeCost = 2; //DELETE AND CREATE - COUNT AS TWO
	static int transformHeightChangeCost = 2; //DELETE AND CREATE - COUNT AS TWO
	static int transformAboveChangeCost = 1;
	static int transformLeftofChangeCost = 1;
	static int transformOverlapChangeCost = 1;
	static int transformAngleChangeCost = 1; //MAYBE 2???
	static int transformExpectedAngleChangeCost = 0; //SPECIAL CASES IN ROTATION/REFLECTION WHERE A SPECIFIC ANGLE CHANGE IS EXPECTED
	static int transformFillChangeCost = 1;
	static int transformInsideChangeCost = 1;
	static int transformAlignmentChangeCost = 2; //DELETE AND CREATE - COUNT AS TWO 
	static int transformCreatedCost = 1;
	static int transformDeletedCost = 1;	
	static int transformUndefinedChangeCost = 10;
	
	public AgentMappingScore(int correspondingMapIndex, ArrayList<AgentTransformation> transformationDelta, 
			ArrayList<AgentTransformation> anticipatedTransformations, ArrayList<ArrayList<AgentTransformation>> transformationsForEachObject) {
		
		this.transformationDelta = transformationDelta;
		this.anticipatedTransformations = anticipatedTransformations;
		this.totalTransformation = getTotalTransformation(transformationsForEachObject);
		this.correspondingMapIndex = correspondingMapIndex;
		
		if(transformationDelta != null) {

			//HERE IS WHERE I WOULD ADD ANY KIND OF WEIGHTING OF TRANSFORMATIONS, LEARNING FROM CASES, ETC
			calculateScore();
			
		}
		
	}
	
	//ACCEPTS AN ARRAY OF TRANSFORMATION ARRAYS FOR EACH OBJECT IN A MAP BETWEEN FIGURES. 
	//RETURNS THE CUMULATIVE LIST OF ALL TRANSFORMATIONS USED IN THE MAP
	private ArrayList<AgentTransformation> getTotalTransformation(ArrayList<ArrayList<AgentTransformation>> list) {
		ArrayList<AgentTransformation> retval = new ArrayList<AgentTransformation>();
		
		for(int i = 0; i < list.size(); ++i) {
			for(int j = 0; j < list.get(i).size(); ++j) {
//				if(list.get(i).get(j) != mappingTransformations.NO_CHANGE)
					retval.add(new AgentTransformation(list.get(i).get(j).theTransformation, list.get(i).get(j).theValue));
			}
		}
		
//		if(retval.size() == 0)
//			retval.add(mappingTransformations.NO_CHANGE);
		
		return retval;
	}
		
	private void calculateScore() {
		
		transformationDeltaCost = getTransformationListWeight(transformationDelta);
		transformationScore = transformationDeltaCost * 100;
		

		transformationTotalCost = getTransformationListWeight(totalTransformation);
	
	}
	
	public static int getTransformationListWeight(ArrayList<AgentTransformation> theList) {
		int retval = 0;
		
		for(int i = 0; i < theList.size(); ++i) {
			retval += getTransformationWeight(theList.get(i));
		}
		
		return retval;
	}
	
	public static int getAnticipatedTransformationListValue(ArrayList<AgentTransformation> theList) {
		if(theList == null)
			return 0;
		
		return theList.size();		
	}
	
	public static int getTransformationWeight(AgentTransformation transformation) {
		if(transformation.theTransformation == mappingTransformations.SHAPE_CHANGE)
			return transformShapeChangeCost;
		if(transformation.theTransformation == mappingTransformations.ABOVE_CHANGE)
			return transformAboveChangeCost;
		if(transformation.theTransformation == mappingTransformations.LEFT_OF_CHANGE)
			return transformLeftofChangeCost;
		if(transformation.theTransformation == mappingTransformations.OVERLAP_CHANGE)
			return transformOverlapChangeCost;
		if(transformation.theTransformation == mappingTransformations.ANGLE_CHANGE)
			return transformAngleChangeCost;
		if(transformation.theTransformation == mappingTransformations.FILL_CHANGE)
			return transformFillChangeCost;
		if(transformation.theTransformation == mappingTransformations.INSIDE_CHANGE)
			return transformInsideChangeCost;
		if(transformation.theTransformation == mappingTransformations.ALIGNMENT_CHANGE)
			return transformAlignmentChangeCost;
		if(transformation.theTransformation == mappingTransformations.CREATED)
			return transformCreatedCost;
		if(transformation.theTransformation == mappingTransformations.DELETED)
			return transformDeletedCost;
		if(transformation.theTransformation == mappingTransformations.EXPECTEDANGLE_CHANGE)
			return transformExpectedAngleChangeCost;
		if(transformation.theTransformation == mappingTransformations.SIZE_CHANGE)
			return transformSizeChangeCost;
		if(transformation.theTransformation == mappingTransformations.WIDTH_CHANGE)
			return transformWidthChangeCost;
		if(transformation.theTransformation == mappingTransformations.HEIGHT_CHANGE)
			return transformHeightChangeCost;
		
		return transformUndefinedChangeCost;
	}
	
	public AgentMappingScore whichScoreIsBetter(AgentMappingScore compare) {
		if(whichScoreIsBetter("this", transformationDeltaCost, anticipatedTransformations.size(), transformationTotalCost, "compare", compare.transformationDeltaCost, compare.anticipatedTransformations.size(), compare.transformationTotalCost) == "this")
			return this;
		
		return compare;
	}
	
	public static String whichScoreIsBetter(String A, int ADelta, int AAnticipated, int ATotal, String B, int BDelta, int BAnticipated, int BTotal) {
		//*********************************************************
		// THIS AND IN AgentShapeMapping.whichTransformListCostsLess ARE
		// THE PLACES TO PUT LEARNING LOGIC
		//*********************************************************
		
		if(ADelta == BDelta) {
			if(AAnticipated == BAnticipated) {
				if(ATotal > BTotal)
					return B;
				else 
					return A;
			}
			else {
				if(AAnticipated > BAnticipated)
					return A;
				else 
					return B;
			}
		}
		else {
			if(ADelta > BDelta)
				return B;
			else 
				return A;
		}		

	}
}
