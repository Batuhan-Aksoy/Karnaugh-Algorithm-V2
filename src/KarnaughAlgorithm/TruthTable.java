package KarnaughAlgorithm;

import java.util.ArrayList;
import java.util.stream.Collectors;

import KarnaughAlgorithm.Models.TruthTableSectionModel;

public class TruthTable {
	
	ArrayList<TruthTableSectionModel> TruthTableSections = new ArrayList<TruthTableSectionModel>();
	public int ParameterLength;
	
	public TruthTable () {
		
	}
	
	public void SetTruthTableSection(TruthTableSectionModel TruthTableSectionModel) {
		this.TruthTableSections.add(TruthTableSectionModel);
	}
	
	public TruthTableSectionModel GetTruthTableSection(int SectionIndex) {
		return this.TruthTableSections.get(SectionIndex);
	}
	
	public TruthTableSectionModel GetTruthTableSection(char SectionName) {
		return this.TruthTableSections.stream().filter(s->s.Section == SectionName).findAny().orElse(null);
	}
	
	public TruthTableSectionModel GetTruthTableFunctionSection() {
		return this.TruthTableSections.stream().filter(s->s.SectionType == 'F').findAny().orElse(null);
	}
	
	public ArrayList<TruthTableSectionModel> GetTruthTableParameterSections() {
		try {
			return this.TruthTableSections.stream().filter(s->s.SectionType == 'P').collect(Collectors.toCollection(ArrayList::new));
		} catch (Exception e) {
			return new ArrayList<TruthTableSectionModel>();
		}	
	}
	
}
