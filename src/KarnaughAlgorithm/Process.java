package KarnaughAlgorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.StringJoiner;

import KarnaughAlgorithm.Models.*;

public class Process {

	ArrayList<ParameterModel> ParameterList = new ArrayList<ParameterModel>();
	ArrayList<String> ExpressionList = new ArrayList<String>();
	FunctionModel FunctionModel = new FunctionModel();
	ArrayList<ExpressionParameterModel> ExpressionParameterList = new ArrayList<ExpressionParameterModel>();

	TruthTable truthTable = new TruthTable();

	public Process(String ReadedLine) {
		this.SetParameters(ReadedLine);
	}

	public void SetTruthTable() {

		int parameterListLength = this.ParameterList.size();
		truthTable.ParameterLength = parameterListLength;

		for (ParameterModel parameterModel : this.ParameterList) {

			TruthTableSectionModel truthTableSectionModel = new TruthTableSectionModel(
					parameterModel.ParameterCharacter, 'P', (int) Math.pow(2, parameterListLength));
			truthTable.SetTruthTableSection(truthTableSectionModel);
		}

		for (int number = 0; number < (int) Math.pow(2, parameterListLength); number++) {

			String binaryExpression = Integer.toBinaryString(number);
			if (binaryExpression.length() < parameterListLength) {
				for (int index = binaryExpression.length(); index < parameterListLength; index++) {
					binaryExpression = "0" + binaryExpression;
				}
			}

			char[] binaryExpressionCharArray = binaryExpression.toCharArray();
			for (ParameterModel parameterModel : this.ParameterList) {

				TruthTableSectionModel truthTableSectionModel = truthTable
						.GetTruthTableSection(parameterModel.ParameterSquence - 1);

				truthTableSectionModel.SectionValues[number] = binaryExpressionCharArray[parameterModel.ParameterSquence
						- 1] == '1' ? true : false;

			}
		}

		TruthTableSectionModel truthTableFunctionSectionModel = new TruthTableSectionModel(FunctionModel.FunctionName,
				'F', (int) Math.pow(2, parameterListLength));

		Arrays.fill(truthTableFunctionSectionModel.SectionValues, false);

		truthTable.SetTruthTableSection(truthTableFunctionSectionModel);

		for (String expression : this.ExpressionList) {
			ExpressionParameterModel expressionParameterModel = new ExpressionParameterModel();

			for (int index = 0; index < expression.length(); index++) {
				boolean complement = true;
				char characterInTheRelevantIndex = expression.charAt(index);

				if (Character.isLetter(characterInTheRelevantIndex)) {

					if (index + 1 < expression.length()) {
						if (expression.charAt(index + 1) == '’') {
							complement = false;
						}
					}

					expressionParameterModel.ExpressionParameterMapList
							.add(new ExpressionParameterMapModel(characterInTheRelevantIndex, complement));

				}
			}

			this.ExpressionParameterList.add(expressionParameterModel);
		}

		for (ExpressionParameterModel expressionParameterModel : this.ExpressionParameterList) {
			ArrayList<Integer> indexList = new ArrayList<Integer>();

			for (ExpressionParameterMapModel expressionParameterMapModel : expressionParameterModel.ExpressionParameterMapList) {
				ArrayList<Integer> tmpIndexList = new ArrayList<Integer>();
				ArrayList<Integer> comparedIndexList = new ArrayList<Integer>();
				TruthTableSectionModel truthTableSectionModel = truthTable
						.GetTruthTableSection(expressionParameterMapModel.Parameter);
				for (int i = 0; i < truthTableSectionModel.SectionValues.length; i++) {
					if (truthTableSectionModel.SectionValues[i] == expressionParameterMapModel.Value) {
						tmpIndexList.add(i);
					}
				}

				if (indexList.size() > 0) {
					for (int i = 0; i < tmpIndexList.size(); i++) {
						if (indexList.contains(tmpIndexList.get(i))) {
							comparedIndexList.add(tmpIndexList.get(i));
						}
					}
					indexList = comparedIndexList;
				} else {
					indexList = tmpIndexList;
				}
			}

			for (int index : indexList) {
				truthTableFunctionSectionModel.SectionValues[index] = true;
			}

		}
	}

	public void PrintTruthTable() {
		
		if(truthTable.GetTruthTableParameterSections().size() == 0)
			return;
		
		for (TruthTableSectionModel truthTableSectionModel : truthTable.TruthTableSections) {
			System.out.print(truthTableSectionModel.Section + " ");
		}

		System.out.println();

		for (int i = 0; i < Math.pow(2, truthTable.ParameterLength); i++) {
			for (TruthTableSectionModel truthTableSectionModel : truthTable.TruthTableSections) {
				System.out.print((truthTableSectionModel.SectionValues[i] == true ? "1" : "0") + " ");
			}
			System.out.println();
		}

	}

	public void PrintSumOfMinterms() {
		
		if(truthTable.GetTruthTableParameterSections().size() == 0)
			return;
		
		ArrayList<Integer> functionOutputIndexList = new ArrayList<Integer>();
		boolean[] functionOutputList;

		TruthTableSectionModel truthTableFunctionSectionModel = truthTable.GetTruthTableFunctionSection();
		functionOutputList = truthTableFunctionSectionModel.SectionValues;

		for (int i = 0; i < functionOutputList.length; i++) {
			if (functionOutputList[i] == true) {
				functionOutputIndexList.add(i);
			}
		}

		String sumOfMintermsLongExpression = "";

		ArrayList<TruthTableSectionModel> truthTableSectionParametersModels = truthTable
				.GetTruthTableParameterSections();

		for (int index : functionOutputIndexList) {
			for (TruthTableSectionModel truthTableSectionPameterModel : truthTableSectionParametersModels) {
				if (truthTableSectionPameterModel.SectionValues[index]) {
					sumOfMintermsLongExpression += Character.toString(truthTableSectionPameterModel.Section);
				} else {
					sumOfMintermsLongExpression += Character.toString(truthTableSectionPameterModel.Section) + "’";
				}
			}

			if (functionOutputIndexList.get(functionOutputIndexList.size() - 1) != index) {
				sumOfMintermsLongExpression += " + ";
			}

		}

		System.out.println(truthTableFunctionSectionModel.Section + " = " + sumOfMintermsLongExpression);

		String sumOfMintermsShortExpression = truthTableFunctionSectionModel.Section + " = Σ(";
		System.out.print(sumOfMintermsShortExpression);
		StringJoiner indexJoiner = new StringJoiner(",");
		functionOutputIndexList.forEach(i -> indexJoiner.add(String.valueOf(i)));
		System.out.print(indexJoiner.toString() + ")");

	}

	public void PrintMultiplicationOfMaxterms() {
		
		if(truthTable.GetTruthTableParameterSections().size() == 0)
			return;
		
		ArrayList<Integer> functionOutputIndexList = new ArrayList<Integer>();
		boolean[] functionOutputList;

		TruthTableSectionModel truthTableFunctionSectionModel = truthTable.GetTruthTableFunctionSection();
		functionOutputList = truthTableFunctionSectionModel.SectionValues;

		for (int i = 0; i < functionOutputList.length; i++) {
			if (functionOutputList[i] == false) {
				functionOutputIndexList.add(i);
			}
		}

		String multiplicationOfMaxtermsLongExpression = "";

		ArrayList<TruthTableSectionModel> truthTableSectionParametersModels = truthTable
				.GetTruthTableParameterSections();

		for (int index : functionOutputIndexList) {
			multiplicationOfMaxtermsLongExpression += "(";
			for (TruthTableSectionModel truthTableSectionPameterModel : truthTableSectionParametersModels) {
				if (!truthTableSectionPameterModel.SectionValues[index]) {
					if (this.ParameterList.getLast().ParameterCharacter != truthTableSectionPameterModel.Section)
						multiplicationOfMaxtermsLongExpression += Character
								.toString(truthTableSectionPameterModel.Section) + "+";
					else
						multiplicationOfMaxtermsLongExpression += Character
								.toString(truthTableSectionPameterModel.Section);
				} else {
					if (this.ParameterList.getLast().ParameterCharacter != truthTableSectionPameterModel.Section)
						multiplicationOfMaxtermsLongExpression += Character
								.toString(truthTableSectionPameterModel.Section) + "’+";
					else
						multiplicationOfMaxtermsLongExpression += Character
								.toString(truthTableSectionPameterModel.Section) + "’";
				}
			}

			if (functionOutputIndexList.get(functionOutputIndexList.size() - 1) != index) {
				multiplicationOfMaxtermsLongExpression += ").";
			} else {
				multiplicationOfMaxtermsLongExpression += ")";
			}

		}

		System.out.println(
				"\n" + truthTableFunctionSectionModel.Section + " = " + multiplicationOfMaxtermsLongExpression);

		String multiplicationOfMaxtermsShortExpression = truthTableFunctionSectionModel.Section + " = ∏(";
		System.out.print(multiplicationOfMaxtermsShortExpression);
		StringJoiner indexJoiner = new StringJoiner(",");
		functionOutputIndexList.forEach(i -> indexJoiner.add(String.valueOf(i)));
		System.out.print(indexJoiner.toString() + ")");
	}

	public void SetParameters(String ReadedLine) {

		try {

			String trimedReadedLine = ReadedLine.replaceAll(" ", "");
			int equalIndex = trimedReadedLine.indexOf("=");
			String parameterSection = trimedReadedLine.substring(equalIndex + 1, trimedReadedLine.length());

			ArrayList<Character> allowedCharacters = new ArrayList<Character>();
			allowedCharacters.add('+');
			allowedCharacters.add('’');

			ArrayList<Character> tmpParameterList = new ArrayList<Character>();
			char[] parameterSectionCharArray = parameterSection.toCharArray();

			for (char ch : parameterSectionCharArray) {
				if (Character.isLetter(ch)) {
					if (!tmpParameterList.contains(ch))
						tmpParameterList.add(ch);
				} else {
					if (!allowedCharacters.contains(ch)) {
						System.out.println("Değişkenler doğru formatta değil. \n"
								+ " Örnek olarak : F = A + CD + A’BC’D’ çarpımların toplamı şeklinde "
								+ " normal veya tümleyen formda olmalıdır.");
						throw new Exception();
					}

				}
			}

			String tmpFunction = trimedReadedLine.substring(0, equalIndex);
			if (tmpFunction.length() == FunctionModel.FunctionNameLength) {
				if (Character.isLetter(tmpFunction.charAt(0)))
					FunctionModel.FunctionName = tmpFunction.charAt(0);
			} else {
				System.out.println("Fonksiyon ismi formatı doğru değil. \n" + "Örnek olarak : F = formunda olmalıdır.");
				throw new Exception();
			}

			if (tmpParameterList.size() == 4) {

				Collections.sort(tmpParameterList);

				for (Character ch : tmpParameterList) {
					this.ParameterList.add(new ParameterModel(ch, tmpParameterList.indexOf(ch) + 1));
				}

				for (String exp : parameterSection.split("\\+")) {
					this.ExpressionList.add(exp);
				}

			} else {
				System.out.println("Boole fonksiyonu 4 değişkenli değil!");
				throw new Exception();
			}

		} catch (Exception e) {

		}

	}
}
