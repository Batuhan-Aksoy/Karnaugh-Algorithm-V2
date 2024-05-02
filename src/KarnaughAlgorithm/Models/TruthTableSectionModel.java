package KarnaughAlgorithm.Models;

public class TruthTableSectionModel {
	public char Section;
	public char SectionType = 'P';
	public String SectionDescription;
	public boolean [] SectionValues;
	
	public TruthTableSectionModel(char Section,char SectionType, int SectionValuesLength) {
		this.Section = Section;
		this.SectionType = SectionType;
		if (SectionType == 'P')
			this.SectionDescription = "Parameter";
		else if(SectionType == 'F')
			this.SectionDescription = "Function";
		this.SectionValues = new boolean [SectionValuesLength];
	}
}
